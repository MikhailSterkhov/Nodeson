package net.nodeson.adapter.impl;

import lombok.NonNull;
import net.nodeson.NodesonObject;
import net.nodeson.NodesonObjectBuilder;
import net.nodeson.adapter.AbstractMappingAdapter;

public final class NodesonObjectBuilderAdapter extends AbstractMappingAdapter<NodesonObjectBuilder> {

    @Override
    protected NodesonObject doSerialize(@NonNull NodesonObjectBuilder source) {
        return source.build();
    }

    @Override
    protected NodesonObject doDeserialize(@NonNull String json) {
        return COMMON_PARSER.toNodeson(json);
    }
}
