package org.apache.sis.referencing.generate.epsgx.load;

import java.util.ArrayList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordOpMethod extends MetaDataBase {

    public Integer coord_op_method_code;                               //  NOT NULL,
    public String coord_op_method_name;                               // (50) NOT NULL,
    public Boolean reverse_op;                                         //  NOT NULL,
    public String formula;                                            // (4000),
    public String example;                                            // (4000),

    @Override
    public Integer getKey() {
        return coord_op_method_code;
    }

    @Override
    public String getName() {
        return coord_op_method_name;
    }

    @Override
    public String getType() {
        return "Method";
    }

    @Override
    public String getFunction() {
        return "method";
    }

    @Transient
    public final List<CoordOpParamUsage> usages = new ArrayList<>();

    public void addUsage(CoordOpParamValue value) {
        for (CoordOpParamUsage u : usages) {
            if(u.parameter_code.equals(value.parameter_code)) {
                u.values.add(value);
                return;
            }
        }

        throw new RuntimeException("value does not match any parameter");
    }

    public static final Table<CoordOpMethod> TABLE = Table.of(CoordOpMethod.class, "Coordinate_Operation Method", "epsg_coordoperationmethod");

    public static class Builder extends MetaDataBase.Builder<CoordOpMethod> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(CoordOpMethod item) {

            out.addComment("formula:", item.formula);
            out.addComment("example:", item.example);

            return super.dumpComments(item);
        }

        @Override
        public OutputAppender dumpTail(CoordOpMethod item) {

            for(CoordOpParamUsage usage:item.usages)
                usage.dump(out);

            return super.dumpTail(item);
        }
    }
}
