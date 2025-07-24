package com.llmapi.service;

import com.llmapi.dto.FinancialProfile;
import com.llmapi.dto.InvestmentGuidance;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentGuidanceService {
    
    /**
     * Generate comprehensive personalized investment guidance
     */
    InvestmentGuidance generatePersonalizedInvestmentGuidance(FinancialProfile profile);
    
    /**
     * Generate specific stock recommendations based on user profile and interests
     */
    List<InvestmentGuidance.StockRecommendation> generateStockRecommendations(FinancialProfile profile);
    
    /**
     * Generate SIP recommendations with specific fund suggestions
     */
    List<InvestmentGuidance.SIPRecommendation> generateSIPRecommendations(FinancialProfile profile);
    
    /**
     * Generate mutual fund recommendations across categories
     */
    List<InvestmentGuidance.MutualFundRecommendation> generateMutualFundRecommendations(FinancialProfile profile);
    
    /**
     * Generate ETF recommendations based on profile
     */
    List<InvestmentGuidance.ETFRecommendation> generateETFRecommendations(FinancialProfile profile);
    
    /**
     * Generate bond and debt instrument recommendations
     */
    List<InvestmentGuidance.BondRecommendation> generateBondRecommendations(FinancialProfile profile);
    
    /**
     * Generate alternative investment suggestions
     */
    List<InvestmentGuidance.AlternativeInvestment> generateAlternativeInvestments(FinancialProfile profile);
    
    /**
     * Create monthly investment plan based on income and expenses
     */
    InvestmentGuidance.MonthlyInvestmentPlan createMonthlyInvestmentPlan(FinancialProfile profile);
    
    /**
     * Generate asset allocation strategy
     */
    InvestmentGuidance.AssetAllocation generateAssetAllocation(FinancialProfile profile);
    
    /**
     * Create risk management strategy
     */
    InvestmentGuidance.RiskManagement createRiskManagementStrategy(FinancialProfile profile);
    
    /**
     * Generate tax optimization recommendations
     */
    InvestmentGuidance.TaxOptimization generateTaxOptimization(FinancialProfile profile);
    
    /**
     * Create rebalancing strategy
     */
    InvestmentGuidance.RebalancingStrategy createRebalancingStrategy(FinancialProfile profile);
    
    /**
     * Generate investment timeline with goals
     */
    InvestmentGuidance.InvestmentTimeline generateInvestmentTimeline(FinancialProfile profile);
    
    /**
     * Create performance tracking guidelines
     */
    InvestmentGuidance.PerformanceTracking createPerformanceTracking(FinancialProfile profile);
    
    /**
     * Calculate recommended monthly investment amount
     */
    BigDecimal calculateRecommendedMonthlyInvestment(FinancialProfile profile);
    
    /**
     * Get investment recommendations based on specific interests
     */
    List<String> getInterestBasedInvestmentRecommendations(List<String> interests);
    
    /**
     * Generate investment strategy based on age and risk tolerance
     */
    InvestmentGuidance.InvestmentStrategy generateInvestmentStrategy(FinancialProfile profile);
}