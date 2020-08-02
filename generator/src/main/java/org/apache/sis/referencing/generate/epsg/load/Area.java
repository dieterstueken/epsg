package org.apache.sis.referencing.generate.epsg.load;

import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class Area extends MetaData {

    public Integer area_code;
	public String area_name;
	public String area_of_use;
	public Double area_south_bound_lat;
	public Double area_north_bound_lat;
	public Double area_west_bound_lon;
	public Double area_east_bound_lon;
	public String area_polygon_file_ref;
	public String iso_a2_code;
	public String iso_a3_code;
    public Integer iso_n_code;

    @Override
    public Integer getKey() {
        return area_code;
    }

    @Override
    public String getName() {
        return area_name;
    }

    @Transient
    public final List<Bound> references = new LinkedList<>();

    public static final Table<Area> TABLE = Table.of(Area.class, "Area", "epsg_area");

    public static class Builder extends MetaData.Builder<Area> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(Area item) {

            super.dumpComments(item);

            out.addComment("area_of_use:", item.area_of_use);

            out.nl().append("// usages:").appendNumber(item.references.size());

            for (Bound ref : item.references)
                out.nl().append("//    ").append(ref.toString());

             out.nl().append("//");

            return out;
        }

        @Override
        public OutputAppender dumpTail(Area item) {

            if(item.area_south_bound_lat!=null && item.area_north_bound_lat!=null && item.area_west_bound_lon!=null & item.area_east_bound_lon!=null)
                out.nl().format(",bounds(%s, %s, %s, %s)",
                        item.area_south_bound_lat, item.area_north_bound_lat, item.area_west_bound_lon, item.area_east_bound_lon);
            else
            if(!OutputAppender.isNullOrEmpty(item.area_polygon_file_ref))
                out.nl().append(",polygonFile(").quote(item.area_polygon_file_ref).append(')');
            else
                out.nl().append(",null");

            return super.dumpTail(item);
        }
    }
}
