package org.geotools.referencing.factory.epsg.direct.item.datum;

import org.geotools.referencing.factory.epsg.direct.item.Item;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

import javax.measure.quantity.Length;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 13:09
 * modified by: $Author$
 * modified on: $Date$
 */
abstract public class Ellipsoid extends Item {

    final UoM<Length> unit;

    final double semiMajorAxis;

    public Ellipsoid(Code code, UoM<Length> unit, double semiMajorAxis) {
        super(code);
        this.unit = unit;
        this.semiMajorAxis = semiMajorAxis;
    }

    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public UoM<Length> getAxisUnit() {
        return unit;
    }

    abstract double getSemiMinorAxis();

    abstract double getInvFlattening();

    public static Ellipsoid flattenedSphere(Code code, UoM<Length> unit,
                                            double semiMajorAxis, double inverseFlattening) {
        return new Ellipsoid(code, unit, semiMajorAxis) {
            @Override
            double getSemiMinorAxis() {
                return semiMajorAxis * (1 - 1/inverseFlattening);
            }

            @Override
            double getInvFlattening() {
                return inverseFlattening;
            }
        };
    }

    public static Ellipsoid ellipsoid(Code code, UoM<Length> unit,
                                      double semiMajorAxis, double semiMinorAxis) {
        return new Ellipsoid(code, unit, semiMajorAxis) {
            @Override
            double getSemiMinorAxis() {
                return semiMinorAxis;
            }

            @Override
            double getInvFlattening() {
                return semiMajorAxis / (semiMajorAxis - semiMinorAxis);
            }
        };
    }

    public static Ellipsoid sphere(Code code, UoM<Length> unit, double semiMajorAxis) {

        return new Ellipsoid(code, unit, semiMajorAxis) {

            @Override
            double getSemiMinorAxis() {
                return semiMajorAxis;
            }

            @Override
            double getInvFlattening() {
                return Double.POSITIVE_INFINITY;
            }
        };
    }
}
