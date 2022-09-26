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
        return (NodesonAdapter<T>) adaptersMap.getOrDefault(NodesonUnsafe.getObjectType(type), OBJECT_ADAPTER);
    }

    @SuppressWarnings("unchecked")
    public NodesonAdapter<Object> getCheckedAdapter(Class<?> type) {
        return getAdapter((Class<Object>) type);
    }

    public <T> void registerAdapter(Class<T> type, NodesonAdapter<T> adapter) {
        adaptersMap.put(type, adapter);
    }

    public NodesonObject wrap(Object object) {
        return COMMON.wrap(object);
    }

    public NodesonObject wrapParallel(Object object) {
        return PARALLEL.wrap(object);
    }

    public <T> T fromJson(String json, Class<T> type) {
        return COMMON.parseFrom(json, type);
    }

    public <T> T fromJsonParallel(String json, Class<T> type) {
        return PARALLEL.parseFrom(json, type);
    }

    public String toJson(Object object) {
        return COMMON.parseTo(object);
    }

    public String toJsonParallel(Object object) {
        return PARALLEL.parseTo(object);
    }
}
