#!/bin/bash

echo "=== Financial Advisory Chatbot API Test ==="
echo

BASE_URL="http://localhost:8080/api/v1"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_header() {
    echo -e "${BLUE}==== $1 ====${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Test 1: Get profile template
print_header "1. Getting Financial Profile Template"
curl -s -X GET "$BASE_URL/financial-advisor/profile/template" | jq '.'
echo -e "\n"

# Test 2: Get advisory modes
print_header "2. Getting Available Advisory Modes"
curl -s -X GET "$BASE_URL/financial-advisor/advisory-modes" | jq '.'
echo -e "\n"

# Test 3: Age-based goals for different ages
print_header "3. Testing Age-Based Financial Goals"

ages=(25 35 45 55 65)
for age in "${ages[@]}"; do
    echo -e "${YELLOW}Age $age Goals:${NC}"
    curl -s -X GET "$BASE_URL/financial-advisor/age-based-goals/$age" | jq '.goals' -r
    echo
done

# Test 4: Investment recommendations for young professional
print_header "4. Investment Recommendations - Young Tech Professional (Age 28)"
curl -s -X POST "$BASE_URL/financial-advisor/investment-recommendations" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 28,
    "income_range": "RANGE_75K_100K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "BEGINNER",
    "interests": ["technology", "artificial intelligence", "renewable energy"],
    "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "SINGLE"
  }' | jq '.recommendations' -r
echo -e "\n"

# Test 5: Investment recommendations for mid-career healthcare worker
print_header "5. Investment Recommendations - Mid-Career Healthcare Worker (Age 42)"
curl -s -X POST "$BASE_URL/financial-advisor/investment-recommendations" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 42,
    "income_range": "RANGE_100K_150K",
    "risk_tolerance": "CONSERVATIVE",
    "investment_experience": "INTERMEDIATE",
    "interests": ["healthcare", "education", "family"],
    "financial_goals": ["RETIREMENT", "CHILD_EDUCATION", "INSURANCE"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "MARRIED",
    "number_of_dependents": 2,
    "current_savings": 150000,
    "monthly_expenses": 8000
  }' | jq '.recommendations' -r
echo -e "\n"

# Test 6: Budgeting advice for different age groups
print_header "6. Budgeting Advice for Different Ages"

echo -e "${YELLOW}Young Adult (Age 24):${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/budgeting-advice" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 24,
    "income_range": "RANGE_50K_75K",
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "SINGLE",
    "current_savings": 5000,
    "monthly_expenses": 3500
  }' | jq '.advice' -r
echo

echo -e "${YELLOW}Pre-Retirement (Age 58):${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/budgeting-advice" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 58,
    "income_range": "ABOVE_150K",
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "MARRIED",
    "current_savings": 800000,
    "monthly_expenses": 12000,
    "debt_amount": 50000
  }' | jq '.advice' -r
echo -e "\n"

# Test 7: Retirement planning
print_header "7. Retirement Planning Examples"

echo -e "${YELLOW}Early Career Retirement Planning:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/retirement-plan" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 32,
    "income_range": "RANGE_75K_100K",
    "retirement_age_target": 65,
    "current_savings": 50000,
    "employment_status": "EMPLOYED_FULL_TIME",
    "financial_goals": ["RETIREMENT"]
  }' | jq '.retirement_plan' -r
echo

echo -e "${YELLOW}Late Career Retirement Planning:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/retirement-plan" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 55,
    "income_range": "ABOVE_150K",
    "retirement_age_target": 62,
    "current_savings": 750000,
    "employment_status": "EMPLOYED_FULL_TIME",
    "financial_goals": ["RETIREMENT"]
  }' | jq '.retirement_plan' -r
echo -e "\n"

# Test 8: Financial advisory chat with different profiles
print_header "8. Financial Advisory Chat Examples"

echo -e "${YELLOW}Young Professional asking about investing:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I am just starting my career and want to begin investing. What should I focus on?",
    "session_id": "young-professional-session",
    "advisory_mode": "INVESTMENT_FOCUSED",
    "financial_profile": {
      "age": 26,
      "income_range": "RANGE_50K_75K",
      "risk_tolerance": "MODERATE",
      "investment_experience": "BEGINNER",
      "interests": ["technology", "startups"],
      "financial_goals": ["RETIREMENT", "EMERGENCY_FUND"],
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "SINGLE"
    }
  }' | jq '.message' -r
echo -e "\n"

echo -e "${YELLOW}Mid-career parent asking about education funding:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I have two kids and want to start saving for their college education. What are my options?",
    "session_id": "parent-session",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 38,
      "income_range": "RANGE_100K_150K",
      "risk_tolerance": "MODERATE",
      "investment_experience": "INTERMEDIATE",
      "interests": ["education", "family planning"],
      "financial_goals": ["CHILD_EDUCATION", "RETIREMENT"],
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "MARRIED",
      "number_of_dependents": 2,
      "current_savings": 120000
    }
  }' | jq '.message' -r
echo -e "\n"

echo -e "${YELLOW}Pre-retiree asking about debt management:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I am 8 years away from retirement but still have a mortgage. Should I pay it off early or keep investing?",
    "session_id": "pre-retiree-session",
    "advisory_mode": "DEBT_MANAGEMENT",
    "financial_profile": {
      "age": 57,
      "income_range": "ABOVE_150K",
      "risk_tolerance": "CONSERVATIVE",
      "investment_experience": "ADVANCED",
      "interests": ["real estate", "conservative investments"],
      "financial_goals": ["RETIREMENT", "DEBT_PAYOFF"],
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "MARRIED",
      "debt_amount": 180000,
      "current_savings": 650000,
      "retirement_age_target": 65
    }
  }' | jq '.message' -r
echo -e "\n"

# Test 9: Profile validation
print_header "9. Profile Validation Examples"

echo -e "${YELLOW}Complete Profile:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/profile/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 35,
    "income_range": "RANGE_75K_100K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "INTERMEDIATE"
  }' | jq '.'
echo

echo -e "${YELLOW}Incomplete Profile:${NC}"
curl -s -X POST "$BASE_URL/financial-advisor/profile/validate" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 30,
    "interests": ["technology"]
  }' | jq '.'
echo -e "\n"

print_success "Financial Advisory API Testing Complete!"
print_warning "Note: Responses are generated using mock data when no OpenAI API key is provided"
print_warning "Set OPENAI_API_KEY environment variable for real LLM responses"