package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Code;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 13:10
 * modified by: $Author$
 * modified on: $Date$
 */
public class BoundItem extends Item {

    final Area bounds;

    public BoundItem(Code code, Area bounds) {
        super(code);
        this.bounds = bounds;
    }
}
