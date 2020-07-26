package org.geotools.referencing.factory.epsg.direct.item;

import javax.measure.Quantity;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  24.07.2020 11:20
 * modified by: $
 * modified on: $
 */
public class Value<Q extends Quantity<Q>> {

    final Number value;

    final UoM<Q> unit;

    public Value(UoM<Q> unit, Number value) {
        this.value = value;
        this.unit = unit;
    }
}
