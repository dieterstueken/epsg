package org.apache.sis.referencing.epsg;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  20.02.14 16:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordAxis extends Aliased {

    final AxisName axisName;
    final String orientation;
    final UoM unit;

    CoordAxis(Integer key, Name name,
              AxisName axisName,
              String orientation,
              UoM unit) {
        super(key, name);

        this.axisName = axisName;
        this.orientation = orientation;
        this.unit = unit;
    }

    public static CoordAxis axis(Integer key, String name, String orientation, UoM unit, AxisName axisName) {
        return new CoordAxis(key, Name.name(name), axisName, orientation, unit);
    }

}
