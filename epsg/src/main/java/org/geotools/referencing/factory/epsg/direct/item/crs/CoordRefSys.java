package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.BoundItem;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 12:52
 * modified by: $
 * modified on: $
 */
public class CoordRefSys extends BoundItem {

    public enum Kind {projected, geographic2D, geographic3D,
        geocentric, vertical, compound, engineering};

    final Kind type;

    final Short cs;

    public CoordRefSys(Code code, Area bounds, Kind type, Short cs) {
        super(code, bounds);
        this.type = type;
        this.cs = cs;
    }

    public static CoordRefSys.Kind getKind(String name) {

        if(name.endsWith("2D"))
            return CoordRefSys.Kind.geographic2D;

        if(name.endsWith("3D"))
            return CoordRefSys.Kind.geographic3D;

        return CoordRefSys.Kind.valueOf(name);
    }
}
