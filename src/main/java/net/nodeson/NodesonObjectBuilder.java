package net.nodeson;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public final class NodesonObjectBuilder {

    private NodesonParser parser;

    private ArrayList<Node> nodes = new ArrayList<>();

    private Object lock = new Object();

    public NodesonObjectBuilder withAll(@NonNull Collection<Node> nodes) {
        synchronized (lock) {
            this.nodes.addAll(nodes);
            return this;
        }
    }

    public NodesonObjectBuilder withAll(@NonNull Node... nodes) {
        synchronized (lock) {
            return withAll(Arrays.asList(nodes));
        }
    }

    public NodesonObjectBuilder with(@NonNull Node node) {
        synchronized (lock) {
            this.nodes.add(node);
            return this;
        }
    }

    public NodesonObjectBuilder with(@NonNull String name, Object value) {
        synchronized (lock) {
            if (value != null) {
                return with(new Node(name, value));
            }

            return this;
        }
    }

    public NodesonObject build() {
        return new NodesonObject(parser, nodes);
    }
}
