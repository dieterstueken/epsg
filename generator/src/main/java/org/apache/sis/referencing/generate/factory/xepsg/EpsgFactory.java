package org.apache.sis.referencing.generate.factory.xepsg;

import org.apache.sis.internal.simple.SimpleCitation;
import org.apache.sis.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.util.FactoryException;
import org.opengis.util.InternationalString;

import java.util.Set;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  31.10.2014 13:11
 * modified by: $Author$
 * modified on: $Date$
 */
public interface EpsgFactory extends AuthorityFactory {

    Citation VENDOR = new SimpleCitation("Serialized EPSG");

    @Override
    default Citation getVendor() {
        return VENDOR;
    }

    @Override
    default Citation getAuthority() {
        return Citations.EPSG;
    }


    @Override
    Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> aClass) throws FactoryException;

    @Override
    default InternationalString getDescriptionText(String code) throws NoSuchAuthorityCodeException {
        return createObject(code).getRemarks();
    }

    @Override
    IdentifiedObject createObject(String s) throws NoSuchAuthorityCodeException;

    default NoSuchAuthorityCodeException error(String code, Class<?> type) {

        final InternationalString authority = getAuthority().getTitle();

        final String message = String.format("No code %s from authority %s found for object of type %s",
                code, getAuthority().getTitle(), type);

        return new NoSuchAuthorityCodeException(message, authority.toString(), code, code);
    }

}
