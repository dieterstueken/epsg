package org.apache.sis.referencing.generate.factory.xepsg;

import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  31.10.2014 13:02
 * modified by: $Author$
 * modified on: $Date$
 */
public class EpsgTable<T> extends EntryMap<T> implements EpsgFactory {

    public static <T> EpsgTable<T> of(Class<T> type) {
        return new EpsgTable<T>(type);
    }

    final Class<T> type;

    public EpsgTable(Class<T> type) {
        this.type = type;
    }

    Class<T> getType() {
        return type;
    }

    public String toString() {
        return String.format("EpsgTable@%s[%d]", type.getSimpleName(), size());
    }

    public <I> I get(String code, Class<I> type) throws NoSuchAuthorityCodeException {

        final Object obj = get(code);

        if(!type.isInstance(obj))
            throw error(code, type);

        return type.cast(obj);
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) {
        return new AbstractSet<String>() {

            @Override
            public Iterator<String> iterator() {
                return entrySet().stream()
                        .filter(entry -> type.isInstance(entry.getValue()))
                        .map(Entry::getKey)
                        .iterator();
            }

            @Override
            public int size() {
                return (int) stream().count();
            }
        };
    }

    @Override
    public IdentifiedObject createObject(String code) throws NoSuchAuthorityCodeException {
        return get(code, IdentifiedObject.class);
    }
}
