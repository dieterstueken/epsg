package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  20.02.14 16:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class Datum extends Bound {

    final String type;
    final Ellipsoid ellipsoid;
    final PrimeMeridian primeMeridian;
    final Short epoch;

    private Datum(Integer key, Name name, String type,
                  Area area, Short epoch,
                  Ellipsoid ellipsoid, PrimeMeridian primeMeridian) {
        super(key, name, area);

        this.type = type;
        this.ellipsoid = ellipsoid;
        this.primeMeridian = primeMeridian;
        this.epoch = epoch;
    }

    static Datum datum(Integer key, Name name, String type,
                       Area area, int epoch,
                       Ellipsoid ellipsoid, PrimeMeridian primeMeridian) {
        return new Datum(key, name, type, area, (short)epoch, ellipsoid, primeMeridian);
    }

    public static Datum geodetic(Integer key, Name name, Area area, int epoch,
                          Ellipsoid ellipsoid, PrimeMeridian primeMeridian ) {
        return datum(key, name, "geodetic", area, (short)epoch, ellipsoid, primeMeridian);
    }

    public static Datum vertical(Integer key, Name name, Area area, int epoch) {
        return datum(key, name, "vertical", area, (short)epoch, null, null);
    }

    public static Datum engineering(Integer key, Name name, Area area, int epoch) {
        return datum(key, name, "engineering", area, (short)epoch, null, null);
    }

}
