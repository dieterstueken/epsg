package org.apache.sis.referencing.generate.epsg.load;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 16.02.14
 * Time: 11:05
 */
public class Ellipsoid extends MetaData {


    Integer ellipsoid_code; // NOT NULL,
    String ellipsoid_name; //(80) NOT NULL,
    Double semi_major_axis; // NOT NULL,
    Integer uom_code; // NOT NULL,
    Double inv_flattening; //,
    Double semi_minor_axis; //,
    Short ellipsoid_shape; // NOT NULL,

    public static enum Type {ellipsoid, flattenedSphere, sphere}

    @Override
    public Integer getKey() {
        return ellipsoid_code;
    }

    @Override
    public String getName() {
        return ellipsoid_name;
    }

    public Type type() {
        if(inv_flattening!=null && inv_flattening!=0.0)
            return Type.flattenedSphere;

        return semi_minor_axis.equals(semi_major_axis) ? Type.sphere : Type.ellipsoid;
    }

    public String getFunction() {
        return type().name();
    }

    public static final Table<Ellipsoid> TABLE = Table.of(Ellipsoid.class, "Ellipsoid", "epsg_ellipsoid");

    public static class Builder extends MetaDataBase.Builder<Ellipsoid> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpTail(Ellipsoid item) {

            resolver.unit(item.uom_code, out);
            out.nl().append(',').appendCode(item.semi_major_axis);

            switch(item.type()) {

                case ellipsoid:
                    out.nl().append(',').appendCode(item.semi_minor_axis);
                    break;

                case flattenedSphere:
                    out.nl().append(',').appendCode(item.inv_flattening);
                    break;

                case sphere:
                    break;
            }

            return super.dumpTail(item);
        }
    }
}
