package io.cucumber.cucumberexpressions;

import io.cucumber.cucumberexpressions.parser.Fragment;
import io.cucumber.cucumberexpressions.parser.Lexer;
import io.cucumber.cucumberexpressions.parser.Parser;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * This test verifies that the regular expression generated
 * from the cucumber expression is as expected.
 */
public class CucumberExpressionPatternTest {
    @Test
    public void translates_no_args() {
        assertPattern(
                "^hello$", "hello",
                Collections.emptyList()
        );
    }

    @Test
    public void translates_an_int_arg() {
        assertPattern(
                "^I have ((?:-?\\d+)|(?:\\d+)) cukes$", "I have {n} cukes",
                Collections.singletonList(int.class)
        );
    }

    @Test
    public void translates_an_integer_arg() {
        assertPattern(
                "^I have ((?:-?\\d+)|(?:\\d+)) cukes$", "I have {n} cukes",
                Collections.singletonList(Integer.class)
        );
    }

    @Test
    public void translates_expression_types() {
        assertPattern(
                "^I have ((?:-?\\d+)|(?:\\d+)) cukes in my (.+)$", "I have {n:int} cukes in my {bodyPart}",
                Collections.emptyList()
        );
    }

    @Test
    public void translates_expression_types_with_explicit_types() {
        List<Type> types = new ArrayList<>();
        types.add(Integer.class);
        types.add(String.class);
        assertPattern(
                "^I have ((?:-?\\d+)|(?:\\d+)) cukes in my (.+)$", "I have {n:int} cukes in my {bodyPart}",
                types
        );
    }

    @Test
    public void translates_parenthesis_to_non_capturing_optional_capture_group() {
        assertPattern(
                "^I have many big(?:ish)? cukes$", "I have many big(ish) cukes",
                Collections.emptyList()
        );
    }

    @Test
    public void preserves_escaped_parenthesis() {
        assertPattern(
                "^I have many big\\(ish\\) cukes$", "I have many big\\(ish\\) cukes",
                Collections.emptyList()
        );
    }

    @Test
    public void preserves_escaped_parenthesis_before_and_after_args() {
        assertPattern(
                "^a (?:b)? \\(c\\) (.+) (?:e)? \\(f\\) g$", "a (b) \\(c\\) {d} (e) \\(f\\) g",
                Collections.emptyList()
        );
    }

    private void assertPattern(String expectedRegexp, String expr, List<Type> types) {
        TransformLookup transformLookup = new TransformLookup(Locale.ENGLISH);
        Fragment regexp = new Parser(new Lexer(expr)).parse().compile(new ArrayList<>(types), transformLookup);
        assertEquals(expectedRegexp, regexp.getText());
    }
}
