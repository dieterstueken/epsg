package org.apache.sis.referencing.generate.epsgx.table;

import java.util.Iterator;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 13:56
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class SparseTable is a wrapper around a managed SparseTree to implement automatic size adaption.
 */
public class SparseTable<T> extends SparseList<T> {

    final int factor;

    private SparseTree<T> tree;

    SparseTable(int groupSize, int factor) {
        this.factor = factor;
        tree = SparseTree.allocate(groupSize, factor);
    }

    @Override
    public T get(int index) {
        return tree.get(index);
    }

    @Override
    public T set(int index, T element) {
        ensureCapacity(index);
        return tree.set(index, element);
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public int range() {
        return tree.range();
    }

    @Override
    public Iterator<T> iterator() {
        return tree.iterator();
    }

    @Override
    public void clear() {
        tree.clear();
    }

    public void ensureCapacity(int range) {
        while(tree.range()<=range)
            tree = tree.expand(factor);
    }

    public static <T> SparseTable<T> create(int groupSize, int factor) {
        return new SparseTable<T>(groupSize, factor);
    }

    public static <T> SparseTable<T> create() {
        return create(10, 10);
    }
}
