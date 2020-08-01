package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  20.02.14 16:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class PrimeMeridian extends Aliased {

    final Double longitude;
    final UoM unit;

    PrimeMeridian(Integer key, Name name,
                  UoM unit, Double longitude) {
        super(key, name);

        this.longitude = longitude;
        this.unit = unit;
    }

    public static PrimeMeridian pm(Integer key, Name name, UoM unit, Double longitude) {
        return new PrimeMeridian(key, name, unit, longitude);
    }
}
