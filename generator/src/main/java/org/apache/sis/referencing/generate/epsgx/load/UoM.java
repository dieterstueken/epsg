package org.apache.sis.referencing.generate.epsgx.load;

import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Length;
import javax.measure.Quantity;

import java.util.Map;
import java.util.TreeMap;

import static org.apache.sis.measure.Units.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  14.02.14 19:00
 * modified by: $Author$
 * modified on: $Date$
 */
public class UoM<Q extends Quantity<Q>> {

    public final Integer key;
    public final String name;
    public final Unit<Q> unit;
    public final String remark;

    UoM(Integer key, String name, Unit<Q> unit, String remark) {
        this.key = key;
        this.name = name;
        this.unit = unit;
        this.remark = remark;
    }

    private static <Q extends Quantity<Q>> UoM<Q>
    uom(Integer key, Unit<Q> unit, String name, String remark) {
        UoM<Q> uom = new UoM<Q>(key, name, unit, remark);
        units.put(uom.key, uom);
        return uom;
    }

    private static <Q extends Quantity<Q>> UoM<Q>
    uom(Integer key, Unit<Q> unit, String name) {
        return uom(key, unit, name, "");
    }

    public Unit<Q>  toSI() {
        return unit;
    }

    @Override
    public String toString() {
        String us = unit.toString();
        return String.format("[%d] %s {%s}", key, name, us);
    }
    
    Unit<Q> mul(double value) {
        return unit.multiply(value);
    }
    
    Unit<Q> div(double value) {
        return unit.divide(value);
    }
    
    Unit<Q> frac(double numerator, double denominator) {
        return mul(numerator).divide(denominator);
    }

    private static Map<Integer, UoM<?>> units = new TreeMap<>();

    public static UoM<?> get(Integer key) {
        return units.get(key);
    }

    static final Unit<Length> METER = METRE;
    
    static final Unit<Angle> DM = DEGREE;
    static final Unit<Angle> DMS = DEGREE;
    static final Unit<Angle> DMS_SCALED = DEGREE;
    static final Unit<Angle> DREP = DEGREE;
       
    public static final UoM<Length> U_9001 = uom(9001, METER, "metre", "Also known as International metre. SI standard unit.");
    public static final UoM<Length> U_9002 = uom(9002, FOOT, "foot");
    public static final UoM<Length> U_9003 = uom(9003, US_SURVEY_FOOT, "US survey foot", "Used in USA.");
    public static final UoM<Length> U_9005 = uom(9005, U_9001.mul(0.3047972654), "Clarke's foot", "Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.   Used in older Australian, southern African & British West Indian mapping.");
    public static final UoM<Length> U_9014 = uom(9014, U_9002.mul(6), "fathom", "= 6 feet.");
    public static final UoM<Length> U_9030 = uom(9030, NAUTICAL_MILE, "nautical mile");
    public static final UoM<Length> U_9031 = uom(9031, U_9001.mul(1.0000135965), "German legal metre", "Used in Namibia.");
    public static final UoM<Length> U_9033 = uom(9033, U_9003.mul(66), "US survey chain", "Used in USA primarily for public lands cadastral work.");
    public static final UoM<Length> U_9034 = uom(9034, U_9033.div(100), "US survey link", "Used in USA primarily for public lands cadastral work.");
    public static final UoM<Length> U_9035 = uom(9035, U_9033.mul(80), "US survey mile", "Used in USA primarily for public lands cadastral work.");
    public static final UoM<Length> U_9036 = uom(9036, KILOMETRE, "kilometre");

    public static final UoM<Length> U_9037 = uom(9037, U_9005.mul(3),    "Clarke's yard", "=3 Clarke's feet.  Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.   Used in older Australian, southern African & British West Indian mapping.");
    public static final UoM<Length> U_9038 = uom(9038, U_9037.mul(22),   "Clarke's chain", "=22 Clarke's yards.  Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.   Used in older Australian, southern African & British West Indian mapping.");
    public static final UoM<Length> U_9039 = uom(9039, U_9038.div(100), "Clarke's link", "=1/100 Clarke's chain. Assumes Clarke's 1865 ratio of 1 British foot = 0.3047972654 French legal metres applies to the international metre.   Used in older Australian, southern African & British West Indian mapping.");

