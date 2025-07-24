package com.llmapi.controller;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.dto.FinancialProfile;
import com.llmapi.service.FinancialAdvisoryService;
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
@RequestMapping("/api/v1/financial-advisor")
@RequiredArgsConstructor
@Tag(name = "💰 Financial Advisory API", description = "Personalized financial advisory chatbot endpoints based on age and interests")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinancialAdvisoryController {

    private final FinancialAdvisoryService financialAdvisoryService;

    @PostMapping("/chat")
    @Operation(
        summary = "Get personalized financial advice",
        description = "Send a message with financial profile to get personalized advice based on age, interests, and risk tolerance",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Chat request with financial profile for personalized advice",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Young Tech Professional",
                        summary = "28-year-old tech worker seeking investment advice",
                        value = """
                        {
                          "message": "I'm 28 and work in tech. I want to start investing but don't know where to begin. What should I focus on?",
                          "session_id": "young-professional-session",
                          "advisory_mode": "INVESTMENT_FOCUSED",
                          "financial_profile": {
                            "age": 28,
                            "income_range": "RANGE_75K_100K",
                            "risk_tolerance": "MODERATE",
                            "investment_experience": "BEGINNER",
                            "interests": ["technology", "artificial intelligence", "startups"],
                            "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
                            "employment_status": "EMPLOYED_FULL_TIME",
                            "marital_status": "SINGLE",
                            "current_savings": 25000,
                            "monthly_expenses": 4000
                          }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Mid-Career Parent",
                        summary = "38-year-old parent planning for children's education",
                        value = """
                        {
                          "message": "I have two kids and want to start saving for their college education. What are my best options?",
                          "session_id": "parent-session",
                          "advisory_mode": "GENERAL",
                          "financial_profile": {
                            "age": 38,
                            "income_range": "RANGE_100K_150K",
                            "risk_tolerance": "MODERATE",
                            "investment_experience": "INTERMEDIATE",
                            "interests": ["education", "family planning", "healthcare"],
                            "financial_goals": ["CHILD_EDUCATION", "RETIREMENT", "INSURANCE"],
                            "employment_status": "EMPLOYED_FULL_TIME",
                            "marital_status": "MARRIED",
                            "number_of_dependents": 2,
                            "current_savings": 120000,
                            "monthly_expenses": 8000
                          }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Pre-Retiree",
                        summary = "57-year-old planning for retirement",
                        value = """
                        {
                          "message": "I'm 8 years from retirement but still have a mortgage. Should I pay it off early or keep investing?",
                          "session_id": "pre-retiree-session",
                          "advisory_mode": "RETIREMENT_PLANNING",
                          "financial_profile": {
                            "age": 57,
                            "income_range": "ABOVE_150K",
                            "risk_tolerance": "CONSERVATIVE",
                            "investment_experience": "ADVANCED",
                            "interests": ["real estate", "conservative investments", "travel"],
                            "financial_goals": ["RETIREMENT", "DEBT_PAYOFF"],
                            "employment_status": "EMPLOYED_FULL_TIME",
                            "marital_status": "MARRIED",
                            "debt_amount": 180000,
                            "current_savings": 650000,
                            "retirement_age_target": 65
                          }
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
            description = "Personalized financial advice generated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": "financial-advice-123",
                      "session_id": "young-professional-session",
                      "message": "Great question! At 28 with your tech background and moderate risk tolerance, here's my personalized advice:\\n\\n**Investment Strategy for Your Age:**\\n- Focus on growth investments (70-80% stocks, 20-30% bonds)\\n- Start with low-cost index funds like S&P 500\\n- Consider tech sector ETFs given your interests\\n\\n**Given Your Interests:**\\n- Technology sector ETFs (QQQ, XLK)\\n- AI and innovation-focused funds\\n- Consider ESG funds for sustainable tech\\n\\n**Action Steps:**\\n1. Build emergency fund (3-6 months expenses)\\n2. Maximize employer 401(k) match\\n3. Open Roth IRA for tax-free growth\\n4. Start with $500/month in diversified index funds\\n\\n⚠️ **Important Disclaimer**: This advice is for educational purposes only. Please consult with a qualified financial advisor for decisions specific to your situation.",
                      "model_name": "gpt-3.5-turbo",
                      "created_at": "2024-01-15T10:30:00",
                      "token_count": 180
                    }
                    """
                )
            )
        )
    })
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
    @Operation(
        summary = "Get investment recommendations",
        description = "Get personalized investment recommendations based on age, risk tolerance, and personal interests",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for investment recommendations",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FinancialProfile.class),
                examples = {
                    @ExampleObject(
                        name = "Young Tech Professional",
                        summary = "28-year-old with tech interests",
                        value = """
                        {
                          "age": 28,
                          "income_range": "RANGE_75K_100K",
                          "risk_tolerance": "MODERATE",
                          "investment_experience": "BEGINNER",
                          "interests": ["technology", "artificial intelligence", "renewable energy"],
                          "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "SINGLE"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Healthcare Professional",
                        summary = "42-year-old healthcare worker",
                        value = """
                        {
                          "age": 42,
                          "income_range": "RANGE_100K_150K",
                          "risk_tolerance": "CONSERVATIVE",
                          "investment_experience": "INTERMEDIATE",
                          "interests": ["healthcare", "education", "family"],
                          "financial_goals": ["RETIREMENT", "CHILD_EDUCATION", "INSURANCE"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "MARRIED",
                          "number_of_dependents": 2,
                          "current_savings": 150000,
                          "monthly_expenses": 8000
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Investment recommendations generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "recommendations": "Based on your age (28), here's a suggested asset allocation:\\n- Stocks/Equity: 72%\\n- Bonds/Fixed Income: 28%\\n\\nWith moderate risk tolerance, consider:\\n- Diversified index funds (S&P 500, Total Stock Market)\\n- Mix of growth and value stocks\\n- International diversification\\n\\nBased on your interests (technology, artificial intelligence, renewable energy), you might consider:\\n- Technology sector ETFs (QQQ, XLK)\\n- ESG (Environmental, Social, Governance) funds\\n- Diversified funds aligned with your interests",
                  "profile_complete": true,
                  "age": 28
                }
                """
            )
        )
    )
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
    @Operation(
        summary = "Get age-appropriate financial goals",
        description = "Get financial milestones and goals appropriate for the specified age group",
        parameters = @Parameter(
            name = "age",
            description = "User's age (18-100)",
            required = true,
            example = "28"
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Age-based goals generated successfully",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Age 28 Goals",
                        summary = "Financial goals for 28-year-old",
                        value = """
                        {
                          "age": 28,
                          "goals": "Financial milestones for your age (28):\\n\\nIn Your 20s - Foundation Building:\\n- Build emergency fund (3-6 months expenses)\\n- Start investing early (even small amounts)\\n- Maximize employer 401(k) match\\n- Pay off high-interest debt\\n- Build good credit history",
                          "session_id": "N/A"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Age 45 Goals",
                        summary = "Financial goals for 45-year-old",
                        value = """
                        {
                          "age": 45,
                          "goals": "Financial milestones for your age (45):\\n\\nIn Your 40s - Peak Earning Years:\\n- Target 3x annual salary in retirement savings by 40\\n- Maximize retirement account contributions\\n- Focus on children's education funding\\n- Consider long-term care insurance\\n- Review estate planning documents",
                          "session_id": "N/A"
                        }
                        """
                    )
                }
            )
        )
    })
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
    @Operation(
        summary = "Get personalized budgeting advice",
        description = "Get budgeting strategies and expense management advice based on age and financial situation",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for budgeting advice",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Young Adult Profile",
                    value = """
                    {
                      "age": 24,
                      "income_range": "RANGE_50K_75K",
                      "employment_status": "EMPLOYED_FULL_TIME",
                      "marital_status": "SINGLE",
                      "current_savings": 5000,
                      "monthly_expenses": 3500
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Budgeting advice generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "advice": "Young Adult Budgeting (20s):\\n- Follow 50/30/20 rule: 50% needs, 30% wants, 20% savings\\n- Prioritize building emergency fund first\\n- Keep housing costs under 30% of income\\n\\nGeneral Budgeting Tips:\\n- Track expenses for at least one month\\n- Use the envelope method for discretionary spending\\n- Automate savings and bill payments\\n- Review and adjust budget quarterly",
                  "age_group": "Young Adult (20s)",
                  "profile_complete": false
                }
                """
            )
        )
    )
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
    @Operation(
        summary = "Get retirement planning advice",
        description = "Get personalized retirement planning strategy based on age, savings, and retirement goals",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for retirement planning",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Early Career",
                        summary = "32-year-old starting retirement planning",
                        value = """
                        {
                          "age": 32,
                          "income_range": "RANGE_75K_100K",
                          "retirement_age_target": 65,
                          "current_savings": 50000,
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "financial_goals": ["RETIREMENT"]
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Late Career",
                        summary = "55-year-old approaching retirement",
                        value = """
                        {
                          "age": 55,
                          "income_range": "ABOVE_150K",
                          "retirement_age_target": 62,
                          "current_savings": 750000,
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "financial_goals": ["RETIREMENT"],
                          "risk_tolerance": "CONSERVATIVE"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Retirement plan generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "retirement_plan": "Personalized Retirement Planning:\\n\\nCurrent Age: 32\\nTarget Retirement Age: 65\\nYears to Retirement: 33\\n\\nMedium-Term Strategy (15-30 years):\\n- Balance growth with some stability\\n- Increase contribution rates annually\\n- Diversify across asset classes",
                  "current_age": 32,
                  "target_retirement_age": 65,
                  "years_to_retirement": 33
                }
                """
            )
        )
    )
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
    @Operation(
        summary = "Validate financial profile completeness",
        description = "Check if the financial profile has sufficient information for personalized advice",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile to validate",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Complete Profile",
                        summary = "Profile with all required fields",
                        value = """
                        {
                          "age": 35,
                          "income_range": "RANGE_75K_100K",
                          "risk_tolerance": "MODERATE",
                          "investment_experience": "INTERMEDIATE"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Incomplete Profile",
                        summary = "Profile missing required fields",
                        value = """
                        {
                          "age": 30,
                          "interests": ["technology"]
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profile validation completed",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Complete Profile Response",
                    value = """
                    {
                      "is_complete": true,
                      "age_provided": true,
                      "risk_tolerance_provided": true,
                      "investment_experience_provided": true,
                      "income_range_provided": true,
                      "recommendations": "Profile is complete for personalized advice"
                    }
                    """
                ),
                @ExampleObject(
                    name = "Incomplete Profile Response",
                    value = """
                    {
                      "is_complete": false,
                      "age_provided": true,
                      "risk_tolerance_provided": false,
                      "investment_experience_provided": false,
                      "income_range_provided": false,
                      "recommendations": "Please provide age, risk tolerance, investment experience, and income range for better recommendations"
                    }
                    """
                )
            }
        )
    )
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
    @Operation(
        summary = "Get available advisory modes",
        description = "List all available advisory modes and their descriptions for personalized financial advice"
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of advisory modes",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "advisory_modes": {
                    "GENERAL": "General financial advice",
                    "INVESTMENT_FOCUSED": "Investment-focused advice",
                    "BUDGETING": "Budgeting and expense management",
                    "RETIREMENT_PLANNING": "Retirement planning",
                    "DEBT_MANAGEMENT": "Debt management",
                    "TAX_PLANNING": "Tax planning",
                    "INSURANCE_PLANNING": "Insurance planning",
                    "EMERGENCY_FUND": "Emergency fund planning"
                  },
                  "default_mode": "GENERAL"
                }
                """
            )
        )
    )
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
    @Operation(
        summary = "Get financial profile template",
        description = "Get a template showing all available profile fields, their types, and example values"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profile template with field descriptions",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "required_fields": {
                    "age": "Integer (18-100)",
                    "risk_tolerance": "CONSERVATIVE, MODERATE, AGGRESSIVE",
                    "investment_experience": "BEGINNER, INTERMEDIATE, ADVANCED",
                    "income_range": "BELOW_25K, RANGE_25K_50K, RANGE_50K_75K, RANGE_75K_100K, RANGE_100K_150K, ABOVE_150K"
                  },
                  "optional_fields": {
                    "financial_goals": "Array of: RETIREMENT, EMERGENCY_FUND, HOME_PURCHASE, EDUCATION, etc.",
                    "interests": "Array of strings (e.g., ['technology', 'healthcare', 'environment'])",
                    "current_savings": "BigDecimal",
                    "monthly_expenses": "BigDecimal",
                    "debt_amount": "BigDecimal",
                    "employment_status": "EMPLOYED_FULL_TIME, EMPLOYED_PART_TIME, SELF_EMPLOYED, etc.",
                    "marital_status": "SINGLE, MARRIED, DIVORCED, WIDOWED",
                    "number_of_dependents": "Integer",
                    "retirement_age_target": "Integer",
                    "preferred_investment_types": "Array of investment types"
                  },
                  "example_interests": [
                    "technology",
                    "healthcare",
                    "environment",
                    "real estate",
                    "travel",
                    "education",
                    "renewable energy",
                    "artificial intelligence"
                  ]
                }
                """
            )
        )
    )
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