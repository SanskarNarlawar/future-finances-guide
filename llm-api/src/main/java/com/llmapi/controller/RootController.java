package com.llmapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "LLM API");
        response.put("status", "running");
        response.put("version", "1.0.0");
        response.put("description", "Large Language Model API with Financial Advisory Services");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("swagger", "/swagger-ui/index.html");
        endpoints.put("api-docs", "/v3/api-docs");
        endpoints.put("llm-chat", "/api/v1/llm/chat");
        endpoints.put("financial-advisor", "/api/v1/financial-advisor/comprehensive-guidance");
        
        response.put("endpoints", endpoints);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "llm-api");
        status.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(status);
    }
}