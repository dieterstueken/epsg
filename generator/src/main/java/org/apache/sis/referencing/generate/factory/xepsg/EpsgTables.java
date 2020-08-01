package org.apache.sis.referencing.generate.factory.xepsg;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.OperationMethod;

import javax.measure.Unit;
import java.io.Serializable;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  03.11.2014 15:34
 * modified by: $Author$
 * modified on: $Date$
 */
public class EpsgTables implements Serializable {

    final EpsgTable<Unit> units = EpsgTable.of(Unit.class);

    final EpsgTable<Datum> datums = EpsgTable.of(Datum.class);
    final EpsgTable<Ellipsoid> ellipsoids = EpsgTable.of(Ellipsoid.class);
    final EpsgTable<PrimeMeridian> meridians = EpsgTable.of(PrimeMeridian.class);
    
    final EpsgTable<OperationMethod> methods = EpsgTable.of(OperationMethod.class);
    final EpsgTable<CoordinateOperation> operations = EpsgTable.of(CoordinateOperation.class);

    final EpsgTable<CoordinateSystem> systems = EpsgTable.of(CoordinateSystem.class);
    final EpsgTable<CoordinateSystemAxis> axes = EpsgTable.of(CoordinateSystemAxis.class);

    final EpsgTable<CoordinateReferenceSystem> reference_systems = EpsgTable.of(CoordinateReferenceSystem.class);
}
