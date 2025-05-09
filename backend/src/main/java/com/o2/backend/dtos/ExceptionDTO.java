package com.o2.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ExceptionDTO {
    private String message;
    private Map<String, String> errors;

    public ExceptionDTO(String message) {
        this.message = message;
        this.errors = null;
    }
}
