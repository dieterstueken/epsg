package org.geotools.referencing.factory.epsg.direct.item.datum;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.BoundItem;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.code.Text;

import java.sql.Date;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 13:11
 * modified by: $Author$
 * modified on: $Date$
 */
public class Datum extends BoundItem {

    public enum Type {geodetic, engineering, vertical};

    final Type type;

    final Ellipsoid ellipsoid;

    final PrimeMeridian pm;

    final Date realizationEpoch;

    final String description;

    final Text scope;

    public Datum(Code code, Type type, Area bounds,
                 Ellipsoid ellipsoid, PrimeMeridian pm,
                 Text scope, Date realizationEpoch, String description) {
        super(code, bounds);
        this.type = type;
        this.description = description;
        this.scope = scope;
        this.realizationEpoch = realizationEpoch;
        this.ellipsoid = ellipsoid;
        this.pm = pm;
    }
}
