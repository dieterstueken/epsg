package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  24.02.14 11:45
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordRefSys extends Bound {

    public CoordRefSys(Integer key, Name name, Area area) {
        super(key, name, area);
    }

    public static CoordRefSys geocentric(Integer key, Name name, Area area,
                                         CoordSys coordsys, Datum datum) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys geographic3D(Integer key, Name name, Area area,
                                           CoordSys coordsys, Datum datum,
                                           CoordRefSys src, Operation op) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys geographic2D(Integer key, Name name, Area area,
                                  CoordSys coordsys, Datum datum,
                                  CoordRefSys src, Operation op) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys projected(Integer key, Name name, Area area,
                                           CoordSys coordsys, CoordRefSys src, Operation op) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys vertical(Integer key, Name name, Area area, CoordSys coordsys, Datum datum) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys compound(Integer key, Name name, Area area,
                                       CoordSys coordsys, CoordRefSys hori, CoordRefSys vert) {
        return new CoordRefSys(key, name, area);
    }

    public static CoordRefSys engineering(Integer key, Name name, Area area, CoordSys coordsys, Datum datum) {
        return new CoordRefSys(key, name, area);
    }}
