# 🚀 **Financial Advisor LLM API - Deployment Guide**

## ✅ **INTEGRATED DEPLOYMENT READY!**

Your Financial Advisor LLM API now includes the web UI and is ready for deployment as a **single, unified application**.

---

## 🎯 **What's Changed for Deployment**

### ✅ **Unified Application**
- **Backend + Frontend**: Both API and UI served from the same Spring Boot application
- **Single Port**: Everything runs on port 8080
- **No Separate Servers**: No need for separate UI server
- **Production Ready**: Optimized for deployment

### ✅ **Static Resources Integration**
- UI files copied to `src/main/resources/static/`
- Spring Boot automatically serves static content
- Relative API URLs for seamless integration
- Web controller handles routing

---

## 🌐 **Deployment Options**

### **Option 1: Simple JAR Deployment**

#### **1. Build the Application**
```bash
cd /workspace/llm-api
mvn clean package -DskipTests
```

#### **2. Run the JAR**
```bash
java -jar target/llm-api-1.0.0.jar
```

#### **3. Access the Application**
- **Web UI**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/api/v1/llm/health

---

### **Option 2: Docker Deployment**

#### **1. Build Docker Image**
```bash
cd /workspace/llm-api
docker build -t financial-advisor-api .
```

#### **2. Run Container**
```bash
docker run -d \
  --name financial-advisor \
  -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key-here \
  financial-advisor-api
```

#### **3. Access the Application**
- **Web UI**: http://localhost:8080
- **Container Logs**: `docker logs financial-advisor`

---

### **Option 3: Docker Compose Deployment**

#### **1. Start Services**
```bash
cd /workspace/llm-api
docker-compose up -d
```

#### **2. View Logs**
```bash
docker-compose logs -f financial-advisor-api
```

#### **3. Stop Services**
```bash
docker-compose down
```

---

## ⚙️ **Configuration for Deployment**

### **Environment Variables**
```bash
# Required
OPENAI_API_KEY=your-openai-api-key-here

# Optional
PORT=8080
SPRING_PROFILES_ACTIVE=deploy
DATABASE_URL=jdbc:h2:mem:financialdb
CORS_ALLOWED_ORIGINS=*
JAVA_OPTS="-Xmx1g -Xms512m"
```

### **Application Properties**
Create `application-deploy.properties` for production:
```properties
server.port=${PORT:8080}
llm.openai.api-key=${OPENAI_API_KEY:}
spring.web.resources.static-locations=classpath:/static/
```

---

## 🌍 **Cloud Deployment**

### **Heroku Deployment**

#### **1. Create Heroku App**
```bash
heroku create your-financial-advisor-app
```

#### **2. Set Environment Variables**
```bash
heroku config:set OPENAI_API_KEY=your-api-key-here
heroku config:set SPRING_PROFILES_ACTIVE=deploy
```

#### **3. Deploy**
```bash
git add .
git commit -m "Deploy financial advisor app"
git push heroku main
```

#### **4. Access**
- **URL**: https://your-financial-advisor-app.herokuapp.com

---

### **AWS Deployment (Elastic Beanstalk)**

#### **1. Build JAR**
```bash
mvn clean package -DskipTests
```

#### **2. Create Application**
- Upload `target/llm-api-1.0.0.jar` to Elastic Beanstalk
- Set environment variables in EB console

#### **3. Configure**
```properties
OPENAI_API_KEY=your-api-key-here
SPRING_PROFILES_ACTIVE=deploy
SERVER_PORT=5000
```

---

### **Google Cloud Run Deployment**

#### **1. Build and Push Image**
```bash
# Build for Cloud Run
docker build -t gcr.io/your-project/financial-advisor .
docker push gcr.io/your-project/financial-advisor
```

#### **2. Deploy to Cloud Run**
```bash
gcloud run deploy financial-advisor \
  --image gcr.io/your-project/financial-advisor \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars OPENAI_API_KEY=your-api-key-here
```

---

## 🔧 **Production Configuration**

### **Database Configuration**
For production, consider using PostgreSQL:

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/financialdb
spring.datasource.username=financial_user
spring.datasource.password=secure_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### **Security Configuration**
```properties
# Security
cors.allowed-origins=https://yourdomain.com
h2.console.enabled=false
management.endpoints.web.exposure.include=health,info
```

### **Logging Configuration**
```properties
# Logging
logging.level.com.llmapi=INFO
logging.file.name=logs/financial-advisor.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 📊 **Monitoring and Health Checks**

### **Health Check Endpoints**
- **Application Health**: `/api/v1/llm/health`
- **Spring Boot Actuator**: `/actuator/health`
- **Application Info**: `/actuator/info`

### **Docker Health Check**
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/llm/health || exit 1
```

### **Kubernetes Health Check**
```yaml
livenessProbe:
  httpGet:
    path: /api/v1/llm/health
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 30
```

---

## 🎯 **Testing Deployment**

### **1. Build and Test Locally**
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/llm-api-1.0.0.jar

# Test UI
curl http://localhost:8080

# Test API
curl http://localhost:8080/api/v1/llm/health
```

### **2. Test with Sample Request**
```bash
curl -X POST http://localhost:8080/api/v1/financial-advisor/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How do I start investing?"}'
```

---

## 🚀 **Quick Deployment Commands**

### **Local Development**
```bash
cd /workspace/llm-api
mvn spring-boot:run
# Access: http://localhost:8080
```

### **Production JAR**
```bash
cd /workspace/llm-api
mvn clean package -DskipTests
java -Dspring.profiles.active=deploy -jar target/llm-api-1.0.0.jar
```

### **Docker Production**
```bash
cd /workspace/llm-api
docker-compose up -d
# Access: http://localhost:8080
```

---

## ✅ **Deployment Checklist**

- [ ] ✅ UI files copied to `src/main/resources/static/`
- [ ] ✅ JavaScript updated to use relative URLs
- [ ] ✅ Web controller added for routing
- [ ] ✅ Security configuration updated
- [ ] ✅ Deployment properties configured
- [ ] ✅ Dockerfile optimized for production
- [ ] ✅ Docker Compose ready
- [ ] ✅ Environment variables documented
- [ ] ✅ Health checks implemented
- [ ] ✅ Static resources properly served

---

## 🎉 **Ready for Deployment!**

Your **Financial Advisor LLM API** is now a **unified, deployment-ready application** that includes:

✨ **Complete Backend API**  
✨ **Integrated Web UI**  
✨ **Single Port Deployment**  
✨ **Docker Support**  
✨ **Cloud-Ready Configuration**  
✨ **Production Optimized**  

**🌐 Deploy anywhere and access both the API and UI from the same URL!**

---

*Deploy with confidence! Your AI Financial Advisor is ready for production! 🚀*