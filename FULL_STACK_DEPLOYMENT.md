# Full-Stack Google Cloud Deployment - React + Java Spring Boot

This guide deploys both the **React frontend** and **Java Spring Boot backend** to Google Cloud.

## 🏗 **Architecture Overview**

- **Frontend**: React/TypeScript SPA → **App Engine** (Standard Environment)
- **Backend**: Java Spring Boot API → **App Engine** (Standard Environment) 
- **Database**: H2 in-memory (for development) or Cloud SQL (for production)

## 🚀 **Deployment Options**

### Option 1: Deploy Both Services to App Engine
### Option 2: Deploy Backend to Cloud Run + Frontend to App Engine
### Option 3: Deploy Both to Google Kubernetes Engine (GKE)

---

## 📋 **Option 1: App Engine Deployment (Recommended)**

### **Step 1: Setup Google Cloud Project**

```bash
# Set your project ID
export PROJECT_ID="YOUR_PROJECT_ID"
gcloud config set project $PROJECT_ID

# Enable required APIs
gcloud services enable appengine.googleapis.com
gcloud services enable cloudbuild.googleapis.com
gcloud services enable sqladmin.googleapis.com

# Create App Engine application
gcloud app create --region=us-central1
```

### **Step 2: Deploy Java Backend First**

```bash
# Navigate to backend directory
cd llm-api/

# Create app.yaml for Java backend
cat > app.yaml << EOF
runtime: java17

service: api

env_variables:
  SPRING_PROFILES_ACTIVE: production
  SERVER_PORT: 8080

automatic_scaling:
  min_instances: 0
  max_instances: 5

health_check:
  enable_health_check: true
  check_interval_sec: 5
  timeout_sec: 4
  unhealthy_threshold: 2
  healthy_threshold: 2
EOF

# Build and deploy backend
mvn clean package -DskipTests
gcloud app deploy app.yaml --version=v1
```

### **Step 3: Update Frontend Configuration**

```bash
# Go back to root directory
cd ..

# Update frontend to point to backend service
# Get backend URL
export BACKEND_URL=$(gcloud app describe --service=api --format="value(defaultHostname)")
echo "Backend URL: https://$BACKEND_URL"
```

Create/update your frontend environment configuration:

```bash
# Create .env.production file
cat > .env.production << EOF
VITE_API_BASE_URL=https://$BACKEND_URL
VITE_NODE_ENV=production
EOF
```

### **Step 4: Deploy React Frontend**

```bash
# Install dependencies and build
npm install
npm run build

# Deploy frontend (default service)
gcloud app deploy app.yaml
```

### **Step 5: Configure CORS in Backend**

Update your Java backend's CORS configuration to allow the frontend domain:

```java
// In SecurityConfig.java or WebConfig.java
@CrossOrigin(origins = "https://YOUR_FRONTEND_URL.appspot.com")
```

---

## 📋 **Option 2: Cloud Run + App Engine**

### **Deploy Backend to Cloud Run**

```bash
cd llm-api/

# Build Docker image
docker build -t gcr.io/$PROJECT_ID/llm-api .

# Push to Container Registry
docker push gcr.io/$PROJECT_ID/llm-api

# Deploy to Cloud Run
gcloud run deploy llm-api \
  --image gcr.io/$PROJECT_ID/llm-api \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8080
```

### **Deploy Frontend to App Engine**

```bash
cd ..

# Get Cloud Run URL
export BACKEND_URL=$(gcloud run services describe llm-api --region=us-central1 --format="value(status.url)")

# Update frontend config
echo "VITE_API_BASE_URL=$BACKEND_URL" > .env.production

# Build and deploy frontend
npm install
npm run build
gcloud app deploy app.yaml
```

---

## 🛠 **Complete Deployment Commands**

Here's the fastest way to deploy both services:

```bash
# 1. Setup project
export PROJECT_ID="YOUR_PROJECT_ID"
gcloud config set project $PROJECT_ID
gcloud services enable appengine.googleapis.com cloudbuild.googleapis.com
gcloud app create --region=us-central1

# 2. Deploy Java backend
cd llm-api/
cat > app.yaml << 'EOF'
runtime: java17
service: api
env_variables:
  SPRING_PROFILES_ACTIVE: production
automatic_scaling:
  min_instances: 0
  max_instances: 5
EOF

mvn clean package -DskipTests
gcloud app deploy app.yaml --version=v1

# 3. Get backend URL and configure frontend
cd ..
export BACKEND_URL=$(gcloud app describe --service=api --format="value(defaultHostname)")
echo "VITE_API_BASE_URL=https://$BACKEND_URL" > .env.production

# 4. Deploy React frontend
npm install
npm run build
gcloud app deploy app.yaml

# 5. Open your app
gcloud app browse
```

---

## 🔧 **Backend Configuration Updates**

### **Update application.properties**

```properties
# src/main/resources/application-production.properties
server.port=${PORT:8080}
spring.profiles.active=production

# CORS Configuration
cors.allowed.origins=${FRONTEND_URL:https://YOUR_PROJECT_ID.appspot.com}

# Database (if using Cloud SQL)
spring.datasource.url=${DATABASE_URL:jdbc:h2:mem:testdb}
spring.jpa.hibernate.ddl-auto=update
```

### **Update CORS Configuration**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Value("${cors.allowed.origins}")
    private String allowedOrigins;
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

---

## 📊 **Service Communication**

### **Frontend → Backend Communication**

```typescript
// In your React app
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

### **Backend Health Check Endpoint**

```java
@RestController
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "llm-api");
        return ResponseEntity.ok(status);
    }
}
```

---

## 🔍 **Monitoring & Management**

### **View Both Services**

```bash
# List all services
gcloud app services list

# View backend logs
gcloud app logs tail -s api

# View frontend logs  
gcloud app logs tail -s default

# Get service URLs
gcloud app browse --service=api
gcloud app browse
```

### **Scaling Configuration**

```yaml
# For high-traffic applications
automatic_scaling:
  min_instances: 2
  max_instances: 20
  target_cpu_utilization: 0.6
  target_throughput_utilization: 0.6
```

---

## 💰 **Cost Optimization**

1. **Auto-scaling**: Both services scale to 0 when not in use
2. **Instance Classes**: Use F1/F2 instances for cost efficiency
3. **Static Assets**: Serve from CDN if needed
4. **Database**: Use Cloud SQL only for production data

---

## 🚨 **Troubleshooting**

### **Common Issues**

1. **CORS Errors**: Ensure backend allows frontend domain
2. **Service Communication**: Check environment variables
3. **Build Failures**: Verify Java 17 and Node.js versions
4. **Database Connection**: Check Cloud SQL configuration

### **Debug Commands**

```bash
# Check service status
gcloud app services list

# View detailed logs
gcloud app logs read -s api --limit=100
gcloud app logs read -s default --limit=100

# Test backend health
curl https://api-dot-YOUR_PROJECT_ID.appspot.com/health

# Test frontend
curl https://YOUR_PROJECT_ID.appspot.com
```

---

## 🔐 **Security Considerations**

1. **HTTPS Only**: All traffic encrypted
2. **CORS**: Properly configured for frontend domain
3. **Environment Variables**: Sensitive data in App Engine environment
4. **Authentication**: Implement proper auth for production

---

**Note**: Replace `YOUR_PROJECT_ID` with your actual Google Cloud Project ID. This setup provides a scalable, cost-effective full-stack deployment on Google Cloud.