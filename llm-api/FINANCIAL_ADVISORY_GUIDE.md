# Financial Advisory Chatbot - Complete Guide

## 🎯 Overview

This Financial Advisory Chatbot provides personalized financial advice based on user's **age** and **personal interests**. The system uses advanced LLM integration to deliver tailored recommendations for investment, budgeting, retirement planning, and more.

## 🚀 Key Features

### 1. **Age-Based Personalization**
- **20s (18-29)**: Foundation building, emergency funds, early investing
- **30s (30-39)**: Acceleration phase, family planning, home ownership
- **40s (40-49)**: Peak earning years, education funding, estate planning
- **50s (50-59)**: Pre-retirement planning, catch-up contributions
- **60+ (60-100)**: Retirement transition, legacy planning

### 2. **Interest-Based Investment Recommendations**
- **Technology**: Tech sector ETFs (QQQ, XLK)
- **Healthcare**: Healthcare sector funds (XLV, VHT)
- **Environment**: ESG funds, sustainable investing
- **Real Estate**: REITs and property investments
- **Travel**: Travel and leisure sector investments

### 3. **Risk Tolerance Integration**
- **Conservative**: Bonds, dividend stocks, CDs
- **Moderate**: Balanced portfolios, index funds
- **Aggressive**: Growth stocks, emerging markets

## 📊 API Endpoints

### Core Financial Advisory Endpoints

#### 1. Personalized Chat
```http
POST /api/v1/financial-advisor/chat
```

**Example Request:**
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
    "financial_goals": ["RETIREMENT", "HOME_PURCHASE"]
  }
}
```

#### 2. Age-Based Goals
```http
GET /api/v1/financial-advisor/age-based-goals/{age}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/financial-advisor/age-based-goals/28"
```

**Response:**
```json
{
  "age": 28,
  "goals": "In Your 20s - Foundation Building:\n- Build emergency fund (3-6 months expenses)\n- Start investing early (even small amounts)\n- Maximize employer 401(k) match\n- Pay off high-interest debt\n- Build good credit history"
}
```

#### 3. Investment Recommendations
```http
POST /api/v1/financial-advisor/investment-recommendations
```

**Example Request:**
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

#### 4. Budgeting Advice
```http
POST /api/v1/financial-advisor/budgeting-advice
```

#### 5. Retirement Planning
```http
POST /api/v1/financial-advisor/retirement-plan
```

#### 6. Profile Validation
```http
POST /api/v1/financial-advisor/profile/validate
```

#### 7. Profile Template
```http
GET /api/v1/financial-advisor/profile/template
```

#### 8. Advisory Modes
```http
GET /api/v1/financial-advisor/advisory-modes
```

## 🎭 Advisory Modes

- **GENERAL**: Comprehensive financial guidance
- **INVESTMENT_FOCUSED**: Investment strategies and portfolio management
- **BUDGETING**: Expense management and budgeting techniques
- **RETIREMENT_PLANNING**: Retirement savings and planning
- **DEBT_MANAGEMENT**: Debt reduction strategies
- **TAX_PLANNING**: Tax-efficient strategies
- **INSURANCE_PLANNING**: Risk management and insurance
- **EMERGENCY_FUND**: Emergency fund building

## 👤 Financial Profile Structure

### Required Fields
```json
{
  "age": 28,
  "risk_tolerance": "MODERATE",
  "investment_experience": "BEGINNER",
  "income_range": "RANGE_75K_100K"
}
```

### Optional Fields
```json
{
  "financial_goals": ["RETIREMENT", "HOME_PURCHASE"],
  "interests": ["technology", "healthcare"],
  "current_savings": 50000,
  "monthly_expenses": 4000,
  "debt_amount": 25000,
  "employment_status": "EMPLOYED_FULL_TIME",
  "marital_status": "SINGLE",
  "number_of_dependents": 0,
  "retirement_age_target": 65,
  "time_horizon": "LONG_TERM"
}
```

## 💡 Usage Examples

### Example 1: Young Tech Professional (Age 26)
```bash
curl -X POST "http://localhost:8080/api/v1/financial-advisor/chat" \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I just started my tech career. How should I invest my first $10,000?",
    "session_id": "tech-professional",
    "advisory_mode": "INVESTMENT_FOCUSED",
    "financial_profile": {
      "age": 26,
      "income_range": "RANGE_75K_100K",
      "risk_tolerance": "MODERATE",
      "investment_experience": "BEGINNER",
      "interests": ["technology", "startups"],
      "financial_goals": ["RETIREMENT", "EMERGENCY_FUND"]
    }
  }'
