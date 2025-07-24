package com.llmapi.service.impl;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.model.ChatMessage;
import com.llmapi.repository.ChatMessageRepository;
import com.llmapi.service.LlmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final ChatMessageRepository chatMessageRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${llm.openai.api-key:}")
    private String openaiApiKey;

    @Value("${llm.openai.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Override
    @Transactional
    public ChatResponse generateResponse(ChatRequest request) {
        try {
            // Generate session ID if not provided
            String sessionId = request.getSessionId() != null ? 
                request.getSessionId() : UUID.randomUUID().toString();

            // Save user message
            ChatMessage userMessage = saveMessage(sessionId, ChatMessage.Role.USER, 
                request.getMessage(), estimateTokenCount(request.getMessage()));

            // Get conversation history for context
            List<ChatMessage> history = getChatHistory(sessionId);

            // Call LLM API (OpenAI compatible)
            String response = callLlmApi(request, history);

            // Save assistant response
            ChatMessage assistantMessage = saveMessage(sessionId, ChatMessage.Role.ASSISTANT, 
                response, estimateTokenCount(response));

            return ChatResponse.builder()
                    .id(UUID.randomUUID().toString())
                    .sessionId(sessionId)
                    .message(response)
                    .modelName(request.getModelName())
                    .createdAt(LocalDateTime.now())
                    .tokenCount(assistantMessage.getTokenCount())
                    .finishReason("stop")
                    .build();

        } catch (Exception e) {
            log.error("Error generating LLM response", e);
            throw new RuntimeException("Failed to generate response: " + e.getMessage());
        }
    }

    @Override
    public List<ChatMessage> getChatHistory(String sessionId) {
        return chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    @Override
    @Transactional
    public void clearChatHistory(String sessionId) {
        chatMessageRepository.deleteBySessionId(sessionId);
    }

    @Override
    @Transactional
    public ChatMessage saveMessage(String sessionId, ChatMessage.Role role, String content, Integer tokenCount) {
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setRole(role);
        message.setContent(content);
        message.setTokenCount(tokenCount);
        return chatMessageRepository.save(message);
    }

    private String callLlmApi(ChatRequest request, List<ChatMessage> history) {
        // If no OpenAI API key is provided, return a mock response
        if (openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            return generateMockResponse(request.getMessage());
        }

        try {
            WebClient client = webClientBuilder
                    .baseUrl(openaiBaseUrl)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + openaiApiKey)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            // Build messages array for OpenAI API
            List<Map<String, String>> messages = history.stream()
                    .map(msg -> Map.of(
                            "role", msg.getRole().name().toLowerCase(),
                            "content", msg.getContent()
                    ))
                    .toList();

            // Add current message
            messages.add(Map.of("role", "user", "content", request.getMessage()));

            Map<String, Object> requestBody = Map.of(
                    "model", request.getModelName(),
                    "messages", messages,
                    "max_tokens", request.getMaxTokens(),
                    "temperature", request.getTemperature(),
                    "top_p", request.getTopP(),
                    "frequency_penalty", request.getFrequencyPenalty(),
                    "presence_penalty", request.getPresencePenalty()
            );

            Mono<Map> response = client.post()
                    .uri("/chat/completions")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, Object> result = response.block();
            if (result != null && result.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) choice.get("message");
                    return message.get("content");
                }
            }

            return "I apologize, but I'm unable to generate a response at the moment.";

        } catch (Exception e) {
            log.error("Error calling LLM API", e);
            return generateMockResponse(request.getMessage());
        }
    }

    private String generateMockResponse(String userMessage) {
        // Simple mock responses for demonstration
        if (userMessage.toLowerCase().contains("hello") || userMessage.toLowerCase().contains("hi")) {
            return "Hello! How can I assist you today?";
        } else if (userMessage.toLowerCase().contains("weather")) {
            return "I'm sorry, I don't have access to real-time weather data. You might want to check a weather service for current conditions.";
        } else if (userMessage.toLowerCase().contains("time")) {
            return "I don't have access to real-time information, but the current server time is approximately " + LocalDateTime.now().toString();
        } else {
            return "Thank you for your message: \"" + userMessage + "\". This is a mock response since no LLM API key is configured. " +
                   "To enable real LLM responses, please configure the llm.openai.api-key property.";
        }
    }

    private Integer estimateTokenCount(String text) {
        // Simple token estimation (roughly 4 characters per token)
        return Math.max(1, text.length() / 4);
    }
}