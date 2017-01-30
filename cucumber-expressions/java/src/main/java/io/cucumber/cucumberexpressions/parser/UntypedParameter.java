package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.Transform;
import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.List;

import static io.cucumber.cucumberexpressions.parser.Expression.shiftOrNull;
import static java.util.Collections.singletonList;

public class UntypedParameter implements Expression {
    final String name;

    public UntypedParameter(String name) {
        this.name = name;
    }

    @Override
    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        Transform<?> transform = transformLookup.lookupTransform(shiftOrNull(types), name, null);
        String text = Transform.getCaptureGroupRegexp(transform.getCaptureGroupRegexps());
        return new Fragment(text, singletonList(transform));
    }
}
