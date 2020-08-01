package org.apache.sis.referencing.generate.factory.xepsg;

import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  29.10.2014 16:26
 * modified by: $Author$
 * modified on: $Date$
 */
public interface CRSFactory extends EpsgFactory, CRSAuthorityFactory {

    <S extends CoordinateReferenceSystem> S createCRS(String code, Class<S> type);
    
    @Override
    default CoordinateReferenceSystem createCoordinateReferenceSystem(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, CoordinateReferenceSystem.class);
    }

    @Override
    default CompoundCRS createCompoundCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, CompoundCRS.class);
    }

    @Override
    default DerivedCRS createDerivedCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, DerivedCRS.class);
    }

    @Override
    default EngineeringCRS createEngineeringCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, EngineeringCRS.class);
    }

    @Override
    default GeographicCRS createGeographicCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, GeographicCRS.class);
    }

    @Override
    default GeocentricCRS createGeocentricCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, GeocentricCRS.class);
    }

    @Override
    default ImageCRS createImageCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, ImageCRS.class);
    }

    @Override
    default ProjectedCRS createProjectedCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, ProjectedCRS.class);
    }

    @Override
    default TemporalCRS createTemporalCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, TemporalCRS.class);
    }

    @Override
    default VerticalCRS createVerticalCRS(String code) throws NoSuchAuthorityCodeException {
        return createCRS(code, VerticalCRS.class);
    }
}
