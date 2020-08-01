package org.apache.sis.referencing.epsg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 17:02
 * modified by: $Author$
 * modified on: $Date$
 */
public class Method extends Aliased {

    public static class Usage {

        final Parameter param;
        final Boolean reverse;

        public Usage(Parameter param, Boolean reverse) {
            this.param = param;
            this.reverse = reverse;
        }
    }

    public static Usage usage(Parameter param, Boolean reverse) {
        return new Usage(param, reverse);
    }

    public static Usage usage(Parameter param) {
        return new Usage(param, null);
    }

    final List<Usage> usages;

    public Method(Integer key, Name name, List<Usage> usages) {
        super(key, name);

        this.usages = usages;
    }

    public static Method method(Integer key, Name name, List<Usage> usages) {
        return new Method(key, name, usages);
    }

    public static Method method(Integer key, Name name) {
        return new Method(key, name, Collections.<Usage>emptyList());
    }

    public static Method method(Integer key, Name name, Usage usage) {
        return new Method(key, name, Collections.singletonList(usage));
    }

    public static Method method(Integer key, Name name, Usage ... usages) {
        return new Method(key, name, Collections.unmodifiableList(Arrays.asList(usages)));
    }

    public static Method method(Method method) {
        return method;
    }

    public static Method method() {
        return null;
    }
}
