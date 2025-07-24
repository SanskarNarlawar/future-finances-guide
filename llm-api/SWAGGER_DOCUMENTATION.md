# 📚 Complete Swagger API Documentation

## 🎯 **Enhanced Swagger Features**

Your Financial Advisory Chatbot now has **comprehensive Swagger documentation** with:

✅ **Detailed API descriptions**  
✅ **Sample request bodies for all endpoints**  
✅ **Complete response examples**  
✅ **Multiple usage scenarios**  
✅ **Interactive testing interface**  
✅ **Schema definitions**  

---

## 🌐 **Access Swagger UI**

**URL**: `http://localhost:8080/swagger-ui/index.html`

**Features**:
- Interactive API testing
- Real-time request/response examples
- Schema validation
- Try-it-out functionality
- Copy-paste ready examples

---

## 📋 **Complete API Documentation Overview**

### **🤖 Basic LLM API (5 endpoints)**

#### 1. **POST /api/v1/llm/chat** - Generate chat response
**Sample Request**:
```json
{
  "message": "Hello, how are you today?",
  "session_id": "user-session-123",
  "model_name": "gpt-3.5-turbo",
  "max_tokens": 1000,
  "temperature": 0.7
}
```

**Sample Response**:
```json
{
  "id": "resp-123456789",
  "session_id": "user-session-123",
  "message": "Hello! I'm doing well, thank you for asking. How can I help you today?",
  "model_name": "gpt-3.5-turbo",
  "created_at": "2024-01-15T10:30:00",
  "token_count": 15,
  "total_tokens": 25,
  "prompt_tokens": 10,
  "completion_tokens": 15
}
```

#### 2. **GET /api/v1/llm/chat/history/{sessionId}** - Get chat history
**Sample Response**:
```json
[
  {
    "id": 1,
    "session_id": "user-session-123",
    "role": "USER",
    "content": "Hello, how are you?",
    "created_at": "2024-01-15T10:30:00",
    "token_count": 5
  },
  {
    "id": 2,
    "session_id": "user-session-123",
    "role": "ASSISTANT",
    "content": "Hello! I'm doing well, thank you for asking. How can I help you today?",
    "created_at": "2024-01-15T10:30:05",
    "token_count": 15
  }
]
```

#### 3. **DELETE /api/v1/llm/chat/history/{sessionId}** - Clear chat history
**Sample Response**:
```json
{
  "message": "Chat history cleared for session: user-session-123"
}
```

#### 4. **GET /api/v1/llm/health** - Health check
**Sample Response**:
```json
{
  "status": "healthy",
  "service": "LLM API",
  "version": "1.0.0"
}
```

#### 5. **GET /api/v1/llm/models** - List available models
**Sample Response**:
```json
{
  "models": [
    {
      "id": "gpt-3.5-turbo",
      "name": "GPT-3.5 Turbo",
      "description": "Fast and efficient model for most tasks"
    },
    {
      "id": "gpt-4",
      "name": "GPT-4",
      "description": "More capable model with better reasoning"
    },
    {
      "id": "gpt-4-turbo",
      "name": "GPT-4 Turbo",
      "description": "Latest GPT-4 model with improved performance"
    }
  ],
  "default": "gpt-3.5-turbo"
}
```

---

### **💰 Financial Advisory API (8 endpoints)**

#### 1. **POST /api/v1/financial-advisor/chat** - Personalized financial advice

**Sample Request (Young Tech Professional)**:
```json
{
  "message": "I'm 28 and work in tech. I want to start investing but don't know where to begin. What should I focus on?",
  "session_id": "young-professional-session",
  "advisory_mode": "INVESTMENT_FOCUSED",
  "financial_profile": {
    "age": 28,
    "income_range": "RANGE_75K_100K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "BEGINNER",
    "interests": ["technology", "artificial intelligence", "startups"],
    "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "SINGLE",
    "current_savings": 25000,
    "monthly_expenses": 4000
  }
}
```

**Sample Request (Mid-Career Parent)**:
```json
{
  "message": "I have two kids and want to start saving for their college education. What are my best options?",
  "session_id": "parent-session",
  "advisory_mode": "GENERAL",
  "financial_profile": {
    "age": 38,
    "income_range": "RANGE_100K_150K",
    "risk_tolerance": "MODERATE",
    "investment_experience": "INTERMEDIATE",
    "interests": ["education", "family planning", "healthcare"],
    "financial_goals": ["CHILD_EDUCATION", "RETIREMENT", "INSURANCE"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "MARRIED",
    "number_of_dependents": 2,
    "current_savings": 120000,
    "monthly_expenses": 8000
  }
}
```

