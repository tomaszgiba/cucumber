package io.cucumber.cucumberexpressions;

public class Argument {
    private final int offset;
    private final String value;
    private final Object transformedValue;

    public Argument(Integer offset, String value, Object transformedValue) {
        this.offset = offset;
        this.value = value;
        this.transformedValue = transformedValue;
    }

    public int getOffset() {
        return offset;
    }

    public String getValue() {
        return value;
    }

    public Object getTransformedValue() {
        return transformedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument argument = (Argument) o;

        if (offset != argument.offset) return false;
        if (!value.equals(argument.value)) return false;
        return transformedValue.equals(argument.transformedValue);
    }

    @Override
    public int hashCode() {
        int result = offset;
        result = 31 * result + value.hashCode();
        result = 31 * result + transformedValue.hashCode();
        return result;
    }
}
