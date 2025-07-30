package com.example.hexagonalorders.infrastructure.out.messaging;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OutboxJpaMapper;
import com.example.hexagonalorders.infrastructure.out.persistence.repository.OutboxRepositoryAdapter;

@Component
public class OutboxProcessor {

    private final OutboxRepositoryAdapter outboxRepository;
    private final KafkaMessagePublisher messagePublisher;
    private final OutboxJpaMapper outboxJpaMapper;

    public OutboxProcessor(OutboxRepositoryAdapter outboxRepository,
        KafkaMessagePublisher messagePublisher, OutboxJpaMapper outboxJpaMapper) {
        this.outboxRepository = outboxRepository;
        this.messagePublisher = messagePublisher;
        this.outboxJpaMapper = outboxJpaMapper;
    }
    
    @Transactional
    @Scheduled(fixedDelayString = "${outbox.poll.ms:1000}")
    public void pollOutbox() {
        List<OutboxMessage> messages = outboxRepository.findPending(10);

        for(OutboxMessage message: messages){
            OutboxJpaEntity jpaMessage = outboxJpaMapper.toJpaEntity(message);
            outboxRepository.markProcessed(message.id());
            
            messagePublisher.publish("Order." + message.eventType(), jpaMessage.getPayload());
        }
    }
}
