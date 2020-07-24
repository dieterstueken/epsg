package org.geotools.referencing.factory.epsg.direct.item;

import org.opengis.util.InternationalString;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 14:47
 * modified by: $
 * modified on: $
 */
public class Code extends Named implements Indexed {

    final int code;

    public Code(int code, InternationalString name, InternationalString remarks) {
        super(name, remarks);
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }
}