    public static final UoM<Length> U_9040 = uom(9040, U_9001.frac(36000000,39370147), "British yard (Sears 1922)", "Uses Sear's 1922 British yard-metre ratio as given by Bomford as 39.370147 inches per metre.  Used in East Malaysian and older New Zealand mapping.");
    public static final UoM<Length> U_9041 = uom(9041, U_9040.div(3),                "British foot (Sears 1922)", "Uses Sear's 1922 British yard-metre ratio as given by Bomford as 39.370147 inches per metre.  Used in East Malaysian and older New Zealand mapping.");
    public static final UoM<Length> U_9042 = uom(9042, U_9040.mul(22),                "British chain (Sears 1922)", "Uses Sear's 1922 British yard-metre ratio as given by Bomford as 39.370147 inches per metre.  Used in East Malaysian and older New Zealand mapping.");
    public static final UoM<Length> U_9043 = uom(9043, U_9042.div(100),              "British link (Sears 1922)", "Uses Sear's 1922 British yard-metre ratio as given by Bomford as 39.370147 inches per metre.  Used in East Malaysian and older New Zealand mapping.");

    public static final UoM<Length> U_9050 = uom(9050, U_9001.frac(9143992, 1E7), "British yard (Benoit 1895 A)", "Uses Benoit's 1895 British yard-metre ratio as given by Clark as 0.9143992 metres per yard.  Used for deriving metric size of ellipsoid in Palestine.");
    public static final UoM<Length> U_9051 = uom(9051, U_9050.div(3),          "British foot (Benoit 1895 A)", "Uses Benoit's 1895 British yard-metre ratio as given by Clark as 0.9143992 metres per yard.  Used for deriving metric size of ellipsoid in Palestine.");
    public static final UoM<Length> U_9052 = uom(9052, U_9050.mul(22),          "British chain (Benoit 1895 A)", "Uses Benoit's 1895 British yard-metre ratio as given by Clark as 0.9143992 metres per yard.  Used for deriving metric size of ellipsoid in Palestine.");
    public static final UoM<Length> U_9053 = uom(9053, U_9052.div(100),        "British link (Benoit 1895 A)", "Uses Benoit's 1895 British yard-metre ratio as given by Clark as 0.9143992 metres per yard.  Used for deriving metric size of ellipsoid in Palestine.");

    public static final UoM<Length> U_9060 = uom(9060, U_9001.frac(36000000,39370113), "British yard (Benoit 1895 B)", "G. Bomford \"Geodesy\" 2nd edition 1962; after J.S.Clark \"Remeasurement of the Old Length Standards\"; Empire Survey Review no. 90; 1953.");
    public static final UoM<Length> U_9061 = uom(9061, U_9060.div(3),                "British foot (Benoit 1895 B)", "Uses Benoit's 1895 British yard-metre ratio as given by Bomford as 39.370113 inches per metre.  Used in West Malaysian mapping.");
    public static final UoM<Length> U_9062 = uom(9062, U_9060.mul(22),                "British chain (Benoit 1895 B)", "Uses Benoit's 1895 British yard-metre ratio as given by Bomford as 39.370113 inches per metre.  Used in West Malaysian mapping.");
    public static final UoM<Length> U_9063 = uom(9063, U_9062.div(100),              "British link (Benoit 1895 B)", "Uses Benoit's 1895 British yard-metre ratio as given by Bomford as 39.370113 inches per metre.  Used in West Malaysian mapping.");

    public static final UoM<Length> U_9070 = uom(9070, U_9001.frac(9144025,3E7), "British foot (1865)", "Uses Clark's estimate of 1853-1865 British foot-metre ratio of 0.9144025 metres per yard.  Used in 1962 and 1975 estimates of Indian foot.");

    public static final UoM<Length> U_9080 = uom(9080, U_9001.frac(12E6,39370142), "Indian foot",       "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (= 3 British feet) taken to be J.S.Clark's 1865 value of 0.9144025 metres.");
    public static final UoM<Length> U_9081 = uom(9081, U_9001.frac(30479841,1E8), "Indian foot (1937)", "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British foot taken to be 1895 Benoit value of 12/39.370113m.  Rounded to 8 decimal places as 0.30479841. Used from Bangladesh to Vietnam.  Previously used in India and Pakistan but superseded.");
    public static final UoM<Length> U_9082 = uom(9082, U_9001.frac(3047996,1E7), "Indian foot (1962)",  "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (3 feet) taken to be J.S. Clark's 1865 value of 0.9144025m. Rounded to 7 significant figures with a small error as 1 Ind ft=0.3047996m.  Used in Pakistan since metrication.");
    public static final UoM<Length> U_9083 = uom(9083, U_9001.frac(3047995,1E7), "Indian foot (1975)",  "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (3 feet) taken to be J.S. Clark's 1865 value of 0.9144025m. Rounded to 7 significant figures as 1 Ind ft=0.3047995m.  Used in India since metrication.");

