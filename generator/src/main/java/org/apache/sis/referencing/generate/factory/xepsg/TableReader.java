package org.apache.sis.referencing.generate.factory.xepsg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  04.11.2014 09:39
 * modified by: $Author$
 * modified on: $Date$
 */
public class TableReader {

    public static void main(String ... args) throws IOException, ClassNotFoundException {

        System.out.println("ready");
        System.in.read();

        try(FileInputStream fis = new FileInputStream("C:\\Temp\\tables.dat");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            System.out.format("loaded: %s\n", obj.getClass().getSimpleName());
        }

        System.out.println("done");

        System.in.read();
    }
}
