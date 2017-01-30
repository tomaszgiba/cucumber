package io.cucumber.cucumberexpressions.parser;

import static io.cucumber.cucumberexpressions.parser.Lexer.TokenType.*;

public class Lexer {
    private final String input;
    private final int end;
    int pos = 0;

    public Lexer(String input) {
        this.input = input;
        this.end = input.length();
    }

    public Token nextToken() {
        StringBuilder value = new StringBuilder();
        TokenType type = null;
        while (true) {
            if (pos == end) {
                break;
            }
            boolean escapeNext = pos > 0 && input.charAt(pos-1) == '\\';
            char c = input.charAt(pos);
            if (c == '(' && !escapeNext) {
                if (type != null)
                    break;
                type = LPAREN;
            } else if (c == ')' && !escapeNext) {
                if (type != null)
                    break;
                type = RPAREN;
            } else if (c == '{' && !escapeNext) {
                if (type != null)
                    break;
                type = LCURLY;
            } else if (c == '}' && !escapeNext) {
                if (type != null)
                    break;
                type = RCURLY;
            } else if (c == ':' && !escapeNext) {
                if (type != null)
                    break;
                type = COLON;
            } else if (Character.isWhitespace(c)) {
                if (type != null && type != WHITESPACE)
                    break;
                type = WHITESPACE;
            } else {
                if (type != null && type != WORD)
                    break;
                type = WORD;
            }
            value.append(c);
            pos++;
        }
        if (type == null) type = EOF;
        return new Token(type, value.toString());
    }

    public void rewind(Token token) {
        pos -= token.value.length();
    }

    enum TokenType {WORD, WHITESPACE, LPAREN, RPAREN, LCURLY, RCURLY, COLON, EOF}

    public class Token {
        final TokenType type;
        final String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }
}
