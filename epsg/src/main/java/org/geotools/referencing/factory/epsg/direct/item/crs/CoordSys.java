package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Item;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

import java.util.List;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 18:32
 * modified by: $
 * modified on: $
 */
public class CoordSys extends Item {

    public enum Type {vertical, spherical, ellipsoidal, cartesian}

    final Type type;

    final List<CoordAxis> axis;

    public CoordSys(Code code, Type type, List<CoordAxis> axis) {
        super(code);
        this.type = type;
        this.axis = axis;
    }
    static final List<Type> OPERATIONS = List.of(Type.values());

    public static Type getType(String name) {
        for (Type type : OPERATIONS) {
            if(name.equalsIgnoreCase(type.name()))
                return type;
        }
        return null;
    }

}
