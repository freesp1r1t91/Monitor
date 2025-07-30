package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import java.time.Instant;
import org.springframework.stereotype.Component;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class OutboxJpaMapper {

    private final ObjectMapper mapper;

    public OutboxJpaMapper() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public OutboxJpaEntity toJpaEntity(OutboxMessage message){ 
        OutboxJpaEntity jpaEntity = new OutboxJpaEntity();
        jpaEntity.setId(message.id());
        jpaEntity.setAggregateType(message.aggregateType());
        jpaEntity.setAggregateId(message.aggregateId());
        jpaEntity.setEventType(message.eventType());
        jpaEntity.setPayload(message.payload());
        jpaEntity.setStatus(toJpaOutboxStatus(message.status()));
        jpaEntity.setCreatedAt(Instant.now());
        jpaEntity.setProcessedAt(message.processedAt());
            
        return jpaEntity;
    }

    public OutboxMessage toDomain(OutboxJpaEntity jpaEntity) {
        return new OutboxMessage(
            jpaEntity.getId(),
            jpaEntity.getAggregateType(),
            jpaEntity.getAggregateId(),
            jpaEntity.getEventType(),
            jpaEntity.getPayload(),
            toDomainOutboxStatus(jpaEntity.getStatus()),
            jpaEntity.getCreatedAt(),
            jpaEntity.getProcessedAt()
        );
    }

    private com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxStatus toJpaOutboxStatus(com.example.hexagonalorders.domain.model.OutboxStatus status) {
        return com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxStatus.valueOf(status.name());
    }

    private com.example.hexagonalorders.domain.model.OutboxStatus toDomainOutboxStatus(com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxStatus status) {
        return com.example.hexagonalorders.domain.model.OutboxStatus.valueOf(status.name());
    }
}
