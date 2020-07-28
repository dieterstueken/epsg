package org.geotools.referencing.factory.epsg.direct.item.datum;


import org.geotools.referencing.factory.epsg.direct.item.Item;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

import javax.measure.quantity.Angle;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 13:05
 * modified by: $Author$
 * modified on: $Date$
 */
public class PrimeMeridian extends Item {

    final double greenwichLongitude;

    final UoM<Angle> angularUnit;

    public PrimeMeridian(Code code, double greenwichLongitude, UoM<Angle> angularUnit) {
        super(code);
        this.greenwichLongitude = greenwichLongitude;
        this.angularUnit = angularUnit;
    }
}
