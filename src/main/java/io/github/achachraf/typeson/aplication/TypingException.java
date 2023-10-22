package io.github.achachraf.typeson.aplication;

public class TypingException extends RuntimeException{

    public TypingException(String message) {
        super(message);
    }

    public TypingException(String message, Throwable cause) {
        super(message, cause);
    }
}
