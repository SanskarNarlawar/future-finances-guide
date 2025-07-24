# Code Structure - Financial Advisory Chatbot APIs

## 📁 **Project Structure**

```
llm-api/
├── src/main/java/com/llmapi/
│   ├── controller/
│   │   ├── LlmController.java              # Basic LLM APIs (5 endpoints)
│   │   └── FinancialAdvisoryController.java # Financial Advisory APIs (8 endpoints)
│   ├── service/
│   │   ├── LlmService.java                 # LLM service interface
│   │   ├── FinancialAdvisoryService.java   # Financial advisory service interface
│   │   └── impl/
│   │       ├── LlmServiceImpl.java         # LLM service implementation
│   │       └── FinancialAdvisoryServiceImpl.java # Financial advisory implementation
│   ├── dto/
│   │   ├── ChatRequest.java                # Request DTOs
│   │   ├── ChatResponse.java               # Response DTOs
│   │   └── FinancialProfile.java           # Financial profile DTO with enums
│   ├── model/
│   │   └── ChatMessage.java                # Database entity
│   ├── repository/
│   │   └── ChatMessageRepository.java      # Data access layer
│   ├── config/
│   │   ├── SecurityConfig.java             # Security configuration
│   │   └── WebClientConfig.java            # HTTP client configuration
│   └── LlmApiApplication.java              # Main Spring Boot application
├── src/main/resources/
│   └── application.yml                     # Application configuration
├── src/test/java/
│   └── com/llmapi/LlmApiApplicationTests.java # Tests
├── test-financial-advisor.sh               # Comprehensive test script
├── API_ENDPOINTS_COMPLETE.md               # Complete API documentation
├── FINANCIAL_ADVISORY_GUIDE.md             # Usage guide
└── pom.xml                                 # Maven dependencies
```

---

## 🎯 **API Implementation Mapping**

### **Basic LLM APIs** → `LlmController.java`

| Endpoint | Method | Implementation |
|----------|--------|----------------|
| `/api/v1/llm/chat` | `POST` | `chat()` method |
| `/api/v1/llm/chat/history/{sessionId}` | `GET` | `getChatHistory()` method |
| `/api/v1/llm/chat/history/{sessionId}` | `DELETE` | `clearChatHistory()` method |
| `/api/v1/llm/health` | `GET` | `health()` method |
| `/api/v1/llm/models` | `GET` | `getModels()` method |

**File**: `src/main/java/com/llmapi/controller/LlmController.java` (87 lines)

### **Financial Advisory APIs** → `FinancialAdvisoryController.java`

| Endpoint | Method | Implementation |
|----------|--------|----------------|
| `/api/v1/financial-advisor/chat` | `POST` | `getFinancialAdvice()` method |
| `/api/v1/financial-advisor/age-based-goals/{age}` | `GET` | `getAgeBasedGoals()` method |
| `/api/v1/financial-advisor/investment-recommendations` | `POST` | `getInvestmentRecommendations()` method |
| `/api/v1/financial-advisor/budgeting-advice` | `POST` | `getBudgetingAdvice()` method |
| `/api/v1/financial-advisor/retirement-plan` | `POST` | `getRetirementPlan()` method |
| `/api/v1/financial-advisor/profile/validate` | `POST` | `validateProfile()` method |
| `/api/v1/financial-advisor/profile/template` | `GET` | `getProfileTemplate()` method |
| `/api/v1/financial-advisor/advisory-modes` | `GET` | `getAdvisoryModes()` method |

**File**: `src/main/java/com/llmapi/controller/FinancialAdvisoryController.java` (242 lines)

---

## 🧩 **Key Components**

### **1. Controllers (API Layer)**

#### `LlmController.java` - Basic LLM functionality
```java
@RestController
@RequestMapping("/api/v1/llm")
public class LlmController {
    
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request)
    
    @GetMapping("/chat/history/{sessionId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String sessionId)
    
    @DeleteMapping("/chat/history/{sessionId}")
    public ResponseEntity<Map<String, String>> clearChatHistory(@PathVariable String sessionId)
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health()
    
    @GetMapping("/models")
    public ResponseEntity<Map<String, Object>> getModels()
}
```

#### `FinancialAdvisoryController.java` - Financial advisory functionality
```java
@RestController
@RequestMapping("/api/v1/financial-advisor")
public class FinancialAdvisoryController {
    
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> getFinancialAdvice(@Valid @RequestBody ChatRequest request)
    
    @GetMapping("/age-based-goals/{age}")
    public ResponseEntity<Map<String, Object>> getAgeBasedGoals(@PathVariable Integer age)
    
    @PostMapping("/investment-recommendations")
    public ResponseEntity<Map<String, Object>> getInvestmentRecommendations(@Valid @RequestBody FinancialProfile profile)
    
    @PostMapping("/budgeting-advice")
    public ResponseEntity<Map<String, Object>> getBudgetingAdvice(@Valid @RequestBody FinancialProfile profile)
    
    @PostMapping("/retirement-plan")
    public ResponseEntity<Map<String, Object>> getRetirementPlan(@Valid @RequestBody FinancialProfile profile)
    
    @PostMapping("/profile/validate")
    public ResponseEntity<Map<String, Object>> validateProfile(@Valid @RequestBody FinancialProfile profile)
    
    @GetMapping("/profile/template")
    public ResponseEntity<Map<String, Object>> getProfileTemplate()
    
    @GetMapping("/advisory-modes")
    public ResponseEntity<Map<String, Object>> getAdvisoryModes()
}
```

