package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Named;
import org.opengis.util.InternationalString;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 18:36
 * modified by: $
 * modified on: $
 */
public class CoordAxis extends Named {
    public final InternationalString description;
    public final InternationalString abbreviation;
    public final InternationalString orientation;
    public final UoM<?> unit;

    public CoordAxis(InternationalString name, InternationalString remarks,
                     InternationalString description, InternationalString abbreviation,
                     UoM<?> unit, InternationalString orientation) {
        super(name, remarks);

        this.description = description;
        this.abbreviation = abbreviation;
        this.orientation = orientation;
        this.unit = unit;
    }
}
