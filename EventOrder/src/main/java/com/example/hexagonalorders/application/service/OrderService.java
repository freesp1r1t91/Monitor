package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.exception.OrderNotFoundException;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementing the order-related use cases.
 * This class orchestrates domain logic and coordinates with output ports.
 * It is part of the application layer and is responsible for:
 * - Implementing use cases defined by the domain
 * - Coordinating between domain objects and services
 * - Managing transactions and use case flow
 */
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderValidationService orderValidationService;
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Validate the order using the domain service
        orderValidationService.validateOrder(order);
        
        OrderNumber orderNumber = orderNumberGenerator.generate();
        Order orderWithNumber = new Order(
            orderNumber,
            order.getCustomerId(),
            order.getOrderDate(),
            order.getItems(),
            order.getStatus()
        );
        Order savedOrder = orderRepository.save(orderWithNumber);

        // Process domain events - publish internally and persist to outbox
        for (DomainEvent event : savedOrder.getDomainEvents()) {
            // Publish internally via Spring's event system
            eventPublisher.publishEvent(event);// Persist to outbox for reliable processing
        }
        savedOrder.clearDomainEvents();
        return savedOrder;
    }

    @Override
    @Transactional
    public Order confirmOrder(OrderNumber orderNumber) {
        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException(orderNumber);
        }

        Order order = orderOpt.get();
        // Confirm the order (this will raise the OrderConfirmedEvent)
        Order confirmedOrder = order.confirm();
        // Save the updated order
        orderRepository.save(confirmedOrder);
        // Process domain events - publish internally
        for (DomainEvent event : confirmedOrder.getDomainEvents()) {
            // Publish internally via Spring's event system
            eventPublisher.publishEvent(event);
        }
        confirmedOrder.clearDomainEvents();
        return confirmedOrder;
    }

    @Override
    public Optional<Order> getOrder(OrderNumber orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }

} 