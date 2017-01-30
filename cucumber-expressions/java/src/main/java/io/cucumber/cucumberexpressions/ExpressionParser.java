package io.cucumber.cucumberexpressions;

import java.lang.reflect.Type;
import java.util.List;

public interface ExpressionParser {
    Expression parse(String expression, List<? extends Type> types);
}
