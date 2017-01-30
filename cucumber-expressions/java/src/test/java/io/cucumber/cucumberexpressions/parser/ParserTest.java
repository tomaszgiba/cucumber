package io.cucumber.cucumberexpressions.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParserTest {
    @Test
    public void parses_word() {
        Parser parser = new Parser(new Lexer("hello"));
        Word word = parser.word();
        assertEquals("hello", word.word);
    }

    @Test
    public void parses_word_as_expression() {
        Parser parser = new Parser(new Lexer("hello"));
        Word word = (Word) parser.expression();
        assertEquals("hello", word.word);
    }

    @Test
    public void parses_word_followed_by_space() {
        Parser parser = new Parser(new Lexer("hello world"));
        Word word = parser.word();
        assertEquals("hello", word.word);
    }

    @Test
    public void parses_whitespace() {
        Parser parser = new Parser(new Lexer("  \t  "));
        Whitespace whitespace = parser.whitespace();
        assertEquals("  \t  ", whitespace.value);
    }

    @Test
    public void parses_untyped_parameter() {
        Parser parser = new Parser(new Lexer("{widgets}"));
        UntypedParameter untypedParameter = parser.untypedParameter();
        assertEquals("widgets", untypedParameter.name);
    }

    @Test
    public void parses_untyped_parameter_as_parameter() {
        Parser parser = new Parser(new Lexer("{widgets}"));
        UntypedParameter untypedParameter = (UntypedParameter) parser.parameter();
        assertEquals("widgets", untypedParameter.name);
    }

    @Test
    public void parses_typed_parameter() {
        Parser parser = new Parser(new Lexer("{widgets:int}"));
        TypedParameter typedParameter = parser.typedParameter();
        assertEquals("widgets", typedParameter.name);
        assertEquals("int", typedParameter.typeName);
    }

    @Test
    public void parses_something_complicated_as_cukexp() {
        Parser parser = new Parser(new Lexer("I have {widgets:int} widgets and an {animal} uff(da)"));
        Expression expression = parser.parse();
    }
}
