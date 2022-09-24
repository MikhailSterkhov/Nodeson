package net.nodeson.parse;

import lombok.NonNull;
import net.nodeson.NodesonObject;
import net.nodeson.NodesonObjectBuilder;
import net.nodeson.NodesonParser;

abstract class AbstractNodesonParser implements NodesonParser {

    @Override
    public <T> T convert(@NonNull NodesonObjectBuilder nodesonObject, @NonNull Class<T> type) {
        return convert(nodesonObject.build(), type);
    }

    @Override
    public <T> T convert(@NonNull NodesonObject nodesonObject, @NonNull Class<T> type) {
        return convert(parse(nodesonObject), type);
    }

    @Override
    public String parse(@NonNull Object src) {
        return parse(toNodeson(src));
    }
}
