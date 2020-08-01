package org.apache.sis.referencing.generate.epsgx.table;

import java.io.Serializable;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 12:17
 * modified by: $Author$
 * modified on: $Date$
 */

/**
 * General purpose factory interface.
 * With Java 8 this might become a Supplier&lt;T&gt;.
 * @param <T> to be created.
 */
public interface Allocator<T> extends Serializable {

    T allocate();
}
