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
    final String epoch;

    private Datum(Integer key, Name name, String type,
                  Area area, String epoch,
                  Ellipsoid ellipsoid, PrimeMeridian primeMeridian) {
        super(key, name, area);

        this.type = type;
        this.ellipsoid = ellipsoid;
        this.primeMeridian = primeMeridian;
        this.epoch = epoch;
    }

    static Datum datum(Integer key, Name name, String type,
                       Area area, String epoch,
                       Ellipsoid ellipsoid, PrimeMeridian primeMeridian) {
        return new Datum(key, name, type, area, epoch, ellipsoid, primeMeridian);
    }

    public static Datum geodetic(Integer key, Name name, Area area, String epoch,
                          Ellipsoid ellipsoid, PrimeMeridian primeMeridian ) {
        return datum(key, name, "geodetic", area, epoch, ellipsoid, primeMeridian);
    }

    public static Datum vertical(Integer key, Name name, Area area, String epoch) {
        return datum(key, name, "vertical", area, epoch, null, null);
    }

    public static Datum engineering(Integer key, Name name, Area area, String epoch) {
        return datum(key, name, "engineering", area, epoch, null, null);
    }

}
