package org.apache.sis.referencing.generate.epsg.load;

import java.io.Serializable;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordOpParamValue implements Serializable {

    public static final Table<CoordOpParamValue> TABLE = Table.of(CoordOpParamValue.class, "Coordinate_Operation Parameter Value", "epsg_coordoperationparamvalue");

    @Transient
    public CoordOpParam param;

    public Integer coord_op_code;
    public Integer coord_op_method_code;
    public Integer parameter_code;
    public Double parameter_value;
    public String param_value_file_ref;
    public Integer uom_code;

    public void dump(OutputAppender out) {

        out.nl();

        if(!OutputAppender.isNullOrEmpty(param_value_file_ref)) {
            out.append(",file(").appendCode("P_", parameter_code);
            out.append(", ").quote(param_value_file_ref);
        } else {
            out.append(",value(").appendCode("P_", parameter_code);

            if(uom_code!=null)
                out.append(", ").appendCode("U_", uom_code);

            out.append(", ").appendCode(parameter_value);
        }

        out.append(')');

        if(param!=null)
            out.remark(param.getName());
    }
}
