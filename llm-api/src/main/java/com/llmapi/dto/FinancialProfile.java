package com.llmapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProfile {
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must not exceed 100")
    private Integer age;
    
    @JsonProperty("income_range")
    private IncomeRange incomeRange;
    
    @JsonProperty("risk_tolerance")
    private RiskTolerance riskTolerance;
    
    @JsonProperty("investment_experience")
    private InvestmentExperience investmentExperience;
    
    @JsonProperty("financial_goals")
    private List<FinancialGoal> financialGoals;
    
    @JsonProperty("current_savings")
    private BigDecimal currentSavings;
    
    @JsonProperty("monthly_expenses")
    private BigDecimal monthlyExpenses;
    
    @JsonProperty("debt_amount")
    private BigDecimal debtAmount;
    
    @JsonProperty("employment_status")
    private EmploymentStatus employmentStatus;
    
    @JsonProperty("marital_status")
    private MaritalStatus maritalStatus;
    
    @JsonProperty("number_of_dependents")
    private Integer numberOfDependents = 0;
    
    @JsonProperty("retirement_age_target")
    private Integer retirementAgeTarget;
    
    @JsonProperty("interests")
    private List<String> interests;
    
    @JsonProperty("preferred_investment_types")
    private List<InvestmentType> preferredInvestmentTypes;
    
    @JsonProperty("time_horizon")
    private TimeHorizon timeHorizon;
    
    // Enums for structured data
    public enum IncomeRange {
        BELOW_25K("Below $25,000"),
        RANGE_25K_50K("$25,000 - $50,000"),
        RANGE_50K_75K("$50,000 - $75,000"),
        RANGE_75K_100K("$75,000 - $100,000"),
        RANGE_100K_150K("$100,000 - $150,000"),
        ABOVE_150K("Above $150,000");
        
        private final String description;
        
        IncomeRange(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum RiskTolerance {
        CONSERVATIVE("Conservative - Prefer stable, low-risk investments"),
        MODERATE("Moderate - Balanced approach to risk and return"),
        AGGRESSIVE("Aggressive - Comfortable with high-risk, high-reward investments");
        
        private final String description;
        
        RiskTolerance(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum InvestmentExperience {
        BEGINNER("Beginner - Little to no investment experience"),
        INTERMEDIATE("Intermediate - Some investment experience"),
        ADVANCED("Advanced - Extensive investment experience");
        
        private final String description;
        
        InvestmentExperience(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum FinancialGoal {
        RETIREMENT("Retirement Planning"),
        EMERGENCY_FUND("Emergency Fund"),
        HOME_PURCHASE("Home Purchase"),
        EDUCATION("Education Funding"),
        DEBT_PAYOFF("Debt Payoff"),
        WEALTH_BUILDING("Wealth Building"),
        TRAVEL("Travel Fund"),
        BUSINESS_INVESTMENT("Business Investment"),
        CHILD_EDUCATION("Children's Education"),
        INSURANCE("Insurance Planning");
        
        private final String description;
        
        FinancialGoal(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum EmploymentStatus {
        EMPLOYED_FULL_TIME("Full-time Employee"),
        EMPLOYED_PART_TIME("Part-time Employee"),
        SELF_EMPLOYED("Self-employed"),
        UNEMPLOYED("Unemployed"),
        RETIRED("Retired"),
        STUDENT("Student");
        
        private final String description;
        
        EmploymentStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum MaritalStatus {
        SINGLE("Single"),
        MARRIED("Married"),
        DIVORCED("Divorced"),
        WIDOWED("Widowed");
        
        private final String description;
        
        MaritalStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum InvestmentType {
        STOCKS("Individual Stocks"),
        BONDS("Bonds"),
        MUTUAL_FUNDS("Mutual Funds"),
        ETF("Exchange-Traded Funds (ETFs)"),
        REAL_ESTATE("Real Estate"),
        CRYPTOCURRENCY("Cryptocurrency"),
        COMMODITIES("Commodities"),
        SAVINGS_ACCOUNT("High-Yield Savings"),
        CD("Certificates of Deposit"),
        ROBO_ADVISOR("Robo-Advisor Portfolios");
        
        private final String description;
        
        InvestmentType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum TimeHorizon {
        SHORT_TERM("Short-term (1-3 years)"),
        MEDIUM_TERM("Medium-term (3-10 years)"),
        LONG_TERM("Long-term (10+ years)");
        
        private final String description;
        
        TimeHorizon(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}