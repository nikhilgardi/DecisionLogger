package com.nikhitech.decisionlogger.exception;

/*
 ===============================================================
 COMPLETE HTTP STATUS CODE ENUM
 ===============================================================

 Covers standard HTTP status codes (RFC 7231 + common extensions)

 WHY USE THIS?
 ✔ Avoid magic numbers (404, 500, etc.)
 ✔ Centralized control
 ✔ Readable + maintainable code

 GROUPS:
 1xx → Informational
 2xx → Success
 3xx → Redirection
 4xx → Client Errors
 5xx → Server Errors
*/

public enum HttpStatusCode {

    /*
     ===============================================================
     1xx INFORMATIONAL
     ===============================================================
    */

    CONTINUE(100, "Continue"),                       // Request received, continue
    SWITCHING_PROTOCOLS(101, "Switching Protocols"), // Protocol change
    PROCESSING(102, "Processing"),                   // WebDAV


    /*
     ===============================================================
     2xx SUCCESS
     ===============================================================
    */

    OK(200, "OK"),                                 // Standard success
    CREATED(201, "Created"),                       // Resource created
    ACCEPTED(202, "Accepted"),                     // Request accepted but not completed
    NON_AUTHORITATIVE(203, "Non-Authoritative"),   // Meta info
    NO_CONTENT(204, "No Content"),                 // Success, no body
    RESET_CONTENT(205, "Reset Content"),           // Reset form
    PARTIAL_CONTENT(206, "Partial Content"),       // Partial response


    /*
     ===============================================================
     3xx REDIRECTION
     ===============================================================
    */

    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),


    /*
     ===============================================================
     4xx CLIENT ERRORS
     ===============================================================
    */

    BAD_REQUEST(400, "Bad Request"),               // Invalid request
    UNAUTHORIZED(401, "Unauthorized"),             // Login required
    PAYMENT_REQUIRED(402, "Payment Required"),     // Reserved
    FORBIDDEN(403, "Forbidden"),                  // Access denied
    NOT_FOUND(404, "Not Found"),                 // Resource missing
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTH_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),                   // Duplicate data
    GONE(410, "Gone"),                           // Resource permanently removed
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    URI_TOO_LONG(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    IM_A_TEAPOT(418, "I'm a teapot"),             // Easter egg 😄
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),


    /*
     ===============================================================
     5xx SERVER ERRORS
     ===============================================================
    */

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"), // Generic failure
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");


    /*
     ===============================================================
     FIELDS
     ===============================================================
    */

    private final int code;
    private final String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /*
     ===============================================================
     GETTERS
     ===============================================================
    */

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    /*
     ===============================================================
     UTILITY METHOD (OPTIONAL)
     ===============================================================

     Convert code → enum
    */

    public static HttpStatusCode fromCode(int code) {
        for (HttpStatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
}