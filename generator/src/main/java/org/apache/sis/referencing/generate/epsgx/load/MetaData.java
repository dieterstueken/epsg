package org.apache.sis.referencing.generate.epsgx.load;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 22.02.14
 * Time: 16:28
 */
abstract public class MetaData extends MetaDataBase {

    String remarks;

    public static class Builder<I extends MetaData> extends MetaDataBase.Builder<I> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(I item) {
            out.addComment("remarks:", item.remarks);

            return super.dumpComments(item);
        }
    }
}
