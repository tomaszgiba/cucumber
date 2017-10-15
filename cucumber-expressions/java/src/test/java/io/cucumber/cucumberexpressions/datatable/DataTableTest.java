package io.cucumber.cucumberexpressions.datatable;

import io.cucumber.cucumberexpressions.ParameterTypeRegistry;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class DataTableTest {
    public static class Ingredient {
        public final String name;
        public final Integer amount;
        public final String unit;

        public Ingredient(String name, Integer amount, String unit) {
            this.name = name;
            this.amount = amount;
            this.unit = unit;
        }
    }

    @Test
    public void creates_pojo_from_row() {
        ParameterTypeRegistry registry = new ParameterTypeRegistry(Locale.ENGLISH);
        DataTable dataTable = new DataTable(registry, singletonList(asList("chocolate", "2", "tbsp")));
        List<Ingredient> ingredients = dataTable.asList(Ingredient.class);
        Ingredient ingredient = ingredients.get(0);
        assertEquals(ingredient.name, "chocolate");
    }

}
