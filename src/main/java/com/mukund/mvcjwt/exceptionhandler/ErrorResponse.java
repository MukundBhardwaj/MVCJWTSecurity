package com.mukund.mvcjwt.exceptionhandler;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * CustomError Response class conforming to the RFC standard for error responses
 * 
 * @since 1.0
 * @author Mukund Bhardwaj
 */
public class ErrorResponse {

    private ZonedDateTime timestamp;
    private String path;
    private Integer status;
    private String error;
    private String message;
    private List<String> errors;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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

    public ErrorResponse(Integer status, String error, String path, ZonedDateTime dateTime, String message,
            List<String> errors) {
        this.status = status;
        this.error = error;
        this.path = path;
        this.timestamp = dateTime;
        this.message = message;
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ErrorResponse [status=" + status + ", error=" + error + ", path=" + path + ", dateTime=" + timestamp
                + ", message=" + message + ", errors=" + errors + "]";
    }

}
