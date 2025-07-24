package com.llmapi.controller;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.model.ChatMessage;
import com.llmapi.service.LlmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "🤖 Basic LLM API", description = "Core Large Language Model API endpoints for basic chat functionality")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LlmController {

    private final LlmService llmService;
    
    @PostMapping("/ask")
    @Operation(
        summary = "🚀 Ask Any Question - Super Simple",
        description = "**SIMPLEST ENDPOINT** - Just send your question as plain text and get detailed financial advice. No JSON, no complex setup!",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Just your question as plain text",
            required = true,
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Simple Question",
                        summary = "Just ask your question",
                        value = """
                        {
                          "question": "I want to buy a house. What should I know about home loans?"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Investment Question", 
                        summary = "Ask about investments",
                        value = """
                        {
                          "question": "Should I invest in mutual funds or stocks?"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "✅ Simple response with detailed advice",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponse.class)
            )
        )
    })
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody Map<String, String> request) {
        try {
            String question = request.get("question");
            if (question == null || question.trim().isEmpty()) {
                question = "Hello! How can I help you with your financial questions today?";
            }
            
            // Create a simple ChatRequest
            ChatRequest chatRequest = new ChatRequest();
            chatRequest.setMessage(question);
            chatRequest.setSessionId("simple-" + System.currentTimeMillis());
            chatRequest.setModelName("gpt-3.5-turbo");
            chatRequest.setMaxTokens(1500);
            chatRequest.setTemperature(0.7);
            
            ChatResponse response = llmService.generateResponse(chatRequest);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error in simple ask endpoint", e);
            ChatResponse errorResponse = ChatResponse.builder()
                    .id("error-" + System.currentTimeMillis())
                    .sessionId("error-session")
                    .message("I apologize, but I'm having trouble processing your question right now. Please try again or contact support.")
                    .modelName("error-handler")
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            return ResponseEntity.ok(errorResponse);
        }
    }

    @PostMapping("/simple-chat")
    @Operation(
        summary = "💬 Simple Chat - Ask Anything",
        description = "**EASIEST ENDPOINT** - Just send any message and get a response. No complex setup required - perfect for quick questions!",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Chat request with message and configuration",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Basic Chat",
                        summary = "Simple chat message",
                        value = """
                        {
                          "message": "Hello, how are you today?",
                          "session_id": "user-session-123",
                          "model_name": "gpt-3.5-turbo",
                          "max_tokens": 1000,
                          "temperature": 0.7
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Advanced Chat",
                        summary = "Chat with advanced parameters",
                        value = """
                        {
                          "message": "Explain quantum computing in simple terms",
                          "session_id": "advanced-session-456",
                          "model_name": "gpt-4",
                          "max_tokens": 1500,
                          "temperature": 0.5,
                          "top_p": 0.9,
                          "frequency_penalty": 0.1,
                          "presence_penalty": 0.1
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chat response generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                      "id": "resp-123456789",
                      "session_id": "user-session-123",
                      "message": "Hello! I'm doing well, thank you for asking. How can I help you today?",
                      "model_name": "gpt-3.5-turbo",
                      "created_at": "2024-01-15T10:30:00",
                      "token_count": 15,
                      "total_tokens": 25,
                      "prompt_tokens": 10,
                      "completion_tokens": 15
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "message": "Sorry, I encountered an error processing your request: Connection timeout"
                    }
                    """
                )
            )
        )
    })
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
    @Operation(
        summary = "Get chat history",
        description = "Retrieve the complete conversation history for a specific session",
        parameters = @Parameter(
            name = "sessionId",
            description = "The session ID to retrieve history for",
            required = true,
            example = "user-session-123"
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chat history retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatMessage.class),
                examples = @ExampleObject(
                    name = "Chat History",
                    value = """
                    [
                      {
                        "id": 1,
                        "session_id": "user-session-123",
                        "role": "USER",
                        "content": "Hello, how are you?",
                        "created_at": "2024-01-15T10:30:00",
                        "token_count": 5
                      },
                      {
                        "id": 2,
                        "session_id": "user-session-123",
                        "role": "ASSISTANT",
                        "content": "Hello! I'm doing well, thank you for asking. How can I help you today?",
                        "created_at": "2024-01-15T10:30:05",
                        "token_count": 15
                      }
                    ]
                    """
                )
            )
        )
    })
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        log.info("Retrieving chat history for session: {}", sessionId);
        List<ChatMessage> history = llmService.getChatHistory(sessionId);
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/chat/history/{sessionId}")
    @Operation(
        summary = "Clear chat history",
        description = "Delete all conversation history for a specific session",
        parameters = @Parameter(
            name = "sessionId",
            description = "The session ID to clear history for",
            required = true,
            example = "user-session-123"
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Chat history cleared successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "message": "Chat history cleared for session: user-session-123"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> clearChatHistory(
            @Parameter(description = "Session ID") @PathVariable String sessionId) {
        log.info("Clearing chat history for session: {}", sessionId);
        llmService.clearChatHistory(sessionId);
        return ResponseEntity.ok(Map.of("message", "Chat history cleared for session: " + sessionId));
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Check if the LLM API service is running and healthy"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is healthy",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "status": "healthy",
                  "service": "LLM API",
                  "version": "1.0.0"
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "service", "LLM API",
                "version", "1.0.0"
        ));
    }

    @GetMapping("/models")
    @Operation(
        summary = "List available models",
        description = "Get a list of all available LLM models with their descriptions and capabilities"
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of available models",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "models": [
                    {
                      "id": "gpt-3.5-turbo",
                      "name": "GPT-3.5 Turbo",
                      "description": "Fast and efficient model for most tasks"
                    },
                    {
                      "id": "gpt-4",
                      "name": "GPT-4",
                      "description": "More capable model with better reasoning"
                    },
                    {
                      "id": "gpt-4-turbo",
                      "name": "GPT-4 Turbo",
                      "description": "Latest GPT-4 model with improved performance"
                    }
                  ],
                  "default": "gpt-3.5-turbo"
                }
                """
            )
        )
    )
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