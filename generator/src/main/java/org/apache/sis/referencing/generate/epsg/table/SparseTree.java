package org.apache.sis.referencing.generate.epsg.table;

import java.util.Collections;
import java.util.Iterator;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.08.2014 13:51
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * Class SparseTree is a tree like implementation of a SparseList.
 * Each level expands the range by a given factor.
 *
 * @param <T>
 */
class SparseTree<T> extends SparseList<T> {

    private final SparseGroup<T> group;

    private final int groupRange;

    public SparseTree(SparseGroup<T> group, int groupRange) {
        this.group = group;
        this.groupRange = groupRange;
    }

    @Override
    public int size() {

        int count=0;

        for (SparseList<T> g : group) {
            count += g.size();
        }

        return count;
    }

    @Override
    public int range() {
        return group.range()* groupRange;
    }

    @Override
    public T get(int index) {
        SparseList<T> group = this.group.get(index / groupRange);
        return group.get(index% groupRange);
    }

    @Override
    public T set(int index, T element) {
        SparseList<T> group = this.group.get(index / groupRange);
        return group.set(index % groupRange, element);
    }

    @Override
    public void clear() {
        group.clear();
    }

    @Override
    public Iterator<T> iterator() {

        final Iterator<? extends SparseList<T>> gi = group.iterator();

        return gi.hasNext() ? new Itr(gi) : Collections.<T>emptyIterator();
    }

    class Itr implements Iterator<T> {

        final Iterator<? extends SparseList<T>> group;

        // local cursor within a group
        Iterator<T> cursor = Collections.emptyIterator();

        Itr(Iterator<? extends SparseList<T>> group) {
            this.group = group;
        }

        @Override
        public boolean hasNext() {

            // if the current cursor is exhausted
            // advance to the next non empty group (if any)
            while(!cursor.hasNext() && group.hasNext())
                cursor = group.next().iterator();

            return cursor.hasNext();
        }

        @Override
        public T next() {
            return cursor.next();
        }

        @Override
        public void remove() {
            cursor.remove();
        }
    }

    SparseTree<T> expand(int factor) {

        SparseGroup<T> newGroup = group.expand(factor);

        // this is the first entry of the new super group.
        newGroup.set(0, this);

        return new SparseTree<T>(newGroup, range());
    }

    static <T> SparseTree<T> allocate(final int groupRange, final int factor) {

        if(groupRange<1 || factor<2)
            throw new IllegalArgumentException("invalid groupSize or factor");

        Allocator<SparseArray<T>> allocator = SparseArray.allocator(groupRange);

        SparseGroup<T> newGroup = new SparseGroup<T>(factor, groupRange, allocator);

        return new SparseTree<T>(newGroup, groupRange);
    }
}
