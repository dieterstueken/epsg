package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.util.InternationalString;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 17:50
 * modified by: $
 * modified on: $
 */
public class Area extends Item {

    final InternationalString use;

    final GeographicBoundingBox bounds;

    final String a2;

    final String a3;

    public Area(Code code, InternationalString use, GeographicBoundingBox bounds, String a2, String a3) {
        super(code);
        this.bounds = bounds;
        this.use = use;
        this.a2 = a2;
        this.a3 = a3;
    }
}
