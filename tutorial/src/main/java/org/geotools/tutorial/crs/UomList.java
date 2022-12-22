package org.geotools.tutorial.crs;

import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.DatumAuthorityFactory;
import org.opengis.referencing.datum.Ellipsoid;

import java.util.Set;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 11:06
 * modified by: $
 * modified on: $
 */
public class UomList {

    public static void main(String ... args) throws FactoryException {
        String authority = "EPSG";

        DatumAuthorityFactory fac = ReferencingFactoryFinder.getDatumAuthorityFactory(authority,null);

        Set<String> codes = fac.getAuthorityCodes(Ellipsoid.class);

        for (String code : codes) {
            code = code.trim();

            Ellipsoid ellp = fac.createEllipsoid(code);

            String desc = fac.getDescriptionText(authority + ":" + code).toString();
            System.out.format("%S: %s\n", code, desc);
        }
    }
}
