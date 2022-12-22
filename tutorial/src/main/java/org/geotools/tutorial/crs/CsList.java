package org.geotools.tutorial.crs;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.CSAuthorityFactory;
import org.opengis.referencing.cs.CoordinateSystem;

import java.util.Set;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 11:06
 * modified by: $
 * modified on: $
 */
public class CsList {

    public static void main(String ... args) throws FactoryException {
        String authority = "EPSG";

        CSAuthorityFactory fac =
                ReferencingFactoryFinder.getCSAuthorityFactory(authority, null);

        Set<String> codes = fac.getAuthorityCodes(CoordinateSystem.class);

        for (String code : codes) {
            code = code.trim();
            String desc = fac.getDescriptionText(authority + ":" + code).toString();
            System.out.format("%S: %s\n", code, desc);
        }

        System.out.format("%d\n", codes.size());
    }
}
