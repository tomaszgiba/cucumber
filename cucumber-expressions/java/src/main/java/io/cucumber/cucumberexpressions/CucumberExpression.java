package io.cucumber.cucumberexpressions;

import io.cucumber.cucumberexpressions.parser.Cukexp;
import io.cucumber.cucumberexpressions.parser.Fragment;
import io.cucumber.cucumberexpressions.parser.Lexer;
import io.cucumber.cucumberexpressions.parser.Parser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CucumberExpression implements Expression {
    private final Pattern pattern;
    private final List<Transform<?>> transforms;
    private final String expression;

    public CucumberExpression(final String expression, final List<? extends Type> types, final TransformLookup transformLookup) {
        Parser parser = new Parser(new Lexer(expression));
        Cukexp cukexp = parser.parse();
        Fragment fragment = cukexp.compile(new ArrayList<Type>(types), transformLookup);
        this.pattern = Pattern.compile(fragment.getText());
        this.transforms = fragment.getTransforms();
        this.expression = expression;
    }

    @Override
    public List<Argument> match(String text) {
        return ArgumentMatcher.matchArguments(pattern, text, transforms);
    }

    @Override
    public String getSource() {
        return expression;
    }

    Pattern getPattern() {
        return pattern;
    }
}
