package org.apache.sis.referencing.generate.epsgx.generate;

import org.apache.sis.referencing.generate.epsgx.load.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 05.03.14
 * Time: 19:58
 */
public class Generator2 {

    final EpsgTables tables;

    final OutputPackage root;

    final OutputPackage generated;
    final OutputPackage areas;

    public Generator2(OutputPackage root, EpsgTables tables) throws FileNotFoundException {

        this.tables = tables;

        this.root = root;

        this.generated = root.newPackage("generated");

        this.areas = generated.newPackage("areas");
    }


    public static void main(String... args) throws SQLException, IOException {

        File root = new File(System.getProperty("epsg.output.dir"));
        if (!root.isDirectory())
            throw new RuntimeException("invalid epsg.output.dir");

        String pkg = System.getProperty("epsg.output.package");
        if (pkg == null)
            throw new RuntimeException("invalid epsg.output.package");

        EpsgTables tables = EpsgTables.load();

        new Generator2(new OutputPackage(root, pkg), tables).run();
    }

    final ItemResolver resolver = new ItemResolver() {

        @Override
        public OutputAppender unit(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append("null");

            out.nl().appendCode(",U_", key);

            UnitOfMeasure unit = tables.units.get(key);
            if (unit != null)
                out.remark(unit.getName());

            return out;
        }

        @Override
        public OutputAppender alias(Alias alias, OutputAppender out) {
            return out.append("s.s").append(alias.naming_system_code).append(".alias(").quote(alias.getName()).append(')');
        }

        @Override
        public OutputAppender area(Area area, OutputAppender out) {
            if (area == null)
                return out.nl().append(",null").remark("no area code");


            return out.nl().format(",A%d.", area.getKey() / 100).appendCode("A_", area.getKey()).remark(area.getName());
        }

        @Override
        public OutputAppender method(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append(",null").remark("no method code");

            CoordOpMethod method = tables.methods.get(key);

            return out.nl().appendCode(",M_", key).remark(method.getName());
        }

        @Override
        public OutputAppender operation(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append(",null").remark("no operation code");

            CoordOp op = tables.coops.get(key);

            return out.nl().format(",OP%d.", key / 100).appendCode("OP_", key).remark(op.coord_op_name);
        }

        @Override
        public OutputAppender crs(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append(",null").remark("no crs code");

            CoordRefSys crs = tables.crsTable.get(key);

            return out.nl().format(",CRS%d.", CoordRefSys.getGroup(key)).appendCode("CRS_", key).remark(crs.getName());
        }

        @Override
        public OutputAppender cs(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append(",null").remark("no crs code");

            return out.nl().appendCode(",CS_", key);
        }

        @Override
        public OutputAppender datum(Integer key, OutputAppender out) {
            if (key == null)
                return out.nl().append(",null").remark("no datum code");

            Datum datum = tables.datums.get(key);

            return out.nl().appendCode(",D_", key).remark(datum.getName());
        }

        @Override
        public OutputAppender ellipsoid(Integer key, OutputAppender out) {
            if (key == null) return out.nl().append(",null");

            Ellipsoid ellipsoid = tables.ellipsoids.get(key);

            return out.nl().appendCode(",E_", key).remark(ellipsoid.getName());
        }

        @Override
        public OutputAppender meridian(Integer key, OutputAppender out) {
            return key == null ? out.nl().append(",null") : out.nl().appendCode(",PM_", key);
        }
    };

    public void run() throws IOException {

        try(OutputClass out = new OutputClass(generated, "EpsgScopes", "extends Scopes")) {

            out.addImport(root);
            out.addImport("org.opengis.metadata.citation.Citation");

            out.body();
            out.format("public %s(Citation authority) {super(authority);}", out.name).nl();
            out.nl();

            new Scope.Builder(resolver, out, "public final", "s"){
                @Override
                public OutputAppender dumpType(Scope item) {
                    return out.append("Scope");
                }
            }.dumpAll(tables.scopes.values());
        }

        try (CodeList<Area> out = areas()) {
            out.dumpAll(tables.areas.values());
        }
    }

    public CodeList<Area> areas() throws FileNotFoundException {

        final OutputClass groups = new OutputClass(generated, "EpsgAreas", "extends Areas");
        groups.addImport(root);
        groups.addImport(areas);
        groups.body();
        groups.append("public ").append(groups.name).append("(EpsgScopes scopes) {super(scopes);}").nl().nl();

        return new CodeList<Area>() {

            @Override
            ItemBuilder<Area> open(int group) throws FileNotFoundException {

                String name = String.format("A%d", group);

                OutputClass out = new OutputClass(areas, name, "extends Area.Group");
                out.nl();
                out.addImport("java.util.List").nl();
                out.addImport(root);
                out.body();
                out.format("public %s(Areas areas) {super(areas);}", name).nl().nl();

                groups.format("public final %s a%d = new %s(this);", name, group, name).nl();

                return new Area.Builder(resolver, out, "public final", "a");
            }

            @Override
            int getGroup(int code) {
                return code / 100;
            }

            @Override
            public void close() throws IOException {
                groups.close();
                super.close();
            }
        };
    }
}
