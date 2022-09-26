package net.nodeson.parse;

import lombok.NonNull;
import net.nodeson.NodesonObject;
import net.nodeson.NodesonObjectBuilder;
import net.nodeson.NodesonParser;

abstract class AbstractNodesonParser implements NodesonParser {

    @Override
    public <T> T parseFrom(@NonNull NodesonObjectBuilder nodesonObject, @NonNull Class<T> type) {
        return parseFrom(nodesonObject.build(), type);
    }

    @Override
    public <T> T parseFrom(@NonNull NodesonObject nodesonObject, @NonNull Class<T> type) {
        return parseFrom(parseTo(nodesonObject), type);
    }

    @Override
    public String parseTo(@NonNull Object src) {
        return parseTo(wrap(src));
    }
}