**Sample Request (Pre-Retiree)**:
```json
{
  "message": "I'm 8 years from retirement but still have a mortgage. Should I pay it off early or keep investing?",
  "session_id": "pre-retiree-session",
  "advisory_mode": "RETIREMENT_PLANNING",
  "financial_profile": {
    "age": 57,
    "income_range": "ABOVE_150K",
    "risk_tolerance": "CONSERVATIVE",
    "investment_experience": "ADVANCED",
    "interests": ["real estate", "conservative investments", "travel"],
    "financial_goals": ["RETIREMENT", "DEBT_PAYOFF"],
    "employment_status": "EMPLOYED_FULL_TIME",
    "marital_status": "MARRIED",
    "debt_amount": 180000,
    "current_savings": 650000,
    "retirement_age_target": 65
  }
}
```

**Sample Response**:
```json
{
  "id": "financial-advice-123",
  "session_id": "young-professional-session",
  "message": "Great question! At 28 with your tech background and moderate risk tolerance, here's my personalized advice:\n\n**Investment Strategy for Your Age:**\n- Focus on growth investments (70-80% stocks, 20-30% bonds)\n- Start with low-cost index funds like S&P 500\n- Consider tech sector ETFs given your interests\n\n**Given Your Interests:**\n- Technology sector ETFs (QQQ, XLK)\n- AI and innovation-focused funds\n- Consider ESG funds for sustainable tech\n\n**Action Steps:**\n1. Build emergency fund (3-6 months expenses)\n2. Maximize employer 401(k) match\n3. Open Roth IRA for tax-free growth\n4. Start with $500/month in diversified index funds\n\n⚠️ **Important Disclaimer**: This advice is for educational purposes only. Please consult with a qualified financial advisor for decisions specific to your situation.",
  "model_name": "gpt-3.5-turbo",
  "created_at": "2024-01-15T10:30:00",
  "token_count": 180
}
```

#### 2. **POST /api/v1/financial-advisor/investment-recommendations** - Investment recommendations

**Sample Request (Young Tech Professional)**:
```json
{
  "age": 28,
  "income_range": "RANGE_75K_100K",
  "risk_tolerance": "MODERATE",
  "investment_experience": "BEGINNER",
  "interests": ["technology", "artificial intelligence", "renewable energy"],
  "financial_goals": ["RETIREMENT", "HOME_PURCHASE", "EMERGENCY_FUND"],
  "employment_status": "EMPLOYED_FULL_TIME",
  "marital_status": "SINGLE"
}
```

**Sample Request (Healthcare Professional)**:
```json
{
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
}
```

**Sample Response**:
```json
{
  "recommendations": "Based on your age (28), here's a suggested asset allocation:\n- Stocks/Equity: 72%\n- Bonds/Fixed Income: 28%\n\nWith moderate risk tolerance, consider:\n- Diversified index funds (S&P 500, Total Stock Market)\n- Mix of growth and value stocks\n- International diversification\n\nBased on your interests (technology, artificial intelligence, renewable energy), you might consider:\n- Technology sector ETFs (QQQ, XLK)\n- ESG (Environmental, Social, Governance) funds\n- Diversified funds aligned with your interests",
  "profile_complete": true,
  "age": 28
}
```

#### 3. **GET /api/v1/financial-advisor/age-based-goals/{age}** - Age-appropriate goals

**Sample Response (Age 28)**:
```json
{
  "age": 28,
  "goals": "Financial milestones for your age (28):\n\nIn Your 20s - Foundation Building:\n- Build emergency fund (3-6 months expenses)\n- Start investing early (even small amounts)\n- Maximize employer 401(k) match\n- Pay off high-interest debt\n- Build good credit history",
  "session_id": "N/A"
}
```

**Sample Response (Age 45)**:
```json
{
  "age": 45,
  "goals": "Financial milestones for your age (45):\n\nIn Your 40s - Peak Earning Years:\n- Target 3x annual salary in retirement savings by 40\n- Maximize retirement account contributions\n- Focus on children's education funding\n- Consider long-term care insurance\n- Review estate planning documents",
  "session_id": "N/A"
}
```

#### 4. **POST /api/v1/financial-advisor/budgeting-advice** - Budgeting advice

**Sample Request**:
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

**Sample Response**:
```json
{
  "advice": "Young Adult Budgeting (20s):\n- Follow 50/30/20 rule: 50% needs, 30% wants, 20% savings\n- Prioritize building emergency fund first\n- Keep housing costs under 30% of income\n\nGeneral Budgeting Tips:\n- Track expenses for at least one month\n- Use the envelope method for discretionary spending\n- Automate savings and bill payments\n- Review and adjust budget quarterly",
  "age_group": "Young Adult (20s)",
  "profile_complete": false
}
```

