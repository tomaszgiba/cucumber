package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.TransformLookup;
import org.junit.Test;

import java.util.Collections;
import java.util.Locale;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ExpressionTest {
    private final TransformLookup transformLookup = new TransformLookup(Locale.ENGLISH);

    @Test
    public void evaluates_word() {
        Expression expression = new Word("hello");
        Fragment fragment = expression.compile(Collections.emptyList(), transformLookup);
        assertEquals("hello", fragment.getText());
    }

    @Test
    public void evaluates_untyped_parameter() {
        Expression expression = new UntypedParameter("thing");
        Fragment fragment = expression.compile(Collections.emptyList(), transformLookup);
        assertEquals("(.+)", fragment.getText());
    }

    @Test
    public void evaluates_typed_parameter_without_type() {
        Expression expression = new TypedParameter("x", "int");
        Fragment fragment = expression.compile(Collections.emptyList(), transformLookup);
        assertEquals("((?:-?\\d+)|(?:\\d+))", fragment.getText());
    }

    @Test
    public void evaluates_optional_text() {
        Expression expression = new OptionalText(new Text(asList(new Word("maybe"))));
        Fragment fragment = expression.compile(Collections.emptyList(), transformLookup);
        assertEquals("(?:maybe)?", fragment.getText());
    }

    @Test
    public void evaluates_cukexp() {
        Expression expression = new Cukexp(asList(
                new Word("hello"),
                new TypedParameter("x", "int"),
                new UntypedParameter("thing"),
                new OptionalText(new Text(asList(new Word("maybe"))))

        ));
        Fragment fragment = expression.compile(Collections.emptyList(), transformLookup);
        assertEquals("^hello((?:-?\\d+)|(?:\\d+))(.+)(?:maybe)?$", fragment.getText());
    }
}
