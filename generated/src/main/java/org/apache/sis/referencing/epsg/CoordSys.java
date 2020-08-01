package org.apache.sis.referencing.epsg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  20.02.14 16:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordSys {

    final Integer key;
    final String name;
    final List<CoordAxis> axes;

    CoordSys(Integer key, String name,
             List<CoordAxis> axes) {
        this.key = key;
        this.name = name;
        this.axes = axes;
    }

    public static CoordSys coordsys(Integer key, String name, List<CoordAxis> axes) {
        return new CoordSys(key, name, axes);
    }

    public static CoordSys vertical(Integer key, String name, CoordAxis axis) {
        return coordsys(key, name, Collections.unmodifiableList(Collections.singletonList(axis)));
    }

    public static CoordSys cartesian(Integer key, String name, CoordAxis x, CoordAxis y) {
        return coordsys(key, name, Collections.unmodifiableList(Arrays.asList(x,y)));
    }

    public static CoordSys cartesian(Integer key, String name, CoordAxis x, CoordAxis y, CoordAxis z) {
        return coordsys(key, name, Collections.unmodifiableList(Arrays.asList(x,y,z)));
    }

    public static CoordSys ellipsoidal(Integer key, String name, CoordAxis x, CoordAxis y) {
        return coordsys(key, name, Collections.unmodifiableList(Arrays.asList(x,y)));
    }

    public static CoordSys ellipsoidal(Integer key, String name, CoordAxis x, CoordAxis y, CoordAxis z) {
        return coordsys(key, name, Collections.unmodifiableList(Arrays.asList(x,y,z)));
    }

    public static CoordSys spherical(Integer key, String name, CoordAxis x, CoordAxis y, CoordAxis z) {
        return coordsys(key, name, Collections.unmodifiableList(Arrays.asList(x,y,z)));
    }
}
