package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Alias;
import org.geotools.referencing.factory.epsg.direct.item.Scope;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  25.07.2020 12:12
 * modified by: $
 * modified on: $
 */
public class Aliases extends Scopes {

    static final String PST = "select OBJECT_CODE, ALIAS, REMARKS, NAMING_SYSTEM_CODE from EPSG_ALIAS where OBJECT_TABLE_NAME=?";

    final PreparedStatement pst;

    Aliases(Connection conn) throws SQLException {
        super(conn);
        pst = conn.prepareStatement(PST);
    }

    void aliases(String tableName, Consumer<Alias> target) throws SQLException {
        pst.setString(1, tableName);
        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Alias alias = alias(rs);
                target.accept(alias);
            }
        }
    }

    Alias alias(ResultSet rs) throws SQLException {
        var code = code(rs);
        int nsc = rs.getInt(4);
        Scope scope = scopes.find(nsc);
        return new Alias(code, scope);
    }
}
