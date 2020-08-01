package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.02.14 12:20
 * modified by: $Author$
 * modified on: $Date$
 */
public class Bound extends Aliased {

    final Area area;

    public Bound(Integer key, Name name, Area area) {
        super(key, name);
        this.area = area;
    }
}
