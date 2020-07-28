package org.geotools.referencing.factory.epsg.direct.item.code;

import org.geotools.referencing.factory.epsg.direct.item.Item;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 11:50
 * modified by: $Author$
 * modified on: $Date$
 */
public class Alias extends Item {

    final Scope scope;

    public Alias(Code code, Scope scope) {
        super(code);
        this.scope = scope;
    }
}
