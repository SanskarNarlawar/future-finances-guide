package com.llmapi.service;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.dto.FinancialProfile;

public interface FinancialAdvisoryService {
    
    /**
     * Generate personalized financial advice based on user profile and query
     */
    ChatResponse generateFinancialAdvice(ChatRequest request);
    
    /**
     * Create a personalized system prompt based on user's financial profile
     */
    String createPersonalizedSystemPrompt(FinancialProfile profile, ChatRequest.AdvisoryMode mode);
    
    /**
     * Validate financial profile completeness for advisory
     */
    boolean isProfileComplete(FinancialProfile profile);
    
    /**
     * Generate investment recommendations based on age and risk tolerance
     */
    String generateInvestmentRecommendations(FinancialProfile profile);
    
    /**
     * Create age-appropriate financial goals and milestones
     */
    String generateAgeBasedGoals(Integer age, FinancialProfile profile);
    
    /**
     * Generate personalized budgeting advice
     */
    String generateBudgetingAdvice(FinancialProfile profile);
    
    /**
     * Create retirement planning suggestions
     */
    String generateRetirementPlan(FinancialProfile profile);
}