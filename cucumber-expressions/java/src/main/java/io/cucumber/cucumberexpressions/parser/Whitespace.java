package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Whitespace implements Expression {
    public final String value;

    public Whitespace(String value) {
        this.value = value;
    }

    @Override
    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        return new Fragment(value, Collections.emptyList());
    }
}
