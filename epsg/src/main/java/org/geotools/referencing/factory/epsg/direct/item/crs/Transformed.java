package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.datum.Datum;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 12:14
 * modified by: $
 * modified on: $
 */
public class Transformed extends Geographic {

    final CoordRefSys src;

    final Operation op;

    public Transformed(Code code, Area bounds, Kind kind, Short cs, Datum datum, CoordRefSys src, Operation op) {
        super(code, bounds, kind, cs, datum);
        this.src = src;
        this.op = op;
    }
}
