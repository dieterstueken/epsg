SELECT count(pcs."COORD_REF_SYS_KIND"), pcs."COORD_REF_SYS_KIND"
FROM EPSG."Coordinate Reference System" pcs, EPSG."Coordinate_Operation" op
where pcs."PROJECTION_CONV_CODE"=op."COORD_OP_CODE"
group by pcs."COORD_REF_SYS_KIND"