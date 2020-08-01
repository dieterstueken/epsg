package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  20.02.14 16:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class Ellipsoid extends Aliased {

    final Double semiMajorAxis;
    final Double semiMinorAxis;
    final Double inverseFlattening;
    final UoM unit;

    Ellipsoid(Integer key, Name name,
                     Double semiMajorAxis,
                     Double semiMinorAxis,
                     Double inverseFlattening,
                     UoM unit) {
        super(key, name);

        this.semiMajorAxis = semiMajorAxis;
        this.semiMinorAxis = semiMinorAxis;
        this.inverseFlattening = inverseFlattening;
        this.unit = unit;
    }

    public static Ellipsoid sphere(Integer key, Name name, UoM unit, Double semiMajorAxis) {

        return new Ellipsoid(key, name, semiMajorAxis, semiMajorAxis, 0.0, unit);
    }

    public static Ellipsoid flattenedSphere(Integer key, Name name,
                                            UoM unit,
                                            Double semiMajorAxis,
                                            Double inverseFlattening) {

        return new Ellipsoid(key, name, semiMajorAxis, semiMajorAxis*(1-1/inverseFlattening), inverseFlattening, unit);
    }

    public static Ellipsoid ellipsoid(Integer key, Name name,
                                      UoM unit,
                                      Double semiMajorAxis,
                                      Double semiMinorAxis) {

        return new Ellipsoid(key, name, semiMajorAxis, semiMinorAxis, semiMajorAxis/(semiMajorAxis-semiMinorAxis), unit);
    }
}
