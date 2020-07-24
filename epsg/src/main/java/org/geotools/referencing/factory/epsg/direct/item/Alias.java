package org.geotools.referencing.factory.epsg.direct.item;

import org.opengis.util.InternationalString;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 11:50
 * modified by: $Author$
 * modified on: $Date$
 */
public class Alias extends Named {

    final String scope;

    public Alias(String scope, InternationalString name, InternationalString remarks) {
        super(name, remarks);
        this.scope = scope;
    }
}
