package org.apache.sis.referencing.generate;

import org.apache.sis.metadata.iso.citation.Citations;
import org.apache.sis.referencing.CommonCRS;

import org.apache.sis.referencing.factory.GeodeticAuthorityFactory;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.util.FactoryException;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  23.03.2015 13:12
 * modified by: $Author$
 * modified on: $Date$
 */
public class Sample {

    public static void main(String ... args) throws FactoryException {

        final GeodeticAuthorityFactory factory = AuthorityFactoryFinder.getAuthorityFactory();

        final CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:31466");
        System.out.println(crs.toString());
        for (Identifier id : crs.getIdentifiers()) {
            final String identifier = Citations.getIdentifier(id.getAuthority());
            System.out.println(identifier);
        }


        final GeodeticDatum datum = CommonCRS.ETRS89.datum();
        System.out.println(datum.toString());


    }
}
