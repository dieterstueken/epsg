package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.referencing.generate.epsg.load.*;

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
public class AreasISO {

    public static int len(String s) {
        return s==null ? 0 : s.length();
    }

    public static int match(String a, String b) {
        int len = Math.min(len(a), len(b));

        for(int i=0; i<len; i++) {
            if(a.charAt(i) != b.charAt(i))
                return i;
        }

        // full match, indicate by sign
        return -len;
    }

    static int slen(String s) {
        if(s==null)
            return Integer.MAX_VALUE;

        if(s.length()==0)
            return Integer.MAX_VALUE-1;

        return s.length();
    }

    public static Comparator<String> SHORTEST = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int order = Integer.compare(slen(o1), slen(o2));
            if(order==0)
                order = o1.compareTo(o2);
            return order;
        }
    };

    public static Comparator<Area> BY_NAME = new Comparator<Area>() {

        @Override
        public int compare(Area o1, Area o2) {
            int order = SHORTEST.compare(o1.iso_a2_code, o2.iso_a2_code);
            if(order==0)
                order = SHORTEST.compare(o1.area_name, o2.area_name);
            return order;
        }
    };

    class Group implements Comparable<Group> {

        final String name;
        final String code;
        int refs = 0;

        final Set<Datum> datums = new HashSet<>();

        final List<Area> areas = new LinkedList<>();

        Group(String name, String code) {
            this.name = name;
            this.code = code==null ? "" : code;
        }

        void addDatum(Datum datum) {
            if(datum!=null)
                datums.add(datum);
        }

        void addCRS(CoordRefSys crs) {
            //if(crs!=null)
            //    addDatum(tables.getDatum(crs));
        }

        void addOP(CoordOp op) {
            if(op.type()== CoordOp.Type.conversion)
                ++refs;
        }

        void addRef(Bound ref) {
            if(ref==null)
                return;

            if(ref instanceof Datum)
                addDatum((Datum) ref);
            else
            if(ref instanceof CoordRefSys)
                addCRS((CoordRefSys) ref);
            else
            if(ref instanceof CoordOp)
                addOP((CoordOp) ref);
            else
                throw new RuntimeException("unknown reference: " + ref.getClass().toString());
        }

        void add(Area area) {
            areas.add(area);

            for (Bound ref : area.references) {
                addRef(ref);
            }
        }

        public void dump(PrintStream out) {

            if(code==null || code.length()==0)
                out.format("%3d %3d %4d      %s\n", datums.size(), areas.size(), refs, name);
            else
                out.format("%3d %3d %4d [%s] %s\n", datums.size(), areas.size(), refs, code, name);
        }

        @Override
        public int compareTo(Group o) {

            //int order = SHORTEST.compare(lead().iso_a2_code, o.lead().iso_a2_code);

            int order = Integer.compare(datums.size(), o.datums.size());

            if(order==0)
                order = Integer.compare(refs, o.refs);

            if(order==0)
                order = name.compareTo(o.name);

            return order;
        }
    }

    Group newGroup(Area area) {

        String name = area.getName();
        int i = name.indexOf(" -");
        if(i>0)
            name = name.substring(0, i);

        Group group = newGroup(name, area.iso_a2_code);
        group.add(area);

        return group;
    }

    Group newGroup(String name) {
        return newGroup(name, "--");
    }

    Group newGroup(String name, String code) {
        Group group = new Group(name, code);
        groups.add(group);

        return group;
    }

    void insert(Area area) {

        Group match = match(area.getName());

        if(match==null)
          match = match(area.area_of_use);

        if(match!=null)
            match.add(area);
        else
            newGroup(area);
    }

    Group match(String name) {

        int l = 0;
        Group match = null;

        for (Group group : groups) {
            int i = match(name, group.name);
            if(i<0 || i>l) {
                l = i;
                match = group;
                if(i<0)
                    break;
            }
        }

        if(l>4 || l<0)
            return match;

        if(match!=null)
            return null;
        else
            return null;
    }

    final EpsgTables tables;

    final List<Group> groups = new ArrayList<>();

    AreasISO() throws SQLException {

        tables = EpsgTables.load();

        final List<Area> areas = new ArrayList<>(tables.areas.size());

        newGroup("Africa");
        newGroup("Asia");
        newGroup("Caribbean");
        newGroup("Latin America");
        newGroup("Europe");
        newGroup("World");

        Group islands = newGroup("Islands");

        for (Area area : tables.areas.values()) {

            if(area.area_name.contains(" Island"))
                islands.areas.add(area);
            else
            if(area.iso_a2_code!=null && !area.iso_a2_code.isEmpty())
                newGroup(area);
            else
                areas.add(area);
        }

        Collections.sort(areas, Area.NAME_ORDER);

        for (Area area : areas) {
            insert(area);
        }
    }

    public static void main(String... args) throws SQLException {

        AreasISO areas = new AreasISO();

        areas.dump(System.out);
    }

    private void dump(PrintStream out) {

        Collections.sort(groups);

        for (Group group : groups) {
            group.dump(out);
        }

        out.format("%d total\n", groups.size());
    }

}
