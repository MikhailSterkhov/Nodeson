package net.nodeson.parse;

import lombok.NonNull;
import net.nodeson.*;
import net.nodeson.token.JsonTokenizer;

public class CommonNodesonParser extends AbstractNodesonParser {

    @Override
    public NodesonObject toNodeson(@NonNull Object src) {
        try {
            return new NodesonObject(this, NodesonUnsafe.toNodesMap(src));
        }
        catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public NodesonObject toNodeson(@NonNull String parsedLine) {
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
        NodesonObject nodesonObject = toNodeson(parsedLine);

        T instance = NodesonUnsafe.allocate(type);

        NodesonUnsafe.applyNodes(instance, nodesonObject);
        return instance;
    }

    @Override
    public String parse(@NonNull NodesonObject nodesonObject) {
        StringBuffer stringBuffer = new StringBuffer()
                .append("{");

        nodesonObject.forEachOrdered(node -> {

            Object value = node.getValue();
            if (value == null) {
                return true;
            }

            Class<?> valueType = NodesonUnsafe.getObjectType(value);
            NodesonAdapter<Object> adapter = Nodeson.getNodesonInstance().getCheckedAdapter(valueType);

            stringBuffer.append(node.getName()).append(":").append(adapter.serialize(value)).append(",");
            return true;
        });

        String line = stringBuffer.toString();
        return line.substring(0, line.length() - 1) + "}";
    }
}
