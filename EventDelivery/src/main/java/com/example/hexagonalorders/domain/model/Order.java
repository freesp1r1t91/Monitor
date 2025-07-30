package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Core domain entity representing an Order in the system.
 * This class is part of the domain layer and contains the business logic and rules
 * related to orders. It is independent of any infrastructure concerns.
 * 
 * An Order consists of:
 * - A unique identifier
 * - An order number (as a value object)
 * - A customer ID (reference to Customer aggregate)
 * - A creation date
 * - A list of order items
 * - A list of domain events
 */
public class Order {
    private final OrderNumber orderNumber;
    private final String customerId;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private final OrderStatus status;

    public Order(OrderNumber orderNumber, String customerId, LocalDateTime orderDate, List<OrderItem> items, OrderStatus status) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
        this.status = status;
    }

    public Order(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.customerId = order.getCustomerId();
        this.orderDate = order.getOrderDate();
        this.items = order.getItems();
        this.status = OrderStatus.CONFIRMED;
    }

    public Order confirm(){
        return new Order(this);
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }
} 
