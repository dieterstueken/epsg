package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.02.14 11:57
 * modified by: $Author$
 * modified on: $Date$
 */
abstract public class Bound extends MetaData {

    public Integer area_of_use_code;

    @Transient
    public Area area = null;

    public Integer getAreaCode() {
        return area_of_use_code;
    }

    abstract public static class Builder<I extends Bound> extends MetaData.Builder<I> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpHead(I item) {
            super.dumpHead(item);
            return resolver.area(item.area, out);
        }
    }
}
