package org.apache.sis.referencing.generate.epsg.stat;

import org.apache.sis.referencing.generate.epsg.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsg.load.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  06.08.2014 12:17
 * modified by: $Author$
 * modified on: $Date$
 */
public class AreaUsages {

    final EpsgTables tables = EpsgTables.load();

    public AreaUsages() throws SQLException {}

    public static void main(String... args) throws SQLException {
        new AreaUsages().run();
    }

    final List<List<Area>> groups = new ArrayList<>(32);

    {
        for(int i=0; i<32; i++)
            groups.add(new LinkedList<Area>());
    }

    int key(CoordRefSys ref) {
        return ref.type()==CoordRefSys.Type.projected ? 1 : 2;
    }

    int key(CoordOp ref) {
        return ref.type()==CoordOp.Type.transformation ? 8 : 4;
    }

    List<Datum> datums = new ArrayList<>();

    int key(Bound ref) {
        if(ref instanceof Datum) {
            datums.add((Datum) ref);
            return 16;
        }

        if(ref instanceof CoordOp)
            return key((CoordOp) ref);

        if(ref instanceof CoordRefSys)
            return key((CoordRefSys) ref);

        return 0;
    }

    int key(Area area) {
        int key = 0;

        for (Bound ref : area.references)
            key |= key(ref);

        return key;
    }

    public void run() {

        for (Area area : tables.areas.values()) {
            int key = key(area);
            groups.get(key).add(area);
        }

        String pattern = "pgctd";

        for (int i = 0; i < groups.size(); i++) {
            List<Area> g = groups.get(i);

            System.out.format("[%2d] ",i);

            for(int k=0; k<5; ++k) {
                System.out.append( ((i&(1<<k))!=0)? pattern.charAt(k) : '.');
            }
            System.out.format(" %6d\n", g.size());
        }

        System.out.println();
    }
}
