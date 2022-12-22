package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.crs.Compound;
import org.geotools.referencing.factory.epsg.direct.item.crs.CoordRefSys;
import org.geotools.referencing.factory.epsg.direct.item.crs.Geographic;
import org.geotools.referencing.factory.epsg.direct.item.crs.Operation;
import org.geotools.referencing.factory.epsg.direct.item.crs.Transformation;
import org.geotools.referencing.factory.epsg.direct.item.crs.Transformed;
import org.geotools.referencing.factory.epsg.direct.item.datum.Datum;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 14:07
 * modified by: $
 * modified on: $
 */
public class ReferenceSystems extends CoordSystems {

    final Items<CoordRefSys> referenceSystems = new Items<>();

    final Items<Operation> operations = new Items<>();

    ReferenceSystems(Connection conn) throws SQLException {
        super(conn);
    }

    static final String SELECT_OPERATION = "select COORD_OP_CODE, COORD_OP_NAME, REMARKS, DEPRECATED, AREA_OF_USE_CODE, " +
            "COORD_OP_TYPE, SOURCE_CRS_CODE, TARGET_CRS_CODE from EPSG_COORDOPERATION ";

    static final String SELECT_CRS = "select COORD_REF_SYS_CODE, COORD_REF_SYS_NAME, REMARKS, DEPRECATED, AREA_OF_USE_CODE, " +
            "COORD_REF_SYS_KIND, COORD_SYS_CODE, DATUM_CODE, SOURCE_GEOGCRS_CODE, PROJECTION_CONV_CODE, " +
            "CMPD_HORIZCRS_CODE, CMPD_VERTCRS_CODE " +
            "from EPSG_COORDINATEREFERENCESYSTEM ";

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addOperation, SELECT_OPERATION + "where COORD_OP_TYPE='conversion'");

        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='vertical'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='geocentric'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='engineering'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='geographic 3D'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='geographic 2D'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='projected'");
        process(this::addCRS, SELECT_CRS +"where COORD_REF_SYS_KIND='compound'");

        process(this::addOperation, SELECT_OPERATION + "where COORD_OP_TYPE='transformation'");
        process(this::addOperation, SELECT_OPERATION + "where COORD_OP_TYPE='concatenated operation'");
    }

    protected void finish() throws SQLException {
        super.finish();
        aliases("EPSG_COORDINATEREFERENCESYSTEM", referenceSystems::aliased);
        aliases("EPSG_COORDOPERATION", operations::aliased);
    }

    private void addCRS(ResultSet rs) throws SQLException {
        referenceSystems.add(readCRS(rs));
    }

    protected void addOperation(ResultSet rs) throws SQLException {
        operations.add(readOperation(rs));
    }

    protected Operation readOperation(ResultSet rs) throws SQLException {
        Code code = codep(rs);
        Area area = findArea(rs);
        Operation.Type type = Operation.getType(rs.getString(6));

        CoordRefSys src = referenceSystems.get(rs.getInt(7));
        CoordRefSys tgt = referenceSystems.get(rs.getInt(8));

        if(type== Operation.Type.conversion) {
            if(src!=null || tgt != null)
                throw new IllegalArgumentException("unexpected operation parameters for:" + code);

            return new Operation(code, type, area);
        } else {
            if(src==null || tgt == null)
                throw new IllegalArgumentException("unexpected operation parameters for:" + code);

            return new Transformation(code, type, area, src, tgt);
        }
    }

    private CoordRefSys readCRS(ResultSet rs) throws SQLException {
        Code code = codep(rs);
        Area area = findArea(rs);

        CoordRefSys.Kind type = CoordRefSys.getKind(rs.getString(6));

        Short cs = rs.getShort(7);
        Datum datum = datums.get(rs.getInt(8));
        CoordRefSys src = referenceSystems.get(rs.getInt(9));
        Operation op = operations.get(rs.getInt(10));

        CoordRefSys hor = referenceSystems.get(rs.getInt(11));
        CoordRefSys ver = referenceSystems.get(rs.getInt(12));

        if(type==CoordRefSys.Kind.compound) {
            if(datum!=null || src!=null || op!=null)
                throw new IllegalArgumentException("unexpected compound parameters for:" + code);

            return new Compound(code, area, type, cs, hor, ver);
        }

        if(hor!=null || ver!=null)
            throw new IllegalArgumentException("unexpected compound parameters for:" + code);

        if(src!=null && op!=null) {
            return new Transformed(code, area, type, cs, datum, src, op);
        }

        if(datum!=null) {
            return new Geographic(code, area, type, cs, datum);
        }

        return new CoordRefSys(code, area, type, cs);
    }


}
