package net.nodeson;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.function.Predicate;

@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
@ToString(onlyExplicitlyIncluded = true)
public class NodesonObject {

    private NodesonParser parser;

    @ToString.Include
    private NodesonMap elements = new NodesonMap();

    public NodesonObject(NodesonParser parser, NodesonMap nodesonMap) {
        this(parser);
        elements.putAll(nodesonMap);
    }

    public NodesonObject(NodesonParser parser, Collection<Node> nodes) {
        this(parser);
        nodes.forEach(elements::put);
    }

    public int size() {
        return elements.size();
    }

    public void clear() {
        elements.clear();
    }

    public Node getNode(String name) {
        if (!elements.containsKey(name)) {
            return null;
        }
        return new Node(name, elements.get(name));
    }

    public boolean addNode(@NonNull Node node) {
        return elements.put(node) == null;
    }

    public boolean removeNode(@NonNull Node node) {
        return elements.remove(node) != null;
    }

    public boolean addNode(@NonNull String name, Object value) {
        Node node = new Node(name, value);

        if (value == null) {
            return removeNode(node);
        }
        return addNode(node);
    }

    public void forEachOrdered(@NonNull Predicate<Node> foreach) {
        elements.forEachOrdered(foreach);
    }
}
