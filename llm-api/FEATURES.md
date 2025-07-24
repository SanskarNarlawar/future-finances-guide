# LLM API Features

## 🚀 Core Features

### 1. **Chat API**
- **Endpoint**: `POST /api/v1/llm/chat`
- Send messages to LLM and get responses
- Session-based conversations with context preservation
- Configurable parameters (temperature, max_tokens, etc.)
- Support for multiple LLM providers (OpenAI integration ready)

### 2. **Conversation Management**
- **History**: `GET /api/v1/llm/history/{sessionId}`
- **Clear History**: `DELETE /api/v1/llm/history/{sessionId}`
- Persistent conversation storage
- Session-based chat contexts
- Message role tracking (user/assistant)

### 3. **Health Monitoring**
- **Endpoint**: `GET /api/v1/llm/health`
- Application health status
- Version information
- Service availability check

## 🛠️ Technical Features

### **Architecture**
- ✅ Spring Boot 3.2.0 with Java 17
- ✅ RESTful API design
- ✅ Layered architecture (Controller → Service → Repository)
- ✅ JPA/Hibernate for data persistence
- ✅ Reactive WebClient for external API calls

### **Database**
- ✅ H2 in-memory database (development)
- ✅ PostgreSQL support (production)
- ✅ JPA entities with proper relationships
- ✅ Database console access (`/h2-console`)

### **Security**
- ✅ Spring Security integration
- ✅ CORS configuration
- ✅ Stateless session management
- ✅ Public API endpoints

### **Documentation**
- ✅ Swagger/OpenAPI integration
- ✅ Interactive API documentation (`/swagger-ui/index.html`)
- ✅ Comprehensive README and guides

### **Validation & Error Handling**
- ✅ Request validation with Bean Validation
- ✅ Global exception handling
- ✅ Structured error responses
- ✅ Input sanitization

## 🔌 LLM Integration

### **OpenAI Support**
- ✅ GPT-3.5 and GPT-4 models
- ✅ Configurable API endpoints
- ✅ Token usage tracking
- ✅ Response streaming ready

### **Mock Responses**
- ✅ Works without API keys for testing
- ✅ Simulated LLM responses
- ✅ Development-friendly defaults

### **Extensible Design**
- ✅ Easy to add new LLM providers
- ✅ Provider abstraction layer
- ✅ Configurable model parameters

## 📊 Data Models

### **ChatMessage Entity**
```java
- id: Long (Primary Key)
- sessionId: String
- role: Role (USER/ASSISTANT)
- content: String
- tokenCount: Integer
- createdAt: LocalDateTime
```

### **API DTOs**
- **ChatRequest**: Input parameters for chat
- **ChatResponse**: Structured response with metadata
- **ErrorResponse**: Standardized error format

## 🚢 Deployment Options

### **Local Development**
```bash
mvn spring-boot:run
```

### **Docker**
```bash
docker build -t llm-api .
docker run -p 8080:8080 llm-api
```

### **Docker Compose**
```bash
docker-compose up -d
```
- Includes PostgreSQL database
- PgAdmin for database management
- Production-ready configuration

## 🔧 Configuration

### **Environment Variables**
- `OPENAI_API_KEY`: Your OpenAI API key
- `OPENAI_BASE_URL`: Custom OpenAI endpoint
- `SPRING_PROFILES_ACTIVE`: Environment profile

### **Application Properties**
- Database configuration
- LLM provider settings
- Security configuration
- Logging levels

## 📈 Monitoring & Observability

### **Health Checks**
- Application health endpoint
- Database connectivity
- External service status

### **Logging**
- Structured logging with SLF4J
- Request/response logging
- Error tracking
- Performance metrics ready

## 🔮 Future Enhancements

### **Planned Features**
- [ ] Multiple LLM provider support (Anthropic, Cohere, etc.)
- [ ] Rate limiting and quotas
- [ ] User authentication and authorization
- [ ] Conversation templates and presets
- [ ] File upload and processing
- [ ] Real-time streaming responses
- [ ] Analytics and usage metrics
- [ ] Conversation export/import
- [ ] Custom model fine-tuning support

### **Performance Optimizations**
- [ ] Response caching
- [ ] Connection pooling
- [ ] Async processing
- [ ] Load balancing support

## 🎯 Use Cases

1. **Chatbot Development**: Build conversational AI applications
2. **Content Generation**: Generate text, articles, and creative content
3. **Code Assistance**: Programming help and code generation
4. **Customer Support**: Automated customer service responses
5. **Educational Tools**: Learning and tutoring applications
6. **Research Assistant**: Information gathering and analysis

## 📚 API Examples

### **Start a Conversation**
```bash
curl -X POST http://localhost:8080/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello, how are you?",
    "session_id": "user-123",
    "temperature": 0.7,
    "max_tokens": 150
  }'
```

### **Get Chat History**
```bash
curl -X GET http://localhost:8080/api/v1/llm/history/user-123
```

### **Health Check**
```bash
curl -X GET http://localhost:8080/api/v1/llm/health
```