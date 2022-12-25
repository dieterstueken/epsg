package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.datum.Datum;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 12:56
 * modified by: $
 * modified on: $
 */
public class Geographic extends CoordRefSys {

    final Datum datum;

    public Geographic(Code code, Area bounds, Kind type, CoordSys cs, Datum datum) {
        super(code, bounds, type, cs);
        this.datum = datum;
    }
}
