package org.apache.sis.referencing.generate.epsgx.generate;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 14:21
 * modified by: $Author$
 * modified on: $Date$
 */
public class OutputClass extends FileAppender {

    public final String name;
    public final String extension;

    public OutputClass(OutputPackage pkg, String name) throws FileNotFoundException {
        this(pkg, name, "");
    }

    public OutputClass(OutputPackage pkg, String name, String extension) throws FileNotFoundException {
        super(pkg.classFile(name));
        this.name = name;
        this.extension = extension;
        append("package ").append(pkg.name).append(";").nl().nl();
    }

    public OutputClass addImport(String name) {
        append("import ").append(name).append(';').nl();
        return this;
    }

    public OutputClass  addImport(OutputPackage pkg) {
         append("import ").append(pkg.name).append(".*;").nl();
        return this;
    }

    public OutputClass  addImport(OutputPackage pkg, String name) {
        append("import ").append(pkg.name).append('.').append(name).append(';').nl();
        return this;
    }

    public OutputClass  addImportStatic(OutputPackage pkg) {
        append("import static ").append(pkg.name).append(".*;").nl();
        return this;
    }

    public OutputClass  addImportStatic(OutputPackage pkg, String name) {
        append("import static ").append(pkg.name).append('.').append(name).append(';').nl();
        return this;
    }

    public OutputClass body() {
        nl();
        append("public class ").append(name).append(' ').append(extension).append(" {");
        indent(4).nl().nl();
        return this;
    }

    @Override
    public void close() throws IOException {

        if(indent>0)
            indent(-indent).nl().append('}').nl();

        super.close();
    }
}
