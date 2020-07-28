package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.code.Text;
import org.geotools.referencing.factory.epsg.direct.item.datum.Datum;
import org.geotools.referencing.factory.epsg.direct.item.datum.Ellipsoid;
import org.geotools.referencing.factory.epsg.direct.item.datum.PrimeMeridian;

import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  26.07.2020 14:17
 * modified by: $
 * modified on: $
 */
public class Ellipsoids extends Areas {

    final Items<Ellipsoid> ellipsoids = new Items<>();
    final Items<PrimeMeridian> meridians = new Items<>();
    final Items<Datum> datums = new Items<>();

    Ellipsoids(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addEllipsoid, "select ELLIPSOID_CODE, ELLIPSOID_NAME, REMARKS, DEPRECATED, " +
                "SEMI_MAJOR_AXIS, UOM_CODE, INV_FLATTENING, SEMI_MINOR_AXIS, ELLIPSOID_SHAPE " +
                "from EPSG_ELLIPSOID");
        aliases("epsg_ellipsoid", ellipsoids::aliased);

        process(this::addMeridian, "select PRIME_MERIDIAN_CODE, PRIME_MERIDIAN_NAME, REMARKS, DEPRECATED, " +
                "GREENWICH_LONGITUDE, UOM_CODE from EPSG_PRIMEMERIDIAN");
        aliases("epsg_primemeridian", meridians::aliased);

        process(this::addDatum, "select DATUM_CODE, DATUM_NAME, REMARKS, DEPRECATED, AREA_OF_USE_CODE, " +
                "DATUM_TYPE, ORIGIN_DESCRIPTION, REALIZATION_EPOCH, ELLIPSOID_CODE, PRIME_MERIDIAN_CODE, DATUM_SCOPE " +
                "from EPSG_DATUM");
        aliases("epsg_datum", datums::aliased);
    }

    private void addEllipsoid(ResultSet rs) throws SQLException {
        ellipsoids.add(readEllipsoid(rs));
    }

    private Ellipsoid readEllipsoid(ResultSet rs) throws SQLException {

        Code code = codep(rs);
        short shape = rs.getShort(9);
        double minor = rs.getDouble(5);
        UoM<Length> uom = units.find(rs.getInt(6)).asType(Length.class);
        if(shape==0) {
            double major = rs.getDouble(7);
            return Ellipsoid.ellipsoid(code, uom, minor, major);
        } else {
            double ifl = rs.getDouble(8);
            return Ellipsoid.flattenedSphere(code, uom, minor, ifl);
        }
    }


    private void addMeridian(ResultSet rs) throws SQLException {
        Code code = codep(rs);
        double lon = rs.getDouble(5);
        UoM<Angle> uom = units.find(rs.getInt(6)).asType(Angle.class);

        var meridian = new PrimeMeridian(code, lon, uom);
        meridians.add(meridian);
    }

    private void addDatum(ResultSet rs) throws SQLException {
        Code code = codep(rs);

        int j=4;
        Area area = areas.find(rs.getInt(++j));
        Datum.Type type = Datum.Type.valueOf(rs.getString(++j));
        String descr = rs.getString(++j);
        Date epoch = rs.getDate(++j);
        Ellipsoid ellps = ellipsoids.find(rs.getInt(++j));
        PrimeMeridian pm = meridians.find(rs.getInt(++j));
        Text scope = text(rs.getString(++j));

        var datum = new Datum(code, type, area, ellps, pm, scope, epoch, descr);
        datums.add(datum);
    }
}
