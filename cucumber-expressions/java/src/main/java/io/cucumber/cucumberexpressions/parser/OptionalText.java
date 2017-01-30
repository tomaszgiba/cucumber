package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.List;

public class OptionalText implements Expression {
    private final Text text;

    public OptionalText(Text text) {
        this.text = text;
    }

    @Override
    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        Fragment fragment = text.compile(types, transformLookup);
        // TODO: Escape text here?
        return new Fragment("(?:" + fragment.getText() + ")?", fragment.getTransforms());
    }
}
