package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.adapter.AbstractToStringAdapter;
import net.nodeson.exception.NodesonAdapterException;

public final class BooleanAdapter extends AbstractToStringAdapter<Boolean> {

    @Override
    public Boolean deserialize(@NonNull Class<? extends Boolean> type, @NonNull String json) {
        if (json.equalsIgnoreCase("true") || json.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(json);
        }

        throw new NodesonAdapterException("Value '%s' isn`t instanceof a Boolean");
    }
}
