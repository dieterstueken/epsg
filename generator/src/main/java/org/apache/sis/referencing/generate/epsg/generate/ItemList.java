package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.referencing.generate.epsg.load.*;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  04.03.14 15:00
 * modified by: $Author$
 * modified on: $Date$
 */
public class ItemList {

    // usage pattern by group
    public static void main2(String ... args) throws SQLException {

        EpsgTables tables = EpsgTables.load();

        int code = 0;
        Integer M_9807 = 9807;

        for (CoordOp coordOp : tables.coops.values()) {

            int group = coordOp.getKey()/100;

            if(100*group>code) {
                code = 100*group;
                System.out.format("\n%3d ", group);
            }

            while(code<coordOp.getKey()) {
                System.out.print(' ');
                ++code;
            }

            switch(coordOp.type()) {
                case conversion:
                    if(M_9807.equals(coordOp.coord_op_method_code))
                        System.out.print('M');
                    else
                        System.out.print('C');
                    break;
                case transformation: System.out.print('T'); break;
                case concatenation: System.out.print('X'); break;
            }
        }
    }

    // a-z
    public static void main3(String ... args) throws SQLException {
        EpsgTables tables = EpsgTables.load();

        Map<Character, Set<CoordOp>> ops = new TreeMap<>();

        for (CoordOp coop : tables.coops.values()) {

            if(coop.type()!=CoordOp.Type.conversion)
                continue;

            Character key = coop.getName().charAt(0);

            Set<CoordOp> set = ops.get(key);

            if(set==null) {
                set = new TreeSet<>(Item.INDEX_ORDER);
                ops.put(key, set);
            }

            set.add(coop);
        }

        for (Map.Entry<Character, Set<CoordOp>> e : ops.entrySet()) {
            System.out.format("%c %4d\n", e.getKey(), e.getValue().size());
        }

    }

    // op -> count(ellipsoid/datum)
    public static void main4(String ... args) throws SQLException {

        EpsgTables tables = EpsgTables.load();

        Map<Integer, List<CoordOp>> usages = new TreeMap<>();

        for (CoordOp op : tables.coops.values()) {

            if(op.references.isEmpty())
                continue;

            Set<Ellipsoid> target = new TreeSet<>(Item.INDEX_ORDER);

            for (CoordRefSys ref : op.references) {
                Datum d = tables.getDatum(ref);
                if(d!=null && d.ellipsoid_code!=null) {
                    Ellipsoid e = tables.ellipsoids.get(d.ellipsoid_code);
                    target.add(e);
                }
            }

            Integer count = target.size();

            List<CoordOp> ops = usages.get(count);
            if(ops==null) {
                ops = new LinkedList<>();
                usages.put(count, ops);
            }

            ops.add(op);
        }

        for (Map.Entry<Integer, List<CoordOp>> e : usages.entrySet()) {
            System.out.format("%3d %3d\n", e.getKey(), e.getValue().size());
        }
    }

    public static void showDatumToOperation(String ... args) throws SQLException {
        EpsgTables tables = EpsgTables.load();

        Map<Datum, List<CoordOp>> usages = new TreeMap<>(Item.INDEX_ORDER);
        int count = 0;

        for (CoordOp op : tables.coops.values()) {
            Datum datum = tables.getDatum(op);
            if(datum!=null) {
                //Ellipsoid eps = tables.ellipsoids.get(datum.ellipsoid_code);

                List<CoordOp> usage = usages.get(datum);
                if(usage==null) {
                    usage = new LinkedList<>();
                    usages.put(datum, usage);
                }
                usage.add(op);
                ++count;
            }
        }

        System.out.format("%d total\n", count);

        for (Map.Entry<Datum, List<CoordOp>> e : usages.entrySet()) {
            Datum d = e.getKey();
            System.out.format("%4d %4d %4d %s\n", e.getValue().size(), d.getKey(), d.ellipsoid_code, d.getName());
        }
    }

    public static void showParameterUsages(String ... args) throws SQLException {

        EpsgTables tables = EpsgTables.load();

        for (CoordOpParam param : tables.params.values()) {

            Map<Object, List<CoordOpParamValue>> values = new HashMap<>();

            for (CoordOpParamUsage usage : param.usages) {
                Object key = Void.class;

                for (CoordOpParamValue val : usage.values) {
                    if(val.uom_code!=null) {
                        UoM unit = UoM.get(val.uom_code);
                        key = unit==null ? val.uom_code : unit.toSI();
                    } else
                    if(!OutputAppender.isNullOrEmpty(val.param_value_file_ref))
                        key = String.class;
                    else
                    if(val.parameter_value==null)
                        continue;   // deprecated usage
                    else
                        key = Double.class;

                    List<CoordOpParamValue> vl = values.get(key);
                    if(vl==null) {
                        vl = new LinkedList<>();
                        values.put(key, vl);
                    }

                    vl.add(val);
                }
            }

            if(values.size()!=1) {
                System.out.println(param);
                for (Map.Entry<Object, List<CoordOpParamValue>> e : values.entrySet()) {
                    System.out.format("\t%3d %s\n", e.getValue().size(), e.getKey().toString());
                }
            }
        }
    }

    public static void showMethodUsages(String ... args) throws SQLException {

        EpsgTables tables = EpsgTables.load();

        for (CoordOpMethod method : tables.methods.values()) {
            System.out.println(method.toString());

            for (CoordOpParamUsage usage : method.usages) {

                System.out.format("\t%s\n", usage.param.toString());

                Map<Object, AtomicInteger> values = new HashMap<>();

                for (CoordOpParamValue val : usage.values) {
                    Object key;
                    if(val.uom_code!=null) {
                        UoM unit = UoM.get(val.uom_code);
                        key = unit==null ? val.uom_code : unit.toSI();
                    } else
                    if(!OutputAppender.isNullOrEmpty(val.param_value_file_ref))
                        key = "Strg";
                    else
                    if(val.parameter_value!=null)
                        key = "null";
                    else
                        continue;

                    AtomicInteger count = values.get(key);
                    if(count==null)
                        values.put(key, new AtomicInteger(1));
                    else
                        count.incrementAndGet();
                }

                for (Map.Entry<Object, AtomicInteger> e : values.entrySet()) {
                    System.out.format("\t\t%4d %s\n", e.getValue().intValue(), e.getKey().toString());
                }
            }
        }
    }

    public static void main(String ... args) throws SQLException {
        showParameterUsages(args);
    }
}
