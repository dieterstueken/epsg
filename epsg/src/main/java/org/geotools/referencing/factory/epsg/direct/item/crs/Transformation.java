package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 17:53
 * modified by: $
 * modified on: $
 */
public class Transformation extends Operation {

    final CoordRefSys source;
    final CoordRefSys target;


    public Transformation(Code code, Type type, Area bounds, CoordRefSys source, CoordRefSys target) {
        super(code, type, bounds);
        this.source = source;
        this.target = target;
    }
}
