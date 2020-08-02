package org.apache.sis.referencing.generate.epsg.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordRefSys extends Bound {

    public Integer coord_ref_sys_code; // NOT NULL,
    public String coord_ref_sys_name; //(80) NOT NULL,
    public String coord_ref_sys_kind; //(24) NOT NULL,
    public Integer coord_sys_code; //,
    public Integer datum_code; //,
    public Integer source_geogcrs_code; //,
    public Integer projection_conv_code; //,
    public Integer cmpd_horizcrs_code; //,
    public Integer cmpd_vertcrs_code; //,
    public String crs_scope; //(254) NOT NULL,
    public Short show_crs; // NOT NULL,

    @Override
    public Integer getKey() {
        return coord_ref_sys_code;
    }

    @Override
    public String getName() {
        return coord_ref_sys_name;
    }

    public static int getGroup(int code) {
        return code<100000 ? code/100 : code / 100000;
    }

    public static final Table<CoordRefSys> TABLE = Table.of(CoordRefSys.class, "Coordinate Reference System", "epsg_coordinatereferencesystem");

    public static enum Type {geocentric, geographic2D, geographic3D, projected, vertical, compound, engineering}

    public Type type() {
        switch(coord_ref_sys_kind) {
            case("geocentric"): return Type.geocentric;
            case("geographic 2D"): return Type.geographic2D;
            case("geographic 3D"): return Type.geographic3D;
            case("projected"): return Type.projected;
            case("vertical"): return Type.vertical;
            case("compound"): return Type.compound;
            case("engineering"): return Type.engineering;
        }

        throw new RuntimeException("invalid crs kind: " + coord_ref_sys_kind);
    }

    @Override
    public String getFunction() {
        return type().name();
    }

    public static class Builder extends Bound.Builder<CoordRefSys> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(CoordRefSys item) {
            out.addComment("scope:", item.crs_scope);

            return super.dumpComments(item);
        }

        @Override
        public OutputAppender dumpTail(CoordRefSys item) {

            resolver.cs(item.coord_sys_code, out);

            switch(item.type()) {
                case geocentric: geocentric(item); break;
                case geographic2D:
                case geographic3D: geographic(item); break;
                case projected: projected(item); break;
                case vertical: vertical(item); break;
                case compound: compound(item); break;
                case engineering: engineering(item); break;
            }

            return super.dumpTail(item);
        }

        private void geographic(CoordRefSys item) {
            resolver.datum(item.datum_code, out);
            resolver.crs(item.source_geogcrs_code, out);
            resolver.operation(item.projection_conv_code, out);
        }

        private void geocentric(CoordRefSys item) {
            resolver.datum(item.datum_code, out);
        }

        private void projected(CoordRefSys item) {
            resolver.crs(item.source_geogcrs_code, out);
            resolver.operation(item.projection_conv_code, out);
        }

        private void vertical(CoordRefSys item) {
            resolver.datum(item.datum_code, out);
        }

        private void compound(CoordRefSys item) {
            resolver.crs(item.cmpd_horizcrs_code, out);
            resolver.crs(item.cmpd_vertcrs_code, out);
        }

        private void engineering(CoordRefSys item) {
            resolver.datum(item.datum_code, out);
        }
    }

}
