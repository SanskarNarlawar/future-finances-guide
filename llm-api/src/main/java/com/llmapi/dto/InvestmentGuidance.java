package com.llmapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentGuidance {
    
    @JsonProperty("user_profile_summary")
    private String userProfileSummary;
    
    @JsonProperty("investment_strategy")
    private InvestmentStrategy investmentStrategy;
    
    @JsonProperty("asset_allocation")
    private AssetAllocation assetAllocation;
    
    @JsonProperty("stock_recommendations")
    private List<StockRecommendation> stockRecommendations;
    
    @JsonProperty("sip_recommendations")
    private List<SIPRecommendation> sipRecommendations;
    
    @JsonProperty("mutual_fund_recommendations")
    private List<MutualFundRecommendation> mutualFundRecommendations;
    
    @JsonProperty("etf_recommendations")
    private List<ETFRecommendation> etfRecommendations;
    
    @JsonProperty("bond_recommendations")
    private List<BondRecommendation> bondRecommendations;
    
    @JsonProperty("alternative_investments")
    private List<AlternativeInvestment> alternativeInvestments;
    
    @JsonProperty("monthly_investment_plan")
    private MonthlyInvestmentPlan monthlyInvestmentPlan;
    
    @JsonProperty("risk_management")
    private RiskManagement riskManagement;
    
    @JsonProperty("tax_optimization")
    private TaxOptimization taxOptimization;
    
    @JsonProperty("rebalancing_strategy")
    private RebalancingStrategy rebalancingStrategy;
    
    @JsonProperty("investment_timeline")
    private InvestmentTimeline investmentTimeline;
    
    @JsonProperty("performance_tracking")
    private PerformanceTracking performanceTracking;
    
    @JsonProperty("important_disclaimers")
    private List<String> importantDisclaimers;
    
    // Nested Classes
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvestmentStrategy {
        private String approach;
        private String rationale;
        @JsonProperty("key_principles")
        private List<String> keyPrinciples;
        @JsonProperty("investment_horizon")
        private String investmentHorizon;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetAllocation {
        @JsonProperty("equity_percentage")
        private Integer equityPercentage;
        @JsonProperty("debt_percentage")
        private Integer debtPercentage;
        @JsonProperty("gold_percentage")
        private Integer goldPercentage;
        @JsonProperty("international_percentage")
        private Integer internationalPercentage;
        @JsonProperty("alternative_percentage")
        private Integer alternativePercentage;
        private String rationale;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockRecommendation {
        @JsonProperty("stock_name")
        private String stockName;
        @JsonProperty("stock_symbol")
        private String stockSymbol;
        private String sector;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
        @JsonProperty("target_allocation")
        private String targetAllocation;
        @JsonProperty("risk_level")
        private String riskLevel;
        @JsonProperty("time_horizon")
        private String timeHorizon;
        @JsonProperty("why_suitable")
        private String whySuitable;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SIPRecommendation {
        @JsonProperty("fund_name")
        private String fundName;
        @JsonProperty("fund_type")
        private String fundType;
        @JsonProperty("recommended_amount")
        private BigDecimal recommendedAmount;
        @JsonProperty("sip_frequency")
        private String sipFrequency;
        @JsonProperty("expected_returns")
        private String expectedReturns;
        @JsonProperty("risk_level")
        private String riskLevel;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
        @JsonProperty("minimum_tenure")
        private String minimumTenure;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MutualFundRecommendation {
        @JsonProperty("fund_name")
        private String fundName;
        @JsonProperty("fund_category")
        private String fundCategory;
        @JsonProperty("fund_house")
        private String fundHouse;
        @JsonProperty("expense_ratio")
        private String expenseRatio;
        @JsonProperty("historical_returns")
        private Map<String, String> historicalReturns;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
        @JsonProperty("suitable_for")
        private String suitableFor;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ETFRecommendation {
        @JsonProperty("etf_name")
        private String etfName;
        @JsonProperty("etf_symbol")
        private String etfSymbol;
        @JsonProperty("underlying_index")
        private String underlyingIndex;
        @JsonProperty("expense_ratio")
        private String expenseRatio;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
        @JsonProperty("liquidity")
        private String liquidity;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BondRecommendation {
        @JsonProperty("bond_type")
        private String bondType;
        @JsonProperty("issuer")
        private String issuer;
        @JsonProperty("maturity")
        private String maturity;
        @JsonProperty("yield")
        private String yield;
        @JsonProperty("credit_rating")
        private String creditRating;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlternativeInvestment {
        @JsonProperty("investment_type")
        private String investmentType;
        @JsonProperty("investment_name")
        private String investmentName;
        @JsonProperty("expected_returns")
        private String expectedReturns;
        @JsonProperty("risk_level")
        private String riskLevel;
        @JsonProperty("liquidity")
        private String liquidity;
        @JsonProperty("minimum_investment")
        private String minimumInvestment;
        @JsonProperty("investment_rationale")
        private String investmentRationale;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyInvestmentPlan {
        @JsonProperty("total_monthly_investment")
        private BigDecimal totalMonthlyInvestment;
        @JsonProperty("sip_allocation")
        private BigDecimal sipAllocation;
        @JsonProperty("direct_equity_allocation")
        private BigDecimal directEquityAllocation;
        @JsonProperty("debt_allocation")
        private BigDecimal debtAllocation;
        @JsonProperty("emergency_fund_allocation")
        private BigDecimal emergencyFundAllocation;
        @JsonProperty("investment_breakdown")
        private List<InvestmentBreakdown> investmentBreakdown;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvestmentBreakdown {
        @JsonProperty("investment_name")
        private String investmentName;
        @JsonProperty("monthly_amount")
        private BigDecimal monthlyAmount;
        @JsonProperty("percentage_of_total")
        private String percentageOfTotal;
        private String rationale;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskManagement {
        @JsonProperty("diversification_strategy")
        private String diversificationStrategy;
        @JsonProperty("stop_loss_strategy")
        private String stopLossStrategy;
        @JsonProperty("hedging_recommendations")
        private List<String> hedgingRecommendations;
        @JsonProperty("emergency_fund_guidance")
        private String emergencyFundGuidance;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaxOptimization {
        @JsonProperty("tax_saving_instruments")
        private List<String> taxSavingInstruments;
        @JsonProperty("ltcg_strategy")
        private String ltcgStrategy;
        @JsonProperty("stcg_minimization")
        private String stcgMinimization;
        @JsonProperty("tax_harvesting_tips")
        private List<String> taxHarvestingTips;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RebalancingStrategy {
        private String frequency;
        private String methodology;
        @JsonProperty("trigger_points")
        private List<String> triggerPoints;
        @JsonProperty("rebalancing_tips")
        private List<String> rebalancingTips;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvestmentTimeline {
        @JsonProperty("short_term_goals")
        private List<TimelineGoal> shortTermGoals;
        @JsonProperty("medium_term_goals")
        private List<TimelineGoal> mediumTermGoals;
        @JsonProperty("long_term_goals")
        private List<TimelineGoal> longTermGoals;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineGoal {
        private String goal;
        @JsonProperty("time_horizon")
        private String timeHorizon;
        @JsonProperty("recommended_investments")
        private List<String> recommendedInvestments;
        @JsonProperty("target_amount")
        private String targetAmount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceTracking {
        @JsonProperty("key_metrics")
        private List<String> keyMetrics;
        @JsonProperty("review_frequency")
        private String reviewFrequency;
        @JsonProperty("benchmark_comparison")
        private String benchmarkComparison;
        @JsonProperty("tracking_tools")
        private List<String> trackingTools;
    }
}