package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 12:14
 * modified by: $
 * modified on: $
 */
public class Compound extends CoordRefSys {

    final CoordRefSys hor;

    final CoordRefSys vert;

    public Compound(Code code, Area bounds, Kind kind, CoordSys cs, CoordRefSys hor, CoordRefSys vert) {
        super(code, bounds, kind, cs);
        this.hor = hor;
        this.vert = vert;
    }
}
