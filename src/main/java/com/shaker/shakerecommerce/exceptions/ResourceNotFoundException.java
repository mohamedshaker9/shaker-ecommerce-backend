package com.shaker.shakerecommerce.exceptions;

import java.io.Serializable;


public class ResourceNotFoundException extends Throwable {
    private String name;
    private String field;
    private String fieldName;
    private Long fieldId;


    public ResourceNotFoundException(String name, String field, String fieldName) {
        super(String.format("Resource not found with name '%s' and field '%s'", name, field));
        this.name = name;
        this.field = field;
        this.fieldName = fieldName;

    }

    public ResourceNotFoundException(String name, String field, Long fieldId) {
        super(String.format("Resource not found with name '%s' and field '%s' with id: %d", name, field, fieldId));
        this.name = name;
        this.field = field;
        this.fieldId = fieldId;

    }

}
