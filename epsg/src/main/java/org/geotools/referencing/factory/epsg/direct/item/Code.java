package org.geotools.referencing.factory.epsg.direct.item;

import org.opengis.util.InternationalString;

import static org.geotools.text.Text.text;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 14:47
 * modified by: $
 * modified on: $
 */
public class Code extends Named implements Indexed {

    public final int code;

    public Code(int code, InternationalString name, InternationalString remarks) {
        super(name, remarks);
        this.code = code;
    }

    public static Code code(int code, String name, String remarks) {
        return new Code(code, text(name), text(remarks));
    }

    public static Code code(int code, String name) {
        return new Code(code, text(name), Text.EMPTY);
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
