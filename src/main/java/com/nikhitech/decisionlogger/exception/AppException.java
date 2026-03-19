package com.nikhitech.decisionlogger.exception;

/*
 ===============================================================
 GENERIC APPLICATION EXCEPTION
 ===============================================================

 ✔ Used everywhere
 ✔ No multiple exception classes
 ✔ Carries HTTP status + message
*/

public class AppException extends RuntimeException {

    private final HttpStatusCode status;

    public AppException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}