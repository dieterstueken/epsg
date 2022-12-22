package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.IndexedSet;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.code.Indexed;
import org.geotools.referencing.factory.epsg.direct.item.code.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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

    protected void finish() throws SQLException {
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
        } catch (Exception e) {
            throw e;
        }
    }

    public <T extends Indexed> IndexedSet<T>
    read(Result<T> factory, String tableName, String... columns) throws SQLException {
        return read(tableName, factory, List.of(columns));
    }

    public <T extends Indexed> IndexedSet<T>
    read(String tableName, Result<T> factory, List<String> columns) throws SQLException {

        StringBuilder query = new StringBuilder();

        query.append("SELECT");
        String sep = " ";
        for (String column : columns) {
            query.append(sep).append(column);
            sep = ", ";
        }

        query.append(" FROM ").append(tableName);

        IndexedSet<T> items = new IndexedSet<T>();

        try(Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query.toString())) {
            while(rs.next()) {
                T item = factory.get(rs);
                items.add(item);
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


    public static void _main(String ... args) throws SQLException {
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


    public static void main(String ... args) throws SQLException {

        String url = System.getProperty("epsg.database.url");
        try(Connection connection = DriverManager.getConnection(url)) {
            var tables = new ReferenceSystems(connection);
            tables.load();
            tables.finish();

            System.out.format("Scopes: %d\n", tables.scopes.size());
            System.out.format("Areas:  %d\n", tables.areas.size());
            System.out.format("Units:  %d\n", tables.units.size());
            System.out.format("Ellps:  %d\n", tables.ellipsoids.size());
            System.out.format("Primes: %d\n", tables.meridians.size());
            System.out.format("Datums: %d\n", tables.datums.size());
            System.out.format("Ops:    %d\n", tables.operations.size());
            System.out.format("CRS:    %d\n", tables.referenceSystems.size());

            for (UoM<?> unit : tables.units) {
                System.out.format("%5d: %s     %s\n", unit.code.code, unit.code.name, unit.unit);
            }

        }
    }
}
