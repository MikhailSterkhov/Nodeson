package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.adapter.AbstractToStringAdapter;
import net.nodeson.exception.NodesonAdapterException;

import java.util.function.Function;

public class NumberAdapter extends AbstractToStringAdapter<Number> {

    private boolean sneakyNumberFormat(String value, Function<String, Number> formatter) {
        try {
            formatter.apply(value);
            return true;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }

    protected boolean isByte(String value) {
        return sneakyNumberFormat(value, Byte::parseByte);
    }

    protected boolean isInteger(String value) {
        return sneakyNumberFormat(value, Integer::parseInt);
    }

    protected boolean isLong(String value) {
        return sneakyNumberFormat(value, Long::parseLong);
    }

    protected boolean isFloat(String value) {
        return sneakyNumberFormat(value, Float::parseFloat);
    }

    protected boolean isDouble(String value) {
        return sneakyNumberFormat(value, Double::parseDouble);
    }

    @Override
    public Number deserialize(@NonNull Class<? extends Number> type, @NonNull String json) {
        if (isByte(json)) {
            return Byte.parseByte(json);
        }

        if (isInteger(json)) {
            return Integer.parseInt(json);
        }

        if (isLong(json)) {
            return Long.parseLong(json);
        }

        if (isFloat(json)) {
            return Float.parseFloat(json);
        }

        if (isDouble(json)) {
            return Double.parseDouble(json);
        }

        throw new NodesonAdapterException("Value '%s' isn`t instanceof a Number", json);
    }
}
