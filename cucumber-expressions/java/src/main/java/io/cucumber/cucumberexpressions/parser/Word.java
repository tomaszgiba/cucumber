package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class Word implements Expression {
    final String word;

    public Word(String word) {
        this.word = word;
    }

    @Override
    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        return new Fragment(word.replaceAll("([\\^$*+?.])", "\\\\$1"), Collections.emptyList());
    }
}
