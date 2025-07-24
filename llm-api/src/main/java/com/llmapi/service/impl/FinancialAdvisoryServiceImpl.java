package com.llmapi.service.impl;

import com.llmapi.dto.ChatRequest;
import com.llmapi.dto.ChatResponse;
import com.llmapi.dto.FinancialProfile;
import com.llmapi.model.ChatMessage;
import com.llmapi.repository.ChatMessageRepository;
import com.llmapi.service.FinancialAdvisoryService;
import com.llmapi.service.LlmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialAdvisoryServiceImpl implements FinancialAdvisoryService {

    private final LlmService llmService;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public ChatResponse generateFinancialAdvice(ChatRequest request) {
        log.info("Generating financial advice for session: {}", request.getSessionId());
        
        try {
            // Generate comprehensive financial response directly
            String financialResponse = generateComprehensiveFinancialResponse(request);
            
            return ChatResponse.builder()
                    .id(UUID.randomUUID().toString())
                    .sessionId(request.getSessionId())
                    .message(financialResponse)
                    .modelName("financial-advisor-ai")
                    .createdAt(LocalDateTime.now())
                    .tokenCount(estimateTokenCount(financialResponse))
                    .advisoryMode(request.getAdvisoryMode() != null ? request.getAdvisoryMode().name() : null)
                    .profileBased(request.getFinancialProfile() != null)
                    .finishReason("stop")
                    .build();
                    
        } catch (Exception e) {
            log.error("Error generating financial advice: {}", e.getMessage(), e);
            return ChatResponse.builder()
                    .id(UUID.randomUUID().toString())
                    .sessionId(request.getSessionId())
                    .message("I apologize, but I'm having trouble processing your request right now. Please try again later or contact support if the issue persists.")
                    .modelName("fallback")
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

    @Override
    public String createPersonalizedSystemPrompt(FinancialProfile profile, ChatRequest.AdvisoryMode mode) {
        if (profile == null) {
            return getDefaultFinancialSystemPrompt();
        }
        
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a knowledgeable and empathetic financial advisor chatbot. ");
        prompt.append("Provide personalized financial advice based on the following user profile:\n\n");
        
        // Age-based context
        prompt.append("USER PROFILE:\n");
        prompt.append("- Age: ").append(profile.getAge()).append(" years old\n");
        
        if (profile.getIncomeRange() != null) {
            prompt.append("- Income Range: ").append(profile.getIncomeRange().getDescription()).append("\n");
        }
        
        if (profile.getRiskTolerance() != null) {
            prompt.append("- Risk Tolerance: ").append(profile.getRiskTolerance().getDescription()).append("\n");
        }
        
        if (profile.getInvestmentExperience() != null) {
            prompt.append("- Investment Experience: ").append(profile.getInvestmentExperience().getDescription()).append("\n");
        }
        
        if (profile.getEmploymentStatus() != null) {
            prompt.append("- Employment: ").append(profile.getEmploymentStatus().getDescription()).append("\n");
        }
        
        if (profile.getMaritalStatus() != null) {
            prompt.append("- Marital Status: ").append(profile.getMaritalStatus().getDescription()).append("\n");
        }
        
        if (profile.getNumberOfDependents() != null && profile.getNumberOfDependents() > 0) {
            prompt.append("- Dependents: ").append(profile.getNumberOfDependents()).append("\n");
        }
        
        // Financial situation
        if (profile.getCurrentSavings() != null) {
            prompt.append("- Current Savings: $").append(profile.getCurrentSavings()).append("\n");
        }
        
        if (profile.getMonthlyExpenses() != null) {
            prompt.append("- Monthly Expenses: $").append(profile.getMonthlyExpenses()).append("\n");
        }
        
        if (profile.getDebtAmount() != null && profile.getDebtAmount().compareTo(BigDecimal.ZERO) > 0) {
            prompt.append("- Current Debt: $").append(profile.getDebtAmount()).append("\n");
        }
        
        // Goals and interests
        if (profile.getFinancialGoals() != null && !profile.getFinancialGoals().isEmpty()) {
            prompt.append("- Financial Goals: ");
            prompt.append(profile.getFinancialGoals().stream()
                .map(goal -> goal.getDescription())
                .collect(Collectors.joining(", ")));
            prompt.append("\n");
        }
        
        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            prompt.append("- Personal Interests: ").append(String.join(", ", profile.getInterests())).append("\n");
        }
        
        // Age-specific guidance
        prompt.append("\nAGE-SPECIFIC CONSIDERATIONS:\n");
        prompt.append(getAgeSpecificGuidance(profile.getAge()));
        
        // Advisory mode specific instructions
        prompt.append("\nFOCUS AREA: ").append(mode.getDescription()).append("\n");
        prompt.append(getModeSpecificInstructions(mode));
        
        prompt.append("\nGUIDELINES:\n");
        prompt.append("- Provide specific, actionable advice tailored to their age and situation\n");
        prompt.append("- Consider their risk tolerance and experience level\n");
        prompt.append("- Incorporate their personal interests when suggesting investment themes\n");
        prompt.append("- Be encouraging but realistic about expectations\n");
        prompt.append("- Always emphasize the importance of diversification\n");
        prompt.append("- Suggest age-appropriate investment timelines\n");
        prompt.append("- Include disclaimers about seeking professional advice for major decisions\n");
        
        return prompt.toString();
    }

    @Override
    public boolean isProfileComplete(FinancialProfile profile) {
        if (profile == null) return false;
        
        return profile.getAge() != null &&
               profile.getRiskTolerance() != null &&
               profile.getInvestmentExperience() != null &&
               profile.getIncomeRange() != null;
    }

    @Override
    public String generateInvestmentRecommendations(FinancialProfile profile) {
        if (profile == null || profile.getAge() == null) {
            return "Please provide your age and risk tolerance for personalized investment recommendations.";
        }
        
        StringBuilder recommendations = new StringBuilder();
        int age = profile.getAge();
        
        // Age-based asset allocation rule of thumb
        int stockPercentage = Math.max(20, 100 - age);
        int bondPercentage = 100 - stockPercentage;
        
        recommendations.append("Based on your age (").append(age).append("), here's a suggested asset allocation:\n");
        recommendations.append("- Stocks/Equity: ").append(stockPercentage).append("%\n");
        recommendations.append("- Bonds/Fixed Income: ").append(bondPercentage).append("%\n\n");
        
        // Risk tolerance adjustments
        if (profile.getRiskTolerance() != null) {
            switch (profile.getRiskTolerance()) {
                case CONSERVATIVE:
                    recommendations.append("Given your conservative risk tolerance, consider:\n");
                    recommendations.append("- High-grade corporate bonds\n");
                    recommendations.append("- Dividend-paying blue-chip stocks\n");
                    recommendations.append("- Treasury bonds and CDs\n");
                    break;
                case MODERATE:
                    recommendations.append("With moderate risk tolerance, consider:\n");
                    recommendations.append("- Diversified index funds (S&P 500, Total Stock Market)\n");
                    recommendations.append("- Mix of growth and value stocks\n");
                    recommendations.append("- International diversification\n");
                    break;
                case AGGRESSIVE:
                    recommendations.append("For aggressive growth, consider:\n");
                    recommendations.append("- Growth stocks and small-cap funds\n");
                    recommendations.append("- Technology and innovation sectors\n");
                    recommendations.append("- Emerging markets (small allocation)\n");
                    break;
            }
        }
        
        // Interest-based recommendations
        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            recommendations.append("\nBased on your interests (").append(String.join(", ", profile.getInterests())).append("), you might consider:\n");
            for (String interest : profile.getInterests()) {
                recommendations.append(getInterestBasedInvestments(interest));
            }
        }
        
        return recommendations.toString();
    }

    @Override
    public String generateAgeBasedGoals(Integer age, FinancialProfile profile) {
        if (age == null) return "Please provide your age for personalized financial goals.";
        
        StringBuilder goals = new StringBuilder();
        goals.append("Financial milestones for your age (").append(age).append("):\n\n");
        
        if (age >= 20 && age < 30) {
            goals.append("In Your 20s - Foundation Building:\n");
            goals.append("- Build emergency fund (3-6 months expenses)\n");
            goals.append("- Start investing early (even small amounts)\n");
            goals.append("- Maximize employer 401(k) match\n");
            goals.append("- Pay off high-interest debt\n");
            goals.append("- Build good credit history\n");
        } else if (age >= 30 && age < 40) {
            goals.append("In Your 30s - Acceleration Phase:\n");
            goals.append("- Aim to save 1x your annual salary by 30\n");
            goals.append("- Increase retirement contributions to 15-20%\n");
            goals.append("- Consider home ownership if financially ready\n");
            goals.append("- Start 529 plans if you have children\n");
            goals.append("- Review and increase insurance coverage\n");
        } else if (age >= 40 && age < 50) {
            goals.append("In Your 40s - Peak Earning Years:\n");
            goals.append("- Target 3x annual salary in retirement savings by 40\n");
            goals.append("- Maximize retirement account contributions\n");
            goals.append("- Focus on children's education funding\n");
            goals.append("- Consider long-term care insurance\n");
            goals.append("- Review estate planning documents\n");
        } else if (age >= 50 && age < 60) {
            goals.append("In Your 50s - Pre-Retirement Planning:\n");
            goals.append("- Aim for 5-7x annual salary in retirement savings\n");
            goals.append("- Take advantage of catch-up contributions\n");
            goals.append("- Create detailed retirement budget\n");
            goals.append("- Consider healthcare costs in retirement\n");
            goals.append("- Pay off mortgage if possible\n");
        } else if (age >= 60) {
            goals.append("In Your 60s+ - Retirement Transition:\n");
            goals.append("- Target 8-10x annual salary for comfortable retirement\n");
            goals.append("- Plan Social Security claiming strategy\n");
            goals.append("- Shift to more conservative investments\n");
            goals.append("- Finalize healthcare and long-term care plans\n");
            goals.append("- Consider legacy and estate planning\n");
        }
        
        return goals.toString();
    }

    @Override
    public String generateBudgetingAdvice(FinancialProfile profile) {
        StringBuilder advice = new StringBuilder();
        advice.append("Personalized Budgeting Strategy:\n\n");
        
        if (profile != null && profile.getAge() != null) {
            int age = profile.getAge();
            
            if (age < 30) {
                advice.append("Young Adult Budgeting (20s):\n");
                advice.append("- Follow 50/30/20 rule: 50% needs, 30% wants, 20% savings\n");
                advice.append("- Prioritize building emergency fund first\n");
                advice.append("- Keep housing costs under 30% of income\n");
            } else if (age < 50) {
                advice.append("Mid-Career Budgeting (30s-40s):\n");
                advice.append("- Increase savings rate to 20-25% if possible\n");
                advice.append("- Balance family expenses with retirement savings\n");
                advice.append("- Consider tax-advantaged accounts for children's education\n");
            } else {
                advice.append("Pre-Retirement Budgeting (50+):\n");
                advice.append("- Aim for 25-30% savings rate with catch-up contributions\n");
                advice.append("- Focus on debt elimination before retirement\n");
                advice.append("- Plan for healthcare cost increases\n");
            }
        }
        
        advice.append("\nGeneral Budgeting Tips:\n");
        advice.append("- Track expenses for at least one month\n");
        advice.append("- Use the envelope method for discretionary spending\n");
        advice.append("- Automate savings and bill payments\n");
        advice.append("- Review and adjust budget quarterly\n");
        
        return advice.toString();
    }

    @Override
    public String generateRetirementPlan(FinancialProfile profile) {
        if (profile == null || profile.getAge() == null) {
            return "Please provide your age and financial information for a personalized retirement plan.";
        }
        
        StringBuilder plan = new StringBuilder();
        int age = profile.getAge();
        int yearsToRetirement = (profile.getRetirementAgeTarget() != null) ? 
            profile.getRetirementAgeTarget() - age : 65 - age;
        
        plan.append("Personalized Retirement Planning:\n\n");
        plan.append("Current Age: ").append(age).append("\n");
        plan.append("Target Retirement Age: ").append(profile.getRetirementAgeTarget() != null ? profile.getRetirementAgeTarget() : 65).append("\n");
        plan.append("Years to Retirement: ").append(Math.max(0, yearsToRetirement)).append("\n\n");
        
        if (yearsToRetirement > 30) {
            plan.append("Long-Term Strategy (30+ years):\n");
            plan.append("- Focus on aggressive growth investments\n");
            plan.append("- Maximize compound interest with early contributions\n");
            plan.append("- Consider Roth IRA for tax-free growth\n");
        } else if (yearsToRetirement > 15) {
            plan.append("Medium-Term Strategy (15-30 years):\n");
            plan.append("- Balance growth with some stability\n");
            plan.append("- Increase contribution rates annually\n");
            plan.append("- Diversify across asset classes\n");
        } else if (yearsToRetirement > 0) {
            plan.append("Short-Term Strategy (<15 years):\n");
            plan.append("- Shift towards more conservative investments\n");
            plan.append("- Maximize catch-up contributions if 50+\n");
            plan.append("- Plan withdrawal strategies\n");
        } else {
            plan.append("Currently in Retirement:\n");
            plan.append("- Focus on capital preservation\n");
            plan.append("- Implement systematic withdrawal plan\n");
            plan.append("- Consider required minimum distributions\n");
        }
        
        return plan.toString();
    }

    private String enhanceMessageWithContext(ChatRequest request) {
        StringBuilder enhancedMessage = new StringBuilder();
        
        if (request.getFinancialProfile() != null) {
            enhancedMessage.append("[Context: User is ").append(request.getFinancialProfile().getAge())
                .append(" years old");
            
            if (request.getFinancialProfile().getRiskTolerance() != null) {
                enhancedMessage.append(", ").append(request.getFinancialProfile().getRiskTolerance().name().toLowerCase())
                    .append(" risk tolerance");
            }
            
            if (request.getFinancialProfile().getInterests() != null && !request.getFinancialProfile().getInterests().isEmpty()) {
                enhancedMessage.append(", interested in: ").append(String.join(", ", request.getFinancialProfile().getInterests()));
            }
            
            enhancedMessage.append("]\n\n");
        }
        
        enhancedMessage.append(request.getMessage());
        return enhancedMessage.toString();
    }

    private void saveSystemContext(String sessionId, String systemPrompt) {
        try {
            ChatMessage contextMessage = new ChatMessage();
            contextMessage.setSessionId(sessionId);
            contextMessage.setRole(ChatMessage.Role.SYSTEM);
            contextMessage.setContent("Financial Advisory Context: " + systemPrompt.substring(0, Math.min(500, systemPrompt.length())));
            contextMessage.setCreatedAt(LocalDateTime.now());
            contextMessage.setTokenCount(systemPrompt.length() / 4); // Rough estimate
            
            chatMessageRepository.save(contextMessage);
        } catch (Exception e) {
            log.warn("Failed to save system context: {}", e.getMessage());
        }
    }

    private String addFinancialDisclaimers(String message) {
        return message + "\n\n⚠️ **Important Disclaimer**: This advice is for educational purposes only and should not be considered as personalized financial advice. Please consult with a qualified financial advisor for decisions specific to your situation. Past performance does not guarantee future results.";
    }

    private String getDefaultFinancialSystemPrompt() {
        return "You are a helpful financial advisor chatbot. Provide general financial advice and encourage users to consult with qualified professionals for personalized guidance. Always include appropriate disclaimers.";
    }

    private String getAgeSpecificGuidance(int age) {
        if (age < 30) {
            return "- Time is your greatest asset - start investing early even with small amounts\n" +
                   "- Focus on building good financial habits and emergency fund\n" +
                   "- Take advantage of compound interest over the long term\n";
        } else if (age < 50) {
            return "- Peak earning years - maximize retirement contributions\n" +
                   "- Balance current family needs with future financial security\n" +
                   "- Consider tax-efficient investment strategies\n";
        } else {
            return "- Catch-up contributions available for retirement accounts\n" +
                   "- Shift towards more conservative investment approach\n" +
                   "- Plan for healthcare costs and potential long-term care needs\n";
        }
    }

    private String getModeSpecificInstructions(ChatRequest.AdvisoryMode mode) {
        switch (mode) {
            case INVESTMENT_FOCUSED:
                return "Focus on investment strategies, asset allocation, and portfolio management suitable for their profile.\n";
            case BUDGETING:
                return "Emphasize budgeting techniques, expense tracking, and spending optimization strategies.\n";
            case RETIREMENT_PLANNING:
                return "Concentrate on retirement savings strategies, timeline planning, and retirement income planning.\n";
            case DEBT_MANAGEMENT:
                return "Prioritize debt reduction strategies, consolidation options, and debt-free planning.\n";
            case TAX_PLANNING:
                return "Focus on tax-efficient strategies, deductions, and tax-advantaged accounts.\n";
            case INSURANCE_PLANNING:
                return "Address insurance needs, coverage gaps, and risk management strategies.\n";
            case EMERGENCY_FUND:
                return "Emphasize emergency fund building, liquidity needs, and financial safety net creation.\n";
            default:
                return "Provide comprehensive financial guidance covering all aspects of personal finance.\n";
        }
    }

    private String getInterestBasedInvestments(String interest) {
        String lowerInterest = interest.toLowerCase();
        if (lowerInterest.contains("technology") || lowerInterest.contains("tech")) {
            return "- Technology sector ETFs (QQQ, XLK)\n";
        } else if (lowerInterest.contains("environment") || lowerInterest.contains("green") || lowerInterest.contains("sustainable")) {
            return "- ESG (Environmental, Social, Governance) funds\n";
        } else if (lowerInterest.contains("healthcare") || lowerInterest.contains("medical")) {
            return "- Healthcare sector funds (XLV, VHT)\n";
        } else if (lowerInterest.contains("real estate") || lowerInterest.contains("property")) {
            return "- Real Estate Investment Trusts (REITs)\n";
        } else if (lowerInterest.contains("travel") || lowerInterest.contains("tourism")) {
            return "- Travel and leisure sector investments\n";
        } else {
            return "- Diversified funds aligned with your interests\n";
        }
    }

    private String buildFinancialAdvisoryPrompt(ChatRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        // System role and context
        prompt.append("You are a professional financial advisor with expertise in Indian financial markets, investment products, real estate, vehicle financing, and comprehensive financial planning. ");
        prompt.append("Provide detailed, practical, and personalized financial advice based on the user's profile and specific questions.\n\n");
        
        // Add user profile context if available
        if (request.getFinancialProfile() != null) {
            prompt.append("USER PROFILE:\n");
            prompt.append(buildProfileContext(request.getFinancialProfile()));
            prompt.append("\n");
        }
        
        // Add advisory mode context
        if (request.getAdvisoryMode() != null) {
            prompt.append("ADVISORY FOCUS: ").append(getAdvisoryModeContext(request.getAdvisoryMode())).append("\n\n");
        }
        
        // Add specific financial expertise context based on question type
        String questionType = detectQuestionType(request.getMessage());
        prompt.append("SPECIALIZED CONTEXT: ").append(getSpecializedContext(questionType)).append("\n\n");
        
        // Add the user's question
        prompt.append("USER QUESTION: ").append(request.getMessage()).append("\n\n");
        
        // Add response guidelines
        prompt.append("RESPONSE GUIDELINES - PROVIDE MAXIMUM SPECIFIC DETAILS:\n");
        prompt.append("1. EXACT AMOUNTS: Always provide specific rupee amounts, EMI calculations, and percentage breakdowns\n");
        prompt.append("2. SPECIFIC LOCATIONS: Mention exact cities, areas, and locations with current market rates\n");
        prompt.append("3. BANK/INSTITUTION NAMES: List specific banks, colleges, companies with their current rates/fees\n");
        prompt.append("4. TIMELINES: Provide exact timeframes, deadlines, and when to take specific actions\n");
        prompt.append("5. CALCULATIONS: Show detailed EMI calculations, interest costs, and total amounts\n");
        prompt.append("6. COMPARATIVE ANALYSIS: Compare multiple options with pros/cons and specific numbers\n");
        prompt.append("7. STEP-BY-STEP PLANS: Provide detailed action plans with specific deadlines and amounts\n");
        prompt.append("8. CURRENT MARKET DATA: Use the provided 2024 market data, rates, and pricing information\n");
        prompt.append("9. MULTIPLE OPTIONS: Present 2-3 specific options with detailed analysis of each\n");
        prompt.append("10. PROFESSIONAL FORMATTING: Use emojis, bullet points, and clear sections for readability\n");
        prompt.append("11. RISK ANALYSIS: Quantify risks with specific percentages and scenarios\n");
        prompt.append("12. TAX IMPLICATIONS: Calculate exact tax savings and implications with specific amounts\n\n");
        
        prompt.append("Please provide comprehensive financial advice:");
        
        return prompt.toString();
    }

    private String buildProfileContext(FinancialProfile profile) {
        StringBuilder context = new StringBuilder();
        
        if (profile.getAge() != null) {
            context.append("- Age: ").append(profile.getAge()).append(" years\n");
        }
        if (profile.getIncomeRange() != null) {
            context.append("- Income Range: ").append(profile.getIncomeRange().getDescription()).append("\n");
        }
        if (profile.getRiskTolerance() != null) {
            context.append("- Risk Tolerance: ").append(profile.getRiskTolerance().getDescription()).append("\n");
        }
        if (profile.getInvestmentExperience() != null) {
            context.append("- Investment Experience: ").append(profile.getInvestmentExperience().name()).append("\n");
        }
        if (profile.getCurrentSavings() != null) {
            context.append("- Current Savings: ₹").append(profile.getCurrentSavings()).append("\n");
        }
        if (profile.getMonthlyExpenses() != null) {
            context.append("- Monthly Expenses: ₹").append(profile.getMonthlyExpenses()).append("\n");
        }
        if (profile.getEmploymentStatus() != null) {
            context.append("- Employment: ").append(profile.getEmploymentStatus().name()).append("\n");
        }
        if (profile.getMaritalStatus() != null) {
            context.append("- Marital Status: ").append(profile.getMaritalStatus().name()).append("\n");
        }
        if (profile.getNumberOfDependents() != null && profile.getNumberOfDependents() > 0) {
            context.append("- Dependents: ").append(profile.getNumberOfDependents()).append("\n");
        }
        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            context.append("- Interests: ").append(String.join(", ", profile.getInterests())).append("\n");
        }
        if (profile.getFinancialGoals() != null && !profile.getFinancialGoals().isEmpty()) {
            context.append("- Financial Goals: ").append(profile.getFinancialGoals().toString()).append("\n");
        }
        if (profile.getRetirementAgeTarget() != null) {
            context.append("- Target Retirement Age: ").append(profile.getRetirementAgeTarget()).append("\n");
        }
        if (profile.getDebtAmount() != null && profile.getDebtAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            context.append("- Current Debt: ₹").append(profile.getDebtAmount()).append("\n");
        }
        
        return context.toString();
    }

    private String getAdvisoryModeContext(ChatRequest.AdvisoryMode mode) {
        return switch (mode) {
            case GENERAL -> "General financial planning and advice";
            case INVESTMENT_FOCUSED -> "Investment recommendations, portfolio optimization, and wealth building strategies";
            case BUDGETING -> "Budget planning, expense management, and financial discipline";
            case RETIREMENT_PLANNING -> "Retirement corpus calculation, pension planning, and post-retirement financial security";
            case DEBT_MANAGEMENT -> "Debt consolidation, EMI optimization, and debt-free strategies";
            case TAX_PLANNING -> "Tax-saving investments, deductions under various sections, and tax optimization";
            case INSURANCE_PLANNING -> "Life insurance, health insurance, and comprehensive risk coverage";
            case EMERGENCY_FUND -> "Emergency fund creation, liquidity management, and financial safety nets";
        };
    }

    private String detectQuestionType(String message) {
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("house") || lowerMessage.contains("home") || lowerMessage.contains("property") || 
            lowerMessage.contains("real estate") || lowerMessage.contains("flat") || lowerMessage.contains("apartment")) {
            return "REAL_ESTATE";
        } else if (lowerMessage.contains("car") || lowerMessage.contains("vehicle") || lowerMessage.contains("bike") || 
                   lowerMessage.contains("auto loan") || lowerMessage.contains("automobile")) {
            return "VEHICLE_FINANCE";
        } else if (lowerMessage.contains("loan") || lowerMessage.contains("emi") || lowerMessage.contains("interest rate") || 
                   lowerMessage.contains("mortgage") || lowerMessage.contains("credit")) {
            return "LOAN_FINANCE";
        } else if (lowerMessage.contains("education") || lowerMessage.contains("college") || lowerMessage.contains("school") || 
                   lowerMessage.contains("child") || lowerMessage.contains("study")) {
            return "EDUCATION_PLANNING";
        } else if (lowerMessage.contains("business") || lowerMessage.contains("startup") || lowerMessage.contains("entrepreneur")) {
            return "BUSINESS_FINANCE";
        } else if (lowerMessage.contains("marriage") || lowerMessage.contains("wedding") || lowerMessage.contains("family")) {
            return "LIFE_EVENTS";
        } else if (lowerMessage.contains("travel") || lowerMessage.contains("vacation") || lowerMessage.contains("holiday")) {
            return "TRAVEL_PLANNING";
        } else if (lowerMessage.contains("investment") || lowerMessage.contains("sip") || lowerMessage.contains("mutual fund") || 
                   lowerMessage.contains("stock") || lowerMessage.contains("equity")) {
            return "INVESTMENT";
        } else {
            return "GENERAL";
        }
    }

    private String getSpecializedContext(String questionType) {
        return switch (questionType) {
            case "REAL_ESTATE" -> """
                REAL ESTATE & PROPERTY EXPERTISE WITH DETAILED SPECIFICS:
                
                CURRENT MARKET DATA & PRICING (2024):
                - Mumbai: ₹15,000-25,000/sq ft (Andheri), ₹35,000-50,000/sq ft (Bandra/BKC)
                - Bangalore: ₹6,000-12,000/sq ft (Electronic City), ₹12,000-18,000/sq ft (Koramangala)
                - Delhi NCR: ₹8,000-15,000/sq ft (Noida), ₹15,000-25,000/sq ft (Gurgaon Golf Course Road)
                - Pune: ₹5,500-9,000/sq ft (Hinjewadi), ₹8,000-12,000/sq ft (Koregaon Park)
                - Chennai: ₹4,500-8,000/sq ft (OMR), ₹7,000-12,000/sq ft (Adyar)
                - Hyderabad: ₹4,000-7,500/sq ft (Gachibowli), ₹6,000-10,000/sq ft (Jubilee Hills)
                
                HOME LOAN SPECIFICS:
                - SBI Home Loan: 8.50%-9.15% (varies by profile), Max: ₹10 crores, Tenure: 30 years
                - HDFC Home Loan: 8.60%-9.50%, Processing fee: 0.5% of loan amount
                - ICICI Home Loan: 8.75%-9.25%, Pre-approved customers get 0.05% discount
                - Axis Bank: 8.75%-9.40%, Special rates for women borrowers (0.05% discount)
                - LIC Housing: 8.40%-8.90% (lowest rates), Slower processing
                
                DETAILED COST BREAKDOWN:
                - Stamp Duty: 5-7% of property value (varies by state)
                - Registration: 1-2% of property value
                - Home Loan Processing: 0.25-0.50% of loan amount
                - Legal Verification: ₹15,000-25,000
                - Home Insurance: ₹3,000-8,000/year
                - Society Maintenance: ₹2-5/sq ft/month
                - Property Tax: 0.05-0.20% of property value annually
                
                BEST LOCATIONS BY BUDGET & GROWTH:
                - Under ₹50L: Noida Extension, Panvel (Mumbai), Electronic City (Bangalore)
                - ₹50L-1Cr: Greater Noida, Thane, Whitefield (Bangalore), Gachibowli (Hyderabad)
                - ₹1Cr-2Cr: Noida Sectors 137-144, Powai (Mumbai), Koramangala (Bangalore)
                - Above ₹2Cr: Gurgaon Golf Course, Lower Parel (Mumbai), UB City area (Bangalore)
                
                TIMING & INVESTMENT STRATEGY:
                - Best time to buy: Post-monsoon (Oct-Dec) for better deals
                - Under-construction vs Ready: Save 10-15% on under-construction, get possession in 2-3 years
                - Rental yield expectations: 2-4% in metros, 4-6% in tier-2 cities
                - Capital appreciation: 5-8% annually in good locations
                """;
                
            case "VEHICLE_FINANCE" -> """
                VEHICLE FINANCING WITH EXACT CALCULATIONS & RECOMMENDATIONS:
                
                CURRENT AUTO LOAN RATES (2024):
                - SBI Auto Loan: 8.70%-9.70%, Max tenure: 7 years, Processing: ₹2,500 + GST
                - HDFC Bank: 8.75%-16.00% (varies by vehicle age), Max: ₹1 crore
                - ICICI Bank: 8.75%-9.50% for new cars, 9.75%-12.50% for used cars
                - Axis Bank: 8.75%-9.25%, Special rates for premium cars
                - Mahindra Finance: 8.90%-18.00%, Good for commercial vehicles
                
                DETAILED COST ANALYSIS BY VEHICLE CATEGORY:
                
                HATCHBACK (₹5-8 Lakhs):
                - Maruti Swift: ₹6.5L, EMI: ₹12,500 (5yr), Insurance: ₹25,000/yr, Maintenance: ₹25,000/yr
                - Hyundai i20: ₹7.5L, EMI: ₹14,400 (5yr), Insurance: ₹28,000/yr, Maintenance: ₹28,000/yr
                - Tata Altroz: ₹6.8L, EMI: ₹13,100 (5yr), Insurance: ₹26,000/yr, Maintenance: ₹26,000/yr
                
                SEDAN (₹10-15 Lakhs):
                - Honda City: ₹12L, EMI: ₹23,100 (5yr), Insurance: ₹35,000/yr, Maintenance: ₹35,000/yr
                - Hyundai Verna: ₹11.5L, EMI: ₹22,100 (5yr), Insurance: ₹33,000/yr, Maintenance: ₹32,000/yr
                - Maruti Ciaz: ₹9.5L, EMI: ₹18,300 (5yr), Insurance: ₹30,000/yr, Maintenance: ₹28,000/yr
                
                SUV (₹15-25 Lakhs):
                - Hyundai Creta: ₹18L, EMI: ₹34,600 (5yr), Insurance: ₹45,000/yr, Maintenance: ₹40,000/yr
                - Tata Harrier: ₹16.5L, EMI: ₹31,700 (5yr), Insurance: ₹42,000/yr, Maintenance: ₹38,000/yr
                - Mahindra XUV700: ₹15L, EMI: ₹28,800 (5yr), Insurance: ₹40,000/yr, Maintenance: ₹35,000/yr
                
                ELECTRIC VEHICLES (Government Incentives):
                - Tata Nexon EV: ₹15L (after ₹1.5L subsidy), Running cost: ₹1.5/km vs ₹6/km petrol
                - MG ZS EV: ₹22L (after subsidy), Home charging cost: ₹8-10/100km
                - Hyundai Kona: ₹24L (after subsidy), Range: 452km, Charging time: 6 hours
                
                OPTIMAL FINANCING STRATEGY:
                - Down payment: 20-25% to get best rates and lower EMI
                - Loan tenure: 5 years optimal (balance of EMI and interest cost)
                - Pre-closure: After 1 year to save interest (check pre-closure charges)
                                 - Insurance: Comprehensive for first 5 years, third-party after that
                 """;
                
            case "LOAN_FINANCE" -> """
                LOAN & CREDIT EXPERTISE WITH DETAILED RATES & CALCULATIONS:
                
                PERSONAL LOAN RATES (2024):
                - SBI Personal Loan: ₹25K-20L, 11.15%-15.65% interest, 6-year tenure, Processing: ₹2,500
                - HDFC Personal Loan: ₹50K-40L, 10.75%-21% interest, Salary account holders get 0.5% discount
                - ICICI Personal Loan: ₹50K-25L, 10.75%-19% interest, Pre-approved customers get instant approval
                - Axis Bank: ₹1L-15L, 11%-22% interest, Digital process with 24-hour approval
                - Bajaj Finserv: ₹1L-25L, 11%-24% interest, Minimal documentation, Quick disbursal
                
                DETAILED EMI CALCULATIONS:
                - ₹5L loan @ 12% for 3 years: EMI ₹16,607, Total interest ₹97,852
                - ₹10L loan @ 12% for 5 years: EMI ₹22,244, Total interest ₹3,34,640
                - ₹15L loan @ 14% for 5 years: EMI ₹34,859, Total interest ₹6,91,540
                
                CREDIT SCORE IMPACT:
                - Above 750: Best rates (10.75%-12%)
                - 700-750: Standard rates (12%-16%)
                - 650-700: Higher rates (16%-20%)
                - Below 650: Difficult approval, rates 20%+
                
                LOAN CONSOLIDATION OPTIONS:
                - Balance transfer: Save 2-4% interest on existing loans
                - Top-up loans: Additional 20-30% of existing loan amount
                - Debt consolidation: Combine multiple loans into single EMI
                
                PREPAYMENT STRATEGY:
                - No prepayment charges after 1 year for most banks
                - Prepay high-interest loans first
                - Use bonus/windfall for prepayment to save significant interest
                """;
                
            case "LIFE_EVENTS" -> """
                LIFE EVENT FINANCIAL PLANNING WITH SPECIFIC COSTS:
                
                WEDDING PLANNING COSTS:
                - Budget Wedding (100 guests): ₹3-8L (venue, catering, photography, decoration)
                - Mid-range Wedding (200-300 guests): ₹8-20L (good venue, designer outfits, elaborate setup)
                - Luxury Wedding (500+ guests): ₹20L-1Cr+ (premium venues, destination wedding, celebrity management)
                
                DETAILED WEDDING EXPENSE BREAKDOWN:
                - Venue & Catering: 40-50% of total budget
                - Photography & Videography: 8-12% of budget
                - Decoration & Flowers: 10-15% of budget
                - Outfits & Jewelry: 15-20% of budget
                - Music & Entertainment: 5-10% of budget
                - Miscellaneous: 10-15% of budget
                
                MATERNITY & CHILDCARE COSTS:
                - Normal delivery (private hospital): ₹50,000-1.5L
                - C-section delivery: ₹1L-3L
                - Monthly childcare expenses: ₹15,000-30,000 (diapers, formula, medical)
                - Annual school fees: ₹50,000-3L (good private schools)
                - Child insurance: ₹5,000-15,000/year premium
                
                ELDER CARE PLANNING:
                - Home nursing: ₹15,000-25,000/month
                - Assisted living facility: ₹25,000-50,000/month
                - Medical expenses: ₹1-3L/year (including medications)
                - Health insurance for seniors: ₹15,000-40,000/year premium
                
                EMERGENCY FUND FOR LIFE EVENTS:
                - Job loss: 12 months of expenses (₹6-12L typically)
                - Medical emergency: ₹5-10L additional to health insurance
                - Family emergencies: ₹2-5L liquid funds
                - Natural disasters: ₹1-3L for immediate needs
                """;
                
            case "EDUCATION_PLANNING" -> """
                EDUCATION FINANCING WITH SPECIFIC COLLEGES, COSTS & LOAN DETAILS:
                
                TOP ENGINEERING COLLEGES & EXACT COSTS (2024):
                
                IITs (Indian Institute of Technology):
                - IIT Bombay/Delhi/Madras: ₹2.5L/year tuition + ₹1.5L/year hostel = ₹16L total (4 years)
                - IIT Kharagpur/Kanpur/Roorkee: ₹2.2L/year tuition + ₹1.2L/year hostel = ₹13.6L total
                - New IITs (Indore/Hyderabad/Gandhi Nagar): ₹2L/year tuition + ₹1L/year hostel = ₹12L total
                
                NITs (National Institute of Technology):
                - NIT Trichy/Warangal/Surathkal: ₹1.8L/year tuition + ₹80K/year hostel = ₹10.3L total
                - Other NITs: ₹1.5L/year tuition + ₹70K/year hostel = ₹8.8L total
                
                PRIVATE ENGINEERING COLLEGES:
                - BITS Pilani: ₹4.5L/year tuition + ₹1.5L/year hostel = ₹24L total
                - VIT Vellore: ₹2L/year tuition + ₹1L/year hostel = ₹12L total
                - Manipal: ₹3.5L/year tuition + ₹1.2L/year hostel = ₹18.8L total
                - SRM Chennai: ₹2.5L/year tuition + ₹80K/year hostel = ₹13.2L total
                
                MEDICAL COLLEGES:
                - AIIMS Delhi: ₹5,000/year (almost free) + ₹20K/year hostel = ₹1.25L total (5.5 years)
                - Government Medical Colleges: ₹50K/year + ₹30K/year hostel = ₹4.4L total
                - Private Medical Colleges: ₹15-25L/year = ₹80L-1.4Cr total
                
                MBA COLLEGES & COSTS:
                - IIM Ahmedabad/Bangalore/Calcutta: ₹25L total fees (2 years)
                - IIM Lucknow/Indore/Kozhikode: ₹22L total fees
                - ISB Hyderabad: ₹36L total fees (1 year)
                - XLRI Jamshedpur: ₹26L total fees
                - Private MBA: ₹8-15L total fees
                
                STUDY ABROAD COSTS:
                
                USA (Engineering/MBA):
                - Top Universities (MIT/Stanford): $60,000-80,000/year = ₹50-65L/year
                - State Universities: $25,000-35,000/year = ₹20-28L/year
                - Living expenses: $15,000-20,000/year = ₹12-16L/year
                - Total for 2-year Masters: ₹65L-1.6Cr
                
                UK (Engineering/MBA):
                - Oxford/Cambridge: £35,000-45,000/year = ₹35-45L/year
                - Other top universities: £20,000-30,000/year = ₹20-30L/year
                - Living expenses: £12,000-15,000/year = ₹12-15L/year
                - Total for 1-year Masters: ₹32-60L
                
                CANADA (Engineering/MBA):
                - Top universities: CAD 35,000-50,000/year = ₹21-30L/year
                - Living expenses: CAD 15,000-20,000/year = ₹9-12L/year
                - Total for 2-year Masters: ₹60-84L
                
                EDUCATION LOAN DETAILS:
                
                GOVERNMENT BANKS:
                - SBI Education Loan: Up to ₹1.5Cr, 7.50%-10.50% interest, 15-year tenure
                - Bank of Baroda: Up to ₹1Cr, 7.85%-9.85% interest, Collateral needed above ₹7.5L
                - Canara Bank: Up to ₹1Cr, 8.50%-9.50% interest, Processing fee: ₹10,000
                
                PRIVATE BANKS:
                - HDFC Credila: Up to ₹1.5Cr, 9.50%-13.50% interest, No collateral up to ₹40L
                - Axis Bank: Up to ₹75L, 10.75%-13.25% interest, Quick processing
                - ICICI Bank: Up to ₹1Cr, 10.50%-11.50% interest, Covers 100% expenses
                
                SCHOLARSHIP OPPORTUNITIES:
                - Merit-cum-Means: ₹20,000-80,000/year for economically weaker students
                - National Talent Search: ₹1,250/month for Class 11-12, ₹2,000/month for graduation
                - Kishore Vaigyanik Protsahan Yojana: ₹7,000/month + annual grant
                - Corporate Scholarships: Tata, Reliance, Aditya Birla (₹50,000-2L/year)
                
                OPTIMAL EDUCATION FUNDING STRATEGY:
                - Start SIP 10 years before: ₹10,000/month SIP can create ₹20L corpus
                - Education loan for 70-80% of expenses, self-funding for 20-30%
                - Choose government banks for lower interest rates
                - Claim tax benefits under Section 80E (interest deduction)
                """;
                
            case "BUSINESS_FINANCE" -> """
                BUSINESS FINANCING WITH DETAILED STARTUP COSTS & FUNDING OPTIONS:
                
                STARTUP COSTS BY BUSINESS TYPE:
                
                TECH STARTUP (App/Software):
                - Office setup (co-working): ₹15,000-25,000/month for 5-10 people
                - Technology infrastructure: ₹2-5L (servers, software licenses, development tools)
                - Team salaries (6 months): ₹15-30L (2-3 developers, 1 designer, 1 marketing)
                - Legal & compliance: ₹50,000-1L (company registration, IP, contracts)
                - Marketing & customer acquisition: ₹5-10L (first 6 months)
                - Working capital: ₹3-5L
                - Total requirement: ₹25-50L for first year
                
                RESTAURANT/FOOD BUSINESS:
                - Restaurant setup (500 sq ft): ₹8-15L (interior, kitchen equipment, furniture)
                - License & permits: ₹50,000-1L (FSSAI, fire NOC, liquor license if applicable)
                - Initial inventory: ₹1-2L
                - Staff salaries (3 months): ₹3-5L (chef, waiters, manager)
                - Marketing & branding: ₹2-3L
                - Working capital: ₹2-3L
                - Total requirement: ₹16-30L
                
                E-COMMERCE BUSINESS:
                - Inventory (initial stock): ₹5-15L
                - Warehouse setup: ₹2-5L
                - Technology platform: ₹3-8L (website, app, integrations)
                - Marketing (first 6 months): ₹5-10L
                - Team & operations: ₹8-15L
                - Working capital: ₹5-10L
                - Total requirement: ₹28-63L
                
                MANUFACTURING BUSINESS:
                - Machinery & equipment: ₹10-50L (depends on industry)
                - Factory setup: ₹5-20L (rent, utilities, setup)
                - Raw material inventory: ₹3-10L
                - Compliance & licenses: ₹1-3L
                - Working capital: ₹5-15L
                - Total requirement: ₹24-98L
                
                DETAILED FUNDING OPTIONS:
                
                BOOTSTRAPPING:
                - Personal savings: Use 60-70% of available funds, keep 30-40% as emergency
                - Friends & family: ₹5-25L typically, 0-5% interest, flexible terms
                - Revenue-based: Start small, reinvest profits for growth
                
                GOVERNMENT SCHEMES:
                - Startup India Seed Fund: Up to ₹50L grant + ₹2Cr debt
                - MUDRA Loan: Up to ₹10L (Shishu: ₹50K, Kishore: ₹5L, Tarun: ₹10L)
                - Stand-up India: ₹10L-1Cr for SC/ST/Women entrepreneurs
                - SIDBI Loans: ₹5L-100Cr for MSMEs, 8.50%-12% interest
                
                BANK LOANS:
                - SBI Business Loan: ₹1L-200Cr, 8.50%-12.50% interest, 10-year tenure
                - HDFC Business Loan: ₹1L-75Cr, 11%-17% interest, Quick processing
                - ICICI Business Loan: ₹10L-150Cr, 10.75%-16% interest, Minimal documentation
                - Axis Bank: ₹5L-50Cr, 11.25%-16.50% interest, Collateral-free up to ₹1Cr
                
                ANGEL INVESTORS & VCs:
                - Angel investment: ₹25L-5Cr, 5-25% equity, Individual investors
                - Seed funding: ₹50L-10Cr, 10-30% equity, Early-stage VCs
                - Series A: ₹5Cr-50Cr, 15-35% equity, Established VCs
                - Series B & beyond: ₹25Cr+, varies by valuation
                
                TOP ANGEL INVESTOR NETWORKS:
                - Indian Angel Network: 500+ angels, ₹25L-5Cr investments
                - Mumbai Angels: 200+ angels, focus on tech startups
                - Chennai Angels: Strong in healthcare & tech
                - Hyderabad Angels: Focus on B2B and enterprise
                
                MAJOR VC FIRMS:
                - Sequoia Capital: $100K-$100M, focus on tech and consumer
                - Accel Partners: $500K-$25M, early to growth stage
                - Matrix Partners: $1M-$15M, consumer and enterprise
                - Kalaari Capital: $500K-$10M, early-stage focus
                
                BUSINESS REGISTRATION COSTS:
                - Private Limited Company: ₹15,000-25,000 (including professional fees)
                - LLP Registration: ₹10,000-15,000
                - Partnership Firm: ₹5,000-8,000
                - Sole Proprietorship: ₹2,000-5,000
                - GST Registration: Free (mandatory if turnover > ₹20L)
                - Trade License: ₹5,000-15,000 (varies by city)
                
                OPTIMAL FUNDING TIMELINE:
                - Month 1-3: Bootstrap with personal funds + friends/family
                - Month 4-12: Government schemes + bank loans for working capital
                - Month 12-24: Angel investors for scaling (if traction exists)
                - Month 24+: VC funding for rapid expansion
                """;
                
            case "TRAVEL_PLANNING" -> """
                TRAVEL FINANCING WITH DETAILED COSTS & DESTINATIONS:
                
                DOMESTIC TRAVEL COSTS (Per Person):
                
                GOA (5 Days):
                - Flight: ₹8,000-15,000 (Delhi/Mumbai to Goa)
                - Accommodation: ₹3,000-8,000/night (beach resort)
                - Food: ₹1,500-3,000/day
                - Activities: ₹5,000-10,000 (water sports, sightseeing)
                - Total: ₹25,000-60,000
                
                KERALA (7 Days):
                - Flight: ₹6,000-12,000 (to Kochi/Trivandrum)
                - Houseboat: ₹8,000-15,000/night (Alleppey)
                - Hill station stay: ₹4,000-8,000/night (Munnar)
                - Food & transport: ₹2,000-3,500/day
                - Total: ₹35,000-70,000
                
                RAJASTHAN (10 Days):
                - Flight: ₹8,000-15,000 (to Jaipur/Udaipur)
                - Heritage hotels: ₹5,000-15,000/night
                - Car rental: ₹2,500-4,000/day with driver
                - Food & activities: ₹2,000-4,000/day
                - Total: ₹50,000-1,20,000
                
                INTERNATIONAL TRAVEL COSTS:
                
                SOUTHEAST ASIA:
                
                THAILAND (7 Days):
                - Flight: ₹25,000-40,000 (Delhi/Mumbai to Bangkok)
                - Accommodation: ₹2,000-6,000/night (beach resort)
                - Food: ₹1,000-2,500/day
                - Activities: ₹15,000-25,000 (island hopping, temples)
                - Visa: ₹2,000 (on arrival)
                - Total: ₹60,000-1,20,000
                
                SINGAPORE (5 Days):
                - Flight: ₹30,000-50,000
                - Hotel: ₹8,000-15,000/night (city center)
                - Food: ₹2,500-4,000/day
                - Activities: ₹20,000-30,000 (Universal Studios, Marina Bay)
                - Total: ₹80,000-1,50,000
                
                EUROPE:
                
                EUROPE TOUR (15 Days, 5 Countries):
                - Flight: ₹60,000-1,00,000 (round trip)
                - Accommodation: ₹4,000-8,000/night (3-4 star hotels)
                - Food: ₹3,000-5,000/day
                - Transport: ₹25,000-40,000 (Eurail pass + local transport)
                - Activities: ₹50,000-80,000 (museums, tours, experiences)
                - Visa: ₹8,000 (Schengen)
                - Total: ₹2,00,000-4,00,000
                
                UK (10 Days):
                - Flight: ₹50,000-80,000
                - Accommodation: ₹8,000-15,000/night (London)
                - Food: ₹3,500-6,000/day
                - Transport: ₹15,000-25,000 (Oyster card + trains)
                - Activities: ₹40,000-60,000
                - Visa: ₹12,000
                - Total: ₹1,80,000-3,20,000
                
                USA (12 Days):
                - Flight: ₹80,000-1,20,000
                - Accommodation: ₹10,000-20,000/night (major cities)
                - Food: ₹4,000-7,000/day
                - Car rental: ₹3,000-5,000/day
                - Activities: ₹60,000-1,00,000
                - Visa: ₹15,000
                - Total: ₹3,00,000-5,50,000
                
                TRAVEL FINANCING OPTIONS:
                
                TRAVEL LOANS:
                - HDFC Personal Loan: ₹50K-40L, 10.75%-21% interest, 5-year tenure
                - SBI Personal Loan: ₹25K-20L, 11.15%-15.65% interest
                - ICICI Travel Loan: ₹50K-25L, 10.75%-19% interest, Quick approval
                - Bajaj Finserv: ₹1L-25L, 11%-24% interest, Minimal documentation
                
                CREDIT CARD OPTIONS:
                - HDFC Regalia: 4 reward points/₹150, airport lounge access
                - SBI Elite: 5% cashback on travel bookings
                - Axis Magnus: 12 reward points/₹200 on travel, unlimited lounge access
                - ICICI Emeralde: 4 reward points/₹100, travel insurance coverage
                
                FOREX & CURRENCY:
                - USD: ₹82-84 (current rate), carry $100-200 cash + forex card
                - EUR: ₹88-92, use forex card for better rates
                - GBP: ₹100-105, inform bank about travel dates
                - THB: ₹2.3-2.5, exchange in Thailand for better rates
                
                TRAVEL INSURANCE:
                - Domestic: ₹200-500/day (medical + trip cancellation)
                - International: ₹1,000-3,000/day (comprehensive coverage)
                - Adventure sports: Additional ₹500-1,000/day
                - Annual multi-trip: ₹8,000-15,000 (covers multiple trips)
                
                OPTIMAL TRAVEL SAVING STRATEGY:
                - Start travel SIP 12-18 months before trip
                - Book flights 2-3 months in advance for domestic, 3-6 months for international
                - Use travel credit cards for bookings to earn rewards
                - Consider shoulder season travel for 20-30% savings
                                 - Group bookings can save 10-15% on accommodation
                 """;
                
            case "INVESTMENT" -> """
                INVESTMENT & WEALTH BUILDING WITH SPECIFIC RECOMMENDATIONS:
                
                MUTUAL FUND RECOMMENDATIONS WITH CURRENT RETURNS:
                
                LARGE CAP FUNDS:
                - SBI Bluechip Fund: 3-year return 15.2%, Expense ratio 0.62%, Min SIP ₹500
                - HDFC Top 100 Fund: 3-year return 14.8%, Expense ratio 1.25%, Min SIP ₹1,000
                - ICICI Prudential Bluechip Fund: 3-year return 14.5%, Expense ratio 1.05%
                
                MID CAP FUNDS:
                - DSP Midcap Fund: 3-year return 18.5%, Expense ratio 1.25%, Higher volatility
                - HDFC Mid-Cap Opportunities: 3-year return 17.8%, Expense ratio 1.30%
                - Axis Midcap Fund: 3-year return 16.9%, Expense ratio 1.15%
                
                SMALL CAP FUNDS:
                - Axis Small Cap Fund: 3-year return 22.3%, Expense ratio 1.35%, High risk
                - DSP Small Cap Fund: 3-year return 21.8%, Expense ratio 1.25%
                - SBI Small Cap Fund: 3-year return 20.5%, Expense ratio 0.98%
                
                SECTOR-SPECIFIC INVESTMENTS:
                - Technology: ICICI Prudential Technology Fund (3-year: 19.5%)
                - Banking: SBI Banking & PSU Debt Fund (3-year: 8.2%)
                - Healthcare: HDFC Healthcare Fund (3-year: 16.8%)
                - Infrastructure: ICICI Prudential Infrastructure Fund (3-year: 15.2%)
                
                DIRECT STOCK RECOMMENDATIONS BY SECTOR:
                
                IT STOCKS:
                - TCS: Current price ₹3,500, Target ₹4,000, Dividend yield 2.8%
                - Infosys: Current price ₹1,450, Target ₹1,650, Dividend yield 2.5%
                - HCL Tech: Current price ₹1,200, Target ₹1,400, Dividend yield 3.2%
                
                BANKING STOCKS:
                - HDFC Bank: Current price ₹1,650, Target ₹1,850, Dividend yield 1.2%
                - ICICI Bank: Current price ₹950, Target ₹1,100, Dividend yield 0.8%
                - Axis Bank: Current price ₹1,000, Target ₹1,200, Dividend yield 0.6%
                
                SIP STRATEGY WITH EXACT AMOUNTS:
                - ₹5,000/month SIP for 10 years @ 12% return = ₹11.6L corpus
                - ₹10,000/month SIP for 15 years @ 12% return = ₹37L corpus
                - ₹15,000/month SIP for 20 years @ 12% return = ₹99L corpus
                
                PORTFOLIO ALLOCATION BY AGE:
                - Age 25-35: 80% Equity, 15% Debt, 5% Gold
                - Age 35-45: 70% Equity, 25% Debt, 5% Gold
                - Age 45-55: 60% Equity, 35% Debt, 5% Gold
                - Age 55+: 40% Equity, 55% Debt, 5% Gold
                
                TAX-SAVING INVESTMENTS:
                - ELSS Funds: ₹1.5L/year, 3-year lock-in, 12-15% expected returns
                - PPF: ₹1.5L/year, 15-year lock-in, current 7.1% returns
                - NSC: ₹1.5L/year, 5-year lock-in, 6.8% returns
                - Tax-saving FDs: ₹1.5L/year, 5-year lock-in, 5.5-6.5% returns
                """;
                
            default -> """
                COMPREHENSIVE FINANCIAL PLANNING WITH SPECIFIC AMOUNTS & TIMELINES:
                
                EMERGENCY FUND CALCULATION:
                - Target: 6-12 months of expenses
                - Monthly expenses ₹50K → Emergency fund: ₹3-6L
                - Keep in liquid funds: SBI Liquid Fund (4-5% returns), HDFC Liquid Fund
                - Build timeline: 12-18 months with ₹15,000-25,000/month SIP
                
                RETIREMENT PLANNING WITH EXACT CORPUS:
                - Age 30, Retirement 60: Need ₹8-12 crores for comfortable retirement
                - Monthly SIP required: ₹15,000-25,000 in equity funds (12% assumed return)
                - PPF contribution: ₹1.5L/year (15-year lock-in, tax-free returns)
                - NPS contribution: ₹50,000/year (additional tax benefit + employer contribution)
                
                INSURANCE COVERAGE:
                - Life insurance: 10-15x annual income (₹50L income → ₹5-7.5Cr coverage)
                - Health insurance: ₹10-20L family floater + ₹50L-1Cr super top-up
                - Term insurance: ₹8,000-15,000/year premium for ₹1Cr coverage (age 30)
                
                TAX SAVING INVESTMENTS (Section 80C):
                - ELSS Mutual Funds: ₹1.5L/year, 3-year lock-in, potential 12-15% returns
                - PPF: ₹1.5L/year, 15-year lock-in, current 7.1% returns
                - NSC: ₹1.5L/year, 5-year lock-in, 6.8% returns
                - ULIP: ₹1.5L/year, 5-year lock-in, market-linked returns
                """;
        };
    }

    private String enhanceFinancialResponse(String llmResponse, ChatRequest request) {
        StringBuilder enhancedResponse = new StringBuilder();
        
        // Add personalized greeting if profile is available
        if (request.getFinancialProfile() != null && request.getFinancialProfile().getAge() != null) {
            enhancedResponse.append("🎯 **Personalized Financial Advice for You**\n\n");
        }
        
        // Add the LLM response
        enhancedResponse.append(llmResponse);
        
        // Add relevant quick actions or follow-up suggestions
        enhancedResponse.append("\n\n");
        enhancedResponse.append(generateQuickActions(request));
        
        // Add standard disclaimer
        enhancedResponse.append("\n\n⚠️ **Important Disclaimer**: This advice is for educational purposes only. ");
        enhancedResponse.append("Please consult with a qualified financial advisor for decisions specific to your situation. ");
        enhancedResponse.append("All investments are subject to market risks.");
        
        return enhancedResponse.toString();
    }

    private String generateQuickActions(ChatRequest request) {
        String questionType = detectQuestionType(request.getMessage());
        
        return switch (questionType) {
            case "REAL_ESTATE" -> """
                🏠 **Next Steps for Property Planning:**
                • Use our comprehensive-guidance API for complete investment planning
                • Calculate your home loan eligibility and EMI
                • Research property locations and market trends
                • Plan your down payment and additional costs
                • Consider property insurance and legal verification
                """;
                
            case "VEHICLE_FINANCE" -> """
                🚗 **Next Steps for Vehicle Planning:**
                • Compare auto loan offers from different banks
                • Calculate total cost of ownership including insurance
                • Consider new vs. used vehicle options
                • Plan for vehicle maintenance and depreciation
                • Explore electric vehicle incentives if applicable
                """;
                
            case "EDUCATION_PLANNING" -> """
                🎓 **Next Steps for Education Planning:**
                • Research education loan options and eligibility
                • Start a dedicated education savings plan
                • Explore scholarship and grant opportunities
                • Consider international study financing if applicable
                • Plan for education inflation over time
                """;
                
            case "BUSINESS_FINANCE" -> """
                💼 **Next Steps for Business Planning:**
                • Prepare a detailed business plan and financial projections
                • Research funding options suitable for your business stage
                • Consider business registration and compliance requirements
                • Plan for working capital and cash flow management
                • Explore business insurance and risk management
                """;
                
            default -> """
                💡 **Recommended Next Steps:**
                • Use our comprehensive-guidance API for complete financial planning
                • Consider creating a detailed financial profile for personalized advice
                • Start with small, consistent steps toward your financial goals
                • Review and adjust your plan regularly
                • Seek professional advice for complex decisions
                """;
        };
    }
    
    private String generateComprehensiveFinancialResponse(ChatRequest request) {
        StringBuilder response = new StringBuilder();
        
        // Add personalized greeting if profile is available
        if (request.getFinancialProfile() != null && request.getFinancialProfile().getAge() != null) {
            response.append("🎯 **Personalized Financial Advice for You**\n\n");
        }
        
        // Detect question type and generate specific response
        String questionType = detectQuestionType(request.getMessage());
        String specificAdvice = generateSpecificAdvice(request, questionType);
        
        response.append(specificAdvice);
        
        // Add relevant quick actions
        response.append("\n\n");
        response.append(generateQuickActions(request));
        
        // Add standard disclaimer
        response.append("\n\n⚠️ **Important Disclaimer**: This advice is for educational purposes only. ");
        response.append("Please consult with a qualified financial advisor for decisions specific to your situation. ");
        response.append("All investments are subject to market risks.");
        
        return response.toString();
    }
    
    private String generateSpecificAdvice(ChatRequest request, String questionType) {
        FinancialProfile profile = request.getFinancialProfile();
        String message = request.getMessage().toLowerCase();
        
        return switch (questionType) {
            case "REAL_ESTATE" -> generateRealEstateAdvice(profile, message);
            case "VEHICLE_FINANCE" -> generateVehicleFinanceAdvice(profile, message);
            case "EDUCATION_PLANNING" -> generateEducationPlanningAdvice(profile, message);
            case "BUSINESS_FINANCE" -> generateBusinessFinanceAdvice(profile, message);
            case "TRAVEL_PLANNING" -> generateTravelPlanningAdvice(profile, message);
            case "LIFE_EVENTS" -> generateLifeEventsAdvice(profile, message);
            case "LOAN_FINANCE" -> generateLoanFinanceAdvice(profile, message);
            case "INVESTMENT" -> generateInvestmentAdvice(profile, message);
            default -> generateGeneralFinancialAdvice(profile, message);
        };
    }
    
    private String generateRealEstateAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("🏠 **Real Estate & Property Planning**\n\n");
        
        if (message.contains("bangalore") || message.contains("bengaluru")) {
            advice.append("📍 **Best Areas in Bangalore for Your Budget:**\n\n");
            advice.append("**Electronic City (₹6,000-12,000/sq ft):**\n");
            advice.append("- 2BHK (1,200 sq ft): ₹72L-1.44Cr\n");
            advice.append("- Pros: IT hub proximity, metro connectivity, good infrastructure\n");
            advice.append("- Cons: Distance from city center, traffic during peak hours\n\n");
            
            advice.append("**Whitefield (₹8,000-15,000/sq ft):**\n");
            advice.append("- 2BHK (1,100 sq ft): ₹88L-1.65Cr\n");
            advice.append("- Pros: Major IT companies, airport proximity, established area\n");
            advice.append("- Cons: Water scarcity issues, higher maintenance costs\n\n");
        } else if (message.contains("mumbai") || message.contains("bombay")) {
            advice.append("📍 **Best Areas in Mumbai for Your Budget:**\n\n");
            advice.append("**Andheri (₹15,000-25,000/sq ft):**\n");
            advice.append("- 2BHK (800 sq ft): ₹1.2Cr-2Cr\n");
            advice.append("- Pros: Central location, good connectivity, commercial hub\n");
            advice.append("- Cons: High density, expensive maintenance\n\n");
            
            advice.append("**Thane (₹8,000-15,000/sq ft):**\n");
            advice.append("- 2BHK (1,000 sq ft): ₹80L-1.5Cr\n");
            advice.append("- Pros: More space, relatively affordable, good infrastructure\n");
            advice.append("- Cons: Longer commute to South Mumbai\n\n");
        }
        
        advice.append("💰 **Home Loan Comparison:**\n\n");
        advice.append("**SBI Home Loan (Recommended):**\n");
        advice.append("- Interest rate: 8.50%-9.15%\n");
        advice.append("- Processing fee: 0.5% of loan amount\n");
        advice.append("- Max tenure: 30 years\n");
        advice.append("- Special rates for women borrowers\n\n");
        
        advice.append("**HDFC Home Loan:**\n");
        advice.append("- Interest rate: 8.60%-9.50%\n");
        advice.append("- Quick processing: 7-10 days\n");
        advice.append("- Pre-approved customers get discount\n\n");
        
        if (profile != null && profile.getAge() != null && profile.getIncomeRange() != null) {
            advice.append("📊 **Personalized Recommendation:**\n");
            advice.append("- Your age: ").append(profile.getAge()).append(" years\n");
            advice.append("- Recommended loan tenure: ").append(profile.getAge() < 35 ? "25-30 years" : "15-20 years").append("\n");
            advice.append("- Suggested down payment: 20-25% of property value\n");
        }
        
        return advice.toString();
    }
    
    private String generateVehicleFinanceAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("🚗 **Vehicle Finance Planning**\n\n");
        
        if (message.contains("car") || message.contains("sedan") || message.contains("suv")) {
            advice.append("🚙 **Popular Car Options with EMI Details:**\n\n");
            
            advice.append("**HATCHBACK SEGMENT:**\n");
            advice.append("- Maruti Swift: ₹6.5L, EMI: ₹12,500/month (5 years)\n");
            advice.append("- Hyundai i20: ₹7.5L, EMI: ₹14,400/month (5 years)\n");
            advice.append("- Insurance: ₹25,000-28,000/year\n\n");
            
            advice.append("**SEDAN SEGMENT:**\n");
            advice.append("- Honda City: ₹12L, EMI: ₹23,100/month (5 years)\n");
            advice.append("- Hyundai Verna: ₹11.5L, EMI: ₹22,100/month (5 years)\n");
            advice.append("- Insurance: ₹32,000-35,000/year\n\n");
            
            advice.append("**SUV SEGMENT:**\n");
            advice.append("- Hyundai Creta: ₹18L, EMI: ₹34,600/month (5 years)\n");
            advice.append("- Tata Harrier: ₹16.5L, EMI: ₹31,700/month (5 years)\n");
            advice.append("- Insurance: ₹40,000-45,000/year\n\n");
        }
        
        advice.append("🏦 **Auto Loan Comparison:**\n\n");
        advice.append("**SBI Auto Loan:**\n");
        advice.append("- Interest rate: 8.70%-9.70%\n");
        advice.append("- Max tenure: 7 years\n");
        advice.append("- Processing fee: ₹2,500 + GST\n\n");
        
        advice.append("**HDFC Auto Loan:**\n");
        advice.append("- Interest rate: 8.75%-16.00%\n");
        advice.append("- Quick approval: 24-48 hours\n");
        advice.append("- Max loan amount: ₹1 crore\n\n");
        
        if (profile != null && profile.getCurrentSavings() != null) {
            advice.append("💡 **Personalized Recommendation:**\n");
            if (profile.getCurrentSavings().compareTo(java.math.BigDecimal.valueOf(500000)) > 0) {
                advice.append("- With your savings of ₹").append(profile.getCurrentSavings()).append(", consider 25-30% down payment\n");
                advice.append("- This will reduce your EMI and total interest cost\n");
            } else {
                advice.append("- Consider minimal down payment to preserve liquidity\n");
                advice.append("- Focus on shorter loan tenure to reduce total interest\n");
            }
        }
        
        return advice.toString();
    }
    
    private String generateEducationPlanningAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("🎓 **Education Planning & College Selection**\n\n");
        
        if (message.contains("engineering") || message.contains("iit") || message.contains("nit")) {
            advice.append("🏛️ **Top Engineering Colleges & Costs:**\n\n");
            
            advice.append("**IIT (Government - Best Value):**\n");
            advice.append("- IIT Bombay/Delhi/Madras: ₹16L total (4 years)\n");
            advice.append("- Average placement: ₹15-25 LPA\n");
            advice.append("- ROI: Excellent (payback in 1-2 years)\n\n");
            
            advice.append("**NIT (Government - Good Value):**\n");
            advice.append("- NIT Trichy/Warangal: ₹10.3L total (4 years)\n");
            advice.append("- Average placement: ₹8-15 LPA\n");
            advice.append("- ROI: Very good\n\n");
            
            advice.append("**Private Colleges:**\n");
            advice.append("- BITS Pilani: ₹24L total (4 years)\n");
            advice.append("- VIT Vellore: ₹12L total (4 years)\n");
            advice.append("- Manipal: ₹18.8L total (4 years)\n\n");
        }
        
        if (message.contains("abroad") || message.contains("usa") || message.contains("uk")) {
            advice.append("🌍 **Study Abroad Costs:**\n\n");
            advice.append("**USA (2-year Masters):**\n");
            advice.append("- Top universities: ₹65L-1.6Cr total\n");
            advice.append("- State universities: ₹40L-56L total\n\n");
            
            advice.append("**UK (1-year Masters):**\n");
            advice.append("- Oxford/Cambridge: ₹47-60L total\n");
            advice.append("- Other top universities: ₹32-45L total\n\n");
        }
        
        advice.append("💰 **Education Loan Options:**\n\n");
        advice.append("**SBI Education Loan:**\n");
        advice.append("- Up to ₹1.5Cr, 7.50%-10.50% interest\n");
        advice.append("- Moratorium during study + 1 year\n\n");
        
        advice.append("**HDFC Credila:**\n");
        advice.append("- Up to ₹1.5Cr, 9.50%-13.50% interest\n");
        advice.append("- No collateral up to ₹40L\n\n");
        
        if (profile != null && profile.getAge() != null) {
            advice.append("📅 **Planning Timeline:**\n");
            int yearsToCollege = profile.getAge() > 40 ? 3 : 15; // Assume parent vs student
            advice.append("- Start education SIP: ₹10,000-20,000/month\n");
            advice.append("- Time available: ~").append(yearsToCollege).append(" years\n");
            advice.append("- Target corpus: ₹15-25L for domestic, ₹50L+ for abroad\n");
        }
        
        return advice.toString();
    }
    
    private String generateBusinessFinanceAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("💼 **Business Startup Financing**\n\n");
        
        if (message.contains("tech") || message.contains("app") || message.contains("software")) {
            advice.append("💻 **Tech Startup Costs:**\n");
            advice.append("- Office setup: ₹15,000-25,000/month\n");
            advice.append("- Technology infrastructure: ₹2-5L\n");
            advice.append("- Team salaries (6 months): ₹15-30L\n");
            advice.append("- Marketing: ₹5-10L\n");
            advice.append("- **Total requirement: ₹25-50L**\n\n");
        } else if (message.contains("restaurant") || message.contains("food")) {
            advice.append("🍽️ **Restaurant Business Costs:**\n");
            advice.append("- Setup (500 sq ft): ₹8-15L\n");
            advice.append("- Licenses & permits: ₹50,000-1L\n");
            advice.append("- Initial inventory: ₹1-2L\n");
            advice.append("- Staff salaries (3 months): ₹3-5L\n");
            advice.append("- **Total requirement: ₹16-30L**\n\n");
        }
        
        advice.append("💰 **Funding Options:**\n\n");
        advice.append("**Government Schemes:**\n");
        advice.append("- MUDRA Loan: Up to ₹10L\n");
        advice.append("- Startup India Seed Fund: Up to ₹50L grant\n");
        advice.append("- Stand-up India: ₹10L-1Cr for women/SC/ST\n\n");
        
        advice.append("**Bank Loans:**\n");
        advice.append("- SBI Business Loan: 8.50%-12.50% interest\n");
        advice.append("- HDFC Business Loan: 11%-17% interest\n");
        advice.append("- Collateral-free up to ₹1Cr\n\n");
        
        advice.append("**Angel Investors:**\n");
        advice.append("- Investment: ₹25L-5Cr\n");
        advice.append("- Equity: 5-25%\n");
        advice.append("- Indian Angel Network, Mumbai Angels\n\n");
        
        if (profile != null && profile.getCurrentSavings() != null) {
            advice.append("📊 **Recommended Funding Mix:**\n");
            advice.append("- Your savings: ₹").append(profile.getCurrentSavings()).append("\n");
            if (profile.getCurrentSavings().compareTo(java.math.BigDecimal.valueOf(1000000)) > 0) {
                advice.append("- Use 60-70% of savings for business\n");
                advice.append("- Bank loan for remaining 30-40%\n");
            } else {
                advice.append("- Consider government schemes first\n");
                advice.append("- Bank loan for major portion\n");
            }
        }
        
        return advice.toString();
    }
    
    private String generateTravelPlanningAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("✈️ **Travel Planning & Financing**\n\n");
        
        if (message.contains("europe") || message.contains("international")) {
            advice.append("🌍 **International Travel Costs:**\n\n");
            advice.append("**Europe (15 days, 5 countries):**\n");
            advice.append("- Flight: ₹60,000-1,00,000\n");
            advice.append("- Accommodation: ₹4,000-8,000/night\n");
            advice.append("- Food: ₹3,000-5,000/day\n");
            advice.append("- **Total: ₹2,00,000-4,00,000**\n\n");
            
            advice.append("**USA (12 days):**\n");
            advice.append("- Flight: ₹80,000-1,20,000\n");
            advice.append("- Accommodation: ₹10,000-20,000/night\n");
            advice.append("- **Total: ₹3,00,000-5,50,000**\n\n");
        } else {
            advice.append("🇮🇳 **Domestic Travel Costs:**\n\n");
            advice.append("**Goa (5 days):**\n");
            advice.append("- Flight: ₹8,000-15,000\n");
            advice.append("- Resort: ₹3,000-8,000/night\n");
            advice.append("- **Total: ₹25,000-60,000**\n\n");
            
            advice.append("**Kerala (7 days):**\n");
            advice.append("- Flight: ₹6,000-12,000\n");
            advice.append("- Houseboat: ₹8,000-15,000/night\n");
            advice.append("- **Total: ₹35,000-70,000**\n\n");
        }
        
        advice.append("💳 **Travel Financing Options:**\n\n");
        advice.append("**Travel Loans:**\n");
        advice.append("- HDFC Personal Loan: ₹50K-40L, 10.75%-21%\n");
        advice.append("- SBI Personal Loan: ₹25K-20L, 11.15%-15.65%\n\n");
        
        advice.append("**Credit Cards:**\n");
        advice.append("- HDFC Regalia: 4 points/₹150, lounge access\n");
        advice.append("- Axis Magnus: 12 points/₹200, unlimited lounge\n\n");
        
        advice.append("💡 **Smart Travel Saving:**\n");
        advice.append("- Start travel SIP 12-18 months before\n");
        advice.append("- Book flights 2-3 months in advance\n");
        advice.append("- Consider shoulder season for 20-30% savings\n");
        
        return advice.toString();
    }
    
    private String generateLifeEventsAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("💍 **Life Event Financial Planning**\n\n");
        
        if (message.contains("wedding") || message.contains("marriage")) {
            advice.append("💒 **Wedding Planning Costs:**\n\n");
            advice.append("**Budget Wedding (100 guests):**\n");
            advice.append("- Total cost: ₹3-8L\n");
            advice.append("- Venue & catering: 40-50% of budget\n");
            advice.append("- Photography: 8-12% of budget\n\n");
            
            advice.append("**Mid-range Wedding (200-300 guests):**\n");
            advice.append("- Total cost: ₹8-20L\n");
            advice.append("- Good venues, designer outfits\n\n");
            
            advice.append("**Luxury Wedding (500+ guests):**\n");
            advice.append("- Total cost: ₹20L-1Cr+\n");
            advice.append("- Premium venues, destination wedding\n\n");
        }
        
        if (message.contains("child") || message.contains("baby")) {
            advice.append("👶 **Childcare Costs:**\n\n");
            advice.append("**Delivery Costs:**\n");
            advice.append("- Normal delivery: ₹50,000-1.5L\n");
            advice.append("- C-section: ₹1L-3L\n\n");
            
            advice.append("**Monthly Childcare:**\n");
            advice.append("- Diapers, formula, medical: ₹15,000-30,000/month\n");
            advice.append("- Annual school fees: ₹50,000-3L\n\n");
        }
        
        advice.append("💰 **Financing Options:**\n");
        advice.append("- Personal loans: 10.75%-21% interest\n");
        advice.append("- Wedding loans: Specialized products available\n");
        advice.append("- Family support: 0-5% interest typically\n\n");
        
        advice.append("📅 **Planning Timeline:**\n");
        advice.append("- Start saving 12-18 months in advance\n");
        advice.append("- Create dedicated SIP for the event\n");
        advice.append("- Consider insurance for protection\n");
        
        return advice.toString();
    }
    
    private String generateLoanFinanceAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("💳 **Loan & Credit Solutions**\n\n");
        
        advice.append("🏦 **Personal Loan Rates (2024):**\n\n");
        advice.append("**SBI Personal Loan:**\n");
        advice.append("- Amount: ₹25K-20L\n");
        advice.append("- Interest: 11.15%-15.65%\n");
        advice.append("- Tenure: Up to 6 years\n\n");
        
        advice.append("**HDFC Personal Loan:**\n");
        advice.append("- Amount: ₹50K-40L\n");
        advice.append("- Interest: 10.75%-21%\n");
        advice.append("- Salary account holders get 0.5% discount\n\n");
        
        advice.append("**ICICI Personal Loan:**\n");
        advice.append("- Amount: ₹50K-25L\n");
        advice.append("- Interest: 10.75%-19%\n");
        advice.append("- Pre-approved customers get instant approval\n\n");
        
        advice.append("📊 **EMI Calculations:**\n");
        advice.append("- ₹5L @ 12% for 3 years: EMI ₹16,607\n");
        advice.append("- ₹10L @ 12% for 5 years: EMI ₹22,244\n");
        advice.append("- ₹15L @ 14% for 5 years: EMI ₹34,859\n\n");
        
        advice.append("💡 **Credit Score Impact:**\n");
        advice.append("- Above 750: Best rates (10.75%-12%)\n");
        advice.append("- 700-750: Standard rates (12%-16%)\n");
        advice.append("- 650-700: Higher rates (16%-20%)\n");
        advice.append("- Below 650: Difficult approval\n\n");
        
        advice.append("🎯 **Optimization Tips:**\n");
        advice.append("- Compare rates from 3-4 banks\n");
        advice.append("- Check for pre-approved offers\n");
        advice.append("- Consider balance transfer for existing loans\n");
        advice.append("- Prepay high-interest loans first\n");
        
        return advice.toString();
    }
    
    private String generateInvestmentAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("📈 **Investment & Wealth Building**\n\n");
        
        advice.append("🎯 **Mutual Fund Recommendations:**\n\n");
        advice.append("**Large Cap Funds:**\n");
        advice.append("- SBI Bluechip Fund: 15.2% (3-year return)\n");
        advice.append("- HDFC Top 100: 14.8% (3-year return)\n");
        advice.append("- Min SIP: ₹500-1,000\n\n");
        
        advice.append("**Mid Cap Funds:**\n");
        advice.append("- DSP Midcap Fund: 18.5% (3-year return)\n");
        advice.append("- Higher volatility, higher returns\n\n");
        
        advice.append("**Small Cap Funds:**\n");
        advice.append("- Axis Small Cap: 22.3% (3-year return)\n");
        advice.append("- High risk, high reward potential\n\n");
        
        if (message.contains("sip") || message.contains("systematic")) {
            advice.append("💰 **SIP Strategy:**\n");
            advice.append("- ₹5,000/month for 10 years @ 12% = ₹11.6L corpus\n");
            advice.append("- ₹10,000/month for 15 years @ 12% = ₹37L corpus\n");
            advice.append("- ₹15,000/month for 20 years @ 12% = ₹99L corpus\n\n");
        }
        
        if (profile != null && profile.getAge() != null) {
            advice.append("🎯 **Age-based Portfolio:**\n");
            if (profile.getAge() < 35) {
                advice.append("- Equity: 80%, Debt: 15%, Gold: 5%\n");
                advice.append("- Focus on growth and wealth creation\n");
            } else if (profile.getAge() < 50) {
                advice.append("- Equity: 70%, Debt: 25%, Gold: 5%\n");
                advice.append("- Balance growth with stability\n");
            } else {
                advice.append("- Equity: 50%, Debt: 45%, Gold: 5%\n");
                advice.append("- Focus on capital preservation\n");
            }
        }
        
        advice.append("💡 **Tax-Saving Options:**\n");
        advice.append("- ELSS Funds: ₹1.5L/year, 3-year lock-in\n");
        advice.append("- PPF: ₹1.5L/year, 15-year lock-in, 7.1% return\n");
        advice.append("- NSC: ₹1.5L/year, 5-year lock-in, 6.8% return\n");
        
        return advice.toString();
    }
    
    private String generateGeneralFinancialAdvice(FinancialProfile profile, String message) {
        StringBuilder advice = new StringBuilder();
        advice.append("💡 **General Financial Planning**\n\n");
        
        if (profile != null) {
            if (profile.getAge() != null) {
                advice.append("📅 **Age-based Financial Goals (").append(profile.getAge()).append(" years):**\n");
                if (profile.getAge() < 30) {
                    advice.append("- Build emergency fund (6 months expenses)\n");
                    advice.append("- Start investing early for compounding\n");
                    advice.append("- Focus on career growth and skill development\n");
                } else if (profile.getAge() < 40) {
                    advice.append("- Target 3x annual salary in savings\n");
                    advice.append("- Consider home ownership\n");
                    advice.append("- Increase retirement contributions\n");
                } else if (profile.getAge() < 50) {
                    advice.append("- Peak earning years - maximize savings\n");
                    advice.append("- Plan for children's education\n");
                    advice.append("- Review insurance coverage\n");
                } else {
                    advice.append("- Focus on retirement planning\n");
                    advice.append("- Consider debt reduction\n");
                    advice.append("- Plan healthcare coverage\n");
                }
                advice.append("\n");
            }
            
            if (profile.getRiskTolerance() != null) {
                advice.append("🎯 **Investment Strategy (").append(profile.getRiskTolerance().name()).append(" Risk):**\n");
                switch (profile.getRiskTolerance()) {
                    case CONSERVATIVE:
                        advice.append("- Focus on fixed deposits, bonds, and debt funds\n");
                        advice.append("- 30% equity, 70% debt allocation\n");
                        break;
                    case MODERATE:
                        advice.append("- Balanced portfolio with diversified funds\n");
                        advice.append("- 60% equity, 40% debt allocation\n");
                        break;
                    case AGGRESSIVE:
                        advice.append("- Growth-focused with equity emphasis\n");
                        advice.append("- 80% equity, 20% debt allocation\n");
                        break;
                }
                advice.append("\n");
            }
        }
        
        advice.append("💰 **Essential Financial Planning:**\n");
        advice.append("- Emergency fund: 6-12 months expenses\n");
        advice.append("- Life insurance: 10-15x annual income\n");
        advice.append("- Health insurance: ₹10-20L family coverage\n");
        advice.append("- Retirement planning: Start early, invest regularly\n\n");
        
        advice.append("📈 **Investment Priorities:**\n");
        advice.append("1. Emergency fund in liquid investments\n");
        advice.append("2. Employer PF/401k matching\n");
        advice.append("3. Tax-saving investments (80C)\n");
        advice.append("4. Long-term wealth creation through equity\n");
        advice.append("5. Diversification across asset classes\n");
        
        return advice.toString();
    }
    
    private Integer estimateTokenCount(String text) {
        // Simple token estimation (roughly 4 characters per token)
        return Math.max(1, text.length() / 4);
    }
}