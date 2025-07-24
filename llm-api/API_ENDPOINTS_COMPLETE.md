# Complete API Endpoints - Financial Advisory Chatbot

## 📋 Overview
This document lists ALL available API endpoints in the Financial Advisory Chatbot system.

---

## 🤖 **Basic LLM APIs** (`/api/v1/llm`)

### 1. **Chat with LLM**
```http
POST /api/v1/llm/chat
```
**Description**: Basic chat with LLM  
**Request Body**:
```json
{
  "message": "Hello, how are you?",
  "session_id": "session-123",
  "model_name": "gpt-3.5-turbo",
  "max_tokens": 1000,
  "temperature": 0.7
}
```

### 2. **Get Chat History**
```http
GET /api/v1/llm/chat/history/{sessionId}
```
**Description**: Retrieve conversation history for a session

### 3. **Clear Chat History**
```http
DELETE /api/v1/llm/chat/history/{sessionId}
```
**Description**: Clear conversation history for a session

### 4. **Health Check**
```http
GET /api/v1/llm/health
```
**Description**: Check if the API is running
**Response**:
```json
{
  "status": "healthy",
  "service": "LLM API",
  "version": "1.0.0"
}
```

### 5. **List Available Models**
```http
GET /api/v1/llm/models
```
**Description**: Get list of available LLM models
**Response**:
```json
{
  "models": [
    {"id": "gpt-3.5-turbo", "name": "GPT-3.5 Turbo", "description": "Fast and efficient model"},
    {"id": "gpt-4", "name": "GPT-4", "description": "More capable model with better reasoning"},
    {"id": "gpt-4-turbo", "name": "GPT-4 Turbo", "description": "Latest GPT-4 model"}
  ],
  "default": "gpt-3.5-turbo"
}
```

---

## 💰 **Financial Advisory APIs** (`/api/v1/financial-advisor`)

### 1. **Personalized Financial Chat**
```http
POST /api/v1/financial-advisor/chat
```
**Description**: Get personalized financial advice based on age and interests  
**Request Body**:
```json
{
  "message": "I'm 28 and interested in tech. How should I start investing?",
  "session_id": "user-session-123",
  "advisory_mode": "INVESTMENT_FOCUSED",
  "financial_profile": {
    "age": 28,
    "income_range": "RANGE_75K_100K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "BEGINNER",
    "interests": ["technology", "artificial intelligence"],
    "financial_goals": ["RETIREMENT", "HOME_PURCHASE"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "SINGLE"
  }
}
```

### 2. **Age-Based Financial Goals**
```http
GET /api/v1/financial-advisor/age-based-goals/{age}
```
**Description**: Get financial milestones appropriate for specific age  
**Example**: `GET /api/v1/financial-advisor/age-based-goals/28`  
**Response**:
```json
{
  "age": 28,
  "goals": "In Your 20s - Foundation Building:\n- Build emergency fund (3-6 months expenses)\n- Start investing early (even small amounts)\n- Maximize employer 401(k) match\n- Pay off high-interest debt\n- Build good credit history",
  "session_id": "N/A"
}
```

### 3. **Investment Recommendations**
```http
POST /api/v1/financial-advisor/investment-recommendations
```
**Description**: Get personalized investment recommendations based on age, risk tolerance, and interests  
**Request Body**:
```json
{
  "age": 28,
  "income_range": "RANGE_75K_100K",
  "risk_tolerance": "MODERATE",
  "investment_experience": "BEGINNER",
  "interests": ["technology", "renewable energy"],
  "financial_goals": ["RETIREMENT", "HOME_PURCHASE"]
}
```
**Response**:
```json
{
  "recommendations": "Based on your age (28), here's a suggested asset allocation:\n- Stocks/Equity: 72%\n- Bonds/Fixed Income: 28%\n\nWith moderate risk tolerance, consider:\n- Diversified index funds (S&P 500, Total Stock Market)\n- Mix of growth and value stocks\n- International diversification\n\nBased on your interests (technology, renewable energy):\n- Technology sector ETFs (QQQ, XLK)\n- ESG (Environmental, Social, Governance) funds",
  "profile_complete": true,
  "age": 28
}
```

### 4. **Budgeting Advice**
```http
POST /api/v1/financial-advisor/budgeting-advice
```
**Description**: Get personalized budgeting strategies based on age and financial situation  
**Request Body**:
```json
{
  "age": 24,
  "income_range": "RANGE_50K_75K",
  "employment_status": "EMPLOYED_FULL_TIME",
  "marital_status": "SINGLE",
  "current_savings": 5000,
  "monthly_expenses": 3500
}
```
**Response**:
```json
{
  "advice": "Young Adult Budgeting (20s):\n- Follow 50/30/20 rule: 50% needs, 30% wants, 20% savings\n- Prioritize building emergency fund first\n- Keep housing costs under 30% of income\n\nGeneral Budgeting Tips:\n- Track expenses for at least one month\n- Use the envelope method for discretionary spending\n- Automate savings and bill payments\n- Review and adjust budget quarterly",
  "age_group": "Young Adult (20s)",
  "profile_complete": false
}
```

### 5. **Retirement Planning**
```http
POST /api/v1/financial-advisor/retirement-plan
```
**Description**: Get personalized retirement planning strategy  
**Request Body**:
```json
{
  "age": 32,
  "income_range": "RANGE_75K_100K",
  "retirement_age_target": 65,
  "current_savings": 50000,
  "employment_status": "EMPLOYED_FULL_TIME",
  "financial_goals": ["RETIREMENT"]
}
```
**Response**:
```json
{
  "retirement_plan": "Personalized Retirement Planning:\n\nCurrent Age: 32\nTarget Retirement Age: 65\nYears to Retirement: 33\n\nMedium-Term Strategy (15-30 years):\n- Balance growth with some stability\n- Increase contribution rates annually\n- Diversify across asset classes",
  "current_age": 32,
  "target_retirement_age": 65,
  "years_to_retirement": 33
}
```

