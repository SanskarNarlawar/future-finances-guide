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
            // Build enhanced prompt with financial context
            String enhancedPrompt = buildFinancialAdvisoryPrompt(request);
            
            // Create enhanced request for LLM
            ChatRequest llmRequest = new ChatRequest();
            llmRequest.setMessage(enhancedPrompt);
            llmRequest.setSessionId(request.getSessionId());
            llmRequest.setModelName(request.getModelName() != null ? request.getModelName() : "gpt-3.5-turbo");
            llmRequest.setMaxTokens(request.getMaxTokens() != null ? request.getMaxTokens() : 1500);
            llmRequest.setTemperature(request.getTemperature() != null ? request.getTemperature() : 0.7);
            
            // Get LLM response
            ChatResponse llmResponse = llmService.generateResponse(llmRequest);
            
            // Enhance response with financial advisory context
            return ChatResponse.builder()
                    .id(UUID.randomUUID().toString())
                    .sessionId(request.getSessionId())
                    .message(enhanceFinancialResponse(llmResponse.getMessage(), request))
                    .modelName(llmResponse.getModelName())
                    .createdAt(LocalDateTime.now())
                    .tokenCount(llmResponse.getTokenCount())
                    .totalTokens(llmResponse.getTotalTokens())
                    .promptTokens(llmResponse.getPromptTokens())
                    .completionTokens(llmResponse.getCompletionTokens())
                    .advisoryMode(request.getAdvisoryMode() != null ? request.getAdvisoryMode().name() : null)
                    .profileBased(request.getFinancialProfile() != null)
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
        prompt.append("RESPONSE GUIDELINES:\n");
        prompt.append("1. Provide specific, actionable advice tailored to the user's profile\n");
        prompt.append("2. Include relevant calculations, timelines, and financial projections when applicable\n");
        prompt.append("3. Mention specific Indian financial products, banks, and investment options\n");
        prompt.append("4. Consider tax implications and regulatory aspects in India\n");
        prompt.append("5. Provide step-by-step action plans where appropriate\n");
        prompt.append("6. Include important disclaimers about financial risks\n");
        prompt.append("7. Use emojis and formatting to make the response engaging and easy to read\n\n");
        
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
                REAL ESTATE & PROPERTY EXPERTISE:
                - Home loan eligibility, interest rates, and EMI calculations
                - Property valuation, location analysis, and market trends
                - Down payment planning and funding strategies
                - Stamp duty, registration costs, and hidden expenses
                - RERA compliance, legal verification, and documentation
                - Property investment vs. self-occupation analysis
                - Tax benefits under Section 80C, 24(b), and capital gains
                - Home insurance and property maintenance costs
                - Rental yield calculations and property management
                - Real estate market cycles and timing considerations
                """;
                
            case "VEHICLE_FINANCE" -> """
                VEHICLE FINANCING EXPERTISE:
                - Auto loan vs. personal loan comparison
                - Down payment optimization and loan tenure planning
                - Interest rate negotiations and bank comparisons
                - Vehicle insurance, maintenance, and depreciation costs
                - New vs. used vehicle financial analysis
                - Electric vehicle incentives and financing options
                - Vehicle loan prepayment strategies
                - Two-wheeler vs. four-wheeler financing decisions
                - Commercial vehicle financing for business use
                - Vehicle upgrade and replacement planning
                """;
                
            case "LOAN_FINANCE" -> """
                LOAN & CREDIT EXPERTISE:
                - Personal loan, business loan, and specialized lending options
                - Interest rate types (fixed vs. floating) and negotiations
                - EMI calculations, loan tenure optimization, and prepayment strategies
                - Credit score improvement and loan eligibility enhancement
                - Loan consolidation and debt restructuring options
                - Collateral vs. unsecured loan considerations
                - Co-applicant benefits and joint loan applications
                - Loan insurance and protection schemes
                - Balance transfer options and refinancing strategies
                - Default management and legal implications
                """;
                
            case "EDUCATION_PLANNING" -> """
                EDUCATION FINANCING EXPERTISE:
                - Education loan eligibility, limits, and interest rates
                - Collateral vs. non-collateral education loans
                - Study abroad financing and forex considerations
                - Education savings plans and child-specific investments
                - Scholarship opportunities and grant applications
                - Professional course financing (MBA, medical, engineering)
                - Education insurance and student protection plans
                - Tax benefits on education expenses and loan interest
                - Career ROI analysis and course selection guidance
                - Education inflation planning and corpus calculation
                """;
                
            case "BUSINESS_FINANCE" -> """
                BUSINESS & ENTREPRENEURSHIP EXPERTISE:
                - Startup funding options (bootstrapping, angel investors, VCs)
                - Business loan types and eligibility criteria
                - Working capital management and cash flow planning
                - Business registration, compliance, and tax planning
                - Partnership vs. proprietorship vs. company structures
                - Business insurance and risk management
                - Equipment financing and asset acquisition strategies
                - Export-import financing and trade finance
                - Business expansion funding and scaling strategies
                - Exit planning and business valuation methods
                """;
                
            case "LIFE_EVENTS" -> """
                LIFE EVENT FINANCIAL PLANNING:
                - Marriage and wedding expense planning
                - Joint financial planning for couples
                - Family protection through insurance and investments
                - Maternity and childcare financial preparation
                - Elder care and parents' financial support
                - Divorce financial planning and asset division
                - Emergency planning for health and job loss
                - Relocation and job change financial management
                - Festival and celebration budgeting
                - Legacy planning and wealth transfer strategies
                """;
                
            case "TRAVEL_PLANNING" -> """
                TRAVEL & LIFESTYLE FINANCING:
                - Travel savings and vacation fund planning
                - Travel insurance and international coverage
                - Forex planning and currency exchange strategies
                - Travel loans and credit card benefits
                - International investment and NRI planning
                - Travel budgeting and expense management
                - Frequent traveler financial optimization
                - Business travel expense management
                - Adventure and luxury travel financing
                - Travel emergency fund and contingency planning
                """;
                
            case "INVESTMENT" -> """
                INVESTMENT & WEALTH BUILDING EXPERTISE:
                - Comprehensive portfolio construction and optimization
                - Tax-efficient investment strategies and planning
                - Risk assessment and diversification techniques
                - Market timing and systematic investment approaches
                - Alternative investments (REITs, gold, commodities)
                - International diversification and global exposure
                - Retirement planning and pension optimization
                - Estate planning and wealth transfer strategies
                - Performance monitoring and rebalancing techniques
                - Behavioral finance and investment psychology
                """;
                
            default -> """
                COMPREHENSIVE FINANCIAL PLANNING:
                - Holistic financial health assessment and improvement
                - Goal-based financial planning and prioritization
                - Cash flow management and budgeting techniques
                - Risk management through insurance and diversification
                - Tax planning and optimization strategies
                - Investment planning across asset classes
                - Retirement and post-retirement financial security
                - Estate planning and wealth preservation
                - Financial discipline and behavioral coaching
                - Regular review and adjustment strategies
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
}