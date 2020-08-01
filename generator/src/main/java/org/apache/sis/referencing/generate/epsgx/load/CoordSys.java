package org.apache.sis.referencing.generate.epsgx.load;

import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordSys extends MetaData {

    public Integer coord_sys_code; // NOT NULL,
    public String coord_sys_name; //(254) NOT NULL,
    public String coord_sys_type; //(24) NOT NULL,
    public Short dimension; // NOT NULL,

    @Transient
    public final List<CoordAxis> axes = new LinkedList<>();

    @Override
    public Integer getKey() {
        return coord_sys_code;
    }

    @Override
    public String getName() {
        return coord_sys_name;
    }

    public static final Table<CoordSys> TABLE = Table.of(CoordSys.class, "Coordinate System", "epsg_coordinatesystem");

    public static enum Type {cartesian, ellipsoidal, spherical, vertical}

    public Type type() {

        switch(coord_sys_type) {
            case "Cartesian": return Type.cartesian;
            case "ellipsoidal": return Type.ellipsoidal;
            case "spherical": return Type.spherical;
            case "vertical": return Type.vertical;
        }

        throw new RuntimeException("unknown coord sys type:" + coord_sys_type);
    }

    @Override
    public String getFunction() {
        return type().name();
    }

    public static class Builder extends MetaDataBase.Builder<CoordSys> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpName(CoordSys item) {
            return out.nl().append(",").quote(item.getName());
        }

        @Override
        public OutputAppender dumpTail(CoordSys item) {

            for (CoordAxis axis : item.axes)
                axis.dump(out);

            return super.dumpTail(item);
        }
    }
}