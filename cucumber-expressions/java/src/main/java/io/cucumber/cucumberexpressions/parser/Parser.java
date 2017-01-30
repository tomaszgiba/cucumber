package io.cucumber.cucumberexpressions.parser;

import java.util.ArrayList;
import java.util.List;

import static io.cucumber.cucumberexpressions.parser.Lexer.TokenType.EOF;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Cukexp parse() {
        List<Expression> expressions = new ArrayList<>();
        while (true) {
            Lexer.Token token = lexer.nextToken();
            lexer.rewind(token);
            if (token.type == EOF)
                break;

            Expression expression = expression();
            expressions.add(expression);
        }
        return new Cukexp(expressions);
    }

    Expression expression() {
        Lexer.Token token = lexer.nextToken();
        lexer.rewind(token);
        switch (token.type) {
            case WHITESPACE:
                return whitespace();
            case WORD:
                return word();
            case LCURLY:
                return parameter();
            case LPAREN:
                return optionalText();
            default:
                throw new Error();
        }
    }

    Expression optionalText() {
        Lexer.Token lparen = lexer.nextToken();
        Text text = text();
        Lexer.Token rparen = lexer.nextToken();
        return new OptionalText(text);
    }

    Text text() {
        List<Expression> expressions = new ArrayList<>();
        loop:
        while (true) {
            Lexer.Token token = lexer.nextToken();
            lexer.rewind(token);
            switch (token.type) {
                case WHITESPACE:
                    expressions.add(whitespace());
                    break;
                case WORD:
                    expressions.add(word());
                    break;
                default:
                    break loop;
            }
        }
        return new Text(expressions);
    }

    Expression parameter() {
        Lexer.Token lcurly = lexer.nextToken();
        Lexer.Token name = lexer.nextToken();
        Lexer.Token token = lexer.nextToken();
        switch (token.type) {
            case COLON:
                Lexer.Token typeName = lexer.nextToken();
                Lexer.Token rcurly = lexer.nextToken();
                return new TypedParameter(name.value, typeName.value);
            case RCURLY:
                return new UntypedParameter(name.value);
            default:
                throw new RuntimeException(String.format("Unexpected token [%s] at column %n", token.value, lexer.pos - token.value.length()));
        }
    }

    Word word() {
        Lexer.Token token = lexer.nextToken();
        return new Word(token.value);
    }

    UntypedParameter untypedParameter() {
        Lexer.Token lcurly = lexer.nextToken();
        Lexer.Token name = lexer.nextToken();
        Lexer.Token rcurly = lexer.nextToken();
        return new UntypedParameter(name.value);
    }

    TypedParameter typedParameter() {
        Lexer.Token lcurly = lexer.nextToken();
        Lexer.Token name = lexer.nextToken();
        Lexer.Token colon = lexer.nextToken();
        Lexer.Token typeName = lexer.nextToken();
        Lexer.Token rcurly = lexer.nextToken();
        return new TypedParameter(name.value, typeName.value);
    }

    Whitespace whitespace() {
        Lexer.Token whitespace = lexer.nextToken();
        return new Whitespace(whitespace.value);
    }
}
