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
    
    // Financial Advisory specific fields
    @JsonProperty("financial_profile")
    private FinancialProfile financialProfile;
    
    @JsonProperty("advisory_mode")
    private AdvisoryMode advisoryMode = AdvisoryMode.GENERAL;
    
    @JsonProperty("include_disclaimers")
    private Boolean includeDisclaimers = true;
    
    @JsonProperty("focus_areas")
    private List<String> focusAreas;
    
    @JsonProperty("context_type")
    private ContextType contextType = ContextType.FINANCIAL_ADVISORY;
    
    // Enums for financial advisory
    public enum AdvisoryMode {
        GENERAL("General financial advice"),
        INVESTMENT_FOCUSED("Investment-focused advice"),
        BUDGETING("Budgeting and expense management"),
        RETIREMENT_PLANNING("Retirement planning"),
        DEBT_MANAGEMENT("Debt management"),
        TAX_PLANNING("Tax planning"),
        INSURANCE_PLANNING("Insurance planning"),
        EMERGENCY_FUND("Emergency fund planning");
        
        private final String description;
        
        AdvisoryMode(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum ContextType {
        FINANCIAL_ADVISORY("Financial Advisory Chat"),
        GENERAL_CHAT("General Chat"),
        EDUCATIONAL("Educational Content"),
        MARKET_ANALYSIS("Market Analysis");
        
        private final String description;
        
        ContextType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}