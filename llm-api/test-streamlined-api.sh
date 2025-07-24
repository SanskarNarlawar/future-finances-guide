#!/bin/bash

echo "🚀 Testing Streamlined Financial Advisory API"
echo "=============================================="
echo "✨ Only 3 APIs providing MAXIMUM information!"

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
}'

AGGRESSIVE_PROFILE='{
  "age": 25,
  "income_range": "RANGE_50K_75K",
  "risk_tolerance": "AGGRESSIVE",
  "investment_experience": "BEGINNER",
  "interests": ["startups", "cryptocurrency", "growth stocks"],
  "financial_goals": ["WEALTH_BUILDING", "RETIREMENT"],
  "current_savings": 80000,
  "monthly_expenses": 30000
}'

echo ""
echo "🎯 API 1: COMPREHENSIVE GUIDANCE (ALL-IN-ONE)"
echo "=============================================="
echo "📊 Profile Analysis:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$YOUNG_TECH_PROFILE" | jq '.profile_analysis'

echo ""
echo "💰 Investment Recommendations Summary:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$YOUNG_TECH_PROFILE" | jq '{
    asset_allocation: .investment_recommendations.asset_allocation,
    stock_count: (.investment_recommendations.stocks | length),
    sip_count: (.investment_recommendations.sips | length),
    mutual_fund_count: (.investment_recommendations.mutual_funds | length),
    etf_count: (.investment_recommendations.etfs | length)
  }'

echo ""
echo "📅 Monthly Investment Plan:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$YOUNG_TECH_PROFILE" | jq '.monthly_plan | {
    total_investment,
    sip_allocation,
    direct_equity,
    debt_allocation,
    emergency_fund
  }'

echo ""
echo "🎯 Complete Guidance Sections Available:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$YOUNG_TECH_PROFILE" | jq 'keys'

echo ""
echo "💬 API 2: INTERACTIVE CHAT"
echo "=========================="
echo "Investment Question:"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I have ₹1 lakh to invest. Should I go for SIPs or direct stocks? I am 28 and work in tech.",
    "session_id": "tech-session-1",
    "advisory_mode": "INVESTMENT_FOCUSED",
    "financial_profile": {
      "age": 28,
      "risk_tolerance": "MODERATE",
      "interests": ["technology"],
      "current_savings": 100000
    }
  }' | jq '{
    session_id,
    advisory_mode,
    response_preview: (.message[:200] + "...")
  }'

echo ""
echo "Retirement Planning Question:"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to retire by 50. Am I on track with ₹15 lakh savings at age 35?",
    "session_id": "retirement-session",
    "advisory_mode": "RETIREMENT_PLANNING",
    "financial_profile": {
      "age": 35,
      "current_savings": 1500000,
      "retirement_age_target": 50
    }
  }' | jq '{
    session_id,
    advisory_mode,
    response_preview: (.message[:200] + "...")
  }'

echo ""
echo "📋 API 3: PROFILE TEMPLATE & OPTIONS"
echo "===================================="
echo "Required Fields:"
curl -s -X GET "$BASE_URL/profile-template" | jq '.required_fields | keys'

echo ""
echo "Sample Profiles Available:"
curl -s -X GET "$BASE_URL/profile-template" | jq '.sample_profiles | keys'

echo ""
echo "Total Endpoints:"
curl -s -X GET "$BASE_URL/profile-template" | jq '.total_endpoints'

echo ""
echo "Endpoint Descriptions:"
curl -s -X GET "$BASE_URL/profile-template" | jq '.endpoint_descriptions'

echo ""
echo "🎯 TESTING DIFFERENT USER SCENARIOS"
echo "==================================="

echo ""
echo "👨‍💼 Mid-Career Professional (38) - Healthcare Interest:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$MID_CAREER_PROFILE" | jq '{
    profile: .profile_analysis.summary,
    monthly_investment: .monthly_plan.total_investment,
    retirement_years: .retirement_planning.years_to_retirement,
    healthcare_stocks: [.investment_recommendations.stocks[] | select(.sector | contains("Pharma")) | .stock_name]
  }'

echo ""
echo "🚀 Young Aggressive Investor (25) - High Risk:"
curl -s -X POST "$BASE_URL/comprehensive-guidance" \
  -H "Content-Type: application/json" \
  -d "$AGGRESSIVE_PROFILE" | jq '{
    profile: .profile_analysis.summary,
    equity_allocation: .investment_recommendations.asset_allocation.equity_percentage,
    monthly_investment: .monthly_plan.total_investment,
    age_group: .age_based_goals.age_group
  }'

echo ""
echo "💡 CHAT API - Different Advisory Modes:"
echo ""

echo "🏠 Home Purchase Planning:"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to buy a house in 3 years. How should I save and invest?",
    "session_id": "home-buyer",
    "advisory_mode": "GENERAL",
    "financial_profile": {"age": 30, "financial_goals": ["HOME_PURCHASE"]}
  }' | jq '{advisory_mode, response_length: (.message | length)}'

echo ""
echo "📊 Tax Planning:"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What are the best tax-saving investments for someone in 30% tax bracket?",
    "session_id": "tax-planning",
    "advisory_mode": "TAX_PLANNING"
  }' | jq '{advisory_mode, response_length: (.message | length)}'

echo ""
echo "✅ STREAMLINED API TESTING COMPLETED!"
echo "====================================="
echo ""
echo "📊 SUMMARY:"
echo "• API 1: comprehensive-guidance - ALL financial guidance in ONE call"
echo "• API 2: chat - Interactive conversations with AI advisor"
echo "• API 3: profile-template - Helper for profile structure"
echo ""
echo "🎯 MAXIMUM INFORMATION WITH MINIMUM APIS:"
echo "• Complete investment guidance (stocks, SIPs, MFs, ETFs, bonds)"
echo "• Budgeting, retirement planning, tax optimization"
echo "• Risk management, asset allocation, monthly plans"
echo "• Interactive chat for specific questions"
echo "• Age-based personalization (20s, 30s, 40s, 50s+)"
echo "• Interest-based recommendations (tech, healthcare, real estate)"
echo "• Risk tolerance adjustments (conservative, moderate, aggressive)"
echo ""
echo "🚀 TOTAL: 3 APIs providing comprehensive financial advisory!"

echo ""
echo "🌐 Access Swagger UI: http://localhost:8080/swagger-ui/index.html"