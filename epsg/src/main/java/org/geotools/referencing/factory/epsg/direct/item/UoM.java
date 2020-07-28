package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Code;

import javax.measure.Quantity;
import javax.measure.Unit;


/**
 * version:     $Revision$
 * created by:  dst
 * created on:  14.02.14 19:00
 * modified by: $Author$
 * modified on: $Date$
 */
public class UoM<Q extends Quantity<Q>> extends Item {

    public final Unit<Q> unit;

    public UoM(Code code, Unit<Q> unit) {
        super(code);
        this.unit = unit;
    }

    public Unit<Q> multiply(long factor) {
        return unit.multiply(factor);
    }

    public Unit<Q> divide(long factor) {
        return unit.divide(factor);
    }

    public Value<Q> of(Number value) {
        return new Value<Q>(this, value);
    }

    public <T extends Quantity<T>> UoM<T> asType(Class<T> type) {
        unit.asType(type);
        return (UoM<T>) this;
    }
}