    public static final UoM<Length> U_9084 = uom(9084, U_9080.mul(3), "Indian yard",        "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (= 3 British feet) taken to be J.S.Clark's 1865 value of 0.9144025 metres.");
    public static final UoM<Length> U_9085 = uom(9085, U_9081.mul(3), "Indian yard (1937)", "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British foot taken to be 1895 Benoit value of 12/39.370113m.  Rounded to 8 decimal places as 0.30479841. Used from Bangladesh to Vietnam.  Previously used in India and Pakistan but superseded.");
    public static final UoM<Length> U_9086 = uom(9086, U_9082.mul(3), "Indian yard (1962)", "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (3 feet) taken to be J.S. Clark's 1865 value of 0.9144025m. Rounded to 7 significant figures with a small error as 1 Ind ft=0.3047996m.  Used in Pakistan since metrication.");
    public static final UoM<Length> U_9087 = uom(9087, U_9083.mul(3), "Indian yard (1975)", "Indian Foot = 0.99999566 British feet (A.R.Clarke 1865).  British yard (3 feet) taken to be J.S. Clark's 1865 value of 0.9144025m. Rounded to 7 significant figures as 1 Ind ft=0.3047995m.  Used in India since metrication.");

    public static final UoM<Length> U_9093 = uom(9093, U_9002.mul(5280), "Statute mile", "=5280 feet");
    public static final UoM<Length> U_9094 = uom(9094, U_9001.frac(6378300,20926201), "Gold Coast foot", "Used in Ghana and some adjacent parts of British west Africa prior to metrication, except for the metrication of projection defining paraU_9001.units when British foot (Sears 1922) used.");
    public static final UoM<Length> U_9095 = uom(9095, U_9001.mul(0.3048007491), "British foot (1936)", "For the 1936 retriangulation OSGB defines the relationship of 10 feet of 1796 to the International metre through the logarithmic relationship (10^0.48401603 exactly). 1 ft = 0.3048007491m. Also used for metric conversions in Ireland.");
    public static final UoM<Length> U_9096 = uom(9096, U_9002.mul(3), "yard", "=3 international feet.");
    public static final UoM<Length> U_9097 = uom(9097, U_9096.mul(22), "chain", "=22 international yards or 66 international feet.");
    public static final UoM<Length> U_9098 = uom(9098, U_9097.div(100), "link", "=1/100 international chain.");
    public static final UoM<Length> U_9099 = uom(9099, U_9001.frac(914398,1E6), "British yard (Sears 1922 truncated)", "Uses Sear's 1922 British yard-metre ratio (UoM<Length>code 9040) truncated to 6 significant figures.");

    public static final UoM<Angle>  U_9101 = uom(9101, RADIAN, "radian", "SI standard unit.");
    public static final UoM<Angle>  U_9102 = uom(9102, DEGREE, "degree", "= pi/180 radians");
    public static final UoM<Angle>  U_9103 = uom(9103, ARC_MINUTE, "arc-minute", "1/60th degree = ((pi/180) / 60) radians");
    public static final UoM<Angle>  U_9104 = uom(9104, ARC_SECOND, "arc-second", "1/60th arc-minute = ((pi/180) / 3600) radians");
    public static final UoM<Angle>  U_9105 = uom(9105, GRAD, "grad", "=pi/200 radians.");
    public static final UoM<Angle>  U_9106 = uom(9106, GRAD, "gon", "=pi/200 radians");

