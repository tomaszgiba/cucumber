package io.cucumber.cucumberexpressions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CucumberExpression implements Expression {
    private static final Pattern OPTIONAL_PATTERN = Pattern.compile("\\(([^\\)]+)\\)");
    private static final Pattern ALTERNATIVE_PATTERN = Pattern.compile("([\\w]+)((\\/[\\w]+)+)");
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\{([^\\}:]+)(:([^\\}]+))?\\}");

    private final Pattern pattern;
    private final List<Transform<?>> transforms = new ArrayList<>();
    private final String expression;

    public CucumberExpression(final String expression, List<? extends Type> types, TransformLookup transformLookup) {
        this.expression = expression;

        String regexp = replaceOptionals(expression);
        regexp = replaceAlternateWords(regexp);
        regexp = replaceParameters(types, transformLookup, regexp);

        pattern = Pattern.compile("^" + regexp + "$");
    }

    private String replaceOptionals(String expression) {
        return OPTIONAL_PATTERN.matcher(expression).replaceAll("(?:$1)?");
    }

    private String replaceAlternateWords(String expr) {
        StringBuffer result = new StringBuffer();
        Matcher alternativeWordMatcher = ALTERNATIVE_PATTERN.matcher(expr);
        while (alternativeWordMatcher.find()) {
            String alternation = "(?:" +
                    alternativeWordMatcher.group(1) +
                    alternativeWordMatcher.group(2).replace('/', '|') +
                    ")";
            alternativeWordMatcher.appendReplacement(result, Matcher.quoteReplacement(alternation));
        }
        alternativeWordMatcher.appendTail(result);
        return result.toString();
    }

    private String replaceParameters(List<? extends Type> types, TransformLookup transformLookup, String expr) {
        StringBuffer result = new StringBuffer();

        Matcher parameterMatcher = PARAMETER_PATTERN.matcher(expr);
        int typeIndex = 0;
        while (parameterMatcher.find()) {
            Type type = types.size() <= typeIndex ? null : types.get(typeIndex++);
            String parameterName = parameterMatcher.group(1);
            String typeName = parameterMatcher.group(3);

            Transform<?> transform = null;
            if (type != null) {
                transform = transformLookup.lookupByType(type);
            }
            if (transform == null && typeName != null) {
                transform = transformLookup.lookupByTypeName(typeName, false);
            }
            if (transform == null) {
                transform = transformLookup.lookupByTypeName(parameterName, true);
            }
            if (transform == null && type != null && type instanceof Class) {
                transform = new ClassTransform<>((Class) type);
            }
            if (transform == null) {
                transform = new ConstructorTransform<>(String.class);
            }
            transforms.add(transform);

            parameterMatcher.appendReplacement(result, Matcher.quoteReplacement("(" + transform.getCaptureGroupRegexps().get(0) + ")"));
        }
        parameterMatcher.appendTail(result);
        return result.toString();
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
