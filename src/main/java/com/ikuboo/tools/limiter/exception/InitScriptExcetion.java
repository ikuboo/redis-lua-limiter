package com.ikuboo.tools.limiter.exception;

public class InitScriptExcetion extends RuntimeException {

    public InitScriptExcetion() {
    }

    public InitScriptExcetion(String message) {
        super(message);
    }

    public InitScriptExcetion(Throwable cause) {
        super(cause);
    }

    public InitScriptExcetion(String message, Throwable cause) {
        super(message, cause);
    }
}
