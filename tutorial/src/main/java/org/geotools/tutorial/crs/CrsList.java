package org.geotools.tutorial.crs;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.Set;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 11:06
 * modified by: $
 * modified on: $
 */
public class CrsList {

    public static void main(String ... args) throws FactoryException {
        String authority = "EPSG";

        CRSAuthorityFactory fac =
                ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null);

        Set<String> codes = fac.getAuthorityCodes(CoordinateReferenceSystem.class);

        for (String code : codes) {
            code = code.trim();
            String desc = fac.getDescriptionText(authority + ":" + code).toString();
            System.out.format("%S: %s\n", code, desc);
        }
    }
}
