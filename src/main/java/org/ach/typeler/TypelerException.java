package org.ach.typeler;

public class TypelerException extends RuntimeException{

    public TypelerException() {
    }

    public TypelerException(String message) {
        super(message);
    }

    public TypelerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypelerException(Throwable cause) {
        super(cause);
    }
}
