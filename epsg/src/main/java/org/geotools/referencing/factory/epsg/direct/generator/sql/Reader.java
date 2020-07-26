package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Code;
import org.geotools.referencing.factory.epsg.direct.item.Indexed;
import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.Text;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 14:53
 * modified by: $
 * modified on: $
 */
public class Reader implements AutoCloseable {

    public static Reader open() throws SQLException {
        return open(System.getProperty("epsg.database.url"));
    }

    public static Reader open(String url) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        return new Reader(connection);
    }

    final Connection connection;

    final Map<String, Text> names = new HashMap<>();

    public Reader(Connection connection) {
        this.connection = connection;
    }

    protected void load() throws SQLException {

    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    protected Text text(String name) {
        return names.computeIfAbsent(name, Text::new);
    }


    protected Code code(ResultSet rs) throws SQLException {
        int code = rs.getInt(1);
        Text name = text(rs.getString(2));
        Text remarks = text(rs.getString(3));

        return new Code(code, name, remarks);
    }

    protected Code codep(ResultSet rs) throws SQLException {
        int code = rs.getInt(1);
        Text name = text(rs.getString(2));
        Text remarks = text(rs.getString(3));

        short deprecated = rs.getShort(4);
        if(deprecated!=0)
            code *= -1;

        return new Code(code, name, remarks);
    }

    void process(Processor processor, String query) throws SQLException {
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)) {
            while(rs.next()) {
                processor.process(rs);
            }
        }
    }

    public <T extends Indexed> Items<T>
    read(Result<T> factory, String tableName, String... columns) throws SQLException {
        return read(tableName, factory, List.of(columns));
    }

    public <T extends Indexed> Items<T>
    read(String tableName, Result<T> factory, List<String> columns) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.append("SELECT");
        String sep = " ";
        for (String column : columns) {
            query.append(sep).append(column);
            sep = ", ";
        }

        query.append(" FROM ").append(tableName);

        Items<T> items = new Items<T>();

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query.toString())) {
            while(rs.next()) {
                T item = factory.get(rs);
                items.put(item);
            }
        }

        return items;
    }


    public void explain(String table) throws SQLException {
        String query = "SELECT * FROM EPSG_" + table;
        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query)) {

            ResultSetMetaData md = rs.getMetaData();
            int n=md.getColumnCount();

            System.out.append(table).append(":");
            for(int i=1; i<=n; ++i) {
                System.out.append(' ').append(md.getColumnName(i));
                //.append("[").append(md.getColumnTypeName(i)).append("]");
            }
            System.out.println();
        }
    }


    public static void main(String ... args) throws SQLException {
        try(var reader = open()) {
            reader.explain("ALIAS");
            reader.explain("NAMINGSYSTEM");
            reader.explain("AREA");

            reader.explain("UNITOFMEASURE");

            reader.explain("PRIMEMERIDIAN");
            reader.explain("ELLIPSOID");
            reader.explain("DATUM");

            reader.explain("COORDINATEAXIS");
            reader.explain("COORDINATEAXISNAME");
            reader.explain("COORDINATEREFERENCESYSTEM");
            reader.explain("COORDINATESYSTEM");
            reader.explain("COORDOPERATION");
            reader.explain("COORDOPERATIONMETHOD");
            reader.explain("COORDOPERATIONPARAM");
            reader.explain("COORDOPERATIONPARAMUSAGE");
            reader.explain("COORDOPERATIONPARAMVALUE");
            reader.explain("COORDOPERATIONPATH");
        }
    }
}
