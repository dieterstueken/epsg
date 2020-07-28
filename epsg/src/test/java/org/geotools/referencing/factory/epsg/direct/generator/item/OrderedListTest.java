package org.geotools.referencing.factory.epsg.direct.generator.item;

import org.geotools.referencing.factory.epsg.direct.item.IndexedSet;
import org.geotools.referencing.factory.epsg.direct.item.code.Indexed;

import java.util.List;

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

        IndexedSet<Indexed> indexed = new IndexedSet<>(root);

        indexed.add(idx(1));
        indexed.add(idx(7));
        indexed.add(idx(4));
    }
}