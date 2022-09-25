package net.nodeson;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.Collection;
import java.util.function.Predicate;

@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ToString(onlyExplicitlyIncluded = true)
public class NodesonObject {

    private NodesonParser parser;

    @ToString.Include
    @NonFinal
    private NodesonMap elements;

    private NodesonMap getElements() {
        if (elements == null) {
            elements = new NodesonMap();
        }

        return elements;
    }

    public NodesonObject(NodesonParser parser, NodesonMap nodesonMap) {
        this(parser);
        getElements().putAll(nodesonMap);
    }

    public NodesonObject(NodesonParser parser, Collection<Node> nodes) {
        this(parser);
        nodes.forEach(getElements()::put);
    }

    public int size() {
        return getElements().size();
    }

    public void clear() {
        getElements().clear();
    }

    public Node getNode(String name) {
        if (!getElements().containsKey(name)) {
            return null;
        }
        return new Node(name, getElements().get(name));
    }

    public boolean addNode(@NonNull Node node) {
        return getElements().put(node) == null;
    }

    public boolean removeNode(@NonNull Node node) {
        return getElements().remove(node) != null;
    }

    public boolean addNode(@NonNull String name, Object value) {
        Node node = new Node(name, value);

        if (value == null) {
            return removeNode(node);
        }
        return addNode(node);
    }

    public void forEachOrdered(@NonNull Predicate<Node> foreach) {
        getElements().forEachOrdered(foreach);
    }
}
