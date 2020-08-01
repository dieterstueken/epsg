package org.apache.sis.referencing.generate.factory.xepsg;

import java.io.Serializable;
import java.util.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  31.10.2014 11:37
 * modified by: $Author$
 * modified on: $Date$
 */
public class EntrySet<T> extends AbstractSet<Map.Entry<String, T>> implements Serializable {

    public static final Comparator<String> NUMERIC_ORDER =
            Comparator.comparing(String::length).
            thenComparing(Comparator.naturalOrder());

    private final List<Map.Entry<String, T>> entries = new ArrayList<>();

    @Override
    public Iterator<Map.Entry<String, T>> iterator() {
        return entries.iterator();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public void clear() {
        entries.clear();
    }

    public T get(String key) {
        int index = indexOf(key);
        return index<0 ? null : entries.get(index).getValue();
    }

    public T remove(String key) {
        int index = indexOf(key);
        return index<0 ? null : entries.remove(index).getValue();
    }

    public T put(String key, T value) {
        int index = indexOf(key);
        Map.Entry<String, T> entry = new AbstractMap.SimpleImmutableEntry<>(key, value);
        T oldValue = null;
        if(index<0) {
            entries.add(-1 - index, entry);
        } else {
            oldValue = entries.set(index, entry).getValue();
        }

        return oldValue;
    }

    public int indexOf(String key) {

        int low = 0;
        int high = entries.size()-1;

        // quick test if beyond last code
        if(high<0 || NUMERIC_ORDER.compare(entries.get(high).getKey(), key)<0)
            return -(high + 2);

        while (low <= high) {
            int mid = (low + high) >>> 1;
            String midVal = entries.get(mid).getKey();
            int cmp = NUMERIC_ORDER.compare(midVal, key);

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found
    }
}
