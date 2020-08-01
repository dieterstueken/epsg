package org.apache.sis.referencing.generate.factory.xepsg;

import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.datum.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  31.10.2014 12:57
 * modified by: $Author$
 * modified on: $Date$
 */
public interface DatumFactory extends EpsgFactory, DatumAuthorityFactory {
    
    <D extends Datum> D createDatum(String code, Class<D> type);

    @Override
    default Datum createDatum(String code) throws NoSuchAuthorityCodeException {
        return createDatum(code, Datum.class);
    }

    @Override
    default EngineeringDatum createEngineeringDatum(String code) throws NoSuchAuthorityCodeException {
        return createDatum(code, EngineeringDatum.class);
    }

    @Override
    default ImageDatum createImageDatum(String code) throws NoSuchAuthorityCodeException {
        return createDatum(code, ImageDatum.class);
    }

    @Override
    default VerticalDatum createVerticalDatum(String code) throws NoSuchAuthorityCodeException{
        return createDatum(code, VerticalDatum.class);
    }

    @Override
    default TemporalDatum createTemporalDatum(String code) throws NoSuchAuthorityCodeException {
        return createDatum(code, TemporalDatum.class);
    }

    @Override
    default GeodeticDatum createGeodeticDatum(String code) throws NoSuchAuthorityCodeException {
        return createDatum(code, GeodeticDatum.class);
    }
}
