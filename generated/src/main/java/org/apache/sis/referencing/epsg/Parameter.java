package org.apache.sis.referencing.epsg;

public class Parameter extends Aliased {

    final String description;

    Parameter(Integer key, Name name, String description) {
        super(key, name);
        this.description = description;
    }

    public static Parameter parameter(Integer key, Name name, String description) {
        return new Parameter(key, name, description);
    }

    public static Parameter parameter(Integer key, Name name) {
        return new Parameter(key, name, null);
    }
}