```

### Example 2: Mid-Career Healthcare Worker (Age 42)
```bash
curl -X POST "http://localhost:8080/api/v1/financial-advisor/investment-recommendations" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 42,
    "income_range": "RANGE_100K_150K",
    "risk_tolerance": "CONSERVATIVE",
    "investment_experience": "INTERMEDIATE",
    "interests": ["healthcare", "education"],
    "financial_goals": ["RETIREMENT", "CHILD_EDUCATION"],
    "marital_status": "MARRIED",
    "number_of_dependents": 2
  }'
```

### Example 3: Pre-Retiree (Age 58)
```bash
curl -X POST "http://localhost:8080/api/v1/financial-advisor/retirement-plan" \
  -H "Content-Type: application/json" \
  -d '{
    "age": 58,
    "income_range": "ABOVE_150K",
    "retirement_age_target": 62,
    "current_savings": 750000,
    "risk_tolerance": "CONSERVATIVE",
    "financial_goals": ["RETIREMENT"]
  }'
```

## 🔧 Configuration

### Environment Variables
```bash
# For real LLM responses (optional)
export OPENAI_API_KEY="your-openai-api-key"
export OPENAI_BASE_URL="https://api.openai.com/v1"
```

### Application Properties
```yaml
# Financial Advisory Configuration
financial-advisory:
  default-advisory-mode: GENERAL
  include-disclaimers: true
  max-profile-cache-hours: 24
```

## 🎨 Age-Specific Investment Allocation

| Age Group | Stocks % | Bonds % | Focus Area |
|-----------|----------|---------|------------|
| 20s | 80-90% | 10-20% | Growth & Foundation |
| 30s | 70-80% | 20-30% | Acceleration |
| 40s | 60-70% | 30-40% | Peak Earning |
| 50s | 50-60% | 40-50% | Pre-Retirement |
| 60+ | 40-50% | 50-60% | Capital Preservation |

## 🌟 Interest-Based Investment Themes

### Technology Enthusiasts
- Technology sector ETFs (QQQ, XLK)
- Innovation-focused funds
- AI and robotics investments

### Healthcare Professionals
- Healthcare sector funds (XLV, VHT)
- Biotech investments
- Medical device companies

### Environmental Advocates
- ESG (Environmental, Social, Governance) funds
- Clean energy ETFs
- Sustainable investing options

### Real Estate Interested
- REITs (Real Estate Investment Trusts)
- Real estate crowdfunding
- Property development funds

## 📈 Testing the API

### Run the Test Suite
```bash
chmod +x test-financial-advisor.sh
./test-financial-advisor.sh
```

### Manual Testing Examples
```bash
# Test age-based goals
curl -X GET "http://localhost:8080/api/v1/financial-advisor/age-based-goals/35"

# Test profile validation
curl -X POST "http://localhost:8080/api/v1/financial-advisor/profile/validate" \
  -H "Content-Type: application/json" \
  -d '{"age": 30, "risk_tolerance": "MODERATE"}'

# Get profile template
curl -X GET "http://localhost:8080/api/v1/financial-advisor/profile/template"
```

## 🔒 Security & Disclaimers

- **Educational Purpose**: All advice is for educational purposes only
- **Professional Consultation**: Users are advised to consult qualified financial advisors
- **No Guarantees**: Past performance does not guarantee future results
- **Risk Awareness**: All investments carry risk of loss

## 🚀 Getting Started

1. **Start the Application**
   ```bash
   cd llm-api
   mvn spring-boot:run
   ```

2. **Access Swagger Documentation**
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

3. **Test Basic Functionality**
   ```bash
   curl -X GET "http://localhost:8080/api/v1/llm/health"
   ```

4. **Try Financial Advisory**
   ```bash
   curl -X GET "http://localhost:8080/api/v1/financial-advisor/age-based-goals/25"
   ```

## 📞 Support

For questions or issues:
- Check the application logs: `tail -f financial-advisor.log`
- Review API documentation at `/swagger-ui/index.html`
- Validate your profile using the validation endpoint

---

**🎉 Your personalized financial advisory chatbot is ready to help users make informed financial decisions based on their age and interests!**