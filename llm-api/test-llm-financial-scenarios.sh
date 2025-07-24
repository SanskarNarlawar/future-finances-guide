#!/bin/bash

echo "🤖 Testing Enhanced LLM Financial Advisory Integration"
echo "===================================================="
echo "✨ AI-Powered Financial Advice for Specific Scenarios!"

BASE_URL="http://localhost:8080/api/v1/financial-advisor"

echo ""
echo "🏠 1. HOME BUYING SCENARIO - Detailed Property Planning"
echo "======================================================"
echo "Question: 3BHK flat purchase strategy in Bangalore"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to buy a 3BHK flat in Bangalore worth ₹80 lakhs. I earn ₹12 LPA and have ₹15 lakhs saved. What is the best financing strategy? Should I take maximum loan or pay higher down payment?",
    "session_id": "home-buyer-bangalore",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 32,
      "income_range": "RANGE_100K_150K",
      "financial_goals": ["HOME_PURCHASE"],
      "current_savings": 1500000,
      "monthly_expenses": 60000,
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "MARRIED"
    }
  }' | jq '{
    scenario: "Home Buying",
    profile_based: .profile_based,
    advisory_mode: .advisory_mode,
    response_length: (.message | length),
    contains_loan_advice: (.message | contains("loan") or contains("EMI") or contains("down payment")),
    contains_tax_benefits: (.message | contains("tax") or contains("80C") or contains("24"))
  }'

echo ""
echo "🚗 2. CAR PURCHASE SCENARIO - Vehicle Financing Decision"
echo "======================================================="
echo "Question: First car purchase - loan vs cash payment"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I am planning to buy my first car worth ₹8 lakhs. Should I take an auto loan or use my savings? I also want to know about insurance, maintenance costs, and whether to buy new or used car.",
    "session_id": "first-car-buyer",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 26,
      "income_range": "RANGE_50K_75K",
      "current_savings": 400000,
      "monthly_expenses": 35000,
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "SINGLE"
    }
  }' | jq '{
    scenario: "Car Purchase",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_auto_loan: (.message | contains("auto loan") or contains("vehicle") or contains("car")),
    contains_insurance: (.message | contains("insurance") or contains("maintenance"))
  }'

echo ""
echo "🎓 3. EDUCATION PLANNING SCENARIO - Child's Higher Studies"
echo "=========================================================="
echo "Question: Planning for child's international education"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "My child is 15 years old and I want to send them abroad for engineering studies. The estimated cost is ₹50 lakhs over 4 years. How should I plan and save for this? Should I consider education loans?",
    "session_id": "education-planning-abroad",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 42,
      "income_range": "RANGE_100K_150K",
      "financial_goals": ["CHILD_EDUCATION"],
      "current_savings": 800000,
      "number_of_dependents": 2,
      "employment_status": "EMPLOYED_FULL_TIME",
      "marital_status": "MARRIED"
    }
  }' | jq '{
    scenario: "Education Planning",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_education_loan: (.message | contains("education") or contains("loan") or contains("study")),
    contains_savings_plan: (.message | contains("save") or contains("plan") or contains("SIP"))
  }'

echo ""
echo "💼 4. BUSINESS STARTUP SCENARIO - Entrepreneurship Funding"
echo "=========================================================="
echo "Question: Tech startup funding and business planning"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to start a tech startup and need ₹25 lakhs initial funding. I have ₹8 lakhs saved. What are my options - personal loan, business loan, or should I look for investors? What about business registration and compliance costs?",
    "session_id": "tech-startup-funding",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 29,
      "income_range": "RANGE_75K_100K",
      "interests": ["technology", "entrepreneurship"],
      "financial_goals": ["BUSINESS_INVESTMENT"],
      "current_savings": 800000,
      "employment_status": "EMPLOYED_FULL_TIME"
    }
  }' | jq '{
    scenario: "Business Startup",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_funding_options: (.message | contains("loan") or contains("investor") or contains("funding")),
    contains_business_advice: (.message | contains("business") or contains("startup") or contains("registration"))
  }'

