package org.apache.sis.referencing.generate;

import org.apache.sis.referencing.factory.GeodeticAuthorityFactory;
import org.apache.sis.referencing.factory.sql.EPSGFactory;
import org.opengis.util.FactoryException;

/**
 * Created by IntelliJ IDEA.
 * User: stueken
 * Date: 27.10.18
 * Time: 00:13
 */
public class AuthorityFactoryFinder {


    public static GeodeticAuthorityFactory getAuthorityFactory() throws FactoryException {
        //Hints.putSystemDefault(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        
        return new EPSGFactory(null);
    }
}
