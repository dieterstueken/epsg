package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import si.uom.quantity.AngularSpeed;
import tech.units.indriya.unit.ProductUnit;
import tech.units.indriya.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javax.measure.MetricPrefix.MILLI;
import static org.geotools.measure.Units.ONE;
import static org.geotools.measure.Units.RADIAN;
import static org.geotools.measure.Units.YEAR;
import static si.uom.SI.RADIAN_PER_SECOND;
import static si.uom.SI.SECOND;
import static systems.uom.common.USCustomary.DEGREE_ANGLE;
import static systems.uom.common.USCustomary.METER;
import static tech.units.indriya.unit.Units.HERTZ;

//import static systems.uom.unicode.CLDR.METER_PER_SECOND;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  24.07.2020 16:17
 * modified by: $
 * modified on: $
 */
public class UoMs extends Aliases {

    public static final Items<UoM<?>> BASE_UNITS = new Items<>();

    static <Q extends Quantity<Q>> UoM<Q> uom(int code, Unit<Q> unit, String name) {
        var uom = new UoM<>(Code.code(code, name), unit);
        if(!BASE_UNITS.add(uom))
            throw new IllegalArgumentException("duplicate entry");
        return uom;
    }

    static final Unit<Angle> DM = DEGREE_ANGLE;
    static final Unit<Angle> DMS = DEGREE_ANGLE;
    static final Unit<Angle> DMS_SCALED = DEGREE_ANGLE;
    static final Unit<Angle> DREP = DEGREE_ANGLE;

    static final Unit<Time> TROPICAL_YEAR = YEAR; //SECOND.multiply(31556925.445);

    public static final UoM<Speed> U_1026 = uom(1026, Units.METRE_PER_SECOND, "metre");

    public static final UoM<Time> U_1029 = uom(1029, TROPICAL_YEAR, "year");
    public static final UoM<Speed> U_1027 = uom(1027, new ProductUnit<Speed>(MILLI(METER).divide(TROPICAL_YEAR)), "millimetres per year");


    public static final UoM<AngularSpeed> U_1035 = uom(1035, RADIAN_PER_SECOND, "radian per second");
    public static final UoM<Frequency> U_1036 = uom(1036, HERTZ, "unity per second");
    public static final UoM<Time> U_1040 = uom(1040, SECOND, "second");
    public static final UoM<Length> U_9001 = uom(9001, METER, "metre");
    public static final UoM<Angle> U_9101 = uom(9101, RADIAN, "radian");
    public static final UoM<Angle> U_9102 = uom(9102, DEGREE_ANGLE, "degree");
    public static final UoM<Dimensionless> U_9201 = uom(9201, ONE, "unity");

    public static final UoM<Angle> U_9107 = uom(9107, DREP, "degree minute second");
    public static final UoM<Angle> U_9108 = uom(9108, DMS_SCALED, "degree minute second hemisphere");

    public static final UoM<Angle> U_9110 = uom(9110, DMS, "sexagesimal DMS");
    public static final UoM<Angle> U_9111 = uom(9111, DM, "sexagesimal DM");

    public static final UoM<Angle> U_9115 = uom(9115, DREP, "degree minute");
    public static final UoM<Angle> U_9116 = uom(9116, DREP, "degree hemisphere");
    public static final UoM<Angle> U_9117 = uom(9117, DREP, "hemisphere degree");
    public static final UoM<Angle> U_9118 = uom(9118, DREP, "degree minute hemisphere");
    public static final UoM<Angle> U_9119 = uom(9119, DREP, "hemisphere degree minute");
    public static final UoM<Angle> U_9120 = uom(9120, DREP, "hemisphere degree minute second");
    public static final UoM<Angle> U_9121 = uom(9121, DREP, "sexagesimal DMS.s");

    //////////////////////////////////

    final Items<UoM<?>> units = new Items<>();

    UoMs(Connection conn) throws SQLException {
        super(conn);

        YEAR.toString();

        units.addAll(BASE_UNITS);
    }

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addUnit, "select UOM_CODE, UNIT_OF_MEAS_NAME, REMARKS, DEPRECATED, " +
                "TARGET_UOM_CODE, FACTOR_B, FACTOR_C " +
                "from EPSG_UNITOFMEASURE");

        aliases("epsg_unitofmeasure", units::aliased);
    }

    void addUnit(ResultSet rs) throws SQLException {
        var unit = unit(rs);
        units.add(unit);
    }

    UoM<?> unit(ResultSet rs) throws SQLException {
        var code = codep(rs);

        UoM<?> base = units.find(code.getCode());
        if(base !=null) {
            return new UoM<>(code, base.unit);
        }

        int target = rs.getInt(5);
        if(target==code.getCode())
            throw new IllegalStateException("unexpected self defined unit: " + target);

        base = units.find(target);
        double a = rs.getDouble(6);
        double b = rs.getDouble(7);

        Unit<?> u = base.unit.multiply(a).divide(b);
        String name = u.toString();
        return new UoM<>(code, u);
    }
}
