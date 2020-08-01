package org.apache.sis.referencing.generate.epsg;

import org.apache.sis.measure.Units;
import org.apache.sis.referencing.generate.epsg2.CodeTable;
import org.apache.sis.referencing.generate.factory.epsg.ThreadedEpsgFactory;
import org.junit.Test;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.Datum;
import org.opengis.util.FactoryException;

import javax.measure.converter.UnitConverter;
import javax.measure.quantity.Angle;
import javax.measure.unit.NonSI;
import javax.measure.unit.Unit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;

public class TablesTest {

    @Test
    public void testTables() throws FactoryException, IOException {

        //CoordinateReferenceSystem crs = CRS.decode("EPSG:27582");
        //System.out.println(crs);

        /*
        Unit _dms = Units.valueOfEPSG(9108);
        _dms.toString();

        Unit<Angle> d = NonSI.DEGREE_ANGLE;
        Unit<Angle> m = NonSI.MINUTE_ANGLE;
        Unit<Angle> s = NonSI.SECOND_ANGLE;

        Unit<Angle> dms = d.compound(m).compound(s);

        double result = d.getConverterTo(dms).convert(52.5);
        */

        // degree
        Unit<?> u9102 = Units.valueOfEPSG(9102);
        String s9102 = u9102.toString();
        System.out.println(s9102);

        // DMS_SCALED
        Unit<Angle> u9108 = (Unit<Angle>) Units.valueOfEPSG(9108);
        String s9108 = u9108.toString();
        System.out.println(s9108);

        UnitConverter converter = u9108.getConverterTo(NonSI.DEGREE_ANGLE);
        double result = converter.convert(523036.00);
        System.out.println(result);

        //final CRSAuthorityFactory crsfactory = CRS.getAuthorityFactory(null);
        final ThreadedEpsgFactory crsfactory = new ThreadedEpsgFactory();

        Datum datum = crsfactory.createDatum("6314");
        String wkt = datum.toWKT();

        CharSequence description = crsfactory.getDescriptionText("31467");
        System.out.format("31467: %s\n", description);

        final Citation vendor = crsfactory.getVendor();
        final Citation authority = crsfactory.getAuthority();

        Class<?> unitType = Unit.class;
        final Set<String> units = crsfactory.getAuthorityCodes((Class<? extends IdentifiedObject>) unitType);

        final Set<String> codes = crsfactory.getAuthorityCodes(CoordinateReferenceSystem.class);

        //final Set<String> codes = CRS.getSupportedCodes("EPSG");

        System.out.println(codes.size());

        CodeTable<CoordinateReferenceSystem> crsTable = new CodeTable<>(CoordinateReferenceSystem.class);

        for (String code : codes) {
            if(code.length()>5)
                continue;

            try {
                CoordinateReferenceSystem crs = crsfactory.createCoordinateReferenceSystem(code);
                System.out.format("%s %s\n", code, crs.getName());
                crsTable.put(code, crs);
            } catch(Exception e) {
                System.out.format("%s %s\n", code, e.toString());
            }
        }

        System.out.format("%d codes\n", crsTable.size());

        try(FileOutputStream fos = new FileOutputStream("C:\\Temp\\crsTable.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(crsTable);
        }
    }
}