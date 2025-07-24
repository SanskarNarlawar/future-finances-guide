package com.llmapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @GetMapping("/deployment")
    public ResponseEntity<Map<String, Object>> testDeployment() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Deployment test successful - v2.0-cors-fixed");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("version", "v2.0-cors-fixed");
        response.put("environment", "App Engine");
        response.put("cors_enabled", true);
        
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With, X-App-Version")
                .body(response);
    }

    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> testEcho(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Echo test successful - received your data");
        response.put("received_data", request);
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("version", "v2.0-cors-fixed");
        
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With, X-App-Version")
                .body(response);
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleTestOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With, X-App-Version")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }
}