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
        curName = curName.startsWith("\"") && curName.endsWith("\"") ? curName.substring(1, curName.length() - 1) : curName;

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
        return value.startsWith("{");
    }

    private Object valueToObject(String value) {
        // TODO - Make adapters validations & remove this code

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
            return parser.toNodeson(value);
        }

        throw new NodesonTokenizeException("Value '%s' can`t be initialize", value);
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

                if (part.startsWith("\"")) {
                    part = part.substring(1);
                }

                String valueAsString;
                part = part.substring(1); // remove ':' sign

                if (part.startsWith("{")) {

                    // find end-object sign index.
                    int lastInternalBeginIndex = 0;
                    int lastInternalEndIndex = 0;

                    boolean blocked = false;

                    for (int i = 1; i < part.length() - 1; i++) {

                        // skip strings values.
                        if (part.charAt(i) == '"') {
                            blocked = !blocked;
                        }

                        if (!blocked) {
                            char sign = part.charAt(i);

                            if (sign == '{') {
                                lastInternalBeginIndex = i;
                            }
                            else if (sign == '}') {

                                if (lastInternalBeginIndex > 0) {
                                    lastInternalEndIndex = i;
                                }
                                else {
                                    break;
                                }
                            }
                        }
                    }

                    valueAsString = part.substring(0, part.indexOf('}', lastInternalEndIndex) + 1);

                } else {
                    valueAsString = part.substring(0, part.contains(",") ? part.indexOf(',') : part.length() - 1);
                }

                doNext(valueAsString.length() + 1, TypeToken.NODE_VALUE);
                return new Node(curName, valueToObject(valueAsString));
            }
        }

        return null;
    }
}
