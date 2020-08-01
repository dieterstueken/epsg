package org.apache.sis.referencing.generate.epsgx.load;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordOpParamUsage implements Comparable<CoordOpParamUsage>, Serializable {

    @Transient
    public CoordOpParam param;

    public Integer coord_op_method_code;
    public Integer parameter_code;
    public Short sort_order;
    public String param_sign_reversal;

    public static final Table<CoordOpParamUsage> TABLE = Table.ordered(CoordOpParamUsage.class, "Coordinate_Operation Parameter Usage", "epsg_coordoperationparam");

    @Transient
    public final List<CoordOpParamValue> values = new LinkedList<>();

    public OutputAppender dump(OutputAppender out) {

        out.nl().append(",usage(");
        out.appendCode("P_", parameter_code);

        if(OutputAppender.isNullOrEmpty(param_sign_reversal)) {
            out.append("Yes".equals(param_sign_reversal) ? ", true" : ", false");
        }

        if(param!=null)
            out.remark(param.getName());

        out.append(')');

        return out;
    }

    @Override
    public int compareTo(CoordOpParamUsage o) {
        int result = Integer.compare(coord_op_method_code, o.coord_op_method_code);

        if(result==0)
            result = Integer.compare(parameter_code, o.parameter_code);

        if(result==0)
            result = Integer.compare(sort_order, o.sort_order);

        return result;
    }
}
