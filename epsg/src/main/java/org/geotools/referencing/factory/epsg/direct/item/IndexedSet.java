package org.geotools.referencing.factory.epsg.direct.item;

import org.geotools.referencing.factory.epsg.direct.item.code.Indexed;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 19:52
 * modified by: $
 * modified on: $
 */
public class IndexedSet<T extends Indexed> extends AbstractSet<T> implements RandomAccess {

    static final Comparator<Indexed> INDEX = Comparator.comparingInt(Indexed::getCode);

    final List<T> items;

    public IndexedSet() {
        items = new ArrayList<>();
    }

    public IndexedSet(Collection<T> items) {
        this.items = new ArrayList<>(items.size());
        addAll(items);
    }

    public T tail() {
        int size = items.size();
        return size==0 ? null : items.get(size-1);
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean addAll(Collection<? extends T> items) {
        items.forEach(this::add);
        return !items.isEmpty();
    }

    @Override
    public boolean add(T item) {
        return put(item)==null;
    }
    
    public T put(T item) {

        T tail = tail();
        T replaced = null;

        if(tail==null || tail.getCode() < item.getCode()) {
            items.add(item);
        } else {
            int index = indexOf(item.getCode());
            if (index < 0)
                items.add(-index - 1, item);
           else
                replaced = items.set(index, item);
        }

        return replaced;
    }

    public T find(int code) {
        int index = indexOf(code);
        return index<0 ? null : items.get(index);
    }

    public T get(int code) {
        if(code==0)
            return null;

        T result = find(code);
        if(result==null)
            throw new NoSuchElementException("code: " + code);

        return result;
    }


    public int indexOf(int code) {
        int low = 0;
        int high = items.size()-1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            T midVal = items.get(mid);
            int cmp = Integer.compare(midVal.getCode(),code);

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