echo ""
echo "💰 5. INVESTMENT DILEMMA - SIP vs Direct Stocks"
echo "==============================================="
echo "Question: Investment strategy for tech professional"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I have ₹2 lakhs to invest and can invest ₹20,000 monthly. Should I go for SIPs in mutual funds or invest directly in stocks? I work in tech and am interested in technology stocks.",
    "session_id": "investment-strategy-tech",
    "advisory_mode": "INVESTMENT_FOCUSED",
    "financial_profile": {
      "age": 28,
      "income_range": "RANGE_75K_100K",
      "risk_tolerance": "MODERATE",
      "investment_experience": "BEGINNER",
      "interests": ["technology", "artificial intelligence"],
      "current_savings": 200000,
      "financial_goals": ["RETIREMENT", "WEALTH_BUILDING"]
    }
  }' | jq '{
    scenario: "Investment Strategy",
    profile_based: .profile_based,
    advisory_mode: .advisory_mode,
    response_length: (.message | length),
    contains_sip_advice: (.message | contains("SIP") or contains("mutual fund")),
    contains_stock_advice: (.message | contains("stock") or contains("equity"))
  }'

echo ""
echo "🏖️ 6. TRAVEL PLANNING SCENARIO - International Vacation Funding"
echo "==============================================================="
echo "Question: Planning and funding for international travel"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to plan a 3-week Europe trip next year that will cost around ₹5 lakhs. How should I save and plan for this? Should I use travel loans or credit cards?",
    "session_id": "europe-travel-planning",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 30,
      "income_range": "RANGE_75K_100K",
      "interests": ["travel", "culture"],
      "financial_goals": ["TRAVEL"],
      "current_savings": 300000,
      "monthly_expenses": 45000
    }
  }' | jq '{
    scenario: "Travel Planning",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_travel_advice: (.message | contains("travel") or contains("vacation") or contains("trip")),
    contains_forex_advice: (.message | contains("forex") or contains("currency") or contains("credit card"))
  }'

echo ""
echo "💍 7. MARRIAGE PLANNING SCENARIO - Wedding Expense Management"
echo "============================================================="
echo "Question: Wedding planning and expense management"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I am getting married next year and the wedding expenses are estimated at ₹15 lakhs. I have ₹8 lakhs saved. How should I manage the remaining funds? Should I take a personal loan?",
    "session_id": "wedding-planning-finance",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 27,
      "income_range": "RANGE_50K_75K",
      "financial_goals": ["EMERGENCY_FUND"],
      "current_savings": 800000,
      "monthly_expenses": 40000,
      "marital_status": "SINGLE"
    }
  }' | jq '{
    scenario: "Wedding Planning",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_wedding_advice: (.message | contains("wedding") or contains("marriage") or contains("expense")),
    contains_loan_advice: (.message | contains("loan") or contains("funding"))
  }'

echo ""
echo "🏥 8. MEDICAL EMERGENCY SCENARIO - Healthcare Financing"
echo "======================================================="
echo "Question: Planning for medical emergencies and health insurance"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "My father needs a surgery that costs ₹8 lakhs. My health insurance covers only ₹3 lakhs. How should I arrange the remaining funds? What are my options for medical loans?",
    "session_id": "medical-emergency-finance",
    "advisory_mode": "EMERGENCY_FUND",
    "financial_profile": {
      "age": 35,
      "income_range": "RANGE_75K_100K",
      "current_savings": 500000,
      "number_of_dependents": 3,
      "financial_goals": ["EMERGENCY_FUND"],
      "employment_status": "EMPLOYED_FULL_TIME"
    }
  }' | jq '{
    scenario: "Medical Emergency",
    profile_based: .profile_based,
    advisory_mode: .advisory_mode,
    response_length: (.message | length),
    contains_medical_advice: (.message | contains("medical") or contains("health") or contains("insurance")),
    contains_emergency_fund: (.message | contains("emergency") or contains("fund"))
  }'

