package com.llmapi.repository;

import com.llmapi.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findBySessionIdOrderByCreatedAtAsc(String sessionId);
    
    @Query("SELECT c FROM ChatMessage c WHERE c.sessionId = :sessionId ORDER BY c.createdAt DESC LIMIT :limit")
    List<ChatMessage> findRecentMessagesBySessionId(@Param("sessionId") String sessionId, @Param("limit") int limit);
    
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.sessionId = :sessionId")
    long countBySessionId(@Param("sessionId") String sessionId);
    
    void deleteBySessionId(String sessionId);
}