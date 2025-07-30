package com.example.hexagonalorders.domain.port.out;

import com.example.hexagonalorders.domain.model.OutboxMessage;

import java.util.List;
import java.util.UUID;

/**
 * Output port defining the contract for Outbox persistence.
 * This interface is part of the domain layer and defines how
 * the application core interacts with the persistence mechanism.
 */
public interface OutboxRepository {
    /**
     * Saves an OutboxMessage
     * @param OutboxMessage the OutboxMessage to save
     * @return the saved OutboxMessage
     */
    OutboxMessage save(OutboxMessage message);

     /**
     * Finds a list of pending OutboxMessage with limit size
     * @param limit the maximum number of pending messages
     * @return the list of pending OutboxMessage
     */   
    List<OutboxMessage> findPending(int limit);

    /**
    * Mark an OutboxMessage as processed
    * @param id the id number of processed OutboxMessage
    */ 
    void markProcessed(UUID id);
    
    /**
    * Mark an OutboxMessage as failed
    * @param id the id number of failed OutboxMessage
    */ 
    void markFailed(UUID id);
} 