#### 5. **POST /api/v1/financial-advisor/retirement-plan** - Retirement planning

**Sample Request (Early Career)**:
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

**Sample Request (Late Career)**:
```json
{
  "age": 55,
  "income_range": "ABOVE_150K",
  "retirement_age_target": 62,
  "current_savings": 750000,
  "employment_status": "EMPLOYED_FULL_TIME",
  "financial_goals": ["RETIREMENT"],
  "risk_tolerance": "CONSERVATIVE"
}
```

**Sample Response**:
```json
{
  "retirement_plan": "Personalized Retirement Planning:\n\nCurrent Age: 32\nTarget Retirement Age: 65\nYears to Retirement: 33\n\nMedium-Term Strategy (15-30 years):\n- Balance growth with some stability\n- Increase contribution rates annually\n- Diversify across asset classes",
  "current_age": 32,
  "target_retirement_age": 65,
  "years_to_retirement": 33
}
```

#### 6. **POST /api/v1/financial-advisor/profile/validate** - Profile validation

**Sample Request (Complete Profile)**:
```json
{
  "age": 35,
  "income_range": "RANGE_75K_100K",
  "risk_tolerance": "MODERATE",
  "investment_experience": "INTERMEDIATE"
}
```

**Sample Request (Incomplete Profile)**:
```json
{
  "age": 30,
  "interests": ["technology"]
}
```

**Sample Response (Complete)**:
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

**Sample Response (Incomplete)**:
```json
{
  "is_complete": false,
  "age_provided": true,
  "risk_tolerance_provided": false,
  "investment_experience_provided": false,
  "income_range_provided": false,
  "recommendations": "Please provide age, risk tolerance, investment experience, and income range for better recommendations"
}
```

#### 7. **GET /api/v1/financial-advisor/advisory-modes** - Advisory modes

**Sample Response**:
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

#### 8. **GET /api/v1/financial-advisor/profile/template** - Profile template

**Sample Response**:
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
    "technology",
    "healthcare",
    "environment",
    "real estate",
    "travel",
    "education",
    "renewable energy",
    "artificial intelligence"
  ]
}
```

---

## 🎯 **Swagger UI Features**

### **Interactive Testing**
1. **Try It Out**: Click "Try it out" on any endpoint
2. **Fill Parameters**: Use the provided examples
3. **Execute**: Click "Execute" to test the API
4. **View Response**: See real-time response with status codes

### **Schema Information**
- **Request Schema**: Complete field definitions
- **Response Schema**: Expected response structure  
- **Validation Rules**: Field constraints and requirements
- **Enum Values**: All available options for dropdown fields

### **Multiple Examples**
Each endpoint includes multiple realistic examples:
- **Young Professional** (20s-30s)
- **Mid-Career** (30s-40s) 
- **Pre-Retirement** (50s-60s)
- **Different Risk Tolerances** (Conservative, Moderate, Aggressive)
- **Various Interests** (Technology, Healthcare, Environment, etc.)

---

## 🔧 **How to Use Swagger UI**

### **Step 1: Access the Interface**
```
http://localhost:8080/swagger-ui/index.html
```

### **Step 2: Explore API Groups**
- **🤖 Basic LLM API**: Core chat functionality
- **💰 Financial Advisory API**: Personalized financial advice

### **Step 3: Test an Endpoint**
1. Click on any endpoint to expand
2. Click "Try it out"
3. Copy one of the provided examples
4. Paste into the request body
5. Click "Execute"
6. View the response

### **Step 4: Customize Requests**
- Modify age, interests, risk tolerance
- Try different advisory modes
- Test various financial profiles

---

## 📊 **Enhanced Documentation Features**

✅ **13 Fully Documented Endpoints**  
✅ **25+ Sample Request Examples**  
✅ **Complete Response Examples**  
✅ **Interactive Testing Interface**  
✅ **Schema Validation**  
✅ **Error Response Examples**  
✅ **Multiple User Personas**  
✅ **Age-Based Scenarios**  
✅ **Interest-Based Examples**  
✅ **Risk Tolerance Variations**  

---

## 🎉 **Ready to Use!**

Your **Financial Advisory Chatbot** now has **comprehensive Swagger documentation** with:

- **Complete API coverage** for all 13 endpoints
- **Realistic sample requests** for different user profiles  
- **Detailed response examples** showing expected outputs
- **Interactive testing interface** for immediate API testing
- **Schema definitions** for all data models
- **Multiple scenarios** covering different ages and interests

**Access the enhanced Swagger UI at**: `http://localhost:8080/swagger-ui/index.html`

**🚀 All APIs are now fully documented and ready for testing!**