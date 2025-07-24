package com.llmapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    /**
     * Serve the main UI page from the root path
     */
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    /**
     * Handle client-side routing - redirect all non-API routes to index.html
     * This allows the single-page application to handle its own routing
     */
    @GetMapping(value = {"/chat", "/advisor", "/financial-advisor"})
    public String clientRoutes() {
        return "index.html";
    }
}