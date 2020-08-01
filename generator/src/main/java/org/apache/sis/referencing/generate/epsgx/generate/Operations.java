package org.apache.sis.referencing.generate.epsgx.generate;

import org.apache.sis.referencing.generate.epsgx.load.CoordOp;
import org.apache.sis.referencing.generate.epsgx.load.CoordRefSys;
import org.apache.sis.referencing.generate.epsgx.load.Datum;
import org.apache.sis.referencing.generate.epsgx.load.Item;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  03.03.14 13:52
 * modified by: $Author$
 * modified on: $Date$
 */
public class Operations {

    final EpsgTables tables;

    final Map<CoordOp, Set<Datum>> operations = new TreeMap<>(Item.INDEX_ORDER);

    Operations() throws SQLException {

        tables = EpsgTables.load();

        for (CoordRefSys crs : tables.crsTable.values()) {

            if(crs.projection_conv_code==null)
                continue;

            if(!"projected".equals(crs.coord_ref_sys_kind))
                continue;

            CoordOp op = tables.coops.get(crs.projection_conv_code);
            if(op==null)
                continue;

            Datum datum = tables.getDatum(crs);

            add(op, datum);
        }
    }

    private void add(CoordOp op, Datum datum) {
        if(datum==null)
            return;

        Set<Datum> datums = operations.get(op);
        if(datums==null) {
            datums = new TreeSet<>(Item.INDEX_ORDER);
            operations.put(op, datums);
        }

        datums.add(datum);
    }


    void dump(PrintStream out) {

        Map<Integer, List<Set<Datum>>> stat = new TreeMap<>();

        for (Map.Entry<CoordOp, Set<Datum>> entry : operations.entrySet()) {

            Integer n = entry.getValue().size();

            List<Set<Datum>> datums = stat.get(n);
            if(datums==null) {
                datums = new LinkedList<>();
                stat.put(n, datums);
            }

            datums.add(entry.getValue());
        }

        for (Map.Entry<Integer, List<Set<Datum>>> e : stat.entrySet()) {

            Set<Datum> datums = new TreeSet<>(Item.INDEX_ORDER);
            for (Set<Datum> ds : e.getValue()) {
                datums.addAll(ds);
            }

            out.format("%d %d %d\n", e.getKey(), e.getValue().size(), datums.size());
        }
    }

    public static void main(String... args) throws SQLException {

        Operations operations = new Operations();

        operations.dump(System.out);
    }

}
