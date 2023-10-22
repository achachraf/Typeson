package io.github.achachraf.typeson.aplication;

public class DeserializationException extends RuntimeException{

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
