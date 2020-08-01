package org.apache.sis.referencing.generate.epsgx.load;

import java.util.LinkedList;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  14.02.14 13:37
 * modified by: $Author$
 * modified on: $Date$
 */
abstract public class Aliased extends Item {

    @Transient
    public final List<Alias> aliases = new LinkedList<>();

    public static class Builder<I extends Aliased> extends ItemBuilder<I> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpName(I item) {

            out.nl().append(",name(").quote(item.getName());

            if(item.aliases.size()==1) {
                out.append(", ");
                resolver.alias(item.aliases.get(0), out);
            } else {
                out.indent(4);
                for (Alias alias : item.aliases) {
                    out.nl().append(',');
                    resolver.alias(alias, out);
                }
                out.indent(-4);
            }

            out.append(")");

            return out;
        }
    }
}
