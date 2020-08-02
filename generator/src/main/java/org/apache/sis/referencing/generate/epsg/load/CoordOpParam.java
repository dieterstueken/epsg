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
public class CoordOpParam extends MetaDataBase {

    Integer parameter_code;
    String parameter_name;
    String description;

    @Override
    public Integer getKey() {
        return parameter_code;
    }

    @Override
    public String getName() {
        return parameter_name;
    }

    @Transient
    public final List<CoordOpParamUsage> usages = new LinkedList<>();

    public static final Table<CoordOpParam> TABLE = Table.of(CoordOpParam.class, "Coordinate_Operation Parameter", "epsg_coordoperationparam");

    @Override
    public String getType() {
        return "Parameter";
    }

    @Override
    public String getFunction() {
        return "parameter";
    }

    public static class Builder extends MetaDataBase.Builder<CoordOpParam> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(CoordOpParam item) {

            out.addComment("description:", item.description);

            return super.dumpComments(item);
        }
    }
}
