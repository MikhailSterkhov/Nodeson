package net.nodeson.token;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.nodeson.Node;
import net.nodeson.NodesonParser;
import net.nodeson.exception.NodesonTokenizeException;

import java.util.Enumeration;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class JsonTokenizer implements Enumeration<Node> {

    private NodesonParser parser;
    private String json;

    @NonFinal
    private TypeToken prev;

    @NonFinal
    private String curName;

    @NonFinal
    private int pointer;

    @NonFinal
    private boolean validated;

    @Override
    public boolean hasMoreElements() {
        return pointer < json.length();
    }

    private void doNext(int pointerAdd, TypeToken token) {
        prev = token;
        pointer += pointerAdd;
    }

    private void doNextAsName(int pointerAdd, String sub) {
        curName = sub.substring(0, pointerAdd);
        doNext(pointerAdd, TypeToken.NODE_NAME);
    }

    private boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    private boolean isString(String value) {
        return value.startsWith("\"") && value.endsWith("\"");
    }

    private boolean isObject(String value) {
        return value.startsWith("{") && value.endsWith("}");
    }

    private Object valueToObject(String value) {
        if (isBoolean(value)) {
            return Boolean.parseBoolean(value);
        }
        else if (isString(value)) {
            return value.substring(1, value.length() - 1);
        }
        else if (isInt(value)) {
            return Integer.parseInt(value);
        }
        else if (isDouble(value)) {
            return Double.parseDouble(value);
        }
        else if (isObject(value)) {
            return parser.convert(value, Object.class);
        }

        throw new NodesonTokenizeException("Value %s can`t be initialize", value);
    }

    @Override
    public Node nextElement() {
        if (!validated) {
            if (!json.startsWith("{") || !json.endsWith("}")) {
                throw new NodesonTokenizeException("'%s' isn`t json!", json);
            }

            validated = true;

            doNext(1, TypeToken.BEGIN);
            return nextElement();
        }

        String part = json.substring(pointer);

        if (part.equals("}")) {
            doNext(1, TypeToken.END);

            return null;
        }

        if (part.startsWith("\"")) {
            part = part.substring(1);
        }

        switch (prev) {

            case DELIMIT:
            case BEGIN: {
                int index = part.indexOf(':');
                if (index <= 0) {
                    throw new NodesonTokenizeException("'%s' - incorrect format", json);
                }

                doNextAsName(index, part);
                return nextElement();
            }

            case NODE_VALUE: {
                if (!part.startsWith(",")) {
                    throw new NodesonTokenizeException("'%s' - incorrect format", json);
                }

                doNext(1, TypeToken.DELIMIT);
                curName = null;

                return nextElement();
            }

            case NODE_NAME: {
                if (curName == null) {
                    throw new NodesonTokenizeException("'%s' - incorrect format", json);
                }

                String valueAsString = part.substring(1, part.contains(",") ? part.indexOf(',') : part.length() - 1);
                doNext(valueAsString.length() + 1, TypeToken.NODE_VALUE);

                return new Node(curName, valueToObject(valueAsString));
            }
        }

        return null;
    }
}
