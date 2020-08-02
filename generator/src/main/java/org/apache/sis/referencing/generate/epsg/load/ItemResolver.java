package org.apache.sis.referencing.generate.epsg.load;

/**
* version:     $Revision$
* created by:  dst
* created on:  28.02.14 19:43
* modified by: $Author$
* modified on: $Date$
*/
public interface ItemResolver {

    OutputAppender alias(Alias alias, OutputAppender out);
    OutputAppender area(Area area, OutputAppender out);
    OutputAppender unit(Integer key, OutputAppender out);

    OutputAppender operation(Integer key, OutputAppender out);
    OutputAppender method(Integer key, OutputAppender out);

    OutputAppender crs(Integer key, OutputAppender out);
    OutputAppender cs(Integer key, OutputAppender out);
    OutputAppender datum(Integer key, OutputAppender out);

    OutputAppender ellipsoid(Integer key, OutputAppender out);
    OutputAppender meridian(Integer key, OutputAppender out);
}
