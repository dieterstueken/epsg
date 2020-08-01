package org.apache.sis.referencing.generate.epsgx.table;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 17:54
 * modified by: $Author$
 * modified on: $Date$
 */
public class SparseGroup<T> extends SparseArray<SparseList<T>> {

    private final int groupRange;

    final Allocator<? extends SparseList<T>> allocator;

    public SparseGroup(int factor, int groupRange, Allocator<? extends SparseList<T>> allocator) {
        super(factor);
        this.groupRange = groupRange;
        this.allocator = allocator;
    }

    @Override
    public SparseList<T> get(int index) {

        SparseList<T> list = super.get(index);

        if(list==null) {
            list = allocator.allocate();
            set(index, list);
        }

        return list;
    }

    SparseGroup<T> expand(int factor) {

        Allocator<SparseList<T>> newAllocator = new Allocator<SparseList<T>>() {
            @Override
            public SparseList<T> allocate() {
                int factor = range();
                SparseGroup<T> newGroup = new SparseGroup<T>(factor, groupRange, allocator);
                return new SparseTree<T>(newGroup, groupRange);
            }
        };

        int newRange = range() * groupRange;

        return new SparseGroup<T>(factor, newRange, newAllocator);
    }
}
