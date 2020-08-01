package org.apache.sis.referencing.generate;

import org.apache.sis.referencing.factory.GeodeticAuthorityFactory;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.util.FactoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.03.2015 16:37
 * modified by: $Author$
 * modified on: $Date$
 */
public class DumpDatabase {

    interface Factory<T extends IdentifiedObject> {
        T createObject(String code) throws NoSuchAuthorityCodeException, FactoryException;
    }

    static <T extends IdentifiedObject> void dump(GeodeticAuthorityFactory factory, Factory<T> lookup, Class<? extends T> type) throws FactoryException {

        Set<String> codes = factory.getAuthorityCodes(type);

        List<T> objects = new ArrayList<>(codes.size());

        for (String code : codes) {
            try {
                T obj = lookup.createObject(code);
                objects.add(obj);
            } catch(Throwable error) {
                //System.out.println(error.toString());
            }
        }

        System.out.format("%s: %d of %d\n", type.getSimpleName(), objects.size(), codes.size());
    }

    public static void main(String[] args) throws FactoryException {
        //Hints.putSystemDefault(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        final GeodeticAuthorityFactory factory = AuthorityFactoryFinder.getAuthorityFactory();

        dump(factory, factory::createPrimeMeridian, PrimeMeridian.class);
        dump(factory, factory::createEllipsoid, Ellipsoid.class);
        dump(factory, factory::createDatum, Datum.class);

        dump(factory, factory::createCoordinateSystemAxis, CoordinateSystemAxis.class);
        dump(factory, factory::createCoordinateReferenceSystem, CoordinateReferenceSystem.class);
    }
}
