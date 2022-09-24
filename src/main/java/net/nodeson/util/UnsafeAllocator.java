package net.nodeson.util;

import lombok.experimental.UtilityClass;
import net.nodeson.Node;
import net.nodeson.NodesonMap;
import net.nodeson.NodesonObject;
import net.nodeson.exception.NodesonApplyingException;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@UtilityClass
public class UnsafeAllocator {

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

    public <T> T allocate(Class<T> type) {
        try {
            @SuppressWarnings("unchecked") T instance
                    = (T) sunUnsafe.allocateInstance(type);

            return instance;
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public NodesonMap toNodesMap(Object src)
    throws IllegalAccessException {

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

            map.put(field.getName(), field.get(src));
        }

        TYPES_VARIABLES_MAP.put(type, map);
        return map;
    }

    public void applyNodes(Object source, NodesonObject nodesonObject) {
        Class<?> type = source.getClass();

        nodesonObject.forEachOrdered(node -> {

            try {
                Field declaredField = type.getDeclaredField(node.getName());

                declaredField.setAccessible(true);
                declaredField.set(source, node.getValue());
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new NodesonApplyingException("Cannot be apply %s for %s", nodesonObject, type);
            }

            return true;
        });
    }

}
