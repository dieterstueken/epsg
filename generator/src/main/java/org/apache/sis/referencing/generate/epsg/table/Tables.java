package org.apache.sis.referencing.generate.epsg.table;

import org.apache.sis.referencing.generate.epsg.generate.EpsgTables;
import org.apache.sis.referencing.generate.epsg.load.CoordRefSys;

import java.io.*;
import java.sql.SQLException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.08.2014 13:47
 * modified by: $Author$
 * modified on: $Date$
 */
public class Tables {

    final EpsgTables tables = EpsgTables.load();

    Tables() throws SQLException, IOException {}

    public void run() throws IOException, ClassNotFoundException {

        SparseList<CoordRefSys> ops = SparseTable.create();

        CoordRefSys last = tables.crsTable.lastEntry().getValue();
        ops.set(last.getKey(), last);

        for (CoordRefSys entry : tables.crsTable.values()) {
            ops.set(entry.getKey(), entry);
        }

        System.out.format("range: %d count: %d\n", ops.range(), ops.size());

        File file = new File("tables.ser");

        try(FileOutputStream fos = new FileOutputStream(file)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(ops);

            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.format("file size: %d\n", file.length());

        try(FileInputStream fis = new FileInputStream("tables.ser")) {
            ObjectInputStream oos = new ObjectInputStream(fis);
            Object obj = oos.readObject();
        }
    }

    public static void main(String ... args) throws IOException, SQLException, ClassNotFoundException {
        new Tables().run();
    }
}
