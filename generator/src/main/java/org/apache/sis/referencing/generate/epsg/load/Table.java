package org.apache.sis.referencing.generate.epsg.load;

import org.apache.sis.referencing.generate.epsg.generate.FileAppender;
import org.apache.sis.util.logging.Logging;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 10:43
 * modified by: $Author$
 * modified on: $Date$
 */
public class Table<T> {

    protected static final Logger LOGGER = Logging.getLogger(Table.class.getName());
    protected static final Level LEVEL = Level.INFO;

    public final String name;

    public final String table;

    public final Class<T> type;

    public final List<Field> columns = new ArrayList<>();

    private Table(Class<T> type, String name, String table) {

        this.name = name;
        this.table = table;
        this.type = type;

        for(Class c = type; c!=null; c=c.getSuperclass()) {

            for (Field field : c.getDeclaredFields()) {

                if(Modifier.isStatic(field.getModifiers()))
                    continue;

                Transient t = field.getAnnotation(Transient.class);

                if(t!=null && t.value())
                    continue;

                field.setAccessible(true);
                columns.add(field);
            }
        }
    }

    public static <T> Table<T> of(Class<T> type, String name, String table) {
        return new Table<T>(type, name, table);
    }

    public static <T extends Comparable<? super T>> Table<T> ordered(Class<T> type, String name, String table) {
        return new Table<T>(type, name, table) {
            @Override
            public List<T> load(Connection connection) throws SQLException {
                List<T> list = super.load(connection);
                Collections.sort(list);
                return list;
            }
        };
    }


    String query() {
        StringBuilder query = new StringBuilder("SELECT");

        String delim = " ";
        for(Field column:columns) {
            String cname = column.getName().toUpperCase();
            query.append(delim).append('"').append(cname).append('"');
            delim = ", ";
        }

        query.append(" FROM \"EPSG\".\"").append(name).append('"');

        return query.toString();
    }

    T read(ResultSet row) throws SQLException {

        try {
            T result = type.newInstance();

            int index = 0;
            for(Field column:columns) {
                Class<?> ctype = column.getType();
                Object value = row.getObject(++index, ctype);
                column.set(result, value);
            }

            return result;
        } catch (InstantiationException|IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> load(Connection connection) throws SQLException {

        String query = query();

        LOGGER.log(LEVEL, query);

        try(Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query)) {

            List<T> values = new ArrayList<T>();

            while(result.next()) {
                T value = read(result);
                values.add(value);
            }

            if(LOGGER.isLoggable(LEVEL))
                LOGGER.log(Level.FINE, String.format("load %d values from %s\n", values.size(), name));

            return values;
        }
    }

    public void show(OutputAppender out) {

        out.format("%s\n", type.getSimpleName());

        for (Field column : columns) {
            out.format("\t%s %s;\n", column.getType().getSimpleName(), column.getName());
        }

        out.nl();
    }

    public static void main(String ... args) {

        OutputAppender out = new FileAppender(System.out);

        Alias.TABLE.show(out);
        Area.TABLE.show(out);

    }
}
