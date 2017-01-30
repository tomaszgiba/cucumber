package io.cucumber.cucumberexpressions;

import io.cucumber.cucumberexpressions.parser.Cukexp;
import io.cucumber.cucumberexpressions.parser.Fragment;
import io.cucumber.cucumberexpressions.parser.Lexer;
import io.cucumber.cucumberexpressions.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class CucumberExpressionGeneratorRoundtripTest {
    private final String text;

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws IOException {
        Collection<Object[]> data = new ArrayList<>();

        for (String text : asList(
                "I have 42 cukes",
                "hello",
                "[]",
                "(::)",
                "^ $ * + ? . ( ) | {x:y} [ ]"
        )) {
            data.add(new String[]{text});
        }
        return data;
    }

    public CucumberExpressionGeneratorRoundtripTest(String text) {
        this.text = text;
    }

    @Test
    public void roundtrip() {
        TransformLookup transformLookup = new TransformLookup(Locale.ENGLISH);
        CucumberExpressionGenerator generator = new CucumberExpressionGenerator(transformLookup);
        GeneratedExpression generatedExpression = generator.generateExpression(text, true);
        List<Transform<?>> transforms = generatedExpression.getTransforms();
        List<Type> types = transforms.stream().map(Transform::getType).collect(Collectors.toList());

        CucumberExpression expression = new CucumberExpression(generatedExpression.getSource(), types, transformLookup);

        List<Argument> arguments = expression.match(text);
        if (arguments == null) {
            throw new Error(String.format("Roundtrip failed: text: '%s', generated cucumber expression: '%s', regexp: '%s'", text, generatedExpression.getSource(), expression.getPattern()));
        }
    }

}
