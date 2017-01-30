package io.cucumber.cucumberexpressions;

import java.lang.reflect.Type;
import java.util.List;

public interface Transform<T> {

    static String getCaptureGroupRegexp(List<String> captureGroupRegexps) {
        StringBuilder sb = new StringBuilder("(");

        if(captureGroupRegexps.size() == 1) {
            sb.append(captureGroupRegexps.get(0));
        } else {
            boolean bar = false;
            for (String captureGroupRegexp : captureGroupRegexps) {
                if(bar) sb.append("|");
                sb.append("(?:").append(captureGroupRegexp).append(")");
                bar = true;
            }
        }

        sb.append(")");
        return sb.toString();
    }

    /**
     * This is used in the type name in typed expressions
     * @return human readable type name
     */
    String getTypeName();

    Type getType();

    List<String> getCaptureGroupRegexps();

    T transform(String value);
}
