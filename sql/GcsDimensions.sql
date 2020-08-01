SELECT count(axs."COORD_SYS_CODE"), cs."COORD_SYS_CODE", cs."COORD_SYS_TYPE", cs."COORD_SYS_NAME"
FROM EPSG."Coordinate Axis" axs, EPSG."Coordinate System" cs
WHERE axs."COORD_SYS_CODE"=cs."COORD_SYS_CODE"
group by cs."COORD_SYS_CODE", cs."COORD_SYS_TYPE", cs."COORD_SYS_NAME"