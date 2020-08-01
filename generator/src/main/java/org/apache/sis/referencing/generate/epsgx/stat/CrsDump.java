package org.apache.sis.referencing.generate.epsgx.stat;

import org.apache.sis.referencing.generate.epsgx.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsgx.load.*;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  26.02.14 11:28
 * modified by: $Author$
 * modified on: $Date$
 */
public class CrsDump {

    final EpsgTables tables;

    public CrsDump() throws SQLException {
        tables = EpsgTables.load();
    }

    public void dump(PrintStream out) {

        Map<Datum, List<CoordRefSys>> dmap = new TreeMap<>(Item.INDEX_ORDER);

        for (CoordRefSys crs : tables.crsTable.values()) {
            Datum d = tables.getDatum(crs);

            List<CoordRefSys> l = dmap.get(d);
            if(l==null) {
                l = new LinkedList<>();
                dmap.put(d, l);
            }

            l.add(crs);
        }

        for (Map.Entry<Datum, List<CoordRefSys>> e : dmap.entrySet()) {

            Datum d = e.getKey();
            int count = e.getValue().size();

            if(d==null)
                out.format("%3d null:\n", count);
            else
                out.format("%3d %d %d %s\n", count, d.getKey(), d.ellipsoid_code, d.getName());
        }
    }

    public static void main(String ... args) throws SQLException, IOException {
        new CrsDump().dump(System.out);
    }
}