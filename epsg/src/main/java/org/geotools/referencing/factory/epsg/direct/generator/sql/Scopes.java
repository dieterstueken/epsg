package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.Scope;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  25.07.2020 16:02
 * modified by: $
 * modified on: $
 */
public class Scopes extends Reader {

    final Items<Scope> scopes = new Items<>();

    public Scopes(Connection connection) {
        super(connection);
    }

    protected void load() throws SQLException {
        super.load();

        process(this::addScope, "select NAMING_SYSTEM_CODE, NAMING_SYSTEM_NAME, REMARKS, DEPRECATED from EPSG_NAMINGSYSTEM");
    }

    void addScope(ResultSet rs) throws SQLException {
        var scope = new Scope(codep(rs));
        scopes.put(scope);
    }

}
