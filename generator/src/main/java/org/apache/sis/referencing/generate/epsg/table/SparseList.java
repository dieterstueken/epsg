package org.apache.sis.referencing.generate.epsg.table;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.RandomAccess;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 12:57
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class SparseList is an indexed set of elements.
 *
 * This is close to a List&lt;T&gt; but without additional insert/delete and ListIterator operations
 * or to a Map&ltInteger, T&gt; without the overhead of Integer objects.
 *
 * Iterator permits a remove() operation to set elements to null.
 *
 * SparseList is not thread safe but does not track and throw any ConcurrentModificationException.
 *
 * Most SparseList implementations manage only a fixed range of index values
 * and throw an IndexOutOfBoundsException.
 *
 * SparseTable however will adapt its size automatically.
 *
 * @param <T> the type of elements in this list
 */
abstract public class SparseList<T> extends AbstractCollection<T> implements RandomAccess, Serializable {

    @Override
    abstract public int size();

    abstract public T get(int index);

    abstract public T set(int index, T element);

    @Override
    abstract public void clear();

    abstract public int range();

}
