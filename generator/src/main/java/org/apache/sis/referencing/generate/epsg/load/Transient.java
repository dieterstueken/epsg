package org.apache.sis.referencing.generate.epsg.load;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  14.02.14 18:19
 * modified by: $Author$
 * modified on: $Date$
 */

@Retention(RUNTIME) @Target(FIELD)
public @interface Transient {
    boolean value() default true;
}
