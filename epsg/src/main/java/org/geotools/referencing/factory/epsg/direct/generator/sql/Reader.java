package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Indexed;
import org.geotools.referencing.factory.epsg.direct.item.OrderedList;

import java.sql.*;
import java.util.List;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 14:53
 * modified by: $
 * modified on: $
 */
public class Reader {

    public static Reader open() throws SQLException {
        return open(System.getProperty("epsg.database.url"));
    }

    public static Reader open(String url) throws SQLException {
        Connection connection = DriverManager.getConnection(url);
        return new Reader(connection);
    }

    final Connection connection;

    public Reader(Connection connection) {
        this.connection = connection;
    }


    public <T extends Indexed> OrderedList<T>
    read(String tableName, ResultOf<T> factory, String ... columns) throws SQLException {
        return read(tableName, factory, List.of(columns));
    }

    public <T extends Indexed> OrderedList<T>
    read(String tableName, ResultOf<T> factory, List<String> columns) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.append("SELECT");
        String sep = " ";
        for (String column : columns) {
            query.append(sep).append(column);
            sep = ", ";
        }

        query.append(" FROM ").append(tableName);

        OrderedList<T> items = new OrderedList<T>();

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

            for(int i=1; i<=n; ++i) {
                System.out.format("%d: %s %s %s\n", i, md.getColumnName(i), md.getColumnTypeName(i), md.getColumnClassName(i));
            }


        }
    }


    public static void main(String ... args) throws SQLException {

        var reader = open();

        reader.explain("ELLIPSOID");

    }
}
