package com.example.hexagonalorders.domain.model;

import java.time.Instant;
import java.util.UUID;

public record OutboxMessage(
    UUID id,
    String aggregateType,
    UUID aggregateId,
    String eventType,
    String payload,
    OutboxStatus status,
    Instant createdAt,
    Instant processedAt
    )   
{
    public static OutboxMessage createPendingMessage(
        String aggregateType,
        String aggregateId,
        String eventType,
        String payload
    ) {
        return new OutboxMessage(
            UUID.randomUUID(),       // genera un nuevo ID
            aggregateType,
            UUID.fromString(aggregateId),
            eventType,
            payload,
            OutboxStatus.PENDING,    // estado por defecto
            Instant.now(),           // marca de tiempo actual
            null                     // aún no procesado
        );
    }

    public static OutboxMessage createProcessedMessage(
        OutboxMessage message
    ) {
        return new OutboxMessage(
            message.id(), 
            message.aggregateType(),
            message.aggregateId(),       
            message.eventType(),
            message.payload(),          // mantiene el payload
            OutboxStatus.PROCESSED,     // marca como procesado
            message.createdAt(),        // mantiene el instante de creacion
            Instant.now()               // marca de tiempo procesado actual
        );
    }

    public static OutboxMessage createFailedMessage(
        OutboxMessage message
    ) {
        return new OutboxMessage(
            message.id(), 
            message.aggregateType(),
            message.aggregateId(),       
            message.eventType(),
            message.payload(),          // mantiene el payload
            OutboxStatus.FAILED,        // marca como fallido
            message.createdAt(),        // mantiene el instante de creacion
            Instant.now()               // marca de tiempo procesado actual
        );
    }
} 