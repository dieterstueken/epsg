package org.apache.sis.referencing.generate.epsgx.stat;

import org.apache.sis.referencing.generate.epsgx.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsgx.load.CoordOp;
import org.apache.sis.referencing.generate.epsgx.load.CoordRefSys;
import org.apache.sis.referencing.generate.epsgx.load.Datum;
import org.apache.sis.referencing.generate.epsgx.load.Item;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  06.08.2014 10:12
 * modified by: $Author$
 * modified on: $Date$
 */
public class Datums {

    EpsgTables tables = EpsgTables.load();

    public Datums() throws SQLException {}

    public void run() {

        for (CoordOp op : tables.coops.values()) {
            Set<Datum> datums = new TreeSet<Datum>(Item.INDEX_ORDER);
            for (CoordRefSys crs : op.references) {
                datum(crs, datums);
            }

            if(datums.size()!=1) {
                System.out.format("%d op_%d: ", datums.size(), op.getKey());
                for (Datum datum : datums) {
                    System.out.format("d_%d: ", datum.getKey());
                }
                System.out.println();
            }
        }
    }

    private void datum(CoordRefSys crs, Set<Datum> datums) {
        if(crs.datum_code!=null)
            datums.add(tables.datums.get(crs.datum_code));

        if(crs.source_geogcrs_code!=null)
            datum(tables.crsTable.get(crs.source_geogcrs_code), datums);

        if(crs.cmpd_horizcrs_code!=null)
            datum(tables.crsTable.get(crs.cmpd_horizcrs_code), datums);

        if(crs.cmpd_vertcrs_code!=null)
            datum(tables.crsTable.get(crs.cmpd_vertcrs_code), datums);
    }

    public static void main(String... args) throws SQLException, IOException {

        new Datums().run();
    }
}
