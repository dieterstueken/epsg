package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 10:17
 * modified by: $Author$
 * modified on: $Date$
 */
public class Scope extends MetaData {

    Integer naming_system_code;
    String naming_system_name;

    public Integer getKey() {
        return naming_system_code;
    }

    public String getName() {
        return naming_system_name;
    }

    @Override
    public String getType() {
        return "NameSpace";
    }

    @Override
    public String getFunction() {
        return "scope";
    }

    public static final Table<Scope> TABLE = Table.of(Scope.class, "Naming System", "epsg_namingsystem");

    public static class Builder extends MetaDataBase.Builder<Scope> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        public OutputAppender dumpName(Scope item) {
            return out.append(",").indent(4).nl().quote(item.getName());
        }

        @Override
        public OutputAppender dumpTail(Scope item) {

            if(!OutputAppender.isNullOrEmpty(item.remarks)) {
                out.append(',').nl().quote(item.remarks);
            }

            out.indent(-4);

            return super.dumpTail(item);
        }
    }
}
