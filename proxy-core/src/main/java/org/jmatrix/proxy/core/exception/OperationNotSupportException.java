package org.jmatrix.proxy.core.exception;

/**
 * Operation not support exception;
 *
 * @author jmatrix
 * @date 16/5/1
 */
public class OperationNotSupportException extends RuntimeException {

    public OperationNotSupportException() {
        super();
    }

    public OperationNotSupportException(String message) {
        super(message);
    }

    public OperationNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
