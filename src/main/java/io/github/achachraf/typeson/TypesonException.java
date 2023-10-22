package io.github.achachraf.typeson;

public class TypesonException extends RuntimeException{

    public TypesonException(String message) {
        super(message);
    }

    public TypesonException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypesonException(Throwable cause) {
        super(cause);
    }
}
