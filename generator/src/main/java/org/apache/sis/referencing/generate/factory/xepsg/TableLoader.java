package org.apache.sis.referencing.generate.factory.xepsg;

import org.apache.sis.referencing.factory.GeodeticAuthorityFactory;
import org.apache.sis.referencing.generate.AuthorityFactoryFinder;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.util.FactoryException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  03.11.2014 15:41
 * modified by: $Author$
 * modified on: $Date$
 */
public class TableLoader {

    interface Loader<T> {
        T readObject(String s) throws FactoryException;
    }
    
    <T> Collection<String> load(EpsgTable<T> table, Set<String> codes, Loader<T> loader) throws FactoryException {

        Collection<String> failed = new ArrayList<>();

        codes.forEach(code -> {
            try {
                T obj = loader.readObject(code);
                table.put(code, obj);
            } catch (FactoryException e) {
                failed.add(code);
                //System.err.format("%s failed\n", code);
            }
        });

        System.out.format("%s %d failed\n", table.toString(), failed.size());
    
        return failed;
    }

    <T> Collection<String> load(EpsgTable<T> table, GeodeticAuthorityFactory factory, Loader<T> loader) throws FactoryException {

        Class<? extends IdentifiedObject> type = (Class<? extends IdentifiedObject>) table.getType();
        return load(table, factory.getAuthorityCodes(type), loader);
    }

        
    EpsgTables load(GeodeticAuthorityFactory factory) throws FactoryException {
        
        EpsgTables  tables = new EpsgTables();
        
        load(tables.units, factory, factory::createUnit);

        load(tables.datums, factory, factory::createDatum);
        load(tables.ellipsoids, factory, factory::createEllipsoid);
        load(tables.meridians, factory, factory::createPrimeMeridian);
        
        load(tables.methods, factory, factory::createOperationMethod);
        load(tables.operations, factory, factory::createCoordinateOperation);
        
        load(tables.systems, factory, factory::createCoordinateSystem);
        load(tables.axes, factory, factory::createCoordinateSystemAxis);

        load(tables.reference_systems, factory, factory::createCoordinateReferenceSystem);
    
        return tables;
    }

    public static void main(String ... args) throws FactoryException, IOException {

        final GeodeticAuthorityFactory factory = AuthorityFactoryFinder.getAuthorityFactory();

        EpsgTables tables = new TableLoader().load(factory);

        System.out.println("ready");

        try(FileOutputStream fos = new FileOutputStream("C:\\Temp\\tables.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tables);
        }
    }
}
