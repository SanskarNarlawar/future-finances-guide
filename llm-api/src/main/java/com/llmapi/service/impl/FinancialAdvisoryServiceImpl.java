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
        
        // Create personalized system context
        String systemPrompt = createPersonalizedSystemPrompt(
            request.getFinancialProfile(), 
            request.getAdvisoryMode()
        );
        
        // Enhance the user message with context
        String enhancedMessage = enhanceMessageWithContext(request);
        
        // Create a new request with enhanced context
        ChatRequest enhancedRequest = new ChatRequest();
        enhancedRequest.setMessage(enhancedMessage);
        enhancedRequest.setSessionId(request.getSessionId());
        enhancedRequest.setModelName(request.getModelName());
        enhancedRequest.setMaxTokens(request.getMaxTokens());
        enhancedRequest.setTemperature(0.7); // Slightly more creative for advisory
        
        // Save system context as assistant message for continuity
        if (request.getFinancialProfile() != null) {
            saveSystemContext(request.getSessionId(), systemPrompt);
        }
        
        // Generate response using base LLM service
        ChatResponse response = llmService.generateResponse(enhancedRequest);
        
        // Add financial disclaimers if requested
        if (request.getIncludeDisclaimers()) {
            response.setMessage(addFinancialDisclaimers(response.getMessage()));
        }
        
        return response;
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
}