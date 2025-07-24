package com.llmapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    
    @NotBlank(message = "Message content cannot be blank")
    private String message;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("model_name")
    private String modelName = "gpt-3.5-turbo";
    
    @JsonProperty("max_tokens")
    private Integer maxTokens = 1000;
    
    private Double temperature = 0.7;
    
    @JsonProperty("top_p")
    private Double topP = 1.0;
    
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty = 0.0;
    
    @JsonProperty("presence_penalty")
    private Double presencePenalty = 0.0;
    
    private List<String> stop;
    
    @JsonProperty("stream")
    private Boolean stream = false;
}