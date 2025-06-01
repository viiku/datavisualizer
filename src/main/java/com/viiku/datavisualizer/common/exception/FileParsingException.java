package com.viiku.datavisualizer.common.exception;

public class FileParsingException extends RuntimeException{

    public FileParsingException(String message) {
        super(message);
    }

    public FileParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileParsingException(Throwable cause) {
        super(cause);
    }
}
