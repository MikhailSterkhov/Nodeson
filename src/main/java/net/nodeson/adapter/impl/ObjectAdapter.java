package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.NodesonObject;
import net.nodeson.adapter.AbstractMappingAdapter;

public final class ObjectAdapter extends AbstractMappingAdapter<Object> {

    @Override
    protected NodesonObject doSerialize(@NonNull Object source) {
        return COMMON_PARSER.wrap(source);
    }

    @Override
    protected NodesonObject doDeserialize(@NonNull String json) {
        return COMMON_PARSER.wrap(json);
    }
}
