package net.nodeson.exception;

public class NodesonAdapterException extends NodesonException {

    public NodesonAdapterException(String message, Object... params) {
        super(String.format(message, params));
    }
}
