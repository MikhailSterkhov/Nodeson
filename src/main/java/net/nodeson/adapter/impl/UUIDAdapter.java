package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.Nodeson;
import net.nodeson.adapter.AbstractToStringAdapter;

import java.util.UUID;

public class UUIDAdapter extends AbstractToStringAdapter<UUID> {

    private static final StringAdapter STRING_ADAPTER = (StringAdapter) Nodeson.getNodesonInstance().getAdapter(String.class);

    @Override
    public String serialize(@NonNull UUID source) {
        return STRING_ADAPTER.serialize(super.serialize(source));
    }

    @Override
    public UUID deserialize(@NonNull Class<? extends UUID> type, @NonNull String json) {
        return UUID.fromString(STRING_ADAPTER.deserialize(String.class, json));
    }
}