echo ""
echo "🏢 9. JOB TRANSITION SCENARIO - Career Change Financial Planning"
echo "==============================================================="
echo "Question: Financial planning during career transition"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I want to quit my job and pursue MBA from a top college. The fees are ₹25 lakhs and I will have no income for 2 years. How should I plan financially? Should I take an education loan?",
    "session_id": "mba-career-transition",
    "advisory_mode": "GENERAL",
    "financial_profile": {
      "age": 28,
      "income_range": "RANGE_75K_100K",
      "financial_goals": ["EDUCATION"],
      "current_savings": 600000,
      "monthly_expenses": 50000,
      "employment_status": "EMPLOYED_FULL_TIME"
    }
  }' | jq '{
    scenario: "Career Transition",
    profile_based: .profile_based,
    response_length: (.message | length),
    contains_mba_advice: (.message | contains("MBA") or contains("education") or contains("career")),
    contains_loan_advice: (.message | contains("loan") or contains("funding"))
  }'

echo ""
echo "🏠 10. REAL ESTATE INVESTMENT SCENARIO - Property Investment"
echo "==========================================================="
echo "Question: Real estate investment vs other investment options"
curl -s -X POST "$BASE_URL/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I have ₹30 lakhs to invest. Should I buy a rental property or invest in mutual funds and stocks? What are the pros and cons of real estate investment vs financial instruments?",
    "session_id": "real-estate-investment",
    "advisory_mode": "INVESTMENT_FOCUSED",
    "financial_profile": {
      "age": 40,
      "income_range": "ABOVE_150K",
      "risk_tolerance": "MODERATE",
      "investment_experience": "INTERMEDIATE",
      "interests": ["real estate", "property"],
      "financial_goals": ["WEALTH_BUILDING"],
      "current_savings": 3000000
    }
  }' | jq '{
    scenario: "Real Estate Investment",
    profile_based: .profile_based,
    advisory_mode: .advisory_mode,
    response_length: (.message | length),
    contains_property_advice: (.message | contains("property") or contains("real estate") or contains("rental")),
    contains_comparison: (.message | contains("mutual fund") or contains("stock") or contains("vs"))
  }'

echo ""
echo "✅ LLM FINANCIAL ADVISORY TESTING COMPLETED!"
echo "============================================="
echo ""
echo "📊 SUMMARY OF ENHANCED LLM INTEGRATION:"
echo "• 🏠 Home buying and property financing strategies"
echo "• 🚗 Vehicle purchase and auto loan decisions"
echo "• 🎓 Education planning and loan options"
echo "• 💼 Business startup funding and entrepreneurship"
echo "• 💰 Investment strategies and portfolio decisions"
echo "• 🏖️ Travel planning and vacation funding"
echo "• 💍 Wedding planning and expense management"
echo "• 🏥 Medical emergency and healthcare financing"
echo "• 🏢 Career transition and professional development funding"
echo "• 🏠 Real estate investment vs other investment options"
echo ""
echo "🤖 SPECIALIZED LLM CAPABILITIES:"
echo "• Contextual question detection (home, car, education, business, etc.)"
echo "• Personalized advice based on user profile (age, income, goals)"
echo "• Detailed financial calculations and recommendations"
echo "• Specific product suggestions (loans, insurance, investments)"
echo "• Tax implications and regulatory considerations"
echo "• Step-by-step action plans and next steps"
echo "• Risk assessment and mitigation strategies"
echo ""
echo "🎯 INTEGRATION FEATURES:"
echo "• Profile-based personalization"
echo "• Advisory mode detection"
echo "• Specialized context injection"
echo "• Enhanced response formatting"
echo "• Comprehensive disclaimers"
echo "• Follow-up action suggestions"
echo ""
echo "🌐 Access Enhanced APIs:"
echo "• Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "• Chat API with LLM: POST /api/v1/financial-advisor/chat"
echo "• Comprehensive Guidance: POST /api/v1/financial-advisor/comprehensive-guidance"