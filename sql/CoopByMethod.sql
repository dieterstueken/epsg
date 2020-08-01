SELECT count(coop."COORD_OP_METHOD_CODE"), coop."COORD_OP_METHOD_CODE", meth."COORD_OP_METHOD_NAME"
FROM EPSG."Coordinate_Operation" coop, EPSG."Coordinate_Operation Method" meth
where coop."COORD_OP_TYPE"='conversion' AND coop."COORD_OP_METHOD_CODE"=meth."COORD_OP_METHOD_CODE"
group by coop."COORD_OP_METHOD_CODE", meth."COORD_OP_METHOD_NAME"