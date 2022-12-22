package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.code.Text;
import org.geotools.referencing.factory.epsg.direct.item.crs.CoordAxis;
import org.geotools.referencing.factory.epsg.direct.item.crs.CoorsdSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 18:48
 * modified by: $
 * modified on: $
 */
public class CoordSystems extends Datums {

    static final String PST = "select COORD_AXIS_NAME, REMARKS, DESCRIPTION," +
            " COORD_AXIS_ABBREVIATION, UOM_CODE, COORD_AXIS_ORIENTATION, DEPRECATED" +
            " from EPSG_COORDINATEAXIS natural left join EPSG_COORDINATEAXISNAME where COORD_SYS_CODE=?"+
            " order by COORD_AXIS_ORDER";

    final PreparedStatement pst;

    final Items<CoorsdSys> coordSystems = new Items<>();

    CoordSystems(Connection conn) throws SQLException {
        super(conn);

        pst = conn.prepareStatement(PST);
    }

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addCoorsdSys, "select COORD_SYS_CODE, COORD_SYS_NAME, REMARKS, DEPRECATED, " +
                "COORD_SYS_TYPE from EPSG_COORDINATESYSTEM;");
    }

    protected void finish() throws SQLException {
        super.finish();
        aliases("EPSG_COORDINATESYSTEM", coordSystems::aliased);
    }

    private void addCoorsdSys(ResultSet rs) throws SQLException {
        coordSystems.add(readCoorsdSys(rs));
    }

    protected CoorsdSys readCoorsdSys(ResultSet rs) throws SQLException {
        Code code = codep(rs);

        CoorsdSys.Type type = CoorsdSys.getType(rs.getString(5));

        List<CoordAxis> axes = new ArrayList<>(3);
        pst.setInt(1, code.getCode());
        try (ResultSet rsa = pst.executeQuery()) {
            while (rsa.next()) {
                CoordAxis axis = readAxis(rsa);
                axes.add(axis);
            }
        }

        return new CoorsdSys(code, type, List.copyOf(axes));
    }

    private CoordAxis readAxis(ResultSet rs) throws SQLException {
        Text name = text(rs.getString(1));
        Text remarks = text(rs.getString(2));
        Text description = text(rs.getString(3));
        Text abbreviation = text(rs.getString(4));
        UoM<?> unit = units.get(rs.getInt(5));
        Text orientation = text(rs.getString(6));
        short deprecated = rs.getShort(7);

        if(deprecated!=0)
            throw new IllegalArgumentException("deprecated axis name");

        return new CoordAxis(name, remarks, description, abbreviation, unit, orientation);
    }
}
