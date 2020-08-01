package org.apache.sis.referencing.generate.epsgx.load;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  13.02.14 10:17
 * modified by: $Author$
 * modified on: $Date$
 */
public class Alias extends Item {

    public Integer alias_code;
    public String object_table_name;
    public Integer object_code;
    public Integer naming_system_code;
    public String alias;
    public String remarks;

    public Integer getKey() {
        return alias_code;
    }

    public String getName() {
        return alias;
    }

    public static final Table<Alias> TABLE = Table.of(Alias.class, "Alias", "epsg_alias");
}
