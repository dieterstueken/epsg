package org.apache.sis.referencing.generate.epsg.load;

import java.io.Serializable;
import java.util.Comparator;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  28.02.14 09:46
 * modified by: $Author$
 * modified on: $Date$
 */
abstract public class Item implements Serializable {

    abstract public Integer getKey();

    abstract public String getName();

    public String toString() {
        return String.format("%s[%d]: %s", getType(), getKey(), getName());
    }

    public String getType() {
        return getClass().getSimpleName();
    }

    public String getFunction() {
        return getType().toLowerCase();
    }

    public static final Comparator<Item> INDEX_ORDER = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if(o1==o2)
                return 0;

            // null last
            if(o1==null)
                return 1;

            if(o2==null)
                return -1;

            return o1.getKey().compareTo(o2.getKey());
        }
    };

    public static final Comparator<Item> NAME_ORDER = new Comparator<Item>() {
        @Override
        public int compare(Item i1, Item i2) {
            int i = Integer.compare(i1.getName().length(), i1.getName().length());
            if(i==0)
                i = i1.getName().compareTo(i2.getName());
            return i;
        }
    };

}
