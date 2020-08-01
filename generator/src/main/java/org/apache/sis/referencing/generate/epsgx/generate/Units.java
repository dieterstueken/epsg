package org.apache.sis.referencing.generate.epsgx.generate;

import org.apache.sis.referencing.generate.epsgx.load.*;

import java.sql.SQLException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  05.11.2014 20:15
 * modified by: $Author$
 * modified on: $Date$
 */
public class Units implements ItemResolver {

    final EpsgTables tables;

    public Units(EpsgTables tables) {
        this.tables = tables;
    }

    public void run() {

        OutputAppender out = new FileAppender(System.out);
        out.indent(4);

        new UnitOfMeasure.Builder(this, out, "public final", "_").dumpAll(tables.units.values());
    }

    public static void main(String ... args) throws SQLException {
        EpsgTables tables = EpsgTables.load();
        new Units(tables).run();
    }

    @Override
    public OutputAppender unit(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append("null");

        out.nl().appendCode(",U_", key);

        UnitOfMeasure unit = tables.units.get(key);
        if(unit!=null)
            out.remark(unit.getName());

        return out;
    }

    @Override
    public OutputAppender alias(Alias alias, OutputAppender out) {
        return out.append("alias(").quote(alias.getName()).append(')')
                .remark(alias.naming_system_code.toString());
    }

    @Override
    public OutputAppender area(Area area, OutputAppender out) {
        if(area==null)
            return out.nl().append(",null").remark("no area code");


        return out.nl().format(",A%d.", area.getKey() / 100).appendCode("A_", area.getKey()).remark(area.getName());
    }

    @Override
    public OutputAppender method(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no method code");

        CoordOpMethod method = tables.methods.get(key);

        return out.nl().appendCode(",M_", key).remark(method.getName());
    }

    @Override
    public OutputAppender operation(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no operation code");

        CoordOp op = tables.coops.get(key);

        return out.nl().format(",OP%d.", key/100).appendCode("OP_", key).remark(op.coord_op_name);
    }

    @Override
    public OutputAppender crs(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no crs code");

        CoordRefSys crs = tables.crsTable.get(key);

        return out.nl().format(",CRS%d.", CoordRefSys.getGroup(key)).appendCode("CRS_", key).remark(crs.getName());
    }

    @Override
    public OutputAppender cs(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no crs code");

        return out.nl().appendCode(",CS_", key);
    }

    @Override
    public OutputAppender datum(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no datum code");

        Datum datum = tables.datums.get(key);

        return out.nl().appendCode(",D_", key).remark(datum.getName());
    }

    @Override
    public OutputAppender ellipsoid(Integer key, OutputAppender out) {
        if(key==null) return out.nl().append(",null");

        Ellipsoid ellipsoid = tables.ellipsoids.get(key);

        return out.nl().appendCode(",E_", key).remark(ellipsoid.getName());
    }

    @Override
    public OutputAppender meridian(Integer key, OutputAppender out) {
        return key==null ? out.nl().append(",null") : out.nl().appendCode(",PM_", key);
    }
}
