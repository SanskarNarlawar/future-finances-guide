#!/bin/bash

echo "🚀 Testing Investment Guidance API Endpoints"
echo "============================================"

BASE_URL="http://localhost:8080/api/v1/financial-advisor"

# Test profiles
YOUNG_TECH_PROFILE='{
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
}'

MID_CAREER_PROFILE='{
  "age": 35,
  "income_range": "RANGE_100K_150K",
  "risk_tolerance": "AGGRESSIVE",
  "investment_experience": "INTERMEDIATE",
  "interests": ["healthcare", "real estate"],
  "financial_goals": ["RETIREMENT", "CHILD_EDUCATION"],
  "current_savings": 800000,
  "monthly_expenses": 75000
}'

CONSERVATIVE_PROFILE='{
  "age": 50,
  "risk_tolerance": "CONSERVATIVE",
  "investment_experience": "INTERMEDIATE",
  "financial_goals": ["RETIREMENT"]
}'

echo ""
echo "📊 1. Testing Comprehensive Investment Guidance"
echo "----------------------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/comprehensive" \
  -H "Content-Type: application/json" \
  -d "$YOUNG_TECH_PROFILE" | jq '{
    user_profile_summary,
    investment_strategy: .investment_strategy.approach,
    asset_allocation,
    stock_count: (.stock_recommendations | length),
    sip_count: (.sip_recommendations | length),
    monthly_investment: .monthly_investment_plan.total_monthly_investment
  }'

echo ""
echo "📈 2. Testing Stock Recommendations"
echo "-----------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/stocks" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 32,
    "risk_tolerance": "MODERATE",
    "interests": ["technology", "artificial intelligence", "software"],
    "investment_experience": "INTERMEDIATE"
  }' | jq '{
    total_recommendations,
    stocks: [.stock_recommendations[] | {
      name: .stock_name,
      symbol: .stock_symbol,
      sector,
      allocation: .target_allocation,
      why_suitable
    }]
  }'

echo ""
echo "💰 3. Testing SIP Recommendations"
echo "---------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/sip" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 25,
    "income_range": "RANGE_50K_75K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "BEGINNER",
    "financial_goals": ["RETIREMENT", "EMERGENCY_FUND"],
    "current_savings": 50000,
    "monthly_expenses": 35000
  }' | jq '{
    total_monthly_sip,
    recommended_monthly_investment,
    sips: [.sip_recommendations[] | {
      fund: .fund_name,
      type: .fund_type,
      amount: .recommended_amount,
      returns: .expected_returns,
      tenure: .minimum_tenure
    }]
  }'

echo ""
echo "🏦 4. Testing Mutual Fund Recommendations"
echo "----------------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/mutual-funds" \
  -H "Content-Type: application/json" \
  -d "$MID_CAREER_PROFILE" | jq '{
    total_funds,
    categories_covered,
    funds: [.mutual_fund_recommendations[] | {
      name: .fund_name,
      category: .fund_category,
      house: .fund_house,
      expense_ratio,
      returns_3yr: .historical_returns."3_year"
    }]
  }'

echo ""
echo "📊 5. Testing ETF Recommendations"
echo "--------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/etf" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 30,
    "risk_tolerance": "MODERATE",
    "investment_experience": "INTERMEDIATE",
    "interests": ["diversification", "low-cost investing"]
  }' | jq '{
    total_etfs,
    average_expense_ratio,
    etfs: [.etf_recommendations[] | {
      name: .etf_name,
      symbol: .etf_symbol,
      index: .underlying_index,
      expense_ratio,
      liquidity
    }]
  }'

echo ""
echo "📅 6. Testing Monthly Investment Plan"
echo "------------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/monthly-plan" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 27,
    "income_range": "RANGE_75K_100K",
    "current_savings": 200000,
    "monthly_expenses": 50000,
    "risk_tolerance": "MODERATE",
    "financial_goals": ["RETIREMENT", "HOME_PURCHASE"]
  }' | jq '{
    estimated_annual_investment,
    plan: {
      total: .monthly_investment_plan.total_monthly_investment,
      sip: .monthly_investment_plan.sip_allocation,
      direct_equity: .monthly_investment_plan.direct_equity_allocation,
      debt: .monthly_investment_plan.debt_allocation,
      emergency: .monthly_investment_plan.emergency_fund_allocation
    },
    breakdown: [.monthly_investment_plan.investment_breakdown[] | {
      type: .investment_name,
      amount: .monthly_amount,
      percentage: .percentage_of_total
    }]
  }'

