package org.apache.sis.referencing.generate.epsg.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class PrimeMeridian extends MetaData {

    Integer prime_meridian_code; // NOT NULL,
    String prime_meridian_name; //(80) NOT NULL,
    Double greenwich_longitude; // PRECISION NOT NULL,
    Integer uom_code; // NOT NULL,

    @Override
    public Integer getKey() {
        return prime_meridian_code;
    }

    @Override
    public String getName() {
        return prime_meridian_name;
    }

    @Override
    public String getType() {
        return "PrimeMeridian";
    }

    @Override
    public String getFunction() {
        return "pm";
    }

    public static final Table<PrimeMeridian> TABLE = Table.of(PrimeMeridian.class, "Prime Meridian", "epsg_primemeridian");

    public static class Builder extends MetaDataBase.Builder<PrimeMeridian> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpTail(PrimeMeridian item) {

            resolver.unit(item.uom_code, out);
            out.nl().append(',').appendCode(item.greenwich_longitude);

            return super.dumpTail(item);
        }
    }
}
