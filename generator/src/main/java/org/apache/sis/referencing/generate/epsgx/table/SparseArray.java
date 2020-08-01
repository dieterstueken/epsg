package org.apache.sis.referencing.generate.epsgx.table;

import java.util.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 11:29
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class SparseArray is a fixed range implementation of a SparseList similar to an ArrayList.
 *
 * An internal array is allocated lazily and grows up to the given maximum range.
 *
 * A SparseArray may be used to allocate further empty SparseArrays.
 *
 * @param <T> the type of elements in this list
 */
public class SparseArray<T> extends SparseList<T> {

    private static final Object[] EMPTY = {};

    private Object array[] = EMPTY;

    final int range;

    public SparseArray(int range) {
        this.range = range;
    }

    @Override
    public int size() {
        int count = 0;

        for (Object o : array) {
            if(o!=null)
                ++count;
        }
        return count;
    }

    @Override
    public int range() {
        return range;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        rangeCheck(index);

        Object elements[] = this.array;
        return index<elements.length ? (T) elements[index] : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        rangeCheck(index);

        Object elements[] = this.array;

        if(elements.length<=index) {

            // don't expand the array if the missing element is set to null anyway.
            if(element==null)
                return null;

            // minimum capacity is 10
            int newCapacity = Math.max(elements.length, 10);

            // grow until reached
            while(newCapacity<=index)
                newCapacity *= 10;

            // truncate by given range
            newCapacity = Math.min(newCapacity, range);

            elements = Arrays.copyOf(elements, newCapacity);
            this.array = elements;
        }

        T oldValue = (T) elements[index];
        elements[index] = element;

        return oldValue;
    }

    @Override
    public void clear() {
        this.array = EMPTY;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= range)
            throw new IndexOutOfBoundsException(String.format("Index: %d, Size: %d", index, range));
    }

    /**
     * Lookup the next index with non null content or -1 if none
     * @param index to start searching (inclusive).
     * @return first non null index or -1.
     */
    private int nextElement(int index) {
        Object elements[] = this.array;

        for(;index<elements.length; ++index)
            if(elements[index]!=null)
                return index;

        return -1;
    }

    @Override
    public Iterator<T> iterator() {
        int cursor = nextElement(0);
        return cursor<0 ? Collections.<T>emptyIterator() : new Itr(cursor);
    }

    class Itr implements Iterator<T> {
        int cursor;         // index of next element to return or -1 if done
        int lastRet = -1;   // Index of element returned by most recent call to next

        Itr(int cursor) {
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor>=0;
        }

        @Override
        public T next() {
            int i = cursor;
            lastRet = i;
            cursor = nextElement(i + 1);
            return get(i);
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            set(lastRet, null);
        }
    }

    public static <T> SparseArray<T> allocate(int range) {
        return new SparseArray<T>(range);
    }

    public static <T> Allocator<SparseArray<T>> allocator(final int range) {
        return  new Allocator<SparseArray<T>>() {
            @Override
            public SparseArray<T> allocate() {
                return SparseArray.allocate(range);
            }
        };
    }
}
