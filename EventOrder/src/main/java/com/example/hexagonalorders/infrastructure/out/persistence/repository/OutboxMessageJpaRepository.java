package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxStatus;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutboxMessageJpaRepository extends JpaRepository<OutboxJpaEntity, Long> { 
    Optional<OutboxJpaEntity> findById(UUID id);
    List<OutboxJpaEntity> findByStatusOrderByCreatedAtAsc(OutboxStatus status, Pageable p);
}






