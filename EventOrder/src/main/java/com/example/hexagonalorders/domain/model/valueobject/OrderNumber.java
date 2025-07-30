package com.example.hexagonalorders.domain.model.valueobject;

/**
 * Value object representing an order number.
 * This encapsulates the business rules and validation for order numbers.
 */
public record OrderNumber(String value) {
    public OrderNumber {

    }

    @Override
    public String toString() {
        return value;
    }
} 