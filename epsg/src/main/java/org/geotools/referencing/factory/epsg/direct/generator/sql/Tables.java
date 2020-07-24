package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.factory.epsg.direct.item.*;
import org.opengis.metadata.extent.GeographicBoundingBox;

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

    public static Tables of() throws SQLException {
        return new Tables(Reader.open());
    }

    final Reader reader;

    final Map<String, Text> names = new HashMap<>();

    final OrderedList<Area> areas;

    Text text(String name) {
        return names.computeIfAbsent(name, Text::new);
    }

    public Tables(Reader reader) throws SQLException {
        this.reader = reader;

        this.areas = reader.read("epsg_area", this::area,
                "area_code", "area_name", "remarks", "deprecated", "area_of_use",
                "area_south_bound_lat", "area_north_bound_lat",
                "area_west_bound_lon", "area_east_bound_lon",
                "iso_a2_code", "iso_a3_code"
                );

    }

    Area area(ResultSet rs) throws SQLException {
        int j = 4;
        Text use = text(rs.getString(++j));
        double s = rs.getDouble(++j);
        double n = rs.getDouble(++j);
        double w = rs.getDouble(++j);
        double e = rs.getDouble(++j);

        GeographicBoundingBox bbox = new GeographicBoundingBoxImpl(w,e,s,n);

        String iso_a2 = rs.getString(++j);
        String iso_a3 = rs.getString(++j);

        return new Area(code(rs), use, bbox, iso_a2, iso_a3);
    }

    Code code(ResultSet rs) throws SQLException {
        int code = rs.getInt(1);
        Text name = text(rs.getString(2));
        Text remarks = text(rs.getString(3));

        short deprecated = rs.getShort(4);
        if(deprecated!=0)
            code *= -1;

        return new Code(code, name, remarks);
    }

    public static void main(String ... args) throws SQLException {
        var tables = Tables.of();

        System.out.println(tables.areas.size());
    }
}
