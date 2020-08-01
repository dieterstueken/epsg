package org.apache.sis.referencing.generate.epsgx.generate;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  28.02.14 09:50
 * modified by: $Author$
 * modified on: $Date$
 */
public class OutputPackage  {

    final File path;
    final String name;

    public OutputPackage(File path, String name) {
        this.path = path;
        this.name = name;
    }

    public OutputPackage newPackage(String name) throws FileNotFoundException {

        File new_path = mkdir(path, name);
        String new_pkg = this.name + '.' + name;

        return new OutputPackage(new_path, new_pkg);
    }

    public static File mkdir(File parent, String name) throws FileNotFoundException {
        File dir =  new File(parent, name);

        dir.mkdirs();

        if(!dir.isDirectory())
            throw new FileNotFoundException(dir.getAbsolutePath());

        return dir;
    }

    public File classFile(String name) {
        int i = name.indexOf(' ');

        if(i>0)
            name = name.substring(0, i);

        return new File(path, name + ".java");
    }
}
