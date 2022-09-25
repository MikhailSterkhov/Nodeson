package net.nodeson.adapter.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import net.nodeson.Nodeson;
import net.nodeson.adapter.AbstractToStringAdapter;

@SuppressWarnings("ALL")
public class ClassAdapter extends AbstractToStringAdapter<Class> {

    private static final StringAdapter STRING_ADAPTER = (StringAdapter) Nodeson.getNodesonInstance().getAdapter(String.class);

    @Override
    public String serialize(@NonNull Class source) {
        return STRING_ADAPTER.serialize(source.getName());
    }

    @SneakyThrows
    @Override
    public Class deserialize(@NonNull Class<? extends Class> type, @NonNull String json) {
        return Class.forName(STRING_ADAPTER.deserialize(String.class, json));
    }

}
