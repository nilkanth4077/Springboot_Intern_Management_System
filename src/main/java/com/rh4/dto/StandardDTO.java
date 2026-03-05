package com.rh4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardDTO<T> {

    private int statusCode;
    private String message;
    private T data;
    private Map<String, Object> metadata;

    public StandardDTO(int value, String loginSuccessful, T responseData, Object o) {
    }
}