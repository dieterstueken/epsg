package org.apache.sis.referencing.generate.epsg.stat;

import org.apache.sis.referencing.generate.epsg.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsg.load.*;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  06.08.2014 12:17
 * modified by: $Author$
 * modified on: $Date$
 */
public class AreaCodes {

    final EpsgTables tables = EpsgTables.load();

    public AreaCodes() throws SQLException {}

    public static void main(String... args) throws SQLException {
        new AreaCodes().run();
    }

    final TreeMap<String, List<Area>> groups = new TreeMap<>();

    void add(Area area) {
        List<Area> areas = new ArrayList<Area>();
        areas.add(area);
        final String name = getKey(area);
        groups.put(name, areas);
    }

    void put(Area area) {

        final String name = getKey(area);

        Map.Entry<String, List<Area>> entry = groups.higherEntry(name);
        if(entry!=null && entry.getKey().startsWith(name)) {
            List<Area> areas = entry.getValue();
            areas.add(area);

            if(!entry.getKey().equals(name)) {
                groups.remove(entry.getKey());
                groups.put(name, areas);
            }
        } else {
            List<Area> areas = new ArrayList<Area>();
            areas.add(area);
            groups.put(name, areas);
        }
    }

    String getKey(Area area) {
        String name = area.area_name;
        int index = name.indexOf(" -");
        return index<0 ? name : name.substring(0, index);
    }

    public void run() {

        for (Area area : tables.areas.values()) {
            if(area.iso_a2_code!=null && !area.iso_a2_code.isEmpty())
                add(area);
        }

        for (Area area : tables.areas.values()) {
            if(area.iso_a2_code!=null && !area.iso_a2_code.isEmpty())
                continue;

            String name = getKey(area);
            Map.Entry<String, List<Area>> entry = groups.floorEntry(name);

            if(entry!=null && name.startsWith(entry.getKey()))
                entry.getValue().add(area);
            else
                put(area);
        }

        for (Map.Entry<String, List<Area>> entry : groups.entrySet()) {
            final List<Area> areas = entry.getValue();

            Set<Datum> datums = new TreeSet<>(Item.INDEX_ORDER);
            Set<CoordOp> coops = new TreeSet<>(Item.INDEX_ORDER);
            for (Area a : areas) {
                for (Bound bound : a.references) {
                    if(bound instanceof Datum) {
                        datums.add((Datum) bound);
                    }
                    if(bound instanceof CoordOp) {
                        CoordOp op = (CoordOp) bound;
                        if(op.type()==CoordOp.Type.conversion)
                            coops.add(op);
                    }
                }
            }

            Area area = areas.get(0);
            String code = area.iso_a2_code != null && !area.iso_a2_code.isEmpty() ? area.iso_a2_code : "--";
            System.out.format("%d [%s] %3d %d %s\n", area.area_code, code, areas.size(), coops.size(), entry.getKey());

            //for (Datum d : datums) {
            //    System.out.format("\t %4d %s\n", d.datum_code, d.datum_name);
            //}
        }

        Consumer<Double> digits = new Consumer<Double>() {
            double fraction = 0.00001;

            @Override
            public void accept(Double value) {
                if(value==null)
                    return;
                double seconds = 3600 * value;
                seconds -= Math.round(seconds);
                seconds = Math.abs(seconds);
                if(seconds>fraction)
                    fraction = seconds;
            }
        };

        for (Area area : tables.areas.values()) {
            digits.accept(area.area_east_bound_lon);
            digits.accept(area.area_west_bound_lon);
            digits.accept(area.area_north_bound_lat);
            digits.accept(area.area_south_bound_lat);
        }

        //System.out.format("max digits: %d\n", digits);
    }

    public static int _digits(Double value) {
        if(value==null)
            return 0;

        long dec = Math.round(Math.abs(value)*100000);
        if(dec==0)
            return 0;

        int digits = 5;
        while(dec!=0 && dec%10==0) {
            dec /= 10;
            --digits;
        }

        if(digits==3)
            digits = 3;

        return digits;
    }
}
