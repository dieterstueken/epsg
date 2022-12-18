package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.referencing.generate.epsg.load.*;
import org.apache.sis.util.logging.Logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 15:10
 * modified by: $Author$
 * modified on: $Date$
 */
public class Generator implements ItemResolver {

    protected static final Logger LOGGER = Logging.getLogger(Generator.class.getName());
    protected static final Level LEVEL = Level.INFO;

    final EpsgTables tables;

    final OutputPackage root;
    final OutputPackage generated;
    final OutputPackage areas;
    final OutputPackage operations;
    final OutputPackage systems;

    public Generator(OutputPackage root, EpsgTables tables) throws FileNotFoundException {
        this.tables = tables;
        this.root = root;

        this.generated = root.newPackage("generated");

        this.areas = generated.newPackage("areas");
        this.operations = generated.newPackage("operations");
        this.systems = generated.newPackage("systems");
    }

    public static void main(String ... args) throws SQLException, IOException {

        File root = new File(System.getProperty("epsg.output.dir"));
        root.mkdirs();
        if(!root.isDirectory())
            throw new RuntimeException("invalid epsg.output.dir");

        String pkg = System.getProperty("epsg.output.package");
        if(pkg==null)
            throw new RuntimeException("invalid epsg.output.package");

        EpsgTables tables = EpsgTables.load();

        new Generator(new OutputPackage(root, pkg), tables).run();
    }

    public void run() throws IOException {
        
        try(OutputClass out = new OutputClass(generated, "Scopes","extends Root")) {
            LOGGER.log(LEVEL, "Scopes");

            out.addImport("org.opengis.util.NameSpace");
            out.addImport("org.opengis.metadata.citation.Citation");
            out.addImport("org.apache.sis.metadata.iso.citation.Citations");
            out.nl();

            out.addImport(root);
            out.addImportStatic(root, "Name.*");

            new Scope.Builder(this, out.body(), "public final", "NS_").dumpAll(tables.scopes.values());
        }

        try(OutputClass out = new OutputClass(generated, "CoordSystems")) {
            LOGGER.log(LEVEL, "CoordSystems");

            out.addImport(root);
            out.addImportStatic(root, "Name.*");
            out.addImportStatic(root, "UoM.*");
            out.addImportStatic(root, "CoordSys.*");
            out.addImportStatic(root, "CoordAxis.*");
            out.addImportStatic(root, "AxisName.*");

            new CoordAxisName.Builder(this, out.body(), "public static final", "AXN_").dumpAll(tables.axisNames.values());
            new CoordSys.Builder(this, out, "public static final", "CS_").dumpAll(tables.csTable.values());
        }


        try(OutputClass out = new OutputClass(generated, "Ellipsoids")) {
            LOGGER.log(LEVEL, "Ellipsoids");

            out.addImport(root);
            out.addImportStatic(root, "Name.*");
            out.addImportStatic(root, "UoM.*");
            out.addImportStatic(root, "Ellipsoid.*");

            new Ellipsoid.Builder(this, out.body(), "public static final", "E_").dumpAll(tables.ellipsoids.values());
        }

        try(OutputClass out = new OutputClass(generated, "PrimeMeridians")) {
            LOGGER.log(LEVEL, "PrimeMeridians");

            out.addImport(root);
            out.addImportStatic(root, "Name.*");
            out.addImportStatic(root, "UoM.*");
            out.addImportStatic(root, "PrimeMeridian.*");

            new PrimeMeridian.Builder(this, out.body(), "public static final", "PM_").dumpAll(tables.primes.values());
        }

        try(OutputClass out = new OutputClass(generated, "Datums")) {
            LOGGER.log(LEVEL, "Datums");

            out.addImport(root);
            out.addImportStatic(root, "Name.*");
            out.addImportStatic(root, "Datum.*");
            out.addImportStatic(generated, "Ellipsoids.*");
            out.addImportStatic(generated, "PrimeMeridians.*");
            out.addImport(areas);

            new Datum.Builder(this, out.body(), "public static final", "D_").dumpAll(tables.datums.values());
        }

        try(OutputClass out = new OutputClass(generated, "Parameters")) {
            LOGGER.log(LEVEL, "Parameters");

            out.addImport(root);
            out.addImportStatic(root, "Parameter.*");
            out.addImportStatic(root, "Name.*");

            new CoordOpParam.Builder(this, out.body(), "public static final", "P_").dumpAll(tables.params.values());
        }

        try(OutputClass out = new OutputClass(generated, "Methods")) {
            LOGGER.log(LEVEL, "Methods");

            out.addImport(root);
            out.addImportStatic(root, "Method.*");
            out.addImportStatic(root, "Name.*");
            out.addImportStatic(generated, "Parameters.*");

            new CoordOpMethod.Builder(this, out.body(), "public static final", "M_").dumpAll(tables.methods.values());
        }

        try(CodeList<Area> out = areas()) {
            LOGGER.log(LEVEL, "Areas");
            out.dumpAll(tables.areas.values());
        }

        try(CodeList<CoordOp> out = operations()) {
            LOGGER.log(LEVEL, "Operations");
            out.dumpAll(tables.coops.values());
        }

        try(CodeList<CoordRefSys> out = systems()) {
            LOGGER.log(LEVEL, "Systems");
            out.dumpAll(tables.crsTable.values());
        }
    }

