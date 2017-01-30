package io.cucumber.cucumberexpressions.parser;

import io.cucumber.cucumberexpressions.Transform;

import java.util.List;

public class Fragment {
    private final String text;
    private final List<Transform<?>> transforms;

    public Fragment(String text, List<Transform<?>> transforms) {
        this.text = text;
        this.transforms = transforms;
    }

    public String getText() {
        return text;
    }

    public List<Transform<?>> getTransforms() {
        return transforms;
    }
}
