SELECT count(alias."OBJECT_TABLE_NAME"), alias."OBJECT_TABLE_NAME"
FROM EPSG."Alias" alias
group by alias."OBJECT_TABLE_NAME"