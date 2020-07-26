package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Code;
import org.geotools.referencing.factory.epsg.direct.item.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 17:18
 * modified by: $
 * modified on: $
 */
public class Tables {

    final Map<String, Text> names = new HashMap<>();


    Text text(String name) {
        return names.computeIfAbsent(name, Text::new);
    }

    Code code(ResultSet rs) throws SQLException {
        int code = rs.getInt(1);
        Text name = text(rs.getString(2));
        Text remarks = text(rs.getString(3));

        return new Code(code, name, remarks);
    }

    Code codep(ResultSet rs) throws SQLException {
        int code = rs.getInt(1);
        Text name = text(rs.getString(2));
        Text remarks = text(rs.getString(3));

        short deprecated = rs.getShort(4);
        if(deprecated!=0)
            code *= -1;

        return new Code(code, name, remarks);
    }
}
