package org.geotools.referencing.factory.epsg.direct.item;

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

    public int code() {
        return Math.abs(code.code);
    }

    @Override
    public int getCode() {
        return code();
    }
}
