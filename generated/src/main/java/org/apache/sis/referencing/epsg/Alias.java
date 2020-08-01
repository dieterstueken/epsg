package org.apache.sis.referencing.epsg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 14:46
 * modified by: $Author$
 * modified on: $Date$
 */
public class Alias {

    final String name;

    final String remarks;

    private Alias(String name, String remarks) {
        this.name = name;
        this.remarks = remarks;
    }

    public static Alias alias(String name, String remarks) {
        return new Alias(name, remarks);
    }

    public static List<Alias> aliases() {
        return Collections.emptyList();
    }

    public static List<Alias> aliases(Alias alias) {
        return Collections.singletonList(alias);
    }

    public static List<Alias> aliases(Alias ... aliases) {
        return Collections.unmodifiableList(Arrays.asList(aliases));
    }
}
