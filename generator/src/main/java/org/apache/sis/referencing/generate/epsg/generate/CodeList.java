package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.referencing.generate.epsg.load.Item;
import org.apache.sis.referencing.generate.epsg.load.ItemBuilder;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
* Created by IntelliJ IDEA.
* User: stueken
* Date: 01.03.14
* Time: 14:35
*/
abstract class CodeList<I extends Item> implements Closeable {

    Map<Integer, ItemBuilder<I>> files = new TreeMap<>();

    abstract ItemBuilder<I> open(int hash) throws IOException;

    abstract int getGroup(int code);

    void dumpItem(I item) throws IOException {
        int hash = getGroup(item.getKey());

        ItemBuilder<I> file = files.get(hash);
        if(file==null) {
            file = open(hash);
            files.put(hash, file);
        }

        file.dumpItem(item);
    }

    void dumpAll(Iterable<? extends I> items) throws IOException {
        for (I item : items) {
            dumpItem(item);
        }
    }

    @Override
    public void close() throws IOException {
        for (ItemBuilder<I> file : files.values()) {
            file.close();
        }

        files.clear();
    }
}
