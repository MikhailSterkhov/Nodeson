package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.adapter.AbstractToStringAdapter;

public final class StringAdapter extends AbstractToStringAdapter<String> {

    private static final String FORMAT = ("\"%s\"");

    @Override
    public String serialize(@NonNull String json) {
        return String.format(FORMAT, json);
    }

    @Override
    public String deserialize(@NonNull Class<? extends String> type, @NonNull String json) {
        return json.startsWith("\"") && json.endsWith("\"") ? json.substring(1, json.length() - 1) : json;
    }
}
