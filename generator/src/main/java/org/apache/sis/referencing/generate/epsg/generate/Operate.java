package org.apache.sis.referencing.generate.epsg.generate;

import org.apache.sis.geometry.DirectPosition2D;
import org.apache.sis.referencing.CRS;
import org.apache.sis.referencing.factory.GeodeticAuthorityFactory;
import org.apache.sis.referencing.generate.AuthorityFactoryFinder;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  06.03.14 17:00
 * modified by: $Author$
 * modified on: $Date$
 */
public class Operate {

    static void println(Object object) {

        if(object==null)
            System.out.println("null");
        else
            try {
                System.out.println(object.toString());
            } catch(Exception e) {
                e.printStackTrace();
            }
    }

    public static void main(String[] args) throws Exception {

        GeodeticAuthorityFactory factory = AuthorityFactoryFinder.getAuthorityFactory();

        ParameterDescriptor<?> p =  factory.createParameterDescriptor("EPSG:8659");
        println(p);

        IdentifiedObject object = factory.createObject("EPSG:9620");
        println(object);

        object = factory.createCoordinateOperation("EPSG:16263");
        println(object);

        object = factory.createObject("EPSG:1450");
        println(object);

        //final OperationMethod method = factory.getOperationMethod("EPSG:9664");
        //System.out.println(method.toString());

        final CoordinateReferenceSystem inCRS = CRS.forCode("EPSG:31467");
        println(inCRS);

        final CoordinateReferenceSystem outCRS = CRS.forCode("EPSG:4326");
        println(outCRS);

        final MathTransform trs = CRS.findOperation(inCRS, outCRS, null).getMathTransform();
        println(trs);

        final DirectPosition d1 = new DirectPosition2D(inCRS, 5763343.0, 3400000.0);
        println("FROM EPSG:31467 = " + d1);

        final DirectPosition d2 = trs.transform(d1, null);
        println("TO EPSG:4326 = " + d2);

    }
}
