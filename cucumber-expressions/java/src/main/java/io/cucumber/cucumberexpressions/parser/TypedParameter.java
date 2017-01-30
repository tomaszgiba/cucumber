package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.Transform;
import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.List;

import static io.cucumber.cucumberexpressions.parser.Expression.shiftOrNull;
import static java.util.Collections.singletonList;

public class TypedParameter implements Expression {
    final String name;
    final String typeName;

    public TypedParameter(String name, String typeName) {
        this.name = name;
        this.typeName = typeName;
    }

    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        Transform<?> transform = transformLookup.lookupTransform(shiftOrNull(types), name, typeName);
        String text = Transform.getCaptureGroupRegexp(transform.getCaptureGroupRegexps());
        // TODO: Store the transform in the Fragment
        return new Fragment(text, singletonList(transform));
    }
}
