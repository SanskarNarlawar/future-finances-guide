package com.llmapi.controller;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.model.ChatMessage;
import com.llmapi.service.LlmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/llm")
@RequiredArgsConstructor
@Tag(name = "LLM API", description = "Large Language Model API endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LlmController {

    private final LlmService llmService;

    @PostMapping("/chat")
    @Operation(summary = "Generate chat response", description = "Send a message to the LLM and get a response")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        log.info("Received chat request for session: {}", request.getSessionId());
        try {
            ChatResponse response = llmService.generateResponse(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing chat request", e);
            return ResponseEntity.internalServerError()
                    .body(ChatResponse.builder()
                            .message("Sorry, I encountered an error processing your request: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/chat/history/{sessionId}")
    @Operation(summary = "Get chat history", description = "Retrieve the conversation history for a session")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        log.info("Retrieving chat history for session: {}", sessionId);
        List<ChatMessage> history = llmService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/chat/history/{sessionId}")
    @Operation(summary = "Clear chat history", description = "Clear the conversation history for a session")
    public ResponseEntity<Map<String, String>> clearChatHistory(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        log.info("Clearing chat history for session: {}", sessionId);
        llmService.clearChatHistory(sessionId);
        return ResponseEntity.ok(Map.of("message", "Chat history cleared for session: " + sessionId));
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the LLM API is running")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "service", "LLM API",
                "version", "1.0.0"
        ));
    }

    @GetMapping("/models")
    @Operation(summary = "List available models", description = "Get list of available LLM models")
    public ResponseEntity<Map<String, Object>> getModels() {
        List<Map<String, String>> models = List.of(
                Map.of("id", "gpt-3.5-turbo", "name", "GPT-3.5 Turbo", "description", "Fast and efficient model"),
                Map.of("id", "gpt-4", "name", "GPT-4", "description", "More capable model with better reasoning"),
                Map.of("id", "gpt-4-turbo", "name", "GPT-4 Turbo", "description", "Latest GPT-4 model with improved performance")
        );
        
        return ResponseEntity.ok(Map.of(
                "models", models,
                "default", "gpt-3.5-turbo"
        ));
    }
}