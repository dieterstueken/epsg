package org.apache.sis.referencing.epsg;

/**
* version:     $Revision$
* created by:  dst
* created on:  20.02.14 20:12
* modified by: $Author$
* modified on: $Date$
*/
public class AxisName {

    final Integer key;

    final String name;

    final String description;

    final String remarks;

    public AxisName(Integer key, String name, String description, String remarks) {
        this.key = key;
        this.name = name;
        this.description = description;
        this.remarks = remarks;
    }

    public static AxisName axisName(Integer key, String name, String description, String remarks) {
        return new AxisName(key, name, description, remarks);
    }

    public static AxisName axisName(Integer key, String name, String description) {
        return new AxisName(key, name, description, "");
    }

    public static AxisName axisName(Integer key, String name) {
        return new AxisName(key, name, "", "");
    }
}
