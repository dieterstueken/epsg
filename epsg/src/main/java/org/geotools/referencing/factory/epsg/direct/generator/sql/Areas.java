package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.Items;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  25.07.2020 17:01
 * modified by: $
 * modified on: $
 */
public class Areas extends UoMs {

    final Items<Area> areas = new Items<>();

    Areas(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addArea, "select AREA_CODE, AREA_NAME, REMARKS, DEPRECATED, " +
                "AREA_SOUTH_BOUND_LAT, AREA_NORTH_BOUND_LAT, AREA_WEST_BOUND_LON, AREA_EAST_BOUND_LON, " +
                "AREA_OF_USE, ISO_A2_CODE, ISO_A3_CODE " +
                "from EPSG_AREA");
    }

    void addArea(ResultSet rs) throws SQLException {
        int j=4;
        double s = rs.getDouble(++j);
        double n = rs.getDouble(++j);
        double w = rs.getDouble(++j);
        double e = rs.getDouble(++j);
        var bounds = new GeographicBoundingBoxImpl(w,e,s,n);

        String use = rs.getString(++j);
        String a2 = rs.getString(++j);
        String a3 = rs.getString(++j);

        var area = new Area(codep(rs), text(use), bounds, a2, a3);
        areas.add(area);
    }

}
