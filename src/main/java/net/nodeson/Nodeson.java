package net.nodeson;

import net.nodeson.adapter.impl.*;
import net.nodeson.parse.CommonNodesonParser;
import net.nodeson.parse.ParallelNodesonParser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Nodeson {

    private static final NodesonParser COMMON = new CommonNodesonParser();
    private static final NodesonParser PARALLEL = new ParallelNodesonParser(COMMON);

    private static final ObjectAdapter OBJECT_ADAPTER = new ObjectAdapter();

    private static final Nodeson instance = new Nodeson();
    static {
        instance.registerAdapter(Number.class, new NumberAdapter());
        instance.registerAdapter(Boolean.class, new BooleanAdapter());
        instance.registerAdapter(String.class, new StringAdapter());
        instance.registerAdapter(UUID.class, new UUIDAdapter());
        instance.registerAdapter(Class.class, new ClassAdapter());
        instance.registerAdapter(NodesonObject.class, new NodesonObjectAdapter());
        instance.registerAdapter(NodesonObjectBuilder.class, new NodesonObjectBuilderAdapter());
    }

    public static Nodeson getNodesonInstance() {
        return instance;
    }

    public static NodesonParser common() {
        return COMMON;
    }

    public static NodesonParser parallel() {
        return PARALLEL;
    }

    private final Map<Class<?>, NodesonAdapter<?>> adaptersMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> NodesonAdapter<T> getAdapter(Class<T> type) {
        return (NodesonAdapter<T>) adaptersMap.getOrDefault(type, OBJECT_ADAPTER);
    }

    @SuppressWarnings("unchecked")
    public NodesonAdapter<Object> getCheckedAdapter(Class<?> type) {
        return getAdapter((Class<Object>) type);
    }

    public <T> void registerAdapter(Class<T> type, NodesonAdapter<T> adapter) {
        adaptersMap.put(type, adapter);
    }

    public <T> T convert(String json, Class<T> type) {
        return COMMON.convert(json, type);
    }

    public <T> T parallelConvert(String json, Class<T> type) {
        return PARALLEL.convert(json, type);
    }

    public NodesonObject wrap(Object object) {
        return COMMON.toNodeson(object);
    }

    public NodesonObject parallelWrap(Object object) {
        return PARALLEL.toNodeson(object);
    }

    public String parse(Object object) {
        return COMMON.parse(object);
    }

    public String parallelParse(Object object) {
        return PARALLEL.parse(object);
    }
}
