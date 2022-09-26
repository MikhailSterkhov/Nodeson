package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.NodesonObject;
import net.nodeson.adapter.AbstractMappingAdapter;

public final class NodesonObjectAdapter extends AbstractMappingAdapter<NodesonObject> {

    @Override
    protected NodesonObject doSerialize(@NonNull NodesonObject source) {
        return source;
    }

    @Override
    protected NodesonObject doDeserialize(@NonNull String json) {
        return COMMON_PARSER.wrap(json);
    }
}
