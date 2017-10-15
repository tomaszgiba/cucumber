package io.cucumber.cucumberexpressions.datatable;

import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.ParameterTypeRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class DataTable {
    private final ParameterTypeRegistry registry;
    private final List<List<String>> rows;

    public DataTable(ParameterTypeRegistry registry, List<List<String>> rows) {
        this.registry = registry;
        this.rows = rows;
    }

    public <T> List<T> asList(Class<T> rowType) {
        try {
            return asList0(rowType);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> asList0(Class<T> rowType) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> list = new ArrayList<>();

        for (List<String> row : rows) {
            Constructor<?> constructor = null;
            Constructor<?>[] constructors = rowType.getConstructors();
            for (Constructor<?> ctor : constructors) {
                if (ctor.getParameterTypes().length == row.size()) {
                    constructor = ctor;
                    break;
                }
            }

            List<ParameterType<?>> parameterTypes = new ArrayList<>();
            for (Class<?> type : constructor.getParameterTypes()) {
                ParameterType<?> parameterType = registry.lookupByType(type);
                parameterTypes.add(parameterType);
            }

            Object[] args = new Object[row.size()];
            for (int i = 0; i < row.size(); i++) {
                String cell = row.get(i);
                ParameterType<?> parameterType = parameterTypes.get(i);
                Object arg = parameterType.transform(singletonList(cell));
                args[i] = arg;
            }
            list.add((T) constructor.newInstance(args));
        }
        return list;
    }
}
