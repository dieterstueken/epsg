package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Alias;

import java.util.NoSuchElementException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  26.07.2020 12:03
 * modified by: $
 * modified on: $
 */
public class Items<T extends Item> extends IndexedSet<T> {

    public void aliased(Alias alias) {
        T item = find(alias.getCode());
        if(item==null)
            throw new NoSuchElementException("missing alias target: " + alias.getCode());
        item.code.aliased(alias);
    }
}
