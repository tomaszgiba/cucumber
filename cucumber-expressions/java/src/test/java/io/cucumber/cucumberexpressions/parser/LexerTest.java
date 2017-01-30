package io.cucumber.cucumberexpressions.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexerTest {
    private Lexer lexer;

    private void assertToken(String value, Lexer.TokenType type) {
        Lexer.Token token = lexer.nextToken();
        assertEquals(value, token.value);
        assertEquals(type, token.type);
    }

    private void assertToken(Lexer.TokenType type) {
        Lexer.Token token = lexer.nextToken();
        assertEquals(type, token.type);
    }

    @Test
    public void lexes_word() {
        lexer = new Lexer("hello");
        assertToken("hello", Lexer.TokenType.WORD);
        assertToken(Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_whitespace() {
        lexer = new Lexer("   ");
        assertToken("   ", Lexer.TokenType.WHITESPACE);
        assertToken(Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_word_then_whitespace() {
        lexer = new Lexer("hello   ");
        assertToken("hello", Lexer.TokenType.WORD);
        assertToken("   ", Lexer.TokenType.WHITESPACE);
        assertToken(Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_whitespace_then_word() {
        lexer = new Lexer("   hello");
        assertToken("   ", Lexer.TokenType.WHITESPACE);
        assertToken("hello", Lexer.TokenType.WORD);
        assertToken(Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_colon() {
        lexer = new Lexer(":");
        assertToken(Lexer.TokenType.COLON);
        assertToken(Lexer.TokenType.EOF);
        assertToken(Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_special_chars() {
        lexer = new Lexer("(){}:");
        assertToken("(", Lexer.TokenType.LPAREN);
        assertToken(")", Lexer.TokenType.RPAREN);
        assertToken("{", Lexer.TokenType.LCURLY);
        assertToken("}", Lexer.TokenType.RCURLY);
        assertToken(":", Lexer.TokenType.COLON);
        assertToken("", Lexer.TokenType.EOF);
        assertToken("", Lexer.TokenType.EOF);
    }

    @Test
    public void lexes_escaped_special_characters() {
        lexer = new Lexer("ab\\({");
        assertToken("ab\\(", Lexer.TokenType.WORD);
        assertToken("{", Lexer.TokenType.LCURLY);
    }
}
