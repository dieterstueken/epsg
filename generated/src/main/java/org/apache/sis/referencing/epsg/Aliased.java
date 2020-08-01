package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 14:50
 * modified by: $Author$
 * modified on: $Date$
 */
public class Aliased  {

    final Integer key;

    public final Name name;

    public Aliased(Integer key, Name name) {
        super();

        this.key = key;
        this.name = name;
    }
}
