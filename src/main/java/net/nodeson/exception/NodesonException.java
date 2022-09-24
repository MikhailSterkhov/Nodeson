package net.nodeson.exception;

public class NodesonException extends RuntimeException {

    public NodesonException(String message, Object... params) {
        super(String.format(message, params));
    }
}
