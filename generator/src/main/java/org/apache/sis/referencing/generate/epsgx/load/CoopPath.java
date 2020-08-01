package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  03.03.14 15:16
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoopPath implements Comparable<CoopPath> {

    public Integer concat_operation_code;
    public Integer single_operation_code;
    public Short op_path_step;

    public int step() {
        return op_path_step==null? 0 : op_path_step;
    }

    @Override
    public int compareTo(CoopPath o) {
        return Integer.compare(step(), o.step());
    }

    public static final Table<CoopPath> TABLE = Table.ordered(CoopPath.class, "Coordinate_Operation Path", "epsg_coordoperationpath");
}
