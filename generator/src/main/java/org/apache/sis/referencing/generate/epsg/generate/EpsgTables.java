package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.referencing.generate.epsg.load.*;
import org.apache.sis.util.logging.Logging;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 10:10
 * modified by: $Author$
 * modified on: $Date$
 */
public class EpsgTables implements Serializable {

    protected static final Logger logger = Logging.getLogger(Table.class.getName());

    public static EpsgTables load() throws SQLException {
        String url = System.getProperty("epsg.database.url");

        if(url==null)
            throw new RuntimeException("missing epsg.database.url");

        return load(url);
    }

    public static EpsgTables load(String url) throws SQLException {
        try(Connection connection = DriverManager.getConnection(url)) {
            return load(connection);
        }
    }

    public static EpsgTables load(Connection connection) throws SQLException {
        return new EpsgTables(connection);
    }

    public final NavigableMap<Integer, Scope> scopes;

    public final List<Alias> aliases;

    public final NavigableMap<Integer, Area> areas;
    public final NavigableMap<Integer, UnitOfMeasure> units;

    public final NavigableMap<Integer, Ellipsoid> ellipsoids;
    public final NavigableMap<Integer, PrimeMeridian> primes;
    public final NavigableMap<Integer, CoordAxisName> axisNames;
    public final List<CoordAxis> axes;
    public final NavigableMap<Integer, Datum> datums;

    public final NavigableMap<Integer, CoordOpMethod> methods;
    public final NavigableMap<Integer, CoordOpParam> params;
    public final NavigableMap<Integer, CoordOp> coops;

    public final NavigableMap<Integer, CoordSys> csTable;
    public final NavigableMap<Integer, CoordRefSys> crsTable;

    public final Map<String, Map<Integer, ? extends Aliased>> aliased = new LinkedHashMap<>();

    public EpsgTables(Connection connection) throws SQLException {


        areas = aliased(Area.TABLE, connection);
        units = aliased(UnitOfMeasure.TABLE, connection);

        ellipsoids = aliased(Ellipsoid.TABLE, connection);
        primes = aliased(PrimeMeridian.TABLE, connection);
        axes = CoordAxis.TABLE.load(connection);

        axisNames = indexed(CoordAxisName.TABLE.load(connection));
        datums = bound(Datum.TABLE, connection);

        params = aliased(CoordOpParam.TABLE, connection);
        methods = aliased(CoordOpMethod.TABLE, connection);

        coops = bound(CoordOp.TABLE, connection);

        csTable = aliased(CoordSys.TABLE, connection);
        crsTable = bound(CoordRefSys.TABLE, connection);

        joinUsages(CoordOpParamUsage.TABLE.load(connection));

        joinValues(CoordOpParamValue.TABLE.load(connection));

        scopes = indexed(Scope.TABLE.load(connection));
        aliases = Alias.TABLE.load(connection);

        for(Alias alias:aliases) {

            //alias.scope = scopes.get(alias.naming_system_code);

            final Map<Integer, ? extends Aliased> map = aliased.get(alias.object_table_name);
            if(map!=null) {
                Aliased entry = map.get(alias.object_code);
                if(entry==null) {
                    String warning = String.format("missing alias target for %s:%d %s",
                            alias.object_table_name, alias.object_code, alias.alias);
                    logger.log(Level.WARNING, warning);
                } else
                    entry.aliases.add(alias);
            } else {
                throw new RuntimeException("missing table: " + alias.object_table_name);
            }
        }

        for (CoordAxis axis : axes) {

            CoordSys cs = csTable.get(axis.coord_sys_code);

            if(cs!=null)
                cs.axes.add(axis);
            else  {
                String warning = String.format("missing crs target for axis %s:%d -> %d",
                        axis.getName(), axis.getKey(), axis.coord_sys_code);
                logger.log(Level.WARNING, warning);
            }
        }

        for (CoordOp op : coops.values())
            if(op.coord_op_method_code!=null)
                op.method = methods.get(op.coord_op_method_code);

        int count = 0;

        for (CoordRefSys crs : crsTable.values()) {
            if(crs.projection_conv_code!=null) {
                CoordOp op = coops.get(crs.projection_conv_code);
                if(op!=null) {
                    op.references.add(crs);
                    int i = op.references.size();
                    if(count<i)
                        count = i;
                } else {
                    String warning = String.format("missing projection_conv_code %s -> %d",
                            crs.getKey(), crs.projection_conv_code);
                    logger.log(Level.WARNING, warning);
                }
            }
        }

        count = 0;

        for(CoopPath path:CoopPath.TABLE.load(connection)) {

            CoordOp op = coops.get(path.concat_operation_code);
            CoordOp step = coops.get(path.single_operation_code);

            op.steps.add(step);
        }
    }

