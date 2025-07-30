package com.example.hexagonalorders.application.event;

import com.fasterxml.jackson.annotation.JsonFormat;import java.time.LocalDateTime;
import com.example.hexagonalorders.domain.model.Order;

/**
* Integration event for order confirmation.
* This event is used for external communication and should have a stable
contract.
*/
public class OrderConfirmedIntegrationEvent {
    private final Order order;
    private final String eventType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime confirmedAt;
    
    public OrderConfirmedIntegrationEvent(Order order) {
        this.order = order;
        this.eventType = "OrderConfirmed";
        this.confirmedAt = LocalDateTime.now();
    }

    public Order getOrder() {
        return order;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
}
