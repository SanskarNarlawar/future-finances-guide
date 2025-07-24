package com.llmapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    private String id;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    private String message;
    
    @JsonProperty("model_name")
    private String modelName;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("token_count")
    private Integer tokenCount;
    
    @JsonProperty("total_tokens")
    private Integer totalTokens;
    
    @JsonProperty("prompt_tokens")
    private Integer promptTokens;
    
    @JsonProperty("completion_tokens")
    private Integer completionTokens;
    
    @JsonProperty("finish_reason")
    private String finishReason;
}