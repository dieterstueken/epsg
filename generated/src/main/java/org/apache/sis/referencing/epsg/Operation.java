package org.apache.sis.referencing.epsg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * version:     $Revision$
 * created by:  dst
 * created on:  18.02.14 16:26
 * modified by: $Author$
 * modified on: $Date$
 */
public class Operation extends Bound {

    public static class Value {
        final Parameter param;
        final UoM unit;
        final Double value;

        private Value(Parameter param, UoM unit, Double value) {
            this.param = param;
            this.unit = unit;
            this.value = value;
        }
    }

    final Method method;

    final List<Value> values;

    public Operation(Integer key, Name name, Area area, Method method, List<Value> values) {
        super(key, name, area);

        this.method = method;
        this.values = values;
    }

    public static Operation create(Integer key, Name name, Area area, Method method, List<Value> values) {
        return new Operation(key, name, area, method, values);
    }

    public static Operation transformation(Integer key, Name name, Area area, Method method,
                                           CoordRefSys src, CoordRefSys tgt, Value ... values) {
        return create(key, name, area, method, values(values));
    }

    public static Operation conversion(Integer key, Name name, Area area, Method method, Value ... values) {
        return create(key, name, area, method, values(values));
    }

    public static Operation concatenation(Integer key, Name name, Area area,
                                          CoordRefSys src, CoordRefSys tgt, Operation ... steps) {
        return create(key, name, area, null, values());
    }

    public static Operation operation(Integer key, Name name, Area area, Method method, Value value) {
        return create(key, name, area, method, values(value));
    }

    public static Value value(Parameter param, UoM unit, Double value) {
        return new Value(param, unit, value);
    }

    public static Value value(Parameter param, Double value) {
        return new Value(param, null, value);
    }

    public static List<Value> values() {
        return Collections.emptyList();
    }

    public static List<Value> values(Value value) {
        return Collections.singletonList(value);
    }

    public static List<Value> values(Value ... values) {
        return Collections.unmodifiableList(Arrays.asList(values));
    }

    public static Value file(Parameter param, String name) {
        return null;
    }
}
