package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxStatus;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OutboxJpaMapper;

@Component
public class OutboxRepositoryAdapter implements OutboxRepository{
    private final OutboxMessageJpaRepository outboxMessageJpaRepository;
    private final OutboxJpaMapper outboxJpaMapper; 

    public OutboxRepositoryAdapter(OutboxMessageJpaRepository outboxMessageJpaRepository, OutboxJpaMapper outboxJpaMapper) {
        this.outboxMessageJpaRepository = outboxMessageJpaRepository;
        this.outboxJpaMapper = outboxJpaMapper;
    }

    @Override
    public OutboxMessage save(OutboxMessage message){
        return outboxJpaMapper.toDomain(outboxMessageJpaRepository.save(outboxJpaMapper.toJpaEntity(message)));
    }

    @Override
    public List<OutboxMessage> findPending(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        
        List<OutboxJpaEntity> result = outboxMessageJpaRepository
            .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING, pageable);

        return result.stream()
            .map(outboxJpaMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void markProcessed(UUID id) {
        
        try{
            OutboxJpaEntity outboxJpaEntity = outboxMessageJpaRepository.findById(id).get();
            OutboxMessage message = OutboxMessage.createProcessedMessage(outboxJpaMapper.toDomain(outboxJpaEntity));
            outboxMessageJpaRepository.save(outboxJpaMapper.toJpaEntity(message));
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("El mensage no existe");
        }
    }

    @Override
    public void markFailed(UUID id) {
        OutboxJpaEntity outboxJpaEntity = outboxMessageJpaRepository.findById(id).get();
        try{
            outboxJpaEntity.setStatus(OutboxStatus.FAILED);
            outboxJpaEntity.setProcessedAt(Instant.now());
            outboxMessageJpaRepository.save(outboxJpaEntity);

        }catch(NoSuchElementException e){
            throw new NoSuchElementException("El mensage no existe");
        }
    }
    
}