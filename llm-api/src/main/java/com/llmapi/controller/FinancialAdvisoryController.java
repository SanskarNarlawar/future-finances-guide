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
@Tag(name = "💰 Complete Financial Advisory API", description = "Comprehensive financial advisory system with personalized investment guidance, budgeting, retirement planning, and chatbot assistance")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinancialAdvisoryController {

    private final FinancialAdvisoryService financialAdvisoryService;
    private final InvestmentGuidanceService investmentGuidanceService;

    @PostMapping("/comprehensive-guidance")
    @Operation(
        summary = "🎯 Get Complete Financial & Investment Guidance",
        description = "**ALL-IN-ONE ENDPOINT** - Get comprehensive financial advisory including personalized investment recommendations (stocks, SIPs, mutual funds, ETFs), budgeting advice, retirement planning, risk management, tax optimization, and monthly investment plan based on age, interests, and risk tolerance.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Complete financial profile for comprehensive guidance",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FinancialProfile.class),
                examples = {
                    @ExampleObject(
                        name = "🚀 Young Tech Professional (28)",
                        summary = "Complete guidance for 28-year-old tech worker",
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
                        name = "🏥 Mid-Career Healthcare Professional (38)",
                        summary = "Complete guidance for 38-year-old healthcare worker with family",
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
                          "monthly_expenses": 80000,
                          "retirement_age_target": 60
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "🏦 Conservative Pre-Retiree (55)",
                        summary = "Complete guidance for 55-year-old planning retirement",
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
                          "monthly_expenses": 120000,
                          "debt_amount": 500000
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "🌱 Young Aggressive Investor (25)",
                        summary = "Complete guidance for 25-year-old aggressive investor",
                        value = """
                        {
                          "age": 25,
                          "income_range": "RANGE_50K_75K",
                          "risk_tolerance": "AGGRESSIVE",
                          "investment_experience": "BEGINNER",
                          "interests": ["startups", "cryptocurrency", "growth stocks"],
                          "financial_goals": ["WEALTH_BUILDING", "RETIREMENT", "EMERGENCY_FUND"],
                          "employment_status": "EMPLOYED_FULL_TIME",
                          "marital_status": "SINGLE",
                          "current_savings": 80000,
                          "monthly_expenses": 30000
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "✅ Complete financial guidance generated successfully",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Map.class),
            examples = @ExampleObject(
                name = "Complete Financial Guidance Response",
                value = """
                {
                  "profile_analysis": {
                    "summary": "28-year-old tech professional, moderate risk tolerance, beginner investor",
                    "investment_capacity": 12750,
                    "risk_profile": "Balanced Growth Strategy",
                    "investment_horizon": "Long-term (30+ years)"
                  },
                  "investment_recommendations": {
                    "asset_allocation": {
                      "equity_percentage": 72,
                      "debt_percentage": 18,
                      "gold_percentage": 10,
                      "rationale": "Age-based allocation with moderate risk adjustment"
                    },
                    "stocks": [
                      {
                        "name": "Infosys",
                        "symbol": "INFY",
                        "sector": "IT",
                        "allocation": "10-15%",
                        "rationale": "Aligns with tech interests"
                      }
                    ],
                    "sips": [
                      {
                        "fund": "SBI Bluechip Fund",
                        "type": "Large Cap",
                        "amount": 6000,
                        "returns": "10-12%"
                      }
                    ],
                    "mutual_funds": [
                      {
                        "name": "HDFC Flexi Cap Fund",
                        "category": "Flexi Cap",
                        "returns_3yr": "15.2%"
                      }
                    ]
                  },
                  "monthly_plan": {
                    "total_investment": 12750,
                    "breakdown": [
                      {
                        "investment_name": "Equity SIPs",
                        "monthly_amount": 6375,
                        "percentage_of_total": "50%",
                        "rationale": "Systematic investment in equity mutual funds for long-term wealth creation"
                      },
                      {
                        "investment_name": "Direct Equity",
                        "monthly_amount": 2550,
                        "percentage_of_total": "20%",
                        "rationale": "Direct stock investments for higher potential returns"
                      },
                      {
                        "investment_name": "Debt Instruments",
                        "monthly_amount": 2550,
                        "percentage_of_total": "20%",
                        "rationale": "Diversified debt instruments for stability"
                      },
                      {
                        "investment_name": "Emergency Fund",
                        "monthly_amount": 1275,
                        "percentage_of_total": "10%",
                        "rationale": "Emergency fund for unexpected expenses"
                      }
                    ],
                    "sip_allocation": 6375,
                    "direct_equity": 2550,
                    "debt_allocation": 2550,
                    "emergency_fund": 1275
                  },
                  "budgeting_advice": {
                    "age_group": "Young Adult (20s)",
                    "advice": "Young Adult Budgeting (20s):\\n- Follow 50/30/20 rule: 50% needs, 30% wants, 20% savings\\n- Prioritize building emergency fund first\\n- Keep housing costs under 30% of income\\n\\nGeneral Budgeting Tips:\\n- Track expenses for at least one month\\n- Use the envelope method for discretionary spending\\n- Automate savings and bill payments\\n- Review and adjust budget quarterly",
                    "profile_complete": false
                  },
                  "retirement_planning": {
                    "plan": "Personalized Retirement Planning:\\n\\nCurrent Age: 32\\nTarget Retirement Age: 65\\nYears to Retirement: 33\\n\\nMedium-Term Strategy (15-30 years):\\n- Balance growth with some stability\\n- Increase contribution rates annually\\n- Diversify across asset classes",
                    "current_age": 32,
                    "target_retirement_age": 65,
                    "years_to_retirement": 33,
                    "timeline": "Long-term (20+ years) - Focus on wealth creation"
                  },
                  "age_based_goals": {
                    "goals": "Financial milestones for your age (28):\\n\\nIn Your 20s - Foundation Building:\\n- Build emergency fund (3-6 months expenses)\\n- Start investing early (even small amounts)\\n- Maximize employer 401(k) match\\n- Pay off high-interest debt\\n- Build good credit history",
                    "age_group": "Young Adult (20s)"
                  },
                  "risk_management": {
                    "diversification": "Across sectors and asset classes",
                    "emergency_fund_months": 6,
                    "insurance_needs": ["Term life", "Health insurance"]
                  },
                  "tax_optimization": {
                    "elss_recommendation": 12500,
                    "ltcg_strategy": "Hold >1 year for tax benefits",
                    "section_80c_options": ["ELSS", "PPF", "NSC"]
                  },
                  "rebalancing_strategy": "Quarterly review recommended",
                  "performance_tracking": "Track key metrics and adjust as needed",
                  "important_disclaimers": ["This advice is for educational purposes only. Please consult with a qualified financial advisor for decisions specific to your situation."]
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getComprehensiveGuidance(
            @Valid @RequestBody FinancialProfile profile) {
        
        log.info("Generating comprehensive financial guidance for age: {}, interests: {}", 
                profile.getAge(), profile.getInterests());
        
        try {
            // Get comprehensive investment guidance
            InvestmentGuidance investmentGuidance = investmentGuidanceService.generatePersonalizedInvestmentGuidance(profile);
            
            // Get budgeting advice
            String budgetingAdvice = financialAdvisoryService.generateBudgetingAdvice(profile);
            
            // Get retirement planning
            String retirementPlan = financialAdvisoryService.generateRetirementPlan(profile);
            
            // Get age-based goals
            String ageBasedGoals = financialAdvisoryService.generateAgeBasedGoals(profile.getAge(), profile);
            
            // Build comprehensive response
            Map<String, Object> response = new java.util.HashMap<>();
            
            // Profile Analysis
            Map<String, Object> profileAnalysis = new java.util.HashMap<>();
            profileAnalysis.put("summary", investmentGuidance.getUserProfileSummary());
            profileAnalysis.put("investment_capacity", investmentGuidance.getMonthlyInvestmentPlan().getTotalMonthlyInvestment());
            profileAnalysis.put("risk_profile", investmentGuidance.getInvestmentStrategy().getApproach());
            profileAnalysis.put("investment_horizon", investmentGuidance.getInvestmentStrategy().getInvestmentHorizon());
            profileAnalysis.put("profile_completeness", financialAdvisoryService.isProfileComplete(profile));
            response.put("profile_analysis", profileAnalysis);
            
            // Investment Recommendations
            Map<String, Object> investmentRecommendations = new java.util.HashMap<>();
            investmentRecommendations.put("asset_allocation", investmentGuidance.getAssetAllocation());
            investmentRecommendations.put("stocks", investmentGuidance.getStockRecommendations());
            investmentRecommendations.put("sips", investmentGuidance.getSipRecommendations());
            investmentRecommendations.put("mutual_funds", investmentGuidance.getMutualFundRecommendations());
            investmentRecommendations.put("etfs", investmentGuidance.getEtfRecommendations());
            investmentRecommendations.put("bonds", investmentGuidance.getBondRecommendations());
            investmentRecommendations.put("alternatives", investmentGuidance.getAlternativeInvestments());
            response.put("investment_recommendations", investmentRecommendations);
            
            // Monthly Plan
            Map<String, Object> monthlyPlan = new java.util.HashMap<>();
            monthlyPlan.put("total_investment", investmentGuidance.getMonthlyInvestmentPlan().getTotalMonthlyInvestment());
            monthlyPlan.put("breakdown", investmentGuidance.getMonthlyInvestmentPlan().getInvestmentBreakdown());
            monthlyPlan.put("sip_allocation", investmentGuidance.getMonthlyInvestmentPlan().getSipAllocation());
            monthlyPlan.put("direct_equity", investmentGuidance.getMonthlyInvestmentPlan().getDirectEquityAllocation());
            monthlyPlan.put("debt_allocation", investmentGuidance.getMonthlyInvestmentPlan().getDebtAllocation());
            monthlyPlan.put("emergency_fund", investmentGuidance.getMonthlyInvestmentPlan().getEmergencyFundAllocation());
            response.put("monthly_plan", monthlyPlan);
            
            // Budgeting Advice
            Map<String, Object> budgetingAdviceMap = new java.util.HashMap<>();
            budgetingAdviceMap.put("age_group", getAgeGroup(profile.getAge()));
            budgetingAdviceMap.put("advice", budgetingAdvice);
            budgetingAdviceMap.put("profile_complete", financialAdvisoryService.isProfileComplete(profile));
            response.put("budgeting_advice", budgetingAdviceMap);
            
            // Retirement Planning
            Map<String, Object> retirementPlanningMap = new java.util.HashMap<>();
            retirementPlanningMap.put("plan", retirementPlan);
            retirementPlanningMap.put("current_age", profile.getAge());
            retirementPlanningMap.put("target_retirement_age", profile.getRetirementAgeTarget() != null ? profile.getRetirementAgeTarget() : 60);
            retirementPlanningMap.put("years_to_retirement", calculateYearsToRetirement(profile));
            retirementPlanningMap.put("timeline", investmentGuidance.getInvestmentTimeline());
            response.put("retirement_planning", retirementPlanningMap);
            
            // Age-based Goals
            Map<String, Object> ageBasedGoalsMap = new java.util.HashMap<>();
            ageBasedGoalsMap.put("goals", ageBasedGoals);
            ageBasedGoalsMap.put("age_group", getAgeGroup(profile.getAge()));
            response.put("age_based_goals", ageBasedGoalsMap);
            
            // Add other components
            response.put("risk_management", investmentGuidance.getRiskManagement());
            response.put("tax_optimization", investmentGuidance.getTaxOptimization());
            response.put("rebalancing_strategy", investmentGuidance.getRebalancingStrategy());
            response.put("performance_tracking", investmentGuidance.getPerformanceTracking());
            response.put("important_disclaimers", investmentGuidance.getImportantDisclaimers());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error generating comprehensive financial guidance: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Unable to generate comprehensive guidance at this time"));
        }
    }

    @PostMapping("/chat")
    @Operation(
        summary = "💬 Interactive Financial Advisory Chat",
        description = "**CONVERSATIONAL ENDPOINT** - Chat with the financial advisor AI for personalized advice, questions, and guidance. Supports context-aware conversations with financial profile integration.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Chat message with optional financial profile for personalized responses",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatRequest.class),
                examples = {
                    @ExampleObject(
                        name = "💡 Investment Question",
                        summary = "Ask specific investment questions",
                        value = """
                        {
                          "message": "I'm 28 and work in tech. Should I invest in individual stocks or mutual funds? I have ₹50,000 to start with.",
                          "session_id": "tech-professional-session",
                          "advisory_mode": "INVESTMENT_FOCUSED",
                          "financial_profile": {
                            "age": 28,
                            "income_range": "RANGE_75K_100K",
                            "risk_tolerance": "MODERATE",
                            "interests": ["technology", "artificial intelligence"],
                            "current_savings": 50000
                          }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "🏠 Home Buying Query",
                        summary = "Questions about home purchase planning",
                        value = """
                        {
                          "message": "I want to buy a house in 5 years. How should I plan my investments and savings?",
                          "session_id": "home-buyer-session",
                          "advisory_mode": "GENERAL",
                          "financial_profile": {
                            "age": 32,
                            "income_range": "RANGE_100K_150K",
                            "financial_goals": ["HOME_PURCHASE"],
                            "current_savings": 300000
                          }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "🎯 Retirement Planning",
                        summary = "Retirement planning discussions",
                        value = """
                        {
                          "message": "I'm 45 and want to retire by 55. Am I on track? What should I change in my investment strategy?",
                          "session_id": "early-retirement-session",
                          "advisory_mode": "RETIREMENT_PLANNING",
                          "financial_profile": {
                            "age": 45,
                            "retirement_age_target": 55,
                            "current_savings": 1500000,
                            "risk_tolerance": "MODERATE"
                          }
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "📊 General Financial Advice",
                        summary = "General financial questions",
                        value = """
                        {
                          "message": "What's the difference between ELSS and regular mutual funds? Which is better for tax saving?",
                          "session_id": "general-query-session",
                          "advisory_mode": "GENERAL"
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
            description = "✅ Personalized financial advice response",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ChatResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": "financial-advice-123",
                      "session_id": "tech-professional-session",
                      "message": "Great question! At 28 with your tech background and ₹50,000 to start, here's my personalized advice:\\n\\n**For beginners like you, I recommend starting with mutual funds:**\\n\\n🎯 **Why Mutual Funds First:**\\n- Professional management\\n- Instant diversification\\n- Lower risk than individual stocks\\n- Perfect for ₹50,000 starting amount\\n\\n📈 **Recommended Allocation:**\\n- Large Cap SIP: ₹20,000 (SBI Bluechip Fund)\\n- Flexi Cap SIP: ₹15,000 (HDFC Flexi Cap)\\n- Tech Sector Fund: ₹10,000 (aligns with your interests)\\n- Emergency Fund: ₹5,000\\n\\n💡 **Next Steps:**\\n1. Start SIPs immediately\\n2. Increase by 10% annually\\n3. After 2-3 years, consider direct stocks\\n\\n⚠️ **Important**: This advice is based on your profile. Consult a financial advisor for personalized guidance.",
                      "model_name": "gpt-3.5-turbo",
                      "created_at": "2024-01-15T10:30:00",
                      "token_count": 180,
                      "advisory_mode": "INVESTMENT_FOCUSED",
                      "profile_based": true
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<ChatResponse> getFinancialAdvice(
            @Valid @RequestBody ChatRequest request) {
        
        log.info("Received financial advisory chat request for session: {}", request.getSessionId());
        
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

    @GetMapping("/profile-template")
    @Operation(
        summary = "📋 Get Financial Profile Template & Options",
        description = "**HELPER ENDPOINT** - Get complete template showing all available profile fields, their types, options, and example values for building financial profiles."
    )
    @ApiResponse(
        responseCode = "200",
        description = "✅ Complete profile template with all options",
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                value = """
                {
                  "required_fields": {
                    "age": {
                      "type": "Integer",
                      "range": "18-100",
                      "description": "Your current age for age-appropriate recommendations"
                    },
                    "risk_tolerance": {
                      "type": "Enum",
                      "options": ["CONSERVATIVE", "MODERATE", "AGGRESSIVE"],
                      "descriptions": {
                        "CONSERVATIVE": "Prefer stable, low-risk investments",
                        "MODERATE": "Balanced approach to risk and return",
                        "AGGRESSIVE": "Comfortable with high-risk, high-reward investments"
                      }
                    },
                    "investment_experience": {
                      "type": "Enum",
                      "options": ["BEGINNER", "INTERMEDIATE", "ADVANCED"],
                      "description": "Your investment knowledge and experience level"
                    },
                    "income_range": {
                      "type": "Enum",
                      "options": ["BELOW_25K", "RANGE_25K_50K", "RANGE_50K_75K", "RANGE_75K_100K", "RANGE_100K_150K", "ABOVE_150K"],
                      "description": "Annual income range for investment capacity calculation"
                    }
                  },
                  "optional_fields": {
                    "interests": {
                      "type": "Array[String]",
                      "examples": ["technology", "healthcare", "real estate", "environment", "education"],
                      "description": "Personal interests for aligned investment recommendations"
                    },
                    "financial_goals": {
                      "type": "Array[Enum]",
                      "options": ["RETIREMENT", "EMERGENCY_FUND", "HOME_PURCHASE", "CHILD_EDUCATION", "WEALTH_BUILDING", "DEBT_PAYOFF", "TRAVEL", "BUSINESS_INVESTMENT"],
                      "description": "Your financial objectives and goals"
                    },
                    "current_savings": {
                      "type": "BigDecimal",
                      "description": "Current total savings amount"
                    },
                    "monthly_expenses": {
                      "type": "BigDecimal", 
                      "description": "Monthly expenses for investment capacity calculation"
                    },
                    "employment_status": {
                      "type": "Enum",
                      "options": ["EMPLOYED_FULL_TIME", "EMPLOYED_PART_TIME", "SELF_EMPLOYED", "UNEMPLOYED", "RETIRED", "STUDENT"],
                      "description": "Current employment situation"
                    },
                    "marital_status": {
                      "type": "Enum",
                      "options": ["SINGLE", "MARRIED", "DIVORCED", "WIDOWED"],
                      "description": "Marital status affecting financial planning"
                    },
                    "number_of_dependents": {
                      "type": "Integer",
                      "description": "Number of financial dependents"
                    },
                    "retirement_age_target": {
                      "type": "Integer",
                      "description": "Target retirement age for planning"
                    }
                  },
                  "sample_profiles": {
                    "young_professional": {
                      "age": 28,
                      "risk_tolerance": "MODERATE",
                      "investment_experience": "BEGINNER",
                      "income_range": "RANGE_75K_100K",
                      "interests": ["technology", "startups"],
                      "financial_goals": ["RETIREMENT", "HOME_PURCHASE"]
                    },
                    "mid_career_parent": {
                      "age": 38,
                      "risk_tolerance": "MODERATE",
                      "investment_experience": "INTERMEDIATE", 
                      "income_range": "RANGE_100K_150K",
                      "interests": ["education", "healthcare"],
                      "financial_goals": ["RETIREMENT", "CHILD_EDUCATION"],
                      "number_of_dependents": 2
                    }
                  }
                }
                """
            )
        )
    )
    public ResponseEntity<Map<String, Object>> getProfileTemplate() {
        
        Map<String, Object> requiredFields = Map.of(
            "age", Map.of(
                "type", "Integer",
                "range", "18-100",
                "description", "Your current age for age-appropriate recommendations"
            ),
            "risk_tolerance", Map.of(
                "type", "Enum",
                "options", List.of("CONSERVATIVE", "MODERATE", "AGGRESSIVE"),
                "descriptions", Map.of(
                    "CONSERVATIVE", "Prefer stable, low-risk investments",
                    "MODERATE", "Balanced approach to risk and return",
                    "AGGRESSIVE", "Comfortable with high-risk, high-reward investments"
                )
            ),
            "investment_experience", Map.of(
                "type", "Enum",
                "options", List.of("BEGINNER", "INTERMEDIATE", "ADVANCED"),
                "description", "Your investment knowledge and experience level"
            ),
            "income_range", Map.of(
                "type", "Enum",
                "options", List.of("BELOW_25K", "RANGE_25K_50K", "RANGE_50K_75K", "RANGE_75K_100K", "RANGE_100K_150K", "ABOVE_150K"),
                "description", "Annual income range for investment capacity calculation"
            )
        );
        
        Map<String, Object> optionalFields = Map.of(
            "interests", Map.of(
                "type", "Array[String]",
                "examples", List.of("technology", "healthcare", "real estate", "environment", "education", "travel", "renewable energy", "artificial intelligence"),
                "description", "Personal interests for aligned investment recommendations"
            ),
            "financial_goals", Map.of(
                "type", "Array[Enum]",
                "options", List.of("RETIREMENT", "EMERGENCY_FUND", "HOME_PURCHASE", "CHILD_EDUCATION", "WEALTH_BUILDING", "DEBT_PAYOFF", "TRAVEL", "BUSINESS_INVESTMENT"),
                "description", "Your financial objectives and goals"
            ),
            "current_savings", Map.of(
                "type", "BigDecimal",
                "description", "Current total savings amount"
            ),
            "monthly_expenses", Map.of(
                "type", "BigDecimal",
                "description", "Monthly expenses for investment capacity calculation"
            )
        );
        
        Map<String, Object> sampleProfiles = Map.of(
            "young_professional", Map.of(
                "age", 28,
                "risk_tolerance", "MODERATE",
                "investment_experience", "BEGINNER",
                "income_range", "RANGE_75K_100K",
                "interests", List.of("technology", "startups"),
                "financial_goals", List.of("RETIREMENT", "HOME_PURCHASE")
            ),
            "mid_career_parent", Map.of(
                "age", 38,
                "risk_tolerance", "MODERATE",
                "investment_experience", "INTERMEDIATE",
                "income_range", "RANGE_100K_150K",
                "interests", List.of("education", "healthcare"),
                "financial_goals", List.of("RETIREMENT", "CHILD_EDUCATION"),
                "number_of_dependents", 2
            )
        );
        
        Map<String, Object> response = Map.of(
            "required_fields", requiredFields,
            "optional_fields", optionalFields,
            "sample_profiles", sampleProfiles,
            "total_endpoints", 3,
            "endpoint_descriptions", Map.of(
                "comprehensive-guidance", "Complete financial & investment guidance - ALL recommendations in one call",
                "chat", "Interactive financial advisory conversations with AI",
                "profile-template", "Helper endpoint for profile structure and options"
            )
        );
        
        return ResponseEntity.ok(response);
    }

    // Helper methods
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
            profile.getRetirementAgeTarget() : 60;
        
        return Math.max(0, targetRetirementAge - profile.getAge());
    }
}