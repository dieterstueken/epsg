package org.geotools.referencing.factory.epsg.direct.item;

import org.opengis.util.InternationalString;

import java.io.Serializable;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  30.10.2014 11:49
 * modified by: $Author$
 * modified on: $Date$
 */
public class Named implements Serializable {

    public final InternationalString name;

    public final InternationalString remarks;

    public Named(InternationalString name, InternationalString remarks) {
        this.name = name;
        this.remarks = remarks;
    }
}
