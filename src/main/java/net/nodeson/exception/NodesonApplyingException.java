package net.nodeson.exception;

public class NodesonApplyingException extends NodesonException {

    public NodesonApplyingException(String message, Object... params) {
        super(String.format(message, params));
    }
}
