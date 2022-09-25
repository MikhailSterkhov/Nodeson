package net.nodeson;

import lombok.experimental.UtilityClass;
import net.nodeson.exception.NodesonApplyingException;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class NodesonUnsafe {

    private Unsafe sunUnsafe;
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);

            sunUnsafe = (Unsafe) theUnsafe.get(null);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private final Map<Class<?>, NodesonMap> TYPES_VARIABLES_MAP = new HashMap<>();

    private final Map<Field, MethodHandle> FIELDS_GETTERS_MAP = new HashMap<>(),
            FIELDS_SETTERS_MAP = new HashMap<>();

    public synchronized <T> T allocate(Class<T> type) {
        try {
            @SuppressWarnings("unchecked") T instance
                    = (T) sunUnsafe.allocateInstance(type);

            return instance;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public synchronized NodesonMap toNodesMap(Object src)
    throws Throwable {

        Class<?> type = src.getClass();

        NodesonMap map = new NodesonMap();

        if (TYPES_VARIABLES_MAP.containsKey(type)) {
            map = TYPES_VARIABLES_MAP.get(type);
        }

        for (Field field : type.getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.TRANSIENT) == Modifier.TRANSIENT) {
                continue;
            }

            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            MethodHandle getter = FIELDS_GETTERS_MAP.computeIfAbsent(field, __ -> {
                try {
                    return LOOKUP.unreflectGetter(field);
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            map.put(field.getName(), getter.invoke(src));
        }

        TYPES_VARIABLES_MAP.put(type, map);
        return map;
    }

    public synchronized Class<?> getObjectType(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return Number.class;
        }
        else if (type.isPrimitive()) {

            if (type.isAssignableFrom(boolean.class)) {
                return Boolean.class;
            }
            else {
                return Number.class;
            }
        }

        return type;
    }

    public synchronized Class<?> getObjectType(Object src) {
        return getObjectType(src.getClass());
    }

    public synchronized void applyNodes(Object source, NodesonObject nodesonObject) {
        if (source instanceof NodesonObjectBuilder) {
            applyNodes(((NodesonObjectBuilder) source).build(), nodesonObject);
            return;
        }

        if (source instanceof NodesonObject) {
            nodesonObject.forEachOrdered(node -> {

                ((NodesonObject) source).addNode(node);
                return true;
            });

            return;
        }

        Class<?> type = source.getClass();

        nodesonObject.forEachOrdered(node -> {
            Object value = node.getValue();

            try {
                Field field = type.getDeclaredField(node.getName());

                Class<?> fieldType = getObjectType(field.getType());
                Class<?> valueType = getObjectType(value);

                if (!valueType.equals(fieldType)) {

                    NodesonAdapter<Object> fieldAdapter = Nodeson.getNodesonInstance().getCheckedAdapter(fieldType);
                    NodesonAdapter<Object> valueAdapter = Nodeson.getNodesonInstance().getCheckedAdapter(valueType);

                    value = fieldAdapter.deserialize(fieldType, valueAdapter.serialize(value));
                }

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                MethodHandle setter = FIELDS_SETTERS_MAP.computeIfAbsent(field, __ -> {
                    try {
                        return LOOKUP.unreflectSetter(field);
                    }
                    catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });

                setter.invoke(source, value);
            }
            catch (Throwable e) {
                throw new NodesonApplyingException("Cannot be apply %s for %s", nodesonObject, type);
            }

            return true;
        });
    }

}
