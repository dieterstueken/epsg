SELECT count(datum."DATUM_TYPE"), datum."DATUM_TYPE"
FROM EPSG."Datum" datum
group by datum."DATUM_TYPE"