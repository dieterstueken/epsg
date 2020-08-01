package org.apache.sis.referencing.generate.epsgx.stat;

import org.apache.sis.referencing.generate.epsgx.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsgx.load.*;

import java.io.PrintStream;
import java.sql.SQLException;
import java.util.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  24.02.14 15:42
 * modified by: $Author$
 * modified on: $Date$
 */
public class Areas {

    final EpsgTables tables;

    Areas() throws SQLException {
        tables = EpsgTables.load();
    }

    class Usage implements Comparable<Usage> {
        final Area area;

        final Set<Datum> datums = new TreeSet<>(Item.INDEX_ORDER);

        void add(Datum datum) {
            if(datum!=null)
                datums.add(datum);
        }

        void add(CoordRefSys crs) {
            add(tables.getDatum(crs));
        }

        void add(CoordOp op) {
            for (CoordRefSys crs : op.references) {
                add(crs);
            }
        }

        void add(Bound ref) {
            if(ref instanceof Datum)
                add((Datum) ref);
            else
            if(ref instanceof CoordRefSys)
                add((CoordRefSys) ref);
            else
            if(ref instanceof CoordOp)
                add((CoordOp) ref);
            else
                throw new RuntimeException(ref.getClass().getSimpleName());
        }

        Usage(Area area) {
            this.area = area;

            for (Bound ref : area.references) {
                add(ref);
            }
        }

        public void dump(PrintStream out) {
            out.format("%d %d %s\n", datums.size(), area.getKey(), area.getName());
        }

        @Override
        public int compareTo(Usage o) {
            int i = Integer.compare(datums.size(), o.datums.size());

            if(i==0)
                i = Item.INDEX_ORDER.compare(area, o.area);

            return i;
        }
    }

    public static void main(String... args) throws SQLException {

        Areas areas = new Areas();

        areas.dump(System.out);
    }

    private void dump(PrintStream out) {

        List<Usage> usages = new ArrayList<>( tables.areas.size());

        for (Area area : tables.areas.values()) {
            usages.add(new Usage(area));
        }

        Collections.sort(usages);

        for (Usage usage : usages) {
            usage.dump(out);
        }

    }

}
