package com.mukund.mvcjwt.exceptionhandler;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Custom Error Response class conforming to the RFC standard for error
 * responses
 * 
 * @since 1.0
 * @author Mukund Bhardwaj
 */
public class CustomErrorResponse {

    private ZonedDateTime timestamp;
    private String path;
    private String status;
    private String message;
    private List<String> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime dateTime) {
        this.timestamp = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public CustomErrorResponse(String status, String path, ZonedDateTime dateTime, String message,
            List<String> errors) {
        this.status = status;
        this.path = path;
        this.timestamp = dateTime;
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ErrorResponse [status=" + status + ", path=" + path + ", dateTime=" + timestamp
                + ", message=" + message + ", errors=" + errors + "]";
    }

}