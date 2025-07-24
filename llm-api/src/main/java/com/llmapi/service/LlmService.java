package com.llmapi.service;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.model.ChatMessage;

import java.util.List;

public interface LlmService {
    
    ChatResponse generateResponse(ChatRequest request);
    
    List<ChatMessage> getChatHistory(String sessionId);
    
    void clearChatHistory(String sessionId);
    
    ChatMessage saveMessage(String sessionId, ChatMessage.Role role, String content, Integer tokenCount);
}