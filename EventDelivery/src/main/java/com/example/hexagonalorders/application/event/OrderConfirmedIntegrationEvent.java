package com.example.hexagonalorders.application.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
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
    
    @JsonCreator
    public OrderConfirmedIntegrationEvent(
            @JsonProperty("order") Order order,
            @JsonProperty("eventType") String eventType,
            @JsonProperty("confirmedAt") LocalDateTime confirmedAt
    ) {
        this.order = order;
        this.eventType = eventType;
        this.confirmedAt = confirmedAt;
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