### 6. **Profile Validation**
```http
POST /api/v1/financial-advisor/profile/validate
```
**Description**: Check if financial profile has sufficient information for personalized advice  
**Request Body**:
```json
{
  "age": 35,
  "income_range": "RANGE_75K_100K",
  "risk_tolerance": "MODERATE",
  "investment_experience": "INTERMEDIATE"
}
```
**Response**:
```json
{
  "is_complete": true,
  "age_provided": true,
  "risk_tolerance_provided": true,
  "investment_experience_provided": true,
  "income_range_provided": true,
  "recommendations": "Profile is complete for personalized advice"
}
```

### 7. **Get Profile Template**
```http
GET /api/v1/financial-advisor/profile/template
```
**Description**: Get template showing all available profile fields and options  
**Response**:
```json
{
  "required_fields": {
    "age": "Integer (18-100)",
    "risk_tolerance": "CONSERVATIVE, MODERATE, AGGRESSIVE",
    "investment_experience": "BEGINNER, INTERMEDIATE, ADVANCED",
    "income_range": "BELOW_25K, RANGE_25K_50K, RANGE_50K_75K, RANGE_75K_100K, RANGE_100K_150K, ABOVE_150K"
  },
  "optional_fields": {
    "financial_goals": "Array of: RETIREMENT, EMERGENCY_FUND, HOME_PURCHASE, EDUCATION, etc.",
    "interests": "Array of strings (e.g., ['technology', 'healthcare', 'environment'])",
    "current_savings": "BigDecimal",
    "monthly_expenses": "BigDecimal",
    "debt_amount": "BigDecimal",
    "employment_status": "EMPLOYED_FULL_TIME, EMPLOYED_PART_TIME, SELF_EMPLOYED, etc.",
    "marital_status": "SINGLE, MARRIED, DIVORCED, WIDOWED",
    "number_of_dependents": "Integer",
    "retirement_age_target": "Integer",
    "preferred_investment_types": "Array of investment types"
  },
  "example_interests": [
    "technology", "healthcare", "environment", "real estate", 
    "travel", "education", "renewable energy", "artificial intelligence"
  ]
}
```

### 8. **Get Advisory Modes**
```http
GET /api/v1/financial-advisor/advisory-modes
```
**Description**: List all available advisory modes and their descriptions  
**Response**:
```json
{
  "advisory_modes": {
    "GENERAL": "General financial advice",
    "INVESTMENT_FOCUSED": "Investment-focused advice",
    "BUDGETING": "Budgeting and expense management",
    "RETIREMENT_PLANNING": "Retirement planning",
    "DEBT_MANAGEMENT": "Debt management",
    "TAX_PLANNING": "Tax planning",
    "INSURANCE_PLANNING": "Insurance planning",
    "EMERGENCY_FUND": "Emergency fund planning"
  },
  "default_mode": "GENERAL"
}
```

---

## 🛠️ **Additional Endpoints**

### Swagger Documentation
```http
GET /swagger-ui/index.html
```
**Description**: Interactive API documentation

### H2 Database Console
```http
GET /h2-console
```
**Description**: Database management interface (development only)

---

## 📊 **Complete API Summary**

### **Total Endpoints: 13**

#### **Basic LLM APIs (5 endpoints)**:
1. `POST /api/v1/llm/chat` - Basic chat
2. `GET /api/v1/llm/chat/history/{sessionId}` - Get chat history
3. `DELETE /api/v1/llm/chat/history/{sessionId}` - Clear chat history
4. `GET /api/v1/llm/health` - Health check
5. `GET /api/v1/llm/models` - List models

#### **Financial Advisory APIs (8 endpoints)**:
1. `POST /api/v1/financial-advisor/chat` - Personalized financial chat
2. `GET /api/v1/financial-advisor/age-based-goals/{age}` - Age-based goals
3. `POST /api/v1/financial-advisor/investment-recommendations` - Investment advice
4. `POST /api/v1/financial-advisor/budgeting-advice` - Budgeting strategies
5. `POST /api/v1/financial-advisor/retirement-plan` - Retirement planning
6. `POST /api/v1/financial-advisor/profile/validate` - Profile validation
7. `GET /api/v1/financial-advisor/profile/template` - Profile template
8. `GET /api/v1/financial-advisor/advisory-modes` - Advisory modes

---

## 🧪 **Testing All APIs**

### Run Complete Test Suite
```bash
chmod +x test-financial-advisor.sh
./test-financial-advisor.sh
```

### Test Individual Endpoints
```bash
# Basic LLM Health Check
curl -X GET "http://localhost:8080/api/v1/llm/health"

# Age-based goals
curl -X GET "http://localhost:8080/api/v1/financial-advisor/age-based-goals/28"

# Investment recommendations
curl -X POST "http://localhost:8080/api/v1/financial-advisor/investment-recommendations" \
  -H "Content-Type: application/json" \
  -d '{"age": 28, "risk_tolerance": "MODERATE", "interests": ["technology"]}'

# Profile validation
curl -X POST "http://localhost:8080/api/v1/financial-advisor/profile/validate" \
  -H "Content-Type: application/json" \
  -d '{"age": 30, "risk_tolerance": "MODERATE"}'
```

---

**🎉 All 13 API endpoints are fully functional and ready to use!**