package org.geotools.referencing.factory.epsg.direct.item.code;

import org.opengis.util.InternationalString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.geotools.text.Text.text;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 14:47
 * modified by: $
 * modified on: $
 */
public class Code extends Named implements Indexed {

    public final int code;

    Collection<Alias> aliases = null;

    public Code(int code, InternationalString name, InternationalString remarks) {
        super(name, remarks);
        this.code = code;
    }

    public static Code code(int code, String name, String remarks) {
        return new Code(code, text(name), text(remarks));
    }

    public static Code code(int code, String name) {
        return new Code(code, text(name), Text.EMPTY);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return code;
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

    public Collection<Alias> getAliases() {
        return aliases==null ? Collections.emptySet() : aliases;
    }

    public void aliased(Alias alias) {
        if(aliases==null)
            aliases = List.of(alias);
        else {
            if(aliases.size()==1) {
                List<Alias> tmp = new ArrayList<>(2);
                tmp.addAll(aliases);
                this.aliases = tmp;
                aliases.add(alias);
            } else {
                aliases.add(alias);
            }
        }
    }
}
