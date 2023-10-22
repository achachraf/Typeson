package io.github.achachraf.typeson.aplication;

public class UnexpectedFieldException extends RuntimeException{

    public UnexpectedFieldException(String message) {
        super(message);
    }

    public UnexpectedFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
