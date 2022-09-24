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
public abstract class AbstractStringAdapter<T> implements NodesonAdapter<T, StringBuffer> {

    @Getter(AccessLevel.PROTECTED)
    private NodesonParser callingSource;
}
