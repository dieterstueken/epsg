package org.geotools.referencing.factory.epsg.direct.generator.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * version:     $
 * created by:  d.stueken
 * created on:  25.07.2020 16:39
 * modified by: $
 * modified on: $
 */
public interface Processor {

    void process(ResultSet rs) throws SQLException;
}
