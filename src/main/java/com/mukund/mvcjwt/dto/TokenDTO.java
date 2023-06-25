package com.mukund.mvcjwt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenDTO(String access, String refresh) {
}