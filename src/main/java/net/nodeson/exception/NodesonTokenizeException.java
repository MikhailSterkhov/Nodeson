package net.nodeson.exception;

public class NodesonTokenizeException extends NodesonException {

    public NodesonTokenizeException(String message, Object... params) {
        super(String.format(message, params));
    }
}
