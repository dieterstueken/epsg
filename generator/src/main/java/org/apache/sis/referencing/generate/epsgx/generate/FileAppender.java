package org.apache.sis.referencing.generate.epsgx.generate;

import org.apache.sis.referencing.generate.epsgx.load.OutputAppender;

import java.io.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  28.02.14 10:08
 * modified by: $Author$
 * modified on: $Date$
 */
public class FileAppender extends OutputAppender implements Closeable {

    final PrintStream out;

    int indent = 0;

    String remarks = null;

    public FileAppender(PrintStream out) {
        this.out = out;
    }

    public FileAppender(File out) throws FileNotFoundException {
        this.out = new PrintStream(out);
    }

    @Override
    public OutputAppender append(CharSequence csq) {
        if(csq!=null)
            out.append(csq);
        return this;
    }

    @Override
    public OutputAppender append(CharSequence csq, int start, int end) {
        out.append(csq);
        return this;
    }

    @Override
    public OutputAppender append(char c) {
        out.append(c);
        return this;
    }

    @Override
    public OutputAppender format(String format, Object... args) {
        out.format(format, args);
        return this;
    }

    public OutputAppender indent(int shift) {
        indent += shift;
        return this;
    }

    public FileAppender remark(String remarks) {
        this.remarks = isNullOrEmpty(remarks) ? null : remarks;
        return this;
    }

    @Override
    public OutputAppender nl() {

        if(remarks !=null) {
            append(" // ").append(remarks);
            remarks = null;
        }

        out.println();

        for(int i=0; i<indent; ++i)
            out.append(' ');

        return this;
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
