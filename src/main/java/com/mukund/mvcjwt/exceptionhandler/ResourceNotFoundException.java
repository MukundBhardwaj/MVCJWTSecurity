package com.mukund.mvcjwt.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
/**
 * Custom Exception class for Resource Not Found exceptions
 * 
 * @since 1.0
 * @author Mukund Bhardwaj
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}