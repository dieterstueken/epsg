SELECT count(datum."DATUM_CODE"), datum."DATUM_CODE", datum."DATUM_NAME"
FROM EPSG."Coordinate Reference System" pcs, EPSG."Coordinate Reference System" gcs, EPSG."Datum" datum
WHERE pcs."SOURCE_GEOGCRS_CODE"=gcs."COORD_REF_SYS_CODE" AND gcs."DATUM_CODE"=datum."DATUM_CODE"
group by datum."DATUM_CODE", datum."DATUM_NAME"