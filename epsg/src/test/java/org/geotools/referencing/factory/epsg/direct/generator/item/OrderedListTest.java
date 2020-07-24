package org.geotools.referencing.factory.epsg.direct.generator.item;

import org.geotools.referencing.factory.epsg.direct.item.Indexed;
import org.geotools.referencing.factory.epsg.direct.item.OrderedList;

import java.util.List;

import static org.junit.Assert.*;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 20:30
 * modified by: $
 * modified on: $
 */
public class OrderedListTest {

    Indexed idx(int i) {
        return new Indexed() {

            @Override
            public int getCode() {
                return i;
            }

            @Override
            public String toString() {
                return Integer.toString(i);
            }
        };
    }

    @org.junit.Test
    public void table() {

        List<Indexed> root = List.of(idx(4), idx(2), idx(3));

        OrderedList<Indexed> indexed = new OrderedList<>(root);

        indexed.put(idx(1));
        indexed.put(idx(7));
        indexed.put(idx(4));
    }
}