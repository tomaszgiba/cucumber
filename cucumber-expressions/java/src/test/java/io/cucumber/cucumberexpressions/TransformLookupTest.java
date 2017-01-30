package io.cucumber.cucumberexpressions;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TransformLookupTest {
    @Test
    public void looks_up_transform_by_type() {
        Transform<Integer> transform = (Transform<Integer>) new TransformLookup(Locale.ENGLISH).lookupTransform(Integer.class, null, null);
        assertEquals(new Integer(22), transform.transform("22"));
    }

    @Test
    public void looks_up_transform_by_type_name() {
        Transform<Integer> transform = (Transform<Integer>) new TransformLookup(Locale.ENGLISH).lookupTransform(null, null, "int");
        assertEquals(new Integer(22), transform.transform("22"));
    }

    @Test
    public void looks_up_transform_by_parameter_name() {
        Transform<Integer> transform = (Transform<Integer>) new TransformLookup(Locale.ENGLISH).lookupTransform(null, "int", null);
        assertEquals(new Integer(22), transform.transform("22"));
    }
}