    public CodeList<CoordOp> operations() {
        return new CodeList<CoordOp>() {

            @Override
            ItemBuilder<CoordOp> open(int group) throws FileNotFoundException {

                String name = String.format("OP%d", group);
                OutputClass out = new OutputClass(operations, name);

                out.addImport(root);

                out.addImportStatic(root, "Name.*");
                out.addImportStatic(root, "Operation.*");
                out.addImportStatic(root, "UoM.*");

                out.addImportStatic(generated, "Parameters.*");
                out.addImportStatic(generated, "Methods.*");

                out.addImport(areas);
                out.addImport(systems);

                return new CoordOp.Builder(Generator.this, out.body(), "public static final", "OP_");
            }

            @Override
            int getGroup(int code) {
                return code/100;
            }
        };
    }

    public CodeList<Area> areas() {
        return new CodeList<Area>() {

            @Override
            ItemBuilder<Area> open(int group) throws FileNotFoundException {

                String name = String.format("A%d", group);
                OutputClass out = new OutputClass(areas, name);

                out.addImport(root);

                out.addImportStatic(root, "Name.*");
                out.addImportStatic(root, "Area.*");

                return new Area.Builder(Generator.this, out.body(), "public static final", "A_");
            }

            @Override
            int getGroup(int code) {
                return code /100;
            }
        };
    }

    public CodeList<CoordRefSys> systems() {
        return new CodeList<CoordRefSys>() {

            @Override
            ItemBuilder<CoordRefSys> open(int group) throws FileNotFoundException {

                String name = String.format("CRS%d", group);
                OutputClass out = new OutputClass(systems, name);

                out.addImport(root);

                out.addImportStatic(root, "Name.*");
                out.addImportStatic(root, "CoordRefSys.*");

                out.addImport(areas);
                out.addImport(operations);

                out.addImportStatic(generated, "CoordSystems.*");
                out.addImportStatic(generated, "Datums.*");

                return new CoordRefSys.Builder(Generator.this, out.body(), "public static final", "CRS_");
            }

            @Override
            int getGroup(int code) {
                return CoordRefSys.getGroup(code);
            }
        };
    }

    @Override
    public OutputAppender unit(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append("null");

        out.nl().appendCode(",U_", key);

        UnitOfMeasure unit = tables.units.get(key);
        if(unit!=null)
            out.remark(unit.getName());

        return out;
    }

    @Override
    public OutputAppender alias(Alias alias, OutputAppender out) {
        return out.append("alias(").quote(alias.getName()).append(')')
                .remark(alias.naming_system_code.toString());
    }

    @Override
    public OutputAppender area(Area area, OutputAppender out) {
        if(area==null)
            return out.nl().append(",null").remark("no area code");


        return out.nl().format(",A%d.", area.getKey() / 100).appendCode("A_", area.getKey()).remark(area.getName());
    }

    @Override
    public OutputAppender method(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no method code");

        CoordOpMethod method = tables.methods.get(key);

        return out.nl().appendCode(",M_", key).remark(method.getName());
    }

    @Override
    public OutputAppender operation(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no operation code");

        CoordOp op = tables.coops.get(key);

        return out.nl().format(",OP%d.", key/100).appendCode("OP_", key).remark(op.coord_op_name);
    }

    @Override
    public OutputAppender crs(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no crs code");

        CoordRefSys crs = tables.crsTable.get(key);

        return out.nl().format(",CRS%d.", CoordRefSys.getGroup(key)).appendCode("CRS_", key).remark(crs.getName());
    }

    @Override
    public OutputAppender cs(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no crs code");

        return out.nl().appendCode(",CS_", key);
    }

    @Override
    public OutputAppender datum(Integer key, OutputAppender out) {
        if(key==null)
            return out.nl().append(",null").remark("no datum code");

        Datum datum = tables.datums.get(key);

        return out.nl().appendCode(",D_", key).remark(datum.getName());
    }

    @Override
    public OutputAppender ellipsoid(Integer key, OutputAppender out) {
        if(key==null) return out.nl().append(",null");

        Ellipsoid ellipsoid = tables.ellipsoids.get(key);

        return out.nl().appendCode(",E_", key).remark(ellipsoid.getName());
    }

    @Override
    public OutputAppender meridian(Integer key, OutputAppender out) {
        return key==null ? out.nl().append(",null") : out.nl().appendCode(",PM_", key);
    }
}
