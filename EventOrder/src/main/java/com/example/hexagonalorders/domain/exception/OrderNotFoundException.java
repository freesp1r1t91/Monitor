package com.example.hexagonalorders.domain.exception;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderNumber) {
        super("Order with number " + orderNumber + " not found");
    }

    public OrderNotFoundException(OrderNumber orderNumber) {
        super("Order with number " + orderNumber + " not found");
    }
}
