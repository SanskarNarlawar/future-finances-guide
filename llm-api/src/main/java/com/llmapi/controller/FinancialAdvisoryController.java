package com.llmapi.controller;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.dto.FinancialProfile;
import com.llmapi.service.FinancialAdvisoryService;
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
@RequestMapping("/api/v1/financial-advisor")
@RequiredArgsConstructor
@Tag(name = "Financial Advisory API", description = "Personalized financial advisory chatbot endpoints")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinancialAdvisoryController {

    private final FinancialAdvisoryService financialAdvisoryService;

    @PostMapping("/chat")
    @Operation(summary = "Get personalized financial advice", 
               description = "Send a message with financial profile to get personalized advice based on age and interests")
    public ResponseEntity<ChatResponse> getFinancialAdvice(
            @Valid @RequestBody ChatRequest request) {
        
        log.info("Received financial advisory request for session: {}", request.getSessionId());
        
        try {
            ChatResponse response = financialAdvisoryService.generateFinancialAdvice(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating financial advice: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(ChatResponse.builder()
                    .message("I apologize, but I'm having trouble processing your request right now. Please try again later.")
                    .sessionId(request.getSessionId())
                    .build());
        }
    }

    @PostMapping("/investment-recommendations")
    @Operation(summary = "Get investment recommendations", 
               description = "Get personalized investment recommendations based on age, risk tolerance, and interests")
    public ResponseEntity<Map<String, Object>> getInvestmentRecommendations(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating investment recommendations for age: {}", profile.getAge());
        
        try {
            String recommendations = financialAdvisoryService.generateInvestmentRecommendations(profile);
            
            return ResponseEntity.ok(Map.of(
                "recommendations", recommendations,
                "profile_complete", financialAdvisoryService.isProfileComplete(profile),
                "age", profile.getAge() != null ? profile.getAge() : "Not provided"
            ));
        } catch (Exception e) {
            log.error("Error generating investment recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate recommendations at this time"));
        }
    }

    @GetMapping("/age-based-goals/{age}")
    @Operation(summary = "Get age-appropriate financial goals", 
               description = "Get financial milestones and goals appropriate for the specified age")
    public ResponseEntity<Map<String, Object>> getAgeBasedGoals(
            @Parameter(description = "User's age") @PathVariable Integer age,
            @RequestParam(required = false) String sessionId) {
        
        log.info("Generating age-based goals for age: {}", age);
        
        try {
            // Create a minimal profile with just age for goals generation
            FinancialProfile profile = FinancialProfile.builder().age(age).build();
            String goals = financialAdvisoryService.generateAgeBasedGoals(age, profile);
            
            return ResponseEntity.ok(Map.of(
                "age", age,
                "goals", goals,
                "session_id", sessionId != null ? sessionId : "N/A"
            ));
        } catch (Exception e) {
            log.error("Error generating age-based goals: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate goals at this time"));
        }
    }

    @PostMapping("/budgeting-advice")
    @Operation(summary = "Get personalized budgeting advice", 
               description = "Get budgeting strategies based on age and financial situation")
    public ResponseEntity<Map<String, Object>> getBudgetingAdvice(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating budgeting advice for age: {}", profile.getAge());
        
        try {
            String advice = financialAdvisoryService.generateBudgetingAdvice(profile);
            
            return ResponseEntity.ok(Map.of(
                "advice", advice,
                "age_group", getAgeGroup(profile.getAge()),
                "profile_complete", financialAdvisoryService.isProfileComplete(profile)
            ));
        } catch (Exception e) {
            log.error("Error generating budgeting advice: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate budgeting advice at this time"));
        }
    }

    @PostMapping("/retirement-plan")
    @Operation(summary = "Get retirement planning advice", 
               description = "Get personalized retirement planning strategy based on age and financial profile")
    public ResponseEntity<Map<String, Object>> getRetirementPlan(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating retirement plan for age: {}", profile.getAge());
        
        try {
            String plan = financialAdvisoryService.generateRetirementPlan(profile);
            
            return ResponseEntity.ok(Map.of(
                "retirement_plan", plan,
                "current_age", profile.getAge(),
                "target_retirement_age", profile.getRetirementAgeTarget() != null ? profile.getRetirementAgeTarget() : 65,
                "years_to_retirement", calculateYearsToRetirement(profile)
            ));
        } catch (Exception e) {
            log.error("Error generating retirement plan: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate retirement plan at this time"));
        }
    }

    @PostMapping("/profile/validate")
    @Operation(summary = "Validate financial profile completeness", 
               description = "Check if the financial profile has sufficient information for personalized advice")
    public ResponseEntity<Map<String, Object>> validateProfile(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Validating financial profile completeness");
        
        boolean isComplete = financialAdvisoryService.isProfileComplete(profile);
        
        return ResponseEntity.ok(Map.of(
            "is_complete", isComplete,
            "age_provided", profile.getAge() != null,
            "risk_tolerance_provided", profile.getRiskTolerance() != null,
            "investment_experience_provided", profile.getInvestmentExperience() != null,
            "income_range_provided", profile.getIncomeRange() != null,
            "recommendations", isComplete ? 
                "Profile is complete for personalized advice" : 
                "Please provide age, risk tolerance, investment experience, and income range for better recommendations"
        ));
    }

    @GetMapping("/advisory-modes")
    @Operation(summary = "Get available advisory modes", 
               description = "List all available advisory modes and their descriptions")
    public ResponseEntity<Map<String, Object>> getAdvisoryModes() {
        
        return ResponseEntity.ok(Map.of(
            "advisory_modes", Map.of(
                "GENERAL", "General financial advice",
                "INVESTMENT_FOCUSED", "Investment-focused advice",
                "BUDGETING", "Budgeting and expense management",
                "RETIREMENT_PLANNING", "Retirement planning",
                "DEBT_MANAGEMENT", "Debt management",
                "TAX_PLANNING", "Tax planning",
                "INSURANCE_PLANNING", "Insurance planning",
                "EMERGENCY_FUND", "Emergency fund planning"
            ),
            "default_mode", "GENERAL"
        ));
    }

    @GetMapping("/profile/template")
    @Operation(summary = "Get financial profile template", 
               description = "Get a template showing all available profile fields and their options")
    public ResponseEntity<Map<String, Object>> getProfileTemplate() {
        
        Map<String, Object> requiredFields = Map.of(
            "age", "Integer (18-100)",
            "risk_tolerance", "CONSERVATIVE, MODERATE, AGGRESSIVE",
            "investment_experience", "BEGINNER, INTERMEDIATE, ADVANCED",
            "income_range", "BELOW_25K, RANGE_25K_50K, RANGE_50K_75K, RANGE_75K_100K, RANGE_100K_150K, ABOVE_150K"
        );
        
        Map<String, Object> optionalFields = Map.of(
            "financial_goals", "Array of: RETIREMENT, EMERGENCY_FUND, HOME_PURCHASE, EDUCATION, etc.",
            "interests", "Array of strings (e.g., ['technology', 'healthcare', 'environment'])",
            "current_savings", "BigDecimal",
            "monthly_expenses", "BigDecimal",
            "debt_amount", "BigDecimal",
            "employment_status", "EMPLOYED_FULL_TIME, EMPLOYED_PART_TIME, SELF_EMPLOYED, etc.",
            "marital_status", "SINGLE, MARRIED, DIVORCED, WIDOWED",
            "number_of_dependents", "Integer",
            "retirement_age_target", "Integer",
            "preferred_investment_types", "Array of investment types"
        );
        
        Map<String, Object> response = Map.of(
            "required_fields", requiredFields,
            "optional_fields", optionalFields,
            "example_interests", List.of(
                "technology", "healthcare", "environment", "real estate", 
                "travel", "education", "renewable energy", "artificial intelligence"
            )
        );
        
        return ResponseEntity.ok(response);
    }

    private String getAgeGroup(Integer age) {
        if (age == null) return "Unknown";
        if (age < 30) return "Young Adult (20s)";
        if (age < 40) return "Early Career (30s)";
        if (age < 50) return "Mid Career (40s)";
        if (age < 60) return "Pre-Retirement (50s)";
        return "Retirement Age (60+)";
    }

    private Integer calculateYearsToRetirement(FinancialProfile profile) {
        if (profile.getAge() == null) return null;
        
        int targetRetirementAge = profile.getRetirementAgeTarget() != null ? 
            profile.getRetirementAgeTarget() : 65;
        
        return Math.max(0, targetRetirementAge - profile.getAge());
    }
}