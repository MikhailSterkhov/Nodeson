package net.nodeson.parse;

import lombok.NonNull;
import net.nodeson.Node;
import net.nodeson.NodesonObject;
import net.nodeson.token.JsonTokenizer;
import net.nodeson.util.UnsafeAllocator;

import java.lang.invoke.MethodHandles;

public class CommonNodesonParser extends AbstractNodesonParser {

    @Override
    public NodesonObject toNodeson(@NonNull Object src) {
        try {
            return new NodesonObject(this, UnsafeAllocator.toNodesMap(src));
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

        T instance = UnsafeAllocator.allocate(type);

        UnsafeAllocator.applyNodes(instance, nodesonObject);
        return instance;
    }

    @Override
    public String parse(@NonNull NodesonObject nodesonObject) {
        StringBuffer stringBuffer = new StringBuffer()
                .append("{");

        nodesonObject.forEachOrdered(node -> {

            stringBuffer.append(node.getName());
            stringBuffer.append(":");

            Object value = node.getValue();

            if (value instanceof String) {
                stringBuffer.append("\"").append(value).append("\"");
            }
            else if (value instanceof Number || value.getClass().isPrimitive()) {
                stringBuffer.append(value);
            }
            else if (value instanceof Node) {
                stringBuffer.append(parse(((Node) value).getValue()));
            }
            else {
                stringBuffer.append(parse(toNodeson(value)));
            }

            stringBuffer.append(",");
            return true;
        });

        String line = stringBuffer.toString();
        return line.substring(0, line.length() - 1) + "}";
    }
}
