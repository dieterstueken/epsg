package org.apache.sis.referencing.generate.epsgx.load;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.02.14
 * Time: 10:57
 */
public class CoordAxisName extends MetaData {
    
    Integer coord_axis_name_code; // NOT NULL,
    String coord_axis_name; //(80) NOT NULL,
    String description; //(254),

    @Override
    public Integer getKey() {
        return coord_axis_name_code;
    }

    @Override
    public String getName() {
        return coord_axis_name;
    }

    @Override
    public String getType() {
        return "AxisName";
    }

    @Override
    public String getFunction() {
        return "axisName";
    }

    public static final Table<CoordAxisName> TABLE = Table.of(CoordAxisName.class, "Coordinate Axis Name", "epsg_coordinateaxisname");

    public static class Builder extends MetaDataBase.Builder<CoordAxisName> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(CoordAxisName item) {

            out.addComment("description:", item.description);

            return super.dumpComments(item);
        }

        @Override
        public OutputAppender dumpName(CoordAxisName item) {
            return out.append(", ").quote(item.getName());
        }
    }
}
