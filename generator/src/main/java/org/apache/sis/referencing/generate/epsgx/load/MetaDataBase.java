package org.apache.sis.referencing.generate.epsgx.load;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 22.02.14
 * Time: 18:16
 */
abstract public class MetaDataBase extends Aliased {

    String information_source;
    String data_source;
    Date revision_date;
    String change_id;
    Boolean deprecated;

    public static class Builder<I extends MetaDataBase> extends Aliased.Builder<I> {

        public Builder(ItemResolver resolver, OutputAppender out, String modifiers, String prefix) {
            super(resolver, out, modifiers, prefix);
        }

        @Override
        public OutputAppender dumpComments(I item) {
            super.dumpComments(item);

            out.nl().append("// information source: ").append(item.information_source);
            out.nl().append("// data source:        ").append(item.data_source);
            out.nl().append("// revision date:      ").append(item.revision_date.toString());
            out.nl().append("// change id:          ").append(item.change_id);
            out.nl().append("//");

            return out;
        }

        @Override
        public OutputAppender dumpHead(I item) {

            if(item.deprecated)
                out.nl().append("@Deprecated");

            return super.dumpHead(item);
        }
    }
}
