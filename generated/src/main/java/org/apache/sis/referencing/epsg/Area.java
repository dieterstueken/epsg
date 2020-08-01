package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.02.14 10:12
 * modified by: $Author$
 * modified on: $Date$
 */
public class Area extends Aliased {

    static class Bounds {
        final double s, n, w, e;

        Bounds(double s, double n, double e, double w) {
            this.e = e;
            this.s = s;
            this.n = n;
            this.w = w;
        }
    }

    public static Bounds bounds(double e, double s, double n, double w) {
        return new Bounds(s, n, e, w);
    }

    public static Bounds bounds(String file) {
        return null;
    }

    final Bounds bounds;

    public Area(Integer key, Name name, Bounds bounds) {
        super(key, name);
        this.bounds = bounds;
    }

    public static Area area(Integer key, Name name, Bounds bounds) {
        return new Area(key, name, bounds);
    }

    public static Area area(Integer key, Name name, double e, double s, double n, double w) {
        return new Area(key, name, bounds(s, n, e, w));
    }

    public static Area area(Area area) {
        return area;
    }
}
