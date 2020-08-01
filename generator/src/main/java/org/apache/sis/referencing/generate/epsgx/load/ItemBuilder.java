package org.apache.sis.referencing.generate.epsgx.load;

import java.io.Closeable;
import java.io.IOException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  28.02.14 18:05
 * modified by: $Author$
 * modified on: $Date$
 */
public class ItemBuilder<I extends Item> implements Closeable {

    protected final ItemResolver resolver;
    protected final OutputAppender out;

    final String modifiers;
    final String prefix;

    public ItemBuilder(ItemResolver resolver, OutputAppender out, final String modifiers, final String prefix) {
        this.resolver = resolver;
        this.out = out;
        this.modifiers = modifiers;
        this.prefix = prefix;
    }

    public OutputAppender dumpComments(I item) {
        return out;
    }

    public OutputAppender dumpName(I item) {
        return out.append(", ").quote(item.getName());
    }

    public OutputAppender dumpType(I item) {
        return out.append(item.getType());
    }

    public OutputAppender dumpFunction(I item) {
        return out.append(item.getFunction()).append('(');
    }

    public OutputAppender dumpHead(I item) {

        out.nl().append(modifiers).sp();
        dumpType(item).sp();
        out.appendCode(prefix, item.getKey()).append(" = ");
        dumpFunction(item);

        out.append(item.getKey().toString());
        out.indent(4);
        dumpName(item);

        return out;
    }

    public OutputAppender dumpTail(I item) {
        out.append(");");
        out.indent(-4);
        return out.nl();
    }

    public OutputAppender dumpItem(I item) {
        dumpComments(item);
        dumpHead(item);
        dumpTail(item);
        return out.nl();
    }

    public void dumpAll(Iterable<? extends I> items) {
        for (I item : items) {
            dumpItem(item);
        }
    }

    @Override
    public void close() throws IOException {
        if(out instanceof Closeable) {
            ((Closeable) out).close();
        }
    }
}
