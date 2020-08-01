package org.apache.sis.referencing.generate.epsgx.load;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.02.14
 * Time: 10:57
 */
public class CoordAxis extends Item implements Comparable<CoordAxis> {
    
    public Integer coord_axis_code; // NOT NULL UNIQUE,
    public Integer coord_sys_code; // NOT NULL,
    public Integer coord_axis_name_code; // NOT NULL,
    public String coord_axis_orientation; //(24) NOT NULL,
    public String coord_axis_abbreviation; //(24) NOT NULL,
    public Integer uom_code; // NOT NULL,
    public Short coord_axis_order; // NOT NULL,

    @Override
    public Integer getKey() {
        return coord_axis_code;
    }

    @Override
    public String getName() {
        return coord_axis_abbreviation;
    }

    @Override
    public int compareTo(CoordAxis o) {

        int result = Integer.compare(coord_axis_code, o.coord_axis_code);

        if(result==0)
            result = Integer.compare(coord_axis_order, o.coord_axis_order);

        return result;
    }

    public static final Table<CoordAxis> TABLE = Table.ordered(CoordAxis.class, "Coordinate Axis", "epsg_coordinateaxis");

    public OutputAppender dump(OutputAppender out) {

        out.nl().append(",axis(");
        out.appendCode(coord_axis_code).append(", ");
        out.quote(coord_axis_abbreviation).append(", ");
        out.quote(coord_axis_orientation).append(", ");
        out.appendCode("U_", uom_code).append(", ");
        out.appendCode("AXN_", coord_axis_name_code).append(')');

        return out;
    }
}
