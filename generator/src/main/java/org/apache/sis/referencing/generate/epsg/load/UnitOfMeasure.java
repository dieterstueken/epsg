package org.apache.sis.referencing.generate.epsg.load;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 22.02.14
 * Time: 18:47
 */
public class UnitOfMeasure extends MetaData {

    Integer uom_code;
    String unit_of_meas_name;
    String unit_of_meas_type;
    Integer target_uom_code;
    Double factor_b;
    Double factor_c;

    public static final Table<UnitOfMeasure> TABLE = Table.of(UnitOfMeasure.class, "Unit of Measure", "epsg_unitofmeasure");

    @Override
    public Integer getKey() {
        return uom_code;
    }

    @Override
    public String getName() {
        return unit_of_meas_name;
    }

    public static class Builder extends MetaDataBase.Builder<UnitOfMeasure> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        public OutputAppender dumpType(UnitOfMeasure item) {

            switch(item.unit_of_meas_type) {
                case "length": out.append("UnitItem<Length>"); break;
                case "angle": out.append("UnitItem<Angle>"); break;
                case "scale": out.append("UnitItem<Dimensionless>"); break;
                default: out.append("UnitItem<?>"); break;
            }

            return out;
        }

        public OutputAppender dumpFunction(UnitOfMeasure item) {

            if(item.uom_code.equals(item.target_uom_code))
                switch(item.target_uom_code) {
                    case 9001:
                        out.append("base(METER"); break;
                    case 9101:
                        out.append("base(RADIAN"); break;
                    case 9102:
                        out.append("base(DEGREE"); break;
                    case 9201:
                        out.append("base(Unit.ONE"); break;
                    default:
                        out.append("base(?"); break;
                }
            else {

                switch(item.target_uom_code) {
                    case 9001:
                        out.append("len(_"); break;
                    case 9101:
                        out.append("rad(_"); break;
                    case 9102:
                        out.append("deg(_"); break;
                    case 9201:
                        out.append("one(_"); break;
                    default:
                        out.append("base(_"); break;
                }

                out.append(item.target_uom_code).append(".unit");

                if(item.factor_b!=null && item.factor_b!=1.0)
                    out.append(".times(").append(item.factor_b).append(')');

                if(item.factor_c!=null && item.factor_c!=1.0)
                    out.append(".divide(").append(item.factor_c).append(')');
            }

            out.append(",").indent(8).nl().append("code(");
            if(item.deprecated)
                out.append('-');

            return out;
        }

        @Override
        public OutputAppender dumpName(UnitOfMeasure item) {
            out.append(", ").quote(item.getName()).append(',');

            if(item.remarks!=null && !item.remarks.isEmpty())
                    out.nl().quote(item.remarks);
            else
                    out.append("null");

            for (Alias alias : item.aliases) {
                out.append(',').nl();
                out.append("alias(").append(alias.naming_system_code).append(", ").quote(alias.getName());
                if(!OutputAppender.isNullOrEmpty(alias.remarks))
                    out.append(", ").quote(alias.remarks);
                out.append(')');
            }

            out.append(")");

            return out.indent(-8);
        }
    }
}