    public static final UoM<Angle>  U_9107 = uom(9107, DREP, "degree minute second", "Degree representation. Format: signed degrees (integer) - arc-minutes (integer) - arc-seconds (real, any precision). Different symbol sets are in use as field separators, for example ° ' \". Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9108 = uom(9108, DMS_SCALED, "degree minute second hemisphere", "Degree representation. Format: degrees (integer) - arc-minutes (integer) - arc-seconds (real) - hemisphere abbreviation (single character N S E or W). Different symbol sets are in use as field separators for example ° ' \". Convert to deg using algorithm.");
    public static final UoM<Angle>  U_9109 = uom(9109, MICRORADIAN, "microradian", "rad * 10E-6");
    public static final UoM<Angle>  U_9110 = uom(9110, DMS, "sexagesimal DMS", "Pseudo unit. Format: signed degrees - period - minutes (2 digits) - integer seconds (2 digits) - fraction of seconds (any precision). Must include leading zero in minutes and seconds and exclude decimal point for seconds. Convert to degree using formula.");
    public static final UoM<Angle>  U_9111 = uom(9111, DM, "sexagesimal DM", "Pseudo unit. Format: sign - degrees - decimal point - integer minutes (two digits) - fraction of minutes (any precision).  Must include leading zero in integer minutes.  Must exclude decimal point for minutes.  Convert to deg using algorithm.");
    public static final UoM<Angle>  U_9112 = uom(9112, U_9105.div(100), "centesimal minute", "1/100 of a grad and gon = ((pi/200) / 100) radians");
    public static final UoM<Angle>  U_9113 = uom(9113, U_9112.div(100), "centesimal second", "1/100 of a centesimal minute or 1/10,000th of a grad and gon = ((pi/200) / 10000) radians");
    public static final UoM<Angle>  U_9114 = uom(9114, U_9102.frac(360,6400), "mil_6400", "Angle subtended by 1/6400 part of a circle.  Approximates to 1/1000th radian.  Note that other approximations (notably 1/6300 circle and 1/6000 circle) also exist.");
    public static final UoM<Angle>  U_9115 = uom(9115, DREP, "degree minute", "Degree representation. Format: signed degrees (integer)  - arc-minutes (real, any precision). Different symbol sets are in use as field separators, for example ° '. Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9116 = uom(9116, DREP, "degree hemisphere", "Degree representation. Format: degrees (real, any precision) - hemisphere abbreviation (single character N S E or W). Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9117 = uom(9117, DREP, "hemisphere degree", "Degree representation. Format: hemisphere abbreviation (single character N S E or W) - degrees (real, any precision). Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9118 = uom(9118, DREP, "degree minute hemisphere", "Degree representation. Format: degrees (integer) - arc-minutes (real, any precision) - hemisphere abbreviation (single character N S E or W). Different symbol sets are in use as field separators, for example ° '. Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9119 = uom(9119, DREP, "hemisphere degree minute", "Degree representation. Format:  hemisphere abbreviation (single character N S E or W) - degrees (integer) - arc-minutes (real, any precision). Different symbol sets are in use as field separators, for example ° '. Convert to degrees using algorithm.");
    public static final UoM<Angle>  U_9120 = uom(9120, DREP, "hemisphere degree minute second", "Degree representation. Format: hemisphere abbreviation (single character N S E or W) - degrees (integer) - arc-minutes (integer) - arc-seconds (real). Different symbol sets are in use as field separators for example ° ' \". Convert to deg using algorithm.");
    public static final UoM<Angle>  U_9121 = uom(9121, DREP, "sexagesimal DMS.s", "Pseudo unit. Format: signed degrees - minutes (two digits) - seconds (real, any precision). Must include leading zero in minutes and seconds where value is under 10 and include decimal separator for seconds. Convert to degree using algorithm.");
    public static final UoM<Angle>  U_9122 = uom(9122, DEGREE, "degree (supplier to define representation)", "= pi/180 radians. The degree representation (e.g. decimal, DMSH, etc.) must be clarified by suppliers of data associated with this code.");

    public static final UoM<Dimensionless> U_9201 = uom(9201, UNITY, "unity");
    public static final UoM<Dimensionless> U_9202 = uom(9202, UNITY.divide(1000000), "parts per million");
    public static final UoM<Dimensionless> U_9203 = uom(9203, UNITY, "coefficient", "Used when paraU_9001.units are coefficients.  They inherently take the units which depend upon the term to which the coefficient applies.");

    public static final UoM<Length> U_9204 = uom(9204, U_9003.mul(330), "Bin width 330 US survey feet");
    public static final UoM<Length> U_9205 = uom(9205, U_9204.div(2), "Bin width 165 US survey feet");
    public static final UoM<Length> U_9206 = uom(9206, U_9204.div(4), "Bin width 82.5 US survey feet");
    public static final UoM<Length> U_9207 = uom(9207, U_9001.frac(75,2), "Bin width 37.5 metres");
    public static final UoM<Length> U_9208 = uom(9208, U_9001.mul(25), "Bin width 25 metres");
    public static final UoM<Length> U_9209 = uom(9209, U_9208.div(2), "Bin width 12.5 metres");
    public static final UoM<Length> U_9210 = uom(9210, U_9208.div(4), "Bin width 6.25 metres");
    public static final UoM<Length> U_9211 = uom(9211, U_9208.div(8), "Bin width 3.125 metres");

    public static final UoM<Length> U_9300 = uom(9300, U_9001.frac(914398, 3E6), "British foot (Sears 1922 truncated)", "Uses Sear's 1922 British yard-metre ratio (UoM<Length>code 9040) truncated to 6 significant figures; this truncated ratio (0.914398, UoM<Length>code 9099) then converted to other imperial units. 3 ftSe(T) = 1 ydSe(T).");
    public static final UoM<Length> U_9301 = uom(9301, U_9300.mul(22), "British chain (Sears 1922 truncated)", "Uses Sear's 1922 British yard-metre ratio (UoM<Length>code 9040) truncated to 6 significant figures; this truncated ratio (0.914398, UoM<Length>code 9099) then converted to other imperial units. 1 chSe(T) = 22 ydSe(T). Used in metrication of Malaya RSO grid.");
    public static final UoM<Length> U_9302 = uom(9302, U_9301.div(100), "British link (Sears 1922 truncated)", "Uses Sear's 1922 British yard-metre ratio (UoM<Length>code 9040) truncated to 6 significant figures; this truncated ratio (0.914398, UoM<Length>code 9099) then converted to other imperial units. 100 lkSe(T) = 1 chSe(T).");

}
