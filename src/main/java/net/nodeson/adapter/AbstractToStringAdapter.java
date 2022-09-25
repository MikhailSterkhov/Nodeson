package net.nodeson.adapter;

import lombok.NonNull;
import net.nodeson.NodesonAdapter;

public abstract class AbstractToStringAdapter<T> implements NodesonAdapter<T> {

    @Override
    public String serialize(@NonNull T source) {
        return source.toString();
    }
}
