package org.apache.sis.referencing.generate.factory.xepsg;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Set;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  31.10.2014 12:44
 * modified by: $Author$
 * modified on: $Date$
 */
public class EntryMap<T> extends AbstractMap<String, T> implements Serializable {

    final EntrySet<T> entries;

    public EntryMap(EntrySet<T> entries) {
        this.entries = entries;
    }

    public EntryMap() {
        this(new EntrySet<>());
    }

    @Override
    public Set<Entry<String, T>> entrySet() {
        return entries;
    }

    @Override
    public boolean containsValue(Object value) {
        return super.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof String && entries.indexOf((String) key) >= 0;
    }

    @Override
    public T get(Object key) {
        return key instanceof String ? entries.get((String) key) : null;
    }

    @Override
    public T put(String key, T value) {
        return entries.put(key, value);
    }

    @Override
    public T remove(Object key) {
        return key instanceof String ? entries.remove((String) key) : null;
    }
}
