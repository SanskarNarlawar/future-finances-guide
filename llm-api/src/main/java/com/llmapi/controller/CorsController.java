package com.llmapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CorsController {

    @RequestMapping(value = "/api/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }

    @GetMapping("/api/v1/cors-test")
    public ResponseEntity<Map<String, String>> corsTest() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "CORS test successful");
        response.put("message", "If you can see this, CORS is working correctly");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With")
                .body(response);
    }
}