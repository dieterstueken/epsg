package org.apache.sis.referencing.generate.epsgx.load;

import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 11:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class CoordOp extends Bound {

    public Integer coord_op_code;                //                      Integer INTEGER; Integer NOT; Integer NULL;,
    public String coord_op_name;                //                      Integer VARCHAR;(Integer 80;) Integer NOT; Integer NULL;,
    public String coord_op_type;                //                      Integer VARCHAR;(Integer 24;) Integer NOT; Integer NULL;,
    public Integer source_crs_code;              //                      Integer INTEGER;,
    public Integer target_crs_code;              //                      Integer INTEGER;,
    public String coord_tfm_version;            //                      Integer VARCHAR;(Integer 24;),
    public Short coord_op_variant;             //                      Integer SMALLINT;,
    public String coord_op_scope;
    public Float coord_op_accuracy;            //                      Integer FLOAT;,
    public Integer coord_op_method_code;         //                      Integer INTEGER;,
    public Integer uom_code_source_coord_diff;   //                      Integer INTEGER;,
    public Integer uom_code_target_coord_diff;   //                      Integer INTEGER;,
    public Short show_operation;               //                      Integer SMALLINT; Integer NOT; Integer NULL;,

    @Transient
    public CoordOpMethod method = null;

    @Transient
    public final List<CoordRefSys> references = new LinkedList<>();

    @Transient
    public final List<CoordOp> steps = new LinkedList<>();

    @Override
    public Integer getKey() {
        return coord_op_code;
    }

    @Override
    public String getName() {
        return coord_op_name;
    }
    public static final Table<CoordOp> TABLE = Table.of(CoordOp.class, "Coordinate_Operation", "epsg_coordoperation");

    @Transient
    public final List<CoordOpParamValue> values = new LinkedList<>();

    public static enum Type {conversion, transformation, concatenation}

    public Type type() {
        if("conversion".equals(coord_op_type))
            return Type.conversion;

        if("transformation".equals(coord_op_type))
            return Type.transformation;

        if("concatenated operation".equals(coord_op_type))
            return Type.concatenation;

        throw new RuntimeException("unknown operation:" + coord_op_type);
    }

    @Override
    public String getType() {
        return "Operation";
    }

    @Override
    public String getFunction() {
        return type().name();
    }

    public static class Builder extends Bound.Builder<CoordOp> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(CoordOp item) {

            super.dumpComments(item);

            out.addComment("scope:", item.coord_op_scope);
            out.addComment("tfm version:", item.coord_tfm_version);

            return out;
        }

        @Override
        public OutputAppender dumpTail(CoordOp item) {

            switch(item.type()) {
                case conversion: conversion(item); break;
                case transformation: transformation(item); break;
                case concatenation: concatenation(item); break;
            }

            return super.dumpTail(item);
        }

        public OutputAppender transformation(CoordOp item) {
            resolver.method(item.coord_op_method_code, out);

            resolver.crs(item.source_crs_code, out);
            resolver.crs(item.target_crs_code, out);

            for(CoordOpParamValue value:item.values)
                value.dump(out);

            return out;
        }

        public OutputAppender conversion(CoordOp item) {

            resolver.method(item.coord_op_method_code, out);

            for(CoordOpParamValue value:item.values)
                value.dump(out);

            return out;
        }

        public OutputAppender concatenation(CoordOp item) {

            resolver.crs(item.source_crs_code, out);
            resolver.crs(item.target_crs_code, out);

            for (CoordOp step : item.steps) {
                resolver.operation(step.getKey(), out);
            }

            return out;
        }
    }
}
