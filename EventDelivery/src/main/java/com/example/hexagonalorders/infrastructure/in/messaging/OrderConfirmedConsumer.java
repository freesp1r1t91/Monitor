package com.example.hexagonalorders.infrastructure.in.messaging;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.hexagonalorders.application.event.OrderConfirmedIntegrationEvent;
import com.example.hexagonalorders.domain.port.in.DeliveryUseCase;
import com.example.hexagonalorders.domain.port.out.DeliveryNumberGenerator;
import com.example.hexagonalorders.infrastructure.in.web.dto.DeliveryDto;
import com.example.hexagonalorders.infrastructure.in.web.mapper.DeliveryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderConfirmedConsumer {
    private final ObjectMapper objectMapper;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryUseCase deliveryUseCase;
    private final DeliveryNumberGenerator deliveryNumberGenerator;

    @KafkaListener(topics = "hexagonal-orders-dev-order-confirmed", groupId = "order-consumer-group")
    public void listen(String message) {

        try{
            OrderConfirmedIntegrationEvent event = objectMapper.readValue(message, OrderConfirmedIntegrationEvent.class);
            DeliveryDto dto = new DeliveryDto(
                deliveryNumberGenerator.generate().value(),
                "Av. San Rafael Atlixco",
                "186",
                "Leyes de Reforma 1ra Secc",
                "Iztapalapa",
                "CDMX",
                "Mexico",
                LocalDateTime.now(),
                "RT-001",
                "REP-001",
                event.getOrder().getOrderNumber().value()
            );
            deliveryUseCase.createDelivery(deliveryMapper.toDomain(dto), event.getOrder());
            System.out.println("Received order:" + event.getOrder().getOrderNumber().value());
        }catch(Exception e){
            throw new RuntimeException("Failed to publish message from Kafka");
        }
    }
}