package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class Datum extends Bound {

    public Integer datum_code; // NOT NULL,
    public String datum_name; //(80) NOT NULL,
    public String datum_type; //(24) NOT NULL,
    public String origin_description; //(254),
    public Short realization_epoch; //,
    public Integer ellipsoid_code; //,
    public Integer prime_meridian_code; //,
    public String datum_scope; //(254) NOT NULL,

    @Override
    public Integer getKey() {
        return datum_code;
    }

    @Override
    public String getName() {
        return datum_name;
    }

    public String getFunction() {
        return datum_type;
    }

    public static final Table<Datum> TABLE = Table.of(Datum.class, "Datum", "epsg_datum");

    //public static final Declarator DECL = new Declarator("public static final", "Datum", "datum");

    public static class Builder extends Bound.Builder<Datum> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(Datum item) {

            super.dumpComments(item);

            out.addComment("scope:", item.datum_scope);
            out.addComment("origin description:", item.origin_description);

            return out;
        }

        @Override
        public OutputAppender dumpTail(Datum item) {

            out.nl().append(',');

            if(item.realization_epoch!=null)
                out.append(item.realization_epoch.toString());
            else
                out.append("0");

            if("geodetic".equals(item.datum_type)) {
                resolver.ellipsoid(item.ellipsoid_code, out);
                resolver.meridian(item.prime_meridian_code, out);
            }

            return super.dumpTail(item);
        }
    }
}
