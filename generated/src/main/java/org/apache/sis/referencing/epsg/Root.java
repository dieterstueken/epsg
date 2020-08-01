package org.apache.sis.referencing.epsg;

import org.apache.sis.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.util.NameSpace;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  27.02.14 13:03
 * modified by: $Author$
 * modified on: $Date$
 */
public class Root {

    public final Citation authority;

    public Root(Citation authority) {
        this.authority = authority;
    }

    public Root() {
        this(Citations.EPSG);
    }

    protected NameSpace scope(int key, String scope) {
        return null;
    }

    protected NameSpace scope(int key, String scope, String remarks) {
        return null;
    }
}
