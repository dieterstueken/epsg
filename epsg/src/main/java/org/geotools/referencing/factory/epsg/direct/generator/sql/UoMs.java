package org.geotools.referencing.factory.epsg.direct.generator.sql;

import org.geotools.measure.Units;
import org.geotools.referencing.factory.epsg.direct.item.Items;
import org.geotools.referencing.factory.epsg.direct.item.UoM;
import org.geotools.referencing.factory.epsg.direct.item.code.Code;
import si.uom.NonSI;
import si.uom.SI;
import si.uom.quantity.AngularSpeed;
import systems.uom.common.USCustomary;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.unit.ProductUnit;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Time;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static tech.units.indriya.unit.Units.METRE_PER_SECOND;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  24.07.2020 16:17
 * modified by: $
 * modified on: $
 */
public class UoMs extends Aliases {

    final Items<UoM<?>> units = new Items<>();

    static final Unit<Angle> DM = NonSI.DEGREE_ANGLE;
    static final Unit<Angle> DMS = Units.SEXAGESIMAL_DMS;
    static final Unit<Angle> DMS_SCALED = NonSI.DEGREE_ANGLE;
    static final Unit<Angle> DREP = NonSI.DEGREE_ANGLE;

    static final double RPI = 3.14159265358979;

    static final Unit<Length> MILLI_METRE = MetricPrefix.MILLI(SI.METRE);
    static final Unit<Time> MILLI_SECONDS = MetricPrefix.MILLI(SI.SECOND);
    static final Unit<Time> TROPICAL_YEAR = MILLI_SECONDS.multiply(31556925445.0);

    public final UoM<Length> U_1025 = uom(1025, MetricPrefix.MILLI(SI.METRE));
    public final UoM<Speed> U_1026 = uom(1026, METRE_PER_SECOND);

    public final UoM<Speed> U_1027 = uom(1027, new ProductUnit<Speed>(MILLI_METRE.divide(TROPICAL_YEAR)));

    public final UoM<Time> U_1029 = uom(1029, TROPICAL_YEAR );

    public final UoM<AngularSpeed> U_1035 = uom(1035, SI.RADIAN_PER_SECOND);
    public final UoM<Frequency> U_1036 = uom(1036, SI.HERTZ);
    public final UoM<Time> U_1040 = uom(1040, SI.SECOND);

    public final UoM<Length> U_9001 = uom(9001, SI.METRE);
    public final UoM<Length> U_9002 = uom(9002, USCustomary.FOOT);
    public final UoM<Length> U_9030 = uom(9030, USCustomary.NAUTICAL_MILE);
    public final UoM<Length> U_9036 = uom(9036, MetricPrefix.KILO(SI.METRE));
    public final UoM<Angle> U_9101 = uom(9101, SI.RADIAN);
    public final UoM<Angle> U_9102 = uom(9102, NonSI.DEGREE_ANGLE);
    public final UoM<Dimensionless> U_9201 = uom(9201, AbstractUnit.ONE);
    public final UoM<Dimensionless> U_9202 = uom(9202, Units.PPM);

    public final UoM<Angle> U_9107 = uom(9107, DREP, "degree minute second");
    public final UoM<Angle> U_9108 = uom(9108, DMS_SCALED, "degree minute second hemisphere");

    public final UoM<Angle> U_9110 = uom(9110, DMS, "sexagesimal DMS");
    public final UoM<Angle> U_9111 = uom(9111, DM, "sexagesimal DM");

    public final UoM<Angle> U_9115 = uom(9115, DREP, "degree minute");
    public final UoM<Angle> U_9116 = uom(9116, DREP, "degree hemisphere");
    public final UoM<Angle> U_9117 = uom(9117, DREP, "hemisphere degree");
    public final UoM<Angle> U_9118 = uom(9118, DREP, "degree minute hemisphere");
    public final UoM<Angle> U_9119 = uom(9119, DREP, "hemisphere degree minute");
    public final UoM<Angle> U_9120 = uom(9120, DREP, "hemisphere degree minute second");
    public final UoM<Angle> U_9121 = uom(9121, DREP, "sexagesimal DMS.s");

    //////////////////////////////////

    
    <Q extends Quantity<Q>> UoM<Q> uom(int code, Unit<Q> unit, String name) {
        if(name==null)
            name = unit.toString();

        var uom = new UoM<>(Code.code(code, name), unit);
        if(!units.add(uom))
            throw new IllegalArgumentException("duplicate entry");
        return uom;
    }

    <Q extends Quantity<Q>> UoM<Q> uom(int code, Unit<Q> unit) {
        return uom(code, unit, unit.getName());
    }

    UoMs(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    protected void load() throws SQLException {
        super.load();

        process(this::addUnit, "select UOM_CODE, UNIT_OF_MEAS_NAME, REMARKS, DEPRECATED, " +
                "TARGET_UOM_CODE, FACTOR_B, FACTOR_C " +
                "from EPSG_UNITOFMEASURE " +
                "order by TARGET_UOM_CODE asc");
    }

    protected void finish() throws SQLException {
        super.finish();

        aliases("epsg_unitofmeasure", units::aliased);
    }

    void addUnit(ResultSet rs) throws SQLException {
        var unit = unit(rs);
        units.add(unit);
    }

    UoM<?> unit(ResultSet rs) throws SQLException {
        var code = codep(rs);

        // find any predefined unit.
        UoM<?> defined = units.find(code.getCode());

        int target = rs.getInt(5);

        if(target==code.getCode() && defined==null) {
            throw new IllegalStateException("unexpected self defined unit: " + target);
        }

        double b = rs.getDouble(6);
        double c = rs.getDouble(7);

        UoM<?> base = units.get(target);

        if(b==RPI) {
            base = U_9102;
            b = 1;
            c *= 2;
        }

        Unit<?> u = (b!=0 && c!=0) ? base.unit.multiply(b).divide(c) : null;

        if(defined!=null) {
            if(u==null || u.isCompatible(defined.unit))
                u = defined.unit;
            else {
                String message = String.format("incompatible units for %d: %s / %s", code.getCode(), defined.unit, u);
                throw new IllegalArgumentException(message);
            }
        }

        return new UoM<>(code, u);
    }
}
