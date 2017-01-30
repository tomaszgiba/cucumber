package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.List;

public interface Expression {
    Fragment compile(List<? extends Type> types, TransformLookup transformLookup);

    static <T> T shiftOrNull(List<T> list) {
        return list.isEmpty() ? null : list.remove(0);
    }
}
