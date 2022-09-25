package net.nodeson.parse;

import lombok.NonNull;
import net.nodeson.*;
import net.nodeson.token.JsonTokenizer;

import java.util.Collections;

public class CommonNodesonParser extends AbstractNodesonParser {

    private boolean isNotJson(String line) {
        return !line.startsWith("{") && !line.endsWith("}");
    }

    @Override
    public NodesonObject toNodeson(@NonNull Object src) {
        try {
            return new NodesonObject(this, NodesonUnsafe.toNodesMap(src));
        }
        catch (Throwable exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public NodesonObject toNodeson(@NonNull String parsedLine) {
        if (isNotJson(parsedLine)) {
            return new NodesonObject(this, Collections.singleton(new Node("value", parsedLine)));
        }

        NodesonObject nodesonObject = new NodesonObject(this);
        JsonTokenizer tokenizer = new JsonTokenizer(this, parsedLine);

        while (tokenizer.hasMoreElements()) {
            Node node = tokenizer.nextElement();

            if (node != null) {
                nodesonObject.addNode(node);
            }
        }

        return nodesonObject;
    }

    @Override
    public <T> T convert(@NonNull String parsedLine, @NonNull Class<T> type) {
        if (!isNotJson(parsedLine)) {
            NodesonAdapter<Object> adapter = Nodeson.getNodesonInstance().getCheckedAdapter(type);

            @SuppressWarnings("unchecked") T uncheckedInstance = (T) adapter.deserialize(type, parsedLine);
            return uncheckedInstance;
        }

        NodesonObject nodesonObject = toNodeson(parsedLine);

        T instance = NodesonUnsafe.allocate(type);

        NodesonUnsafe.applyNodes(instance, nodesonObject);
        return instance;
    }

    @Override
    public String parse(@NonNull NodesonObject nodesonObject) {
        StringBuilder stringBuilder = new StringBuilder()
                .append("{");

        nodesonObject.forEachOrdered(node -> {

            Object value = node.getValue();
            if (value == null) {
                return true;
            }

            NodesonAdapter<Object> adapter = Nodeson.getNodesonInstance().getCheckedAdapter(value.getClass());
            stringBuilder.append(node.getName()).append(":").append(adapter.serialize(value)).append(",");

            return true;
        });

        String line = stringBuilder.toString();
        return line.substring(0, line.length() - 1) + "}";
    }
}