echo ""
echo "⚖️ 7. Testing Asset Allocation"
echo "------------------------------"
curl -s -X POST "$BASE_URL/investment-guidance/asset-allocation" \
  -H "Content-Type: application/json" \
  -d "$CONSERVATIVE_PROFILE" | jq '{
    allocation_strategy,
    total_percentage,
    allocation: {
      equity: .asset_allocation.equity_percentage,
      debt: .asset_allocation.debt_percentage,
      gold: .asset_allocation.gold_percentage,
      international: .asset_allocation.international_percentage,
      alternative: .asset_allocation.alternative_percentage
    },
    rationale: .asset_allocation.rationale
  }'

echo ""
echo "🎯 8. Testing Different Risk Profiles"
echo "-------------------------------------"

echo "Conservative (Age 50):"
curl -s -X POST "$BASE_URL/investment-guidance/asset-allocation" \
  -H "Content-Type: application/json" \
  -d '{"age": 50, "risk_tolerance": "CONSERVATIVE"}' | jq '.asset_allocation | {equity_percentage, debt_percentage}'

echo "Moderate (Age 35):"
curl -s -X POST "$BASE_URL/investment-guidance/asset-allocation" \
  -H "Content-Type: application/json" \
  -d '{"age": 35, "risk_tolerance": "MODERATE"}' | jq '.asset_allocation | {equity_percentage, debt_percentage}'

echo "Aggressive (Age 25):"
curl -s -X POST "$BASE_URL/investment-guidance/asset-allocation" \
  -H "Content-Type: application/json" \
  -d '{"age": 25, "risk_tolerance": "AGGRESSIVE"}' | jq '.asset_allocation | {equity_percentage, debt_percentage}'

echo ""
echo "🔍 9. Testing Interest-Based Recommendations"
echo "--------------------------------------------"

echo "Healthcare Interest:"
curl -s -X POST "$BASE_URL/investment-guidance/stocks" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 29,
    "risk_tolerance": "MODERATE",
    "interests": ["healthcare", "pharmaceuticals", "medical research"],
    "investment_experience": "BEGINNER"
  }' | jq '[.stock_recommendations[] | select(.sector | contains("Pharma")) | {name: .stock_name, sector, why_suitable}]'

echo ""
echo "Real Estate Interest:"
curl -s -X POST "$BASE_URL/investment-guidance/comprehensive" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 35,
    "interests": ["real estate", "property"],
    "risk_tolerance": "MODERATE"
  }' | jq '[.alternative_investments[] | select(.investment_type == "REIT") | {name: .investment_name, type: .investment_type, rationale: .investment_rationale}]'

echo ""
echo "✅ All Investment Guidance API Tests Completed!"
echo "==============================================="
echo ""
echo "📝 Summary of Available Endpoints:"
echo "• POST /investment-guidance/comprehensive - Complete investment guidance"
echo "• POST /investment-guidance/stocks - Stock recommendations"
echo "• POST /investment-guidance/sip - SIP recommendations"
echo "• POST /investment-guidance/mutual-funds - Mutual fund recommendations"
echo "• POST /investment-guidance/etf - ETF recommendations"
echo "• POST /investment-guidance/monthly-plan - Monthly investment plan"
echo "• POST /investment-guidance/asset-allocation - Asset allocation strategy"
echo ""
echo "🌟 Features Demonstrated:"
echo "• Age-based investment strategies"
echo "• Risk tolerance adjustments"
echo "• Interest-based personalization (tech, healthcare, real estate)"
echo "• Comprehensive asset allocation"
echo "• Detailed monthly investment planning"
echo "• Tax optimization recommendations"
echo "• Risk management strategies"
echo "• Performance tracking guidelines"