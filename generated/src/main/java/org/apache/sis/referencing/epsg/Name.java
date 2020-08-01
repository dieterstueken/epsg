package org.apache.sis.referencing.epsg;

import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  19.02.14 12:07
 * modified by: $Author$
 * modified on: $Date$
 */
public class Name {

    final String name;

    public final List<Alias> aliases;

    private Name(String name, List<Alias> aliases) {
        this.aliases = aliases;
        this.name = name;
    }

    public static Name name(String name, List<Alias> aliases) {
        return new Name(name, aliases);
    }

    public static Name name(String name) {
        return name(name, Alias.aliases());
    }

    public static Name name(String name, Alias alias) {
        return name(name, Alias.aliases(alias));
    }

    public static Name name(String name, Alias ... aliases) {
        return name(name, Alias.aliases(aliases));
    }

    public static Alias alias(String name, String remarks) {
        return Alias.alias(name, remarks);
    }

    public static Alias alias(String name) {
        return alias(name, null);
    }
}
