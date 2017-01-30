package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.Transform;
import io.cucumber.cucumberexpressions.TransformLookup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Text implements Expression {
    private final List<Expression> expressions;

    public Text(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public Fragment compile(List<? extends Type> types, TransformLookup transformLookup) {
        StringBuilder regexp = new StringBuilder();
        List<Transform<?>> transforms = new ArrayList<>();
        for (Expression expression : expressions) {
            Fragment fragment = expression.compile(types, transformLookup);
            regexp.append(fragment.getText());
            transforms.addAll(fragment.getTransforms());
        }
        return new Fragment(regexp.toString(), transforms);
    }
}
