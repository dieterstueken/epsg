package org.geotools.referencing.factory.epsg.direct.item.crs;

import org.geotools.referencing.factory.epsg.direct.item.Area;
import org.geotools.referencing.factory.epsg.direct.item.BoundItem;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;

import java.util.List;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  21.12.2022 12:58
 * modified by: $
 * modified on: $
 */
public class Operation extends BoundItem {

    public enum Type {conversion, transformation, concatenated};

    final Type type;

    public Operation(Code code, Type type, Area bounds) {
        super(code, bounds);
        this.type = type;
    }


    static final List<Type> OPERATIONS = List.of(Operation.Type.values());

    public static Operation.Type getType(String name) {
        for (Operation.Type type : OPERATIONS) {
            if(name.startsWith(type.name()))
                return type;
        }
        return null;
    }
}
