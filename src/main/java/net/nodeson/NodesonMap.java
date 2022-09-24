package net.nodeson;

import lombok.NonNull;

import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class NodesonMap extends TreeMap<String, Object> {

    public synchronized Node put(@NonNull Node node) {
        return new Node(node.getName(), super.put(node.getName(), node.getValue()));
    }

    public synchronized Node remove(@NonNull Node node) {
        return new Node(node.getName(), super.remove(node.getName()));
    }

    public synchronized Node get(@NonNull String name) {
        return new Node(name, super.get(name));
    }

    public synchronized void forEachOrdered(@NonNull Predicate<Node> foreach) {
        AtomicBoolean breakable = new AtomicBoolean();

        super.forEach((name, value) -> {
            if (breakable.get()) {
                return;
            }

            if (!foreach.test(new Node(name, value))) {
                breakable.set(true);
            }
        });
    }

    public synchronized void putAll(@NonNull NodesonMap nodesonMap) {
        nodesonMap.forEachOrdered(node -> {

            this.put(node);
            return true;
        });
    }
}