    private void joinUsages(List<CoordOpParamUsage> usages) {

        for(CoordOpParamUsage usage:usages) {
            CoordOpMethod method = methods.get(usage.coord_op_method_code);
            CoordOpParam param = params.get(usage.parameter_code);

            if(method==null || param==null) {
                String warning = String.format("bogus usage mapping %d:%d",
                        usage.coord_op_method_code, usage.parameter_code);
                logger.log(Level.WARNING, warning);
            } else {
                method.usages.add(usage);
                usage.param = param;
                param.usages.add(usage);
            }
        }
    }

    private void joinValues(List<CoordOpParamValue> values) {
        for(CoordOpParamValue value:values) {

            CoordOp coop = coops.get(value.coord_op_code);
            CoordOpParam param = params.get(value.parameter_code);

            if(coop==null) {
                String warning = String.format("bogus value mapping %d:%d",
                        value.coord_op_method_code, value.parameter_code);
                logger.log(Level.WARNING, warning);
            } else {
                value.param = param;
                coop.values.add(value);
            }

            CoordOpMethod method = methods.get(value.coord_op_method_code);

            method.addUsage(value);
        }
    }

    public String toString() {
        return "EpsgTables";
    }

    public <V  extends Aliased>
    NavigableMap<Integer, V> aliased(Table<V> table, Connection connection) throws SQLException {

        List<V> values = table.load(connection);
        NavigableMap<Integer, V> map = indexed(values);

        aliased.put(table.name, map);
        return map;
    }

    public <V  extends Bound>
    NavigableMap<Integer, V> bound(Table<V> table, Connection connection) throws SQLException {

        List<V> values = table.load(connection);
        NavigableMap<Integer, V> map = indexed(values);

        for(Bound bound:values) {
            Area area = areas.get(bound.getAreaCode());
            if(area!=null) {
                area.references.add(bound);
                bound.area = area;
            }
        }

        aliased.put(table.name, map);
        return map;
    }

    public <V  extends Item>
    NavigableMap<Integer, V> indexed(Iterable<V> values) {

        NavigableMap<Integer, V> map = new TreeMap<>();

        for(V v:values)
            map.put(v.getKey(), v);

        return map;
    }

    public Datum getDatum(CoordRefSys crs) {
        if(crs == null)
            return null;

        if(crs.datum_code!=null)
            return datums.get(crs.datum_code);

        Datum datum = null;

        if(crs.source_geogcrs_code!=null)
            datum = getDatum(crsTable.get(crs.source_geogcrs_code));

        if(datum==null && crs.cmpd_horizcrs_code!=null)
            datum = getDatum(crsTable.get(crs.cmpd_horizcrs_code));

        if(datum==null && crs.cmpd_vertcrs_code!=null)
            datum = getDatum(crsTable.get(crs.cmpd_vertcrs_code));

        return datum;
    }

    public Datum getDatum(CoordOp op) {
        Datum datum = null;

        for (CoordRefSys crs : op.references) {
            Datum d = getDatum(crs);
            if(Item.INDEX_ORDER.compare(d, datum)<0)
                datum = d;
        }

        return datum;

    }

    public static void main(String ... args) throws SQLException, IOException, ClassNotFoundException {

        EpsgTables tables = load();

        try(FileOutputStream fos = new FileOutputStream("tables.ser")) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(tables);

            oos.close();
        }

        logger.log(Level.INFO, "read");

        try(FileInputStream fis = new FileInputStream("tables.ser")) {
            ObjectInputStream oos = new ObjectInputStream(fis);
            Object obj = oos.readObject();
        }

        logger.log(Level.INFO, "done");
    }
}
