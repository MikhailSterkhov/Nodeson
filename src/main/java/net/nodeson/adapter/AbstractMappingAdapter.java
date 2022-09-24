package net.nodeson.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.nodeson.NodesonAdapter;
import net.nodeson.NodesonMap;
import net.nodeson.NodesonParser;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public abstract class AbstractMappingAdapter<T> implements NodesonAdapter<T, NodesonMap> {

    @Getter(AccessLevel.PROTECTED)
    private NodesonParser callingSource;
}
