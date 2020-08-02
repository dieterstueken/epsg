package org.apache.sis.referencing.generate.epsg.stat;

import org.apache.sis.referencing.generate.epsg.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsg.load.Area;
import org.apache.sis.referencing.generate.epsg.load.Item;

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
public class AreasNames {

    final EpsgTables tables;

    AreasNames() throws SQLException {
        tables = EpsgTables.load();

        for (Area area : tables.areas.values()) {
            add(area);
        }
    }

    class Group {

        final String name;

        final List<Area> areas = new ArrayList<>();

        Group(String name) {
            this.name = name;
        }

        public void dump(PrintStream out) {
            Collections.sort(areas, Item.NAME_ORDER);
            Area a = areas.get(0);
            out.format("%s %s %4d %s\n", name, a.iso_a2_code, areas.size(), a.getName());
        }
    }

    final Map<String, Group> groups = new TreeMap<>();

    void add(Area area) {

        String hash = area.getName();
        if(hash.length()>2)
            hash = hash.substring(0, 2);

        Group group = groups.get(hash);

        if(group==null) {
            group = new Group(hash);
            groups.put(hash, group);
        }

        group.areas.add(area);
    }

    public static void main(String... args) throws SQLException {

        AreasNames areas = new AreasNames();

        areas.dump(System.out);
    }

    private void dump(PrintStream out) {

        for (Group group : groups.values()) {
            group.dump(out);
        }

        out.format("%d total\n", groups.size());
    }

}
