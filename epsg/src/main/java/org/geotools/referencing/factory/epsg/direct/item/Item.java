package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import org.geotools.referencing.factory.epsg.direct.item.code.Indexed;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 18:03
 * modified by: $
 * modified on: $
 */
public class Item implements Indexed {

    public final Code code;

    public Item(Code code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return Math.abs(code.code);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s", getCode(), code.name);
    }
}
