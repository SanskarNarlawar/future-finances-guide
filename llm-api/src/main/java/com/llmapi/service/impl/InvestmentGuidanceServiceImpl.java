package com.llmapi.service.impl;

import com.llmapi.dto.FinancialProfile;
import com.llmapi.dto.InvestmentGuidance;
import com.llmapi.service.InvestmentGuidanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvestmentGuidanceServiceImpl implements InvestmentGuidanceService {

    @Override
    public InvestmentGuidance generatePersonalizedInvestmentGuidance(FinancialProfile profile) {
        log.info("Generating personalized investment guidance for age: {}", profile.getAge());
        
        return InvestmentGuidance.builder()
                .userProfileSummary(generateUserProfileSummary(profile))
                .investmentStrategy(generateInvestmentStrategy(profile))
                .assetAllocation(generateAssetAllocation(profile))
                .stockRecommendations(generateStockRecommendations(profile))
                .sipRecommendations(generateSIPRecommendations(profile))
                .mutualFundRecommendations(generateMutualFundRecommendations(profile))
                .etfRecommendations(generateETFRecommendations(profile))
                .bondRecommendations(generateBondRecommendations(profile))
                .alternativeInvestments(generateAlternativeInvestments(profile))
                .monthlyInvestmentPlan(createMonthlyInvestmentPlan(profile))
                .riskManagement(createRiskManagementStrategy(profile))
                .taxOptimization(generateTaxOptimization(profile))
                .rebalancingStrategy(createRebalancingStrategy(profile))
                .investmentTimeline(generateInvestmentTimeline(profile))
                .performanceTracking(createPerformanceTracking(profile))
                .importantDisclaimers(generateDisclaimers())
                .build();
    }

    @Override
    public List<InvestmentGuidance.StockRecommendation> generateStockRecommendations(FinancialProfile profile) {
        List<InvestmentGuidance.StockRecommendation> recommendations = new ArrayList<>();
        
        if (profile.getAge() == null) return recommendations;
        
        // Age-based stock allocation
        boolean isYoung = profile.getAge() < 35;
        boolean isConservative = profile.getRiskTolerance() == FinancialProfile.RiskTolerance.CONSERVATIVE;
        
        // Large Cap Recommendations (Stable)
        recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                .stockName("Reliance Industries")
                .stockSymbol("RELIANCE")
                .sector("Oil & Gas / Telecom")
                .investmentRationale("Diversified business model with strong presence in energy, retail, and telecom")
                .targetAllocation(isConservative ? "15-20%" : "10-15%")
                .riskLevel("Medium")
                .timeHorizon("3-5 years")
                .whySuitable("Stable dividend-paying stock suitable for conservative investors")
                .build());
                
        recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                .stockName("HDFC Bank")
                .stockSymbol("HDFCBANK")
                .sector("Banking")
                .investmentRationale("Leading private sector bank with consistent performance and strong fundamentals")
                .targetAllocation("10-15%")
                .riskLevel("Medium")
                .timeHorizon("3-7 years")
                .whySuitable("Banking sector exposure with stable returns and dividend yield")
                .build());

        // Tech stocks for tech-interested investors
        if (profile.getInterests() != null && 
            profile.getInterests().stream().anyMatch(interest -> 
                interest.toLowerCase().contains("technology") || 
                interest.toLowerCase().contains("tech") ||
                interest.toLowerCase().contains("artificial intelligence"))) {
            
            recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                    .stockName("Infosys")
                    .stockSymbol("INFY")
                    .sector("Information Technology")
                    .investmentRationale("Global IT services leader with strong digital transformation capabilities")
                    .targetAllocation(isYoung ? "10-15%" : "5-10%")
                    .riskLevel("Medium")
                    .timeHorizon("3-5 years")
                    .whySuitable("Aligns with your interest in technology and offers exposure to global IT services")
                    .build());
                    
            recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                    .stockName("Tata Consultancy Services")
                    .stockSymbol("TCS")
                    .sector("Information Technology")
                    .investmentRationale("India's largest IT services company with strong global presence and consistent growth")
                    .targetAllocation("8-12%")
                    .riskLevel("Medium")
                    .timeHorizon("3-7 years")
                    .whySuitable("Premium IT stock with strong fundamentals and regular dividends")
                    .build());
        }
        
        // Healthcare stocks for healthcare-interested investors
        if (profile.getInterests() != null && 
            profile.getInterests().stream().anyMatch(interest -> 
                interest.toLowerCase().contains("healthcare") || 
                interest.toLowerCase().contains("medical"))) {
            
            recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                    .stockName("Dr. Reddy's Laboratories")
                    .stockSymbol("DRREDDY")
                    .sector("Pharmaceuticals")
                    .investmentRationale("Leading pharmaceutical company with strong generic and API business")
                    .targetAllocation("5-10%")
                    .riskLevel("Medium-High")
                    .timeHorizon("3-5 years")
                    .whySuitable("Exposure to growing healthcare sector aligned with your interests")
                    .build());
        }
        
        // Growth stocks for younger, aggressive investors
        if (isYoung && profile.getRiskTolerance() == FinancialProfile.RiskTolerance.AGGRESSIVE) {
            recommendations.add(InvestmentGuidance.StockRecommendation.builder()
                    .stockName("Asian Paints")
                    .stockSymbol("ASIANPAINT")
                    .sector("Paints & Coatings")
                    .investmentRationale("Market leader in decorative paints with strong brand and distribution network")
                    .targetAllocation("5-8%")
                    .riskLevel("Medium")
                    .timeHorizon("5-7 years")
                    .whySuitable("Quality growth stock with consistent performance and market leadership")
                    .build());
        }
        
        return recommendations;
    }

    @Override
    public List<InvestmentGuidance.SIPRecommendation> generateSIPRecommendations(FinancialProfile profile) {
        List<InvestmentGuidance.SIPRecommendation> recommendations = new ArrayList<>();
        
        BigDecimal monthlyInvestment = calculateRecommendedMonthlyInvestment(profile);
        BigDecimal sipAllocation = monthlyInvestment.multiply(new BigDecimal("0.6")); // 60% in SIPs
        
        // Large Cap SIP
        recommendations.add(InvestmentGuidance.SIPRecommendation.builder()
                .fundName("SBI Bluechip Fund")
                .fundType("Large Cap Equity")
                .recommendedAmount(sipAllocation.multiply(new BigDecimal("0.4")))
                .sipFrequency("Monthly")
                .expectedReturns("10-12% annually")
                .riskLevel("Medium")
                .investmentRationale("Invests in large-cap stocks providing stability and consistent returns")
                .minimumTenure("5+ years")
                .build());
        
        // Multi Cap SIP for diversification
        recommendations.add(InvestmentGuidance.SIPRecommendation.builder()
                .fundName("HDFC Flexi Cap Fund")
                .fundType("Flexi Cap Equity")
                .recommendedAmount(sipAllocation.multiply(new BigDecimal("0.3")))
                .sipFrequency("Monthly")
                .expectedReturns("12-15% annually")
                .riskLevel("Medium-High")
                .investmentRationale("Flexible allocation across market caps for optimal risk-return balance")
                .minimumTenure("7+ years")
                .build());
        
        // Small & Mid Cap for younger investors
        if (profile.getAge() != null && profile.getAge() < 40 && 
            profile.getRiskTolerance() != FinancialProfile.RiskTolerance.CONSERVATIVE) {
            
            recommendations.add(InvestmentGuidance.SIPRecommendation.builder()
                    .fundName("Axis Small Cap Fund")
                    .fundType("Small Cap Equity")
                    .recommendedAmount(sipAllocation.multiply(new BigDecimal("0.2")))
                    .sipFrequency("Monthly")
                    .expectedReturns("15-18% annually")
                    .riskLevel("High")
                    .investmentRationale("Higher growth potential through small-cap exposure for long-term wealth creation")
                    .minimumTenure("10+ years")
                    .build());
        }
        
        // ELSS for tax saving
        recommendations.add(InvestmentGuidance.SIPRecommendation.builder()
                .fundName("Mirae Asset Tax Saver Fund")
                .fundType("ELSS (Tax Saving)")
                .recommendedAmount(new BigDecimal("12500")) // ~1.5L annually for 80C
                .sipFrequency("Monthly")
                .expectedReturns("12-15% annually")
                .riskLevel("Medium-High")
                .investmentRationale("Tax-saving investment under Section 80C with equity exposure")
                .minimumTenure("3 years (lock-in)")
                .build());
        
        // International diversification
        if (profile.getAge() != null && profile.getAge() < 50) {
            recommendations.add(InvestmentGuidance.SIPRecommendation.builder()
                    .fundName("Motilal Oswal Nasdaq 100 Fund")
                    .fundType("International Equity")
                    .recommendedAmount(sipAllocation.multiply(new BigDecimal("0.1")))
                    .sipFrequency("Monthly")
                    .expectedReturns("10-14% annually")
                    .riskLevel("Medium-High")
                    .investmentRationale("Exposure to US technology giants and international diversification")
                    .minimumTenure("7+ years")
                    .build());
        }
        
        return recommendations;
    }

    @Override
    public List<InvestmentGuidance.MutualFundRecommendation> generateMutualFundRecommendations(FinancialProfile profile) {
        List<InvestmentGuidance.MutualFundRecommendation> recommendations = new ArrayList<>();
        
        // Large Cap Fund
        Map<String, String> largeCapReturns = Map.of(
                "1_year", "12.5%",
                "3_year", "15.2%",
                "5_year", "13.8%"
        );
        
        recommendations.add(InvestmentGuidance.MutualFundRecommendation.builder()
                .fundName("ICICI Prudential Bluechip Fund")
                .fundCategory("Large Cap")
                .fundHouse("ICICI Prudential")
                .expenseRatio("1.05%")
                .historicalReturns(largeCapReturns)
                .investmentRationale("Consistent performer in large-cap category with experienced fund management")
                .suitableFor("Conservative to moderate risk investors seeking steady returns")
                .build());
        
        // Mid Cap Fund
        Map<String, String> midCapReturns = Map.of(
                "1_year", "18.3%",
                "3_year", "22.1%",
                "5_year", "16.7%"
        );
        
        recommendations.add(InvestmentGuidance.MutualFundRecommendation.builder()
                .fundName("DSP Midcap Fund")
                .fundCategory("Mid Cap")
                .fundHouse("DSP Mutual Fund")
                .expenseRatio("1.75%")
                .historicalReturns(midCapReturns)
                .investmentRationale("Well-diversified mid-cap portfolio with focus on quality companies")
                .suitableFor("Moderate to aggressive investors with 7+ year investment horizon")
                .build());
        
        // Hybrid Fund for balanced approach
        Map<String, String> hybridReturns = Map.of(
                "1_year", "9.8%",
                "3_year", "11.5%",
                "5_year", "10.2%"
        );
        
        recommendations.add(InvestmentGuidance.MutualFundRecommendation.builder()
                .fundName("HDFC Balanced Advantage Fund")
                .fundCategory("Hybrid - Dynamic Asset Allocation")
                .fundHouse("HDFC Mutual Fund")
                .expenseRatio("1.15%")
                .historicalReturns(hybridReturns)
                .investmentRationale("Dynamic asset allocation between equity and debt based on market conditions")
                .suitableFor("Conservative investors seeking balanced exposure to equity and debt")
                .build());
        
        return recommendations;
    }

    @Override
    public List<InvestmentGuidance.ETFRecommendation> generateETFRecommendations(FinancialProfile profile) {
        List<InvestmentGuidance.ETFRecommendation> recommendations = new ArrayList<>();
        
        // Nifty 50 ETF
        recommendations.add(InvestmentGuidance.ETFRecommendation.builder()
                .etfName("SBI ETF Nifty 50")
                .etfSymbol("SETFNIF50")
                .underlyingIndex("Nifty 50")
                .expenseRatio("0.10%")
                .investmentRationale("Low-cost exposure to India's top 50 companies with high liquidity")
                .liquidity("High")
                .build());
        
        // Bank Nifty ETF
        recommendations.add(InvestmentGuidance.ETFRecommendation.builder()
                .etfName("ICICI Prudential Nifty Bank ETF")
                .etfSymbol("BANKBEES")
                .underlyingIndex("Nifty Bank")
                .expenseRatio("0.15%")
                .investmentRationale("Focused exposure to banking sector with sectoral diversification")
                .liquidity("High")
                .build());
        
        // Gold ETF for hedging
        recommendations.add(InvestmentGuidance.ETFRecommendation.builder()
                .etfName("HDFC Gold ETF")
                .etfSymbol("HDFCGOLD")
                .underlyingIndex("Gold Prices")
                .expenseRatio("0.50%")
                .investmentRationale("Hedge against inflation and currency devaluation")
                .liquidity("Medium")
                .build());
        
        return recommendations;
    }

    @Override
    public List<InvestmentGuidance.BondRecommendation> generateBondRecommendations(FinancialProfile profile) {
        List<InvestmentGuidance.BondRecommendation> recommendations = new ArrayList<>();
        
        // Government bonds for conservative investors
        recommendations.add(InvestmentGuidance.BondRecommendation.builder()
                .bondType("Government Bond")
                .issuer("Government of India")
                .maturity("10 years")
                .yield("7.2%")
                .creditRating("AAA (Sovereign)")
                .investmentRationale("Risk-free investment with guaranteed returns backed by government")
                .build());
        
        // Corporate bonds for higher yield
        if (profile.getRiskTolerance() != FinancialProfile.RiskTolerance.CONSERVATIVE) {
            recommendations.add(InvestmentGuidance.BondRecommendation.builder()
                    .bondType("Corporate Bond")
                    .issuer("HDFC Ltd")
                    .maturity("5 years")
                    .yield("8.5%")
                    .creditRating("AAA")
                    .investmentRationale("Higher yield than government bonds with AAA credit rating")
                    .build());
        }
        
        return recommendations;
    }

    @Override
    public List<InvestmentGuidance.AlternativeInvestment> generateAlternativeInvestments(FinancialProfile profile) {
        List<InvestmentGuidance.AlternativeInvestment> recommendations = new ArrayList<>();
        
        // REITs for real estate exposure
        if (profile.getInterests() != null && 
            profile.getInterests().stream().anyMatch(interest -> 
                interest.toLowerCase().contains("real estate") || 
                interest.toLowerCase().contains("property"))) {
            
            recommendations.add(InvestmentGuidance.AlternativeInvestment.builder()
                    .investmentType("REIT")
                    .investmentName("Embassy Office Parks REIT")
                    .expectedReturns("8-10%")
                    .riskLevel("Medium")
                    .liquidity("Medium (Listed)")
                    .minimumInvestment("₹10,000")
                    .investmentRationale("Exposure to commercial real estate with regular dividend income")
                    .build());
        }
        
        // Gold for portfolio diversification
        recommendations.add(InvestmentGuidance.AlternativeInvestment.builder()
                .investmentType("Commodity")
                .investmentName("Digital Gold")
                .expectedReturns("6-8%")
                .riskLevel("Medium")
                .liquidity("High")
                .minimumInvestment("₹100")
                .investmentRationale("Inflation hedge and portfolio diversification")
                .build());
        
        return recommendations;
    }

    @Override
    public InvestmentGuidance.MonthlyInvestmentPlan createMonthlyInvestmentPlan(FinancialProfile profile) {
        BigDecimal totalMonthlyInvestment = calculateRecommendedMonthlyInvestment(profile);
        
        BigDecimal sipAllocation = totalMonthlyInvestment.multiply(new BigDecimal("0.50"));
        BigDecimal directEquityAllocation = totalMonthlyInvestment.multiply(new BigDecimal("0.20"));
        BigDecimal debtAllocation = totalMonthlyInvestment.multiply(new BigDecimal("0.20"));
        BigDecimal emergencyFundAllocation = totalMonthlyInvestment.multiply(new BigDecimal("0.10"));
        
        List<InvestmentGuidance.InvestmentBreakdown> breakdown = Arrays.asList(
                InvestmentGuidance.InvestmentBreakdown.builder()
                        .investmentName("Equity SIPs")
                        .monthlyAmount(sipAllocation)
                        .percentageOfTotal("50%")
                        .rationale("Systematic investment in equity mutual funds for long-term wealth creation")
                        .build(),
                InvestmentGuidance.InvestmentBreakdown.builder()
                        .investmentName("Direct Equity")
                        .monthlyAmount(directEquityAllocation)
                        .percentageOfTotal("20%")
                        .rationale("Direct stock investments for higher potential returns")
                        .build(),
                InvestmentGuidance.InvestmentBreakdown.builder()
                        .investmentName("Debt Instruments")
                        .monthlyAmount(debtAllocation)
                        .percentageOfTotal("20%")
                        .rationale("Fixed income investments for stability and regular income")
                        .build(),
                InvestmentGuidance.InvestmentBreakdown.builder()
                        .investmentName("Emergency Fund")
                        .monthlyAmount(emergencyFundAllocation)
                        .percentageOfTotal("10%")
                        .rationale("Liquid savings for unexpected expenses")
                        .build()
        );
        
        return InvestmentGuidance.MonthlyInvestmentPlan.builder()
                .totalMonthlyInvestment(totalMonthlyInvestment)
                .sipAllocation(sipAllocation)
                .directEquityAllocation(directEquityAllocation)
                .debtAllocation(debtAllocation)
                .emergencyFundAllocation(emergencyFundAllocation)
                .investmentBreakdown(breakdown)
                .build();
    }

    @Override
    public InvestmentGuidance.AssetAllocation generateAssetAllocation(FinancialProfile profile) {
        int age = profile.getAge() != null ? profile.getAge() : 35;
        
        // Age-based allocation with risk tolerance adjustment
        int baseEquityPercentage = Math.max(20, 100 - age);
        
        // Adjust based on risk tolerance
        int equityPercentage = baseEquityPercentage;
        if (profile.getRiskTolerance() == FinancialProfile.RiskTolerance.AGGRESSIVE) {
            equityPercentage = Math.min(85, baseEquityPercentage + 10);
        } else if (profile.getRiskTolerance() == FinancialProfile.RiskTolerance.CONSERVATIVE) {
            equityPercentage = Math.max(20, baseEquityPercentage - 15);
        }
        
        int debtPercentage = 80 - equityPercentage;
        int goldPercentage = 10;
        int internationalPercentage = age < 50 ? 10 : 5;
        int alternativePercentage = 5;
        
        // Adjust to ensure total is 100%
        int total = equityPercentage + debtPercentage + goldPercentage + internationalPercentage + alternativePercentage;
        if (total > 100) {
            debtPercentage -= (total - 100);
        }
        
        return InvestmentGuidance.AssetAllocation.builder()
                .equityPercentage(equityPercentage)
                .debtPercentage(debtPercentage)
                .goldPercentage(goldPercentage)
                .internationalPercentage(internationalPercentage)
                .alternativePercentage(alternativePercentage)
                .rationale(String.format("Age-based allocation (%d years) with %s risk tolerance adjustment", 
                    age, profile.getRiskTolerance() != null ? profile.getRiskTolerance().name().toLowerCase() : "moderate"))
                .build();
    }

    @Override
    public InvestmentGuidance.RiskManagement createRiskManagementStrategy(FinancialProfile profile) {
        return InvestmentGuidance.RiskManagement.builder()
                .diversificationStrategy("Diversify across asset classes, sectors, and market caps to reduce concentration risk")
                .stopLossStrategy("Set stop-loss at 15-20% for individual stocks, review and adjust quarterly")
                .hedgingRecommendations(Arrays.asList(
                        "Maintain 10% allocation in gold for inflation hedge",
                        "Keep 6-month emergency fund in liquid investments",
                        "Consider international diversification for currency risk"
                ))
                .emergencyFundGuidance("Maintain 6-12 months of expenses in liquid funds or savings account")
                .build();
    }

    @Override
    public InvestmentGuidance.TaxOptimization generateTaxOptimization(FinancialProfile profile) {
        return InvestmentGuidance.TaxOptimization.builder()
                .taxSavingInstruments(Arrays.asList(
                        "ELSS Mutual Funds (Section 80C)",
                        "PPF (Section 80C)",
                        "NSC (Section 80C)",
                        "Health Insurance (Section 80D)"
                ))
                .ltcgStrategy("Hold equity investments for more than 1 year to benefit from LTCG tax rates")
                .stcgMinimization("Avoid frequent trading to minimize short-term capital gains tax")
                .taxHarvestingTips(Arrays.asList(
                        "Book profits and losses strategically to optimize tax liability",
                        "Use LTCG exemption limit of ₹1 lakh annually",
                        "Consider tax-free bonds for debt allocation in higher tax brackets"
                ))
                .build();
    }

    @Override
    public InvestmentGuidance.RebalancingStrategy createRebalancingStrategy(FinancialProfile profile) {
        return InvestmentGuidance.RebalancingStrategy.builder()
                .frequency("Quarterly review, annual rebalancing")
                .methodology("Threshold-based rebalancing when allocation deviates by 5% or more")
                .triggerPoints(Arrays.asList(
                        "Asset allocation deviation > 5%",
                        "Major life events (marriage, job change, etc.)",
                        "Significant market movements (>20% in any direction)"
                ))
                .rebalancingTips(Arrays.asList(
                        "Use new investments to rebalance before selling existing holdings",
                        "Consider tax implications when rebalancing in taxable accounts",
                        "Maintain discipline and stick to target allocation"
                ))
                .build();
    }

    @Override
    public InvestmentGuidance.InvestmentTimeline generateInvestmentTimeline(FinancialProfile profile) {
        List<InvestmentGuidance.TimelineGoal> shortTerm = Arrays.asList(
                InvestmentGuidance.TimelineGoal.builder()
                        .goal("Emergency Fund")
                        .timeHorizon("6-12 months")
                        .recommendedInvestments(Arrays.asList("Liquid Funds", "Savings Account", "FD"))
                        .targetAmount("6 months of expenses")
                        .build()
        );
        
        List<InvestmentGuidance.TimelineGoal> mediumTerm = Arrays.asList(
                InvestmentGuidance.TimelineGoal.builder()
                        .goal("Home Purchase")
                        .timeHorizon("5-7 years")
                        .recommendedInvestments(Arrays.asList("Hybrid Funds", "Debt Funds", "PPF"))
                        .targetAmount("20-30% of property value")
                        .build()
        );
        
        List<InvestmentGuidance.TimelineGoal> longTerm = Arrays.asList(
                InvestmentGuidance.TimelineGoal.builder()
                        .goal("Retirement")
                        .timeHorizon("20-30 years")
                        .recommendedInvestments(Arrays.asList("Equity SIPs", "EPF", "NPS", "Direct Equity"))
                        .targetAmount("25-30x annual expenses")
                        .build()
        );
        
        return InvestmentGuidance.InvestmentTimeline.builder()
                .shortTermGoals(shortTerm)
                .mediumTermGoals(mediumTerm)
                .longTermGoals(longTerm)
                .build();
    }

    @Override
    public InvestmentGuidance.PerformanceTracking createPerformanceTracking(FinancialProfile profile) {
        return InvestmentGuidance.PerformanceTracking.builder()
                .keyMetrics(Arrays.asList(
                        "Absolute Returns",
                        "CAGR (Compound Annual Growth Rate)",
                        "Portfolio Beta",
                        "Sharpe Ratio",
                        "Asset Allocation Drift"
                ))
                .reviewFrequency("Monthly monitoring, quarterly detailed review")
                .benchmarkComparison("Compare equity portion with Nifty 50, debt portion with 10-year G-Sec")
                .trackingTools(Arrays.asList(
                        "Portfolio tracking apps",
                        "Mutual fund statements",
                        "Demat account statements",
                        "Financial advisor reports"
                ))
                .build();
    }

    @Override
    public BigDecimal calculateRecommendedMonthlyInvestment(FinancialProfile profile) {
        if (profile.getCurrentSavings() == null || profile.getMonthlyExpenses() == null) {
            // Default recommendation based on income range
            return getDefaultInvestmentByIncomeRange(profile.getIncomeRange());
        }
        
        // Calculate based on income and expenses if available
        BigDecimal monthlyIncome = estimateMonthlyIncome(profile.getIncomeRange());
        BigDecimal availableForInvestment = monthlyIncome.subtract(profile.getMonthlyExpenses());
        
        // Recommend 20-30% of available surplus
        BigDecimal recommendedPercentage = new BigDecimal("0.25");
        if (profile.getAge() != null && profile.getAge() < 30) {
            recommendedPercentage = new BigDecimal("0.30"); // Higher for younger investors
        }
        
        return availableForInvestment.multiply(recommendedPercentage).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public List<String> getInterestBasedInvestmentRecommendations(List<String> interests) {
        if (interests == null || interests.isEmpty()) {
            return Arrays.asList("Diversified Equity Funds", "Large Cap Stocks", "Index Funds");
        }
        
        return interests.stream()
                .flatMap(interest -> getInvestmentsByInterest(interest).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public InvestmentGuidance.InvestmentStrategy generateInvestmentStrategy(FinancialProfile profile) {
        String approach = determineInvestmentApproach(profile);
        String rationale = generateStrategyRationale(profile);
        List<String> keyPrinciples = generateKeyPrinciples(profile);
        String investmentHorizon = determineInvestmentHorizon(profile);
        
        return InvestmentGuidance.InvestmentStrategy.builder()
                .approach(approach)
                .rationale(rationale)
                .keyPrinciples(keyPrinciples)
                .investmentHorizon(investmentHorizon)
                .build();
    }

    // Helper methods
    
    private String generateUserProfileSummary(FinancialProfile profile) {
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Investor Profile: %d years old", profile.getAge()));
        
        if (profile.getRiskTolerance() != null) {
            summary.append(String.format(", %s risk tolerance", profile.getRiskTolerance().name().toLowerCase()));
        }
        
        if (profile.getInvestmentExperience() != null) {
            summary.append(String.format(", %s investment experience", profile.getInvestmentExperience().name().toLowerCase()));
        }
        
        if (profile.getIncomeRange() != null) {
            summary.append(String.format(", %s income range", profile.getIncomeRange().getDescription()));
        }
        
        if (profile.getInterests() != null && !profile.getInterests().isEmpty()) {
            summary.append(String.format(", interested in: %s", String.join(", ", profile.getInterests())));
        }
        
        return summary.toString();
    }
    
    private BigDecimal getDefaultInvestmentByIncomeRange(FinancialProfile.IncomeRange incomeRange) {
        if (incomeRange == null) return new BigDecimal("10000");
        
        return switch (incomeRange) {
            case BELOW_25K -> new BigDecimal("2000");
            case RANGE_25K_50K -> new BigDecimal("5000");
            case RANGE_50K_75K -> new BigDecimal("10000");
            case RANGE_75K_100K -> new BigDecimal("15000");
            case RANGE_100K_150K -> new BigDecimal("25000");
            case ABOVE_150K -> new BigDecimal("40000");
        };
    }
    
    private BigDecimal estimateMonthlyIncome(FinancialProfile.IncomeRange incomeRange) {
        if (incomeRange == null) return new BigDecimal("50000");
        
        return switch (incomeRange) {
            case BELOW_25K -> new BigDecimal("20000");
            case RANGE_25K_50K -> new BigDecimal("37500");
            case RANGE_50K_75K -> new BigDecimal("62500");
            case RANGE_75K_100K -> new BigDecimal("87500");
            case RANGE_100K_150K -> new BigDecimal("125000");
            case ABOVE_150K -> new BigDecimal("200000");
        };
    }
    
    private List<String> getInvestmentsByInterest(String interest) {
        String lowerInterest = interest.toLowerCase();
        
        if (lowerInterest.contains("technology") || lowerInterest.contains("tech")) {
            return Arrays.asList("IT Sector Funds", "Technology ETFs", "Infosys", "TCS", "Tech Mutual Funds");
        } else if (lowerInterest.contains("healthcare") || lowerInterest.contains("medical")) {
            return Arrays.asList("Pharma Sector Funds", "Healthcare ETFs", "Dr. Reddy's", "Biocon");
        } else if (lowerInterest.contains("environment") || lowerInterest.contains("green")) {
            return Arrays.asList("ESG Funds", "Green Bonds", "Renewable Energy Stocks");
        } else if (lowerInterest.contains("real estate")) {
            return Arrays.asList("REITs", "Real Estate Mutual Funds", "Property Stocks");
        } else {
            return Arrays.asList("Diversified Equity Funds");
        }
    }
    
    private String determineInvestmentApproach(FinancialProfile profile) {
        if (profile.getRiskTolerance() == FinancialProfile.RiskTolerance.CONSERVATIVE) {
            return "Conservative Growth Strategy";
        } else if (profile.getRiskTolerance() == FinancialProfile.RiskTolerance.AGGRESSIVE) {
            return "Aggressive Wealth Creation Strategy";
        } else {
            return "Balanced Growth Strategy";
        }
    }
    
    private String generateStrategyRationale(FinancialProfile profile) {
        int age = profile.getAge() != null ? profile.getAge() : 35;
        
        if (age < 35) {
            return "Focus on long-term wealth creation through equity investments with time advantage for compounding";
        } else if (age < 50) {
            return "Balanced approach between growth and stability to build wealth while managing risk";
        } else {
            return "Capital preservation with moderate growth to secure retirement and reduce volatility";
        }
    }
    
    private List<String> generateKeyPrinciples(FinancialProfile profile) {
        return Arrays.asList(
                "Start early and invest regularly through SIPs",
                "Diversify across asset classes and sectors",
                "Maintain emergency fund before investing",
                "Review and rebalance portfolio periodically",
                "Stay invested for long-term to benefit from compounding",
                "Don't try to time the market",
                "Invest based on goals, not emotions"
        );
    }
    
    private String determineInvestmentHorizon(FinancialProfile profile) {
        int age = profile.getAge() != null ? profile.getAge() : 35;
        int retirementAge = profile.getRetirementAgeTarget() != null ? profile.getRetirementAgeTarget() : 60;
        int yearsToRetirement = retirementAge - age;
        
        if (yearsToRetirement > 20) {
            return "Long-term (20+ years) - Focus on wealth creation";
        } else if (yearsToRetirement > 10) {
            return "Medium to Long-term (10-20 years) - Balanced growth approach";
        } else {
            return "Medium-term (5-10 years) - Capital preservation with moderate growth";
        }
    }
    
    private List<String> generateDisclaimers() {
        return Arrays.asList(
                "This investment guidance is for educational purposes only and should not be considered as personalized financial advice",
                "Past performance does not guarantee future results",
                "All investments are subject to market risks, please read all scheme-related documents carefully",
                "Consult with a qualified financial advisor before making investment decisions",
                "Consider your risk tolerance, investment horizon, and financial goals before investing",
                "Diversification does not guarantee profits or protect against losses",
                "Tax implications may vary based on individual circumstances and may change over time"
        );
    }
}