### **2. Services (Business Logic Layer)**

#### `LlmService.java` & `LlmServiceImpl.java`
- Basic LLM chat functionality
- OpenAI integration
- Chat history management
- Mock responses when no API key

#### `FinancialAdvisoryService.java` & `FinancialAdvisoryServiceImpl.java`
- Age-based personalization logic
- Interest-based investment recommendations
- Risk tolerance integration
- Personalized system prompts
- Financial planning algorithms

### **3. DTOs (Data Transfer Objects)**

#### `ChatRequest.java`
```java
public class ChatRequest {
    private String message;
    private String sessionId;
    private String modelName;
    private FinancialProfile financialProfile;  // Enhanced for financial advisory
    private AdvisoryMode advisoryMode;
    private ContextType contextType;
    // ... other fields
}
```

#### `FinancialProfile.java`
```java
public class FinancialProfile {
    private Integer age;                    // Core field for age-based advice
    private List<String> interests;         // Core field for interest-based recommendations
    private RiskTolerance riskTolerance;
    private InvestmentExperience investmentExperience;
    private List<FinancialGoal> financialGoals;
    // ... 15+ other fields with enums
}
```

#### `ChatResponse.java`
```java
public class ChatResponse {
    private String id;
    private String sessionId;
    private String message;
    private LocalDateTime createdAt;
    private Integer tokenCount;
    // ... other fields
}
```

### **4. Configuration**

#### `SecurityConfig.java`
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/v1/llm/**").permitAll()
                .requestMatchers("/api/v1/financial-advisor/**").permitAll()  // Added for financial APIs
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

#### `application.yml`
```yaml
# Financial Advisory Configuration
financial-advisory:
  default-advisory-mode: GENERAL
  include-disclaimers: true
  max-profile-cache-hours: 24
  age-groups:
    young-adult: { min-age: 18, max-age: 29 }
    early-career: { min-age: 30, max-age: 39 }
    mid-career: { min-age: 40, max-age: 49 }
    pre-retirement: { min-age: 50, max-age: 59 }
    retirement: { min-age: 60, max-age: 100 }
```

---

## 🔍 **How Age & Interest Personalization Works**

### **Age-Based Logic** (in `FinancialAdvisoryServiceImpl.java`)

```java
private String getAgeSpecificGuidance(int age) {
    if (age < 30) {
        return "- Time is your greatest asset - start investing early\n" +
               "- Focus on building good financial habits\n" +
               "- Take advantage of compound interest\n";
    } else if (age < 50) {
        return "- Peak earning years - maximize retirement contributions\n" +
               "- Balance family needs with future security\n" +
               "- Consider tax-efficient strategies\n";
    } else {
        return "- Catch-up contributions available\n" +
               "- Shift towards conservative approach\n" +
               "- Plan for healthcare costs\n";
    }
}
```

### **Interest-Based Logic** (in `FinancialAdvisoryServiceImpl.java`)

```java
private String getInterestBasedInvestments(String interest) {
    String lowerInterest = interest.toLowerCase();
    if (lowerInterest.contains("technology")) {
        return "- Technology sector ETFs (QQQ, XLK)\n";
    } else if (lowerInterest.contains("healthcare")) {
        return "- Healthcare sector funds (XLV, VHT)\n";
    } else if (lowerInterest.contains("environment")) {
        return "- ESG (Environmental, Social, Governance) funds\n";
    }
    // ... more interest mappings
}
```

---

## 🧪 **Testing**

### **Test Script**: `test-financial-advisor.sh`
- Tests all 13 API endpoints
- Provides sample requests for different age groups
- Demonstrates interest-based recommendations
- Validates profile completeness

### **Manual Testing Examples**:
```bash
# Test age-based goals
curl -X GET "http://localhost:8080/api/v1/financial-advisor/age-based-goals/28"

# Test investment recommendations with interests
curl -X POST "http://localhost:8080/api/v1/financial-advisor/investment-recommendations" \
  -H "Content-Type: application/json" \
  -d '{"age": 28, "interests": ["technology", "healthcare"], "risk_tolerance": "MODERATE"}'
```

---

## 📊 **Summary**

### **Total Lines of Code**: ~1,500 lines
- Controllers: ~330 lines
- Services: ~650 lines  
- DTOs: ~350 lines
- Configuration: ~70 lines
- Tests & Scripts: ~100 lines

### **Key Features Implemented**:
✅ **13 REST API endpoints**  
✅ **Age-based personalization** (20s, 30s, 40s, 50s, 60+)  
✅ **Interest-based investment recommendations**  
✅ **Risk tolerance integration**  
✅ **Comprehensive financial profile system**  
✅ **Multiple advisory modes**  
✅ **Profile validation and templates**  
✅ **Complete test suite**  
✅ **Swagger documentation**  
✅ **Security configuration**  

**🎉 All APIs are fully implemented and ready to use!**