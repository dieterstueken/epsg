package org.apache.sis.referencing.generate.factory.xepsg;

import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.cs.*;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  28.10.2014 15:45
 * modified by: $Author$
 * modified on: $Date$
 */
public interface CSFactory extends EpsgFactory, CSAuthorityFactory {
    
    <S extends CoordinateSystem> S createCS(String code, Class<S> type);
   
    @Override
    default CoordinateSystem createCoordinateSystem(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, CoordinateSystem.class);
    }

    @Override
    default CartesianCS createCartesianCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, CartesianCS.class);
    }

    @Override
    default PolarCS createPolarCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, PolarCS.class);
    }

    @Override
    default CylindricalCS createCylindricalCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, CylindricalCS.class);
    }

    @Override
    default SphericalCS createSphericalCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, SphericalCS.class);
    }

    @Override
    default EllipsoidalCS createEllipsoidalCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, EllipsoidalCS.class);
    }

    @Override
    default VerticalCS createVerticalCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, VerticalCS.class);
    }

    @Override
    default TimeCS createTimeCS(String code) throws NoSuchAuthorityCodeException {
        return createCS(code, TimeCS.class);
    }
}
