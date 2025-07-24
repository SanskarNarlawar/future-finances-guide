package com.llmapi.controller;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.dto.FinancialProfile;
import com.llmapi.dto.InvestmentGuidance;
import com.llmapi.service.FinancialAdvisoryService;
import com.llmapi.service.InvestmentGuidanceService;
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
    private final InvestmentGuidanceService investmentGuidanceService;

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

    @PostMapping("/investment-guidance/comprehensive")
    @Operation(
        summary = "Get comprehensive personalized investment guidance",
        description = "Get detailed investment recommendations including stocks, SIPs, mutual funds, ETFs, bonds, asset allocation, and monthly investment plan based on your financial profile",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for comprehensive investment guidance",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FinancialProfile.class),
                examples = {
                    @ExampleObject(
                        name = "Young Tech Professional",
                        summary = "28-year-old tech worker with moderate risk tolerance",
                        value = """
                        {
                          "age": 28,
                          "income_range": "RANGE_75K_100K",
                          "risk_tolerance": "MODERATE",
                          "investment_experience": "BEGINNER",
                          "interests": ["technology", "artificial intelligence", "startups"],
                          "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "SINGLE",
                          "current_savings": 150000,
                          "monthly_expenses": 45000
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Mid-Career Healthcare Professional",
                        summary = "38-year-old healthcare worker with family",
                        value = """
                        {
                          "age": 38,
                          "income_range": "RANGE_100K_150K",
                          "risk_tolerance": "MODERATE",
                          "investment_experience": "INTERMEDIATE",
                          "interests": ["healthcare", "education", "family planning"],
                          "financial_goals": ["RETIREMENT", "CHILD_EDUCATION", "INSURANCE"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "MARRIED",
                          "number_of_dependents": 2,
                          "current_savings": 500000,
                          "monthly_expenses": 80000
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Conservative Pre-Retiree",
                        summary = "55-year-old planning for retirement",
                        value = """
                        {
                          "age": 55,
                          "income_range": "ABOVE_150K",
                          "risk_tolerance": "CONSERVATIVE",
                          "investment_experience": "ADVANCED",
                          "interests": ["real estate", "conservative investments", "travel"],
                          "financial_goals": ["RETIREMENT", "DEBT_PAYOFF"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "MARRIED",
                          "retirement_age_target": 60,
                          "current_savings": 2500000,
                          "monthly_expenses": 120000
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Comprehensive investment guidance generated successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = InvestmentGuidance.class),
            examples = @ExampleObject(
                value = """
                {
                  "user_profile_summary": "Investor Profile: 28 years old, moderate risk tolerance, beginner investment experience, 75K-100K income range, interested in: technology, artificial intelligence, startups",
                  "investment_strategy": {
                    "approach": "Balanced Growth Strategy",
                    "rationale": "Focus on long-term wealth creation through equity investments with time advantage for compounding",
                    "key_principles": ["Start early and invest regularly through SIPs", "Diversify across asset classes and sectors"],
                    "investment_horizon": "Long-term (20+ years) - Focus on wealth creation"
                  },
                  "asset_allocation": {
                    "equity_percentage": 75,
                    "debt_percentage": 15,
                    "gold_percentage": 10,
                    "international_percentage": 10,
                    "alternative_percentage": 5,
                    "rationale": "Age-based allocation (28 years) with moderate risk tolerance adjustment"
                  },
                  "stock_recommendations": [
                    {
                      "stock_name": "Infosys",
                      "stock_symbol": "INFY",
                      "sector": "Information Technology",
                      "investment_rationale": "Global IT services leader with strong digital transformation capabilities",
                      "target_allocation": "10-15%",
                      "risk_level": "Medium",
                      "time_horizon": "3-5 years",
                      "why_suitable": "Aligns with your interest in technology and offers exposure to global IT services"
                    }
                  ],
                  "sip_recommendations": [
                    {
                      "fund_name": "SBI Bluechip Fund",
                      "fund_type": "Large Cap Equity",
                      "recommended_amount": 6000,
                      "sip_frequency": "Monthly",
                      "expected_returns": "10-12% annually",
                      "risk_level": "Medium",
                      "investment_rationale": "Invests in large-cap stocks providing stability and consistent returns",
                      "minimum_tenure": "5+ years"
                    }
                  ],
                  "monthly_investment_plan": {
                    "total_monthly_investment": 15000,
                    "sip_allocation": 7500,
                    "direct_equity_allocation": 3000,
                    "debt_allocation": 3000,
                    "emergency_fund_allocation": 1500
                  }
                }
                """
            )
        )
    )
    public ResponseEntity<InvestmentGuidance> getComprehensiveInvestmentGuidance(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating comprehensive investment guidance for age: {}", profile.getAge());
        
        try {
            InvestmentGuidance guidance = investmentGuidanceService.generatePersonalizedInvestmentGuidance(profile);
            return ResponseEntity.ok(guidance);
        } catch (Exception e) {
            log.error("Error generating comprehensive investment guidance: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/investment-guidance/stocks")
    @Operation(
        summary = "Get personalized stock recommendations",
        description = "Get specific stock recommendations based on age, risk tolerance, and personal interests including IT, healthcare, banking, and growth stocks",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for stock recommendations",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Tech Enthusiast",
                        value = """
                        {
                          "age": 32,
                          "risk_tolerance": "MODERATE",
                          "interests": ["technology", "artificial intelligence", "software"],
                          "investment_experience": "INTERMEDIATE"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Healthcare Professional",
                        value = """
                        {
                          "age": 29,
                          "risk_tolerance": "AGGRESSIVE",
                          "interests": ["healthcare", "pharmaceuticals", "medical research"],
                          "investment_experience": "BEGINNER"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Stock recommendations generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "stock_recommendations": [
                    {
                      "stock_name": "Infosys",
                      "stock_symbol": "INFY",
                      "sector": "Information Technology",
                      "investment_rationale": "Global IT services leader with strong digital transformation capabilities",
                      "target_allocation": "10-15%",
                      "risk_level": "Medium",
                      "time_horizon": "3-5 years",
                      "why_suitable": "Aligns with your interest in technology and offers exposure to global IT services"
                    },
                    {
                      "stock_name": "TCS",
                      "stock_symbol": "TCS",
                      "sector": "Information Technology",
                      "investment_rationale": "India's largest IT services company with strong global presence",
                      "target_allocation": "8-12%",
                      "risk_level": "Medium",
                      "time_horizon": "3-7 years",
                      "why_suitable": "Premium IT stock with strong fundamentals and regular dividends"
                    }
                  ]
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getStockRecommendations(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating stock recommendations for profile with interests: {}", profile.getInterests());
        
        try {
            List<InvestmentGuidance.StockRecommendation> recommendations = 
                investmentGuidanceService.generateStockRecommendations(profile);
            
            return ResponseEntity.ok(Map.of(
                "stock_recommendations", recommendations,
                "total_recommendations", recommendations.size(),
                "profile_age", profile.getAge() != null ? profile.getAge() : "Not provided",
                "risk_tolerance", profile.getRiskTolerance() != null ? profile.getRiskTolerance() : "Not provided"
            ));
        } catch (Exception e) {
            log.error("Error generating stock recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate stock recommendations at this time"));
        }
    }

    @PostMapping("/investment-guidance/sip")
    @Operation(
        summary = "Get personalized SIP recommendations",
        description = "Get systematic investment plan recommendations with specific mutual funds, amounts, and tenure based on your financial profile",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for SIP recommendations",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Young Professional Starting SIPs",
                        value = """
                        {
                          "age": 25,
                          "income_range": "RANGE_50K_75K",
                          "risk_tolerance": "MODERATE",
                          "investment_experience": "BEGINNER",
                          "financial_goals": ["RETIREMENT", "EMERGENCY_FUND"],
                          "current_savings": 50000,
                          "monthly_expenses": 35000
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Mid-Career Investor",
                        value = """
                        {
                          "age": 35,
                          "income_range": "RANGE_100K_150K",
                          "risk_tolerance": "AGGRESSIVE",
                          "investment_experience": "INTERMEDIATE",
                          "financial_goals": ["RETIREMENT", "CHILD_EDUCATION"],
                          "current_savings": 800000,
                          "monthly_expenses": 75000
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "SIP recommendations generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "sip_recommendations": [
                    {
                      "fund_name": "SBI Bluechip Fund",
                      "fund_type": "Large Cap Equity",
                      "recommended_amount": 4000,
                      "sip_frequency": "Monthly",
                      "expected_returns": "10-12% annually",
                      "risk_level": "Medium",
                      "investment_rationale": "Invests in large-cap stocks providing stability and consistent returns",
                      "minimum_tenure": "5+ years"
                    },
                    {
                      "fund_name": "HDFC Flexi Cap Fund",
                      "fund_type": "Flexi Cap Equity",
                      "recommended_amount": 3000,
                      "sip_frequency": "Monthly",
                      "expected_returns": "12-15% annually",
                      "risk_level": "Medium-High",
                      "investment_rationale": "Flexible allocation across market caps for optimal risk-return balance",
                      "minimum_tenure": "7+ years"
                    }
                  ],
                  "total_monthly_sip": 7000,
                  "recommended_monthly_investment": 10000
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getSIPRecommendations(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating SIP recommendations for age: {}", profile.getAge());
        
        try {
            List<InvestmentGuidance.SIPRecommendation> recommendations = 
                investmentGuidanceService.generateSIPRecommendations(profile);
            
            // Calculate total monthly SIP amount
            double totalMonthlySIP = recommendations.stream()
                .mapToDouble(sip -> sip.getRecommendedAmount().doubleValue())
                .sum();
            
            return ResponseEntity.ok(Map.of(
                "sip_recommendations", recommendations,
                "total_monthly_sip", totalMonthlySIP,
                "recommended_monthly_investment", investmentGuidanceService.calculateRecommendedMonthlyInvestment(profile),
                "number_of_sips", recommendations.size()
            ));
        } catch (Exception e) {
            log.error("Error generating SIP recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate SIP recommendations at this time"));
        }
    }

    @PostMapping("/investment-guidance/mutual-funds")
    @Operation(
        summary = "Get mutual fund recommendations",
        description = "Get detailed mutual fund recommendations across categories with historical returns, expense ratios, and fund house information",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for mutual fund recommendations",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Balanced Investor",
                    value = """
                    {
                      "age": 40,
                      "risk_tolerance": "MODERATE",
                      "investment_experience": "INTERMEDIATE",
                      "financial_goals": ["RETIREMENT", "CHILD_EDUCATION"]
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Mutual fund recommendations generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "mutual_fund_recommendations": [
                    {
                      "fund_name": "ICICI Prudential Bluechip Fund",
                      "fund_category": "Large Cap",
                      "fund_house": "ICICI Prudential",
                      "expense_ratio": "1.05%",
                      "historical_returns": {
                        "1_year": "12.5%",
                        "3_year": "15.2%",
                        "5_year": "13.8%"
                      },
                      "investment_rationale": "Consistent performer in large-cap category with experienced fund management",
                      "suitable_for": "Conservative to moderate risk investors seeking steady returns"
                    }
                  ]
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getMutualFundRecommendations(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating mutual fund recommendations for risk tolerance: {}", profile.getRiskTolerance());
        
        try {
            List<InvestmentGuidance.MutualFundRecommendation> recommendations = 
                investmentGuidanceService.generateMutualFundRecommendations(profile);
            
            return ResponseEntity.ok(Map.of(
                "mutual_fund_recommendations", recommendations,
                "categories_covered", recommendations.stream()
                    .map(InvestmentGuidance.MutualFundRecommendation::getFundCategory)
                    .distinct()
                    .toList(),
                "total_funds", recommendations.size()
            ));
        } catch (Exception e) {
            log.error("Error generating mutual fund recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate mutual fund recommendations at this time"));
        }
    }

    @PostMapping("/investment-guidance/etf")
    @Operation(
        summary = "Get ETF recommendations",
        description = "Get Exchange Traded Fund recommendations including index ETFs, sector ETFs, and commodity ETFs with expense ratios and liquidity information",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for ETF recommendations",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "age": 30,
                      "risk_tolerance": "MODERATE",
                      "investment_experience": "INTERMEDIATE",
                      "interests": ["diversification", "low-cost investing"]
                    }
                    """
                )
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "ETF recommendations generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "etf_recommendations": [
                    {
                      "etf_name": "SBI ETF Nifty 50",
                      "etf_symbol": "SETFNIF50",
                      "underlying_index": "Nifty 50",
                      "expense_ratio": "0.10%",
                      "investment_rationale": "Low-cost exposure to India's top 50 companies with high liquidity",
                      "liquidity": "High"
                    }
                  ]
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getETFRecommendations(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating ETF recommendations");
        
        try {
            List<InvestmentGuidance.ETFRecommendation> recommendations = 
                investmentGuidanceService.generateETFRecommendations(profile);
            
            return ResponseEntity.ok(Map.of(
                "etf_recommendations", recommendations,
                "average_expense_ratio", calculateAverageExpenseRatio(recommendations),
                "total_etfs", recommendations.size()
            ));
        } catch (Exception e) {
            log.error("Error generating ETF recommendations: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate ETF recommendations at this time"));
        }
    }

    @PostMapping("/investment-guidance/monthly-plan")
    @Operation(
        summary = "Get personalized monthly investment plan",
        description = "Get a detailed monthly investment plan with allocation across SIPs, direct equity, debt instruments, and emergency fund based on income and expenses",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for monthly investment plan",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Young Professional",
                        value = """
                        {
                          "age": 27,
                          "income_range": "RANGE_75K_100K",
                          "current_savings": 200000,
                          "monthly_expenses": 50000,
                          "risk_tolerance": "MODERATE",
                          "financial_goals": ["RETIREMENT", "HOME_PURCHASE"]
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Experienced Investor",
                        value = """
                        {
                          "age": 42,
                          "income_range": "RANGE_100K_150K",
                          "current_savings": 1500000,
                          "monthly_expenses": 85000,
                          "risk_tolerance": "AGGRESSIVE",
                          "financial_goals": ["RETIREMENT", "CHILD_EDUCATION", "WEALTH_CREATION"]
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Monthly investment plan generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "monthly_investment_plan": {
                    "total_monthly_investment": 20000,
                    "sip_allocation": 10000,
                    "direct_equity_allocation": 4000,
                    "debt_allocation": 4000,
                    "emergency_fund_allocation": 2000,
                    "investment_breakdown": [
                      {
                        "investment_name": "Equity SIPs",
                        "monthly_amount": 10000,
                        "percentage_of_total": "50%",
                        "rationale": "Systematic investment in equity mutual funds for long-term wealth creation"
                      },
                      {
                        "investment_name": "Direct Equity",
                        "monthly_amount": 4000,
                        "percentage_of_total": "20%",
                        "rationale": "Direct stock investments for higher potential returns"
                      }
                    ]
                  },
                  "estimated_annual_investment": 240000,
                  "investment_capacity": "Based on income and expenses analysis"
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getMonthlyInvestmentPlan(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating monthly investment plan for income range: {}", profile.getIncomeRange());
        
        try {
            InvestmentGuidance.MonthlyInvestmentPlan plan = 
                investmentGuidanceService.createMonthlyInvestmentPlan(profile);
            
            return ResponseEntity.ok(Map.of(
                "monthly_investment_plan", plan,
                "estimated_annual_investment", plan.getTotalMonthlyInvestment().multiply(java.math.BigDecimal.valueOf(12)),
                "investment_capacity", "Based on income and expenses analysis",
                "breakdown_count", plan.getInvestmentBreakdown().size()
            ));
        } catch (Exception e) {
            log.error("Error generating monthly investment plan: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate monthly investment plan at this time"));
        }
    }

    @PostMapping("/investment-guidance/asset-allocation")
    @Operation(
        summary = "Get personalized asset allocation strategy",
        description = "Get age-appropriate asset allocation across equity, debt, gold, international, and alternative investments with rationale",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Financial profile for asset allocation",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Conservative Investor",
                        value = """
                        {
                          "age": 50,
                          "risk_tolerance": "CONSERVATIVE",
                          "investment_experience": "INTERMEDIATE"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Aggressive Young Investor",
                        value = """
                        {
                          "age": 25,
                          "risk_tolerance": "AGGRESSIVE",
                          "investment_experience": "BEGINNER"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Asset allocation strategy generated successfully",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "asset_allocation": {
                    "equity_percentage": 65,
                    "debt_percentage": 25,
                    "gold_percentage": 10,
                    "international_percentage": 10,
                    "alternative_percentage": 5,
                    "rationale": "Age-based allocation (35 years) with moderate risk tolerance adjustment"
                  },
                  "allocation_strategy": "Age-based with risk adjustment",
                  "rebalancing_frequency": "Quarterly review recommended"
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getAssetAllocation(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating asset allocation for age: {} with risk tolerance: {}", 
                profile.getAge(), profile.getRiskTolerance());
        
        try {
            InvestmentGuidance.AssetAllocation allocation = 
                investmentGuidanceService.generateAssetAllocation(profile);
            
            return ResponseEntity.ok(Map.of(
                "asset_allocation", allocation,
                "allocation_strategy", "Age-based with risk adjustment",
                "rebalancing_frequency", "Quarterly review recommended",
                "total_percentage", allocation.getEquityPercentage() + allocation.getDebtPercentage() + 
                                  allocation.getGoldPercentage() + allocation.getInternationalPercentage() + 
                                  allocation.getAlternativePercentage()
            ));
        } catch (Exception e) {
            log.error("Error generating asset allocation: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate asset allocation at this time"));
        }
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

    // Helper methods
    private String calculateAverageExpenseRatio(List<InvestmentGuidance.ETFRecommendation> recommendations) {
        if (recommendations.isEmpty()) return "N/A";
        
        double average = recommendations.stream()
            .mapToDouble(etf -> {
                try {
                    return Double.parseDouble(etf.getExpenseRatio().replace("%", ""));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            })
            .average()
            .orElse(0.0);
        
        return String.format("%.2f%%", average);
    }
}