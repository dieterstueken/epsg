package org.geotools.referencing.factory.epsg.direct.generator.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  23.07.2020 21:18
 * modified by: $
 * modified on: $
 */
public interface Result<R> {

    R get(ResultSet rs) throws SQLException;
}
