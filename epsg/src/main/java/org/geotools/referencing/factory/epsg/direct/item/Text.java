package org.geotools.referencing.factory.epsg.direct.item;

import org.opengis.util.InternationalString;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  03.11.2015 11:30
 * modified by: $Author$
 * modified on: $Date$
 */
public class Text implements CharSequence, InternationalString, Serializable {

    final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public String toString(Locale locale) {
        return text;
    }

    @Override
    public int length() {
        return text.length();
    }

    @Override
    public char charAt(int index) {
        return text.charAt(index);
    }

    @Override
    public String subSequence(int start, int end) {
        return text.substring(start, end);
    }

    @Override
    public int compareTo(InternationalString object) {
        return object==null ? 1 : this.text.compareTo(object.toString());
    }

    @Override
    public boolean equals(Object o) {
        return this==o || ((o instanceof InternationalString) && Objects.equals(text, o.toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
