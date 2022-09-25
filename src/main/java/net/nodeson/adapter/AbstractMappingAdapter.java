package net.nodeson.adapter;

import lombok.NonNull;
import net.nodeson.*;

public abstract class AbstractMappingAdapter<T> implements NodesonAdapter<T> {

    protected static final NodesonParser COMMON_PARSER = Nodeson.common();

    protected abstract NodesonObject doSerialize(@NonNull T source);

    protected abstract NodesonObject doDeserialize(@NonNull String json);

    @Override
    public String serialize(@NonNull T source) {
        NodesonObject nodesonObject = doSerialize(source);
        return COMMON_PARSER.parse(nodesonObject);
    }

    @Override
    public T deserialize(@NonNull Class<? extends T> type, @NonNull String json) {
        NodesonObject nodesonObject = doDeserialize(json);
        return COMMON_PARSER.convert(nodesonObject, type);
    }
}
