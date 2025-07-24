# 🚀 **Step-by-Step Deployment Instructions**

## 📋 **Prerequisites**

Before deploying, ensure you have:
- ✅ Java 17 or higher installed
- ✅ Maven 3.6+ installed
- ✅ Docker installed (for Docker deployments)
- ✅ Git installed
- ✅ OpenAI API key (optional, for full LLM functionality)

---

## 🎯 **Method 1: Local JAR Deployment (Simplest)**

### **Step 1: Navigate to Project Directory**
```bash
cd /workspace/llm-api
```

### **Step 2: Clean and Build the Application**
```bash
mvn clean package -DskipTests
```
**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 2-3 minutes
```

### **Step 3: Verify JAR File Creation**
```bash
ls -la target/
```
**Look for:** `llm-api-1.0.0.jar` (approximately 50-80 MB)

### **Step 4: Run the Application**
```bash
java -jar target/llm-api-1.0.0.jar
```

### **Step 5: Verify Deployment**
**Wait for this message:**
```
Started LlmApiApplication in X.XXX seconds
```

**Then test in browser:**
- **Web UI**: http://localhost:8080
- **API Health**: http://localhost:8080/api/v1/llm/health
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

### **Step 6: Test the Application**
```bash
# In a new terminal, test the API
curl -X POST http://localhost:8080/api/v1/financial-advisor/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How do I start investing?"}'
```

**✅ SUCCESS!** Your application is running locally.

---

## 🐳 **Method 2: Docker Deployment**

### **Step 1: Navigate to Project Directory**
```bash
cd /workspace/llm-api
```

### **Step 2: Build Docker Image**
```bash
docker build -t financial-advisor-api .
```
**Expected Output:**
```
Successfully built [image-id]
Successfully tagged financial-advisor-api:latest
```

### **Step 3: Verify Image Creation**
```bash
docker images | grep financial-advisor-api
```

### **Step 4: Run Docker Container**
```bash
docker run -d \
  --name financial-advisor \
  -p 8080:8080 \
  -e OPENAI_API_KEY=your-api-key-here \
  financial-advisor-api
```

### **Step 5: Check Container Status**
```bash
docker ps
```
**Look for:** Container with name `financial-advisor` and status `Up`

### **Step 6: View Container Logs**
```bash
docker logs financial-advisor
```
**Wait for:** `Started LlmApiApplication`

### **Step 7: Test the Application**
- **Web UI**: http://localhost:8080
- **API Health**: http://localhost:8080/api/v1/llm/health

### **Step 8: Stop Container (when needed)**
```bash
docker stop financial-advisor
docker rm financial-advisor
```

**✅ SUCCESS!** Your application is running in Docker.

---

## 🐙 **Method 3: Docker Compose Deployment**

### **Step 1: Navigate to Project Directory**
```bash
cd /workspace/llm-api
```

### **Step 2: Review Docker Compose Configuration**
```bash
cat docker-compose.yml
```

### **Step 3: Start Services**
```bash
docker-compose up -d
```
**Expected Output:**
```
Creating financial-advisor-api ... done
```

### **Step 4: Check Service Status**
```bash
docker-compose ps
```

### **Step 5: View Logs**
```bash
docker-compose logs -f financial-advisor-api
```
**Wait for:** `Started LlmApiApplication`

### **Step 6: Test the Application**
- **Web UI**: http://localhost:8080
- **Health Check**: http://localhost:8080/api/v1/llm/health

### **Step 7: Stop Services (when needed)**
```bash
docker-compose down
```

**✅ SUCCESS!** Your application is running with Docker Compose.

---

## ☁️ **Method 4: Heroku Cloud Deployment**

### **Step 1: Install Heroku CLI**
```bash
# For Ubuntu/Debian
curl https://cli-assets.heroku.com/install.sh | sh

# For macOS
brew tap heroku/brew && brew install heroku

# For Windows
# Download from https://devcenter.heroku.com/articles/heroku-cli
```

### **Step 2: Login to Heroku**
```bash
heroku login
```

### **Step 3: Navigate to Project Directory**
```bash
cd /workspace/llm-api
```

### **Step 4: Initialize Git Repository (if not already done)**
```bash
git init
git add .
git commit -m "Initial commit for deployment"
```

### **Step 5: Create Heroku Application**
```bash
heroku create your-financial-advisor-app
```
**Replace `your-financial-advisor-app` with your desired app name**

### **Step 6: Set Environment Variables**
```bash
heroku config:set SPRING_PROFILES_ACTIVE=deploy
heroku config:set OPENAI_API_KEY=your-actual-api-key-here
```

### **Step 7: Deploy to Heroku**
```bash
git push heroku main
```
**Wait for:** `Build succeeded` and `deployed to Heroku`

### **Step 8: Open Your Application**
```bash
heroku open
```
**Or visit:** `https://your-financial-advisor-app.herokuapp.com`

### **Step 9: View Logs (if needed)**
```bash
heroku logs --tail
```

**✅ SUCCESS!** Your application is deployed on Heroku.

---

## 🌩️ **Method 5: AWS Elastic Beanstalk Deployment**

### **Step 1: Install AWS CLI and EB CLI**
```bash
# Install AWS CLI
pip install awscli

# Install EB CLI
pip install awsebcli
```

### **Step 2: Configure AWS Credentials**
```bash
aws configure
```
**Enter your AWS Access Key ID, Secret Access Key, and region**

### **Step 3: Build the JAR File**
```bash
cd /workspace/llm-api
mvn clean package -DskipTests
```

### **Step 4: Initialize Elastic Beanstalk**
```bash
eb init
```
**Follow the prompts:**
- Select region
- Choose "Create new application"
- Application name: `financial-advisor-api`
- Platform: `Java`
- Platform version: `Java 17`

### **Step 5: Create Environment**
```bash
eb create production
```

### **Step 6: Set Environment Variables**
```bash
eb setenv SPRING_PROFILES_ACTIVE=deploy
eb setenv OPENAI_API_KEY=your-actual-api-key-here
```

### **Step 7: Deploy Application**
```bash
eb deploy
```

### **Step 8: Open Your Application**
```bash
eb open
```

**✅ SUCCESS!** Your application is deployed on AWS.

---

## 🔥 **Method 6: Production Server Deployment**

### **Step 1: Prepare Production Server**
```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Install Java 17
sudo apt install openjdk-17-jdk -y

# Verify Java installation
java -version
```

### **Step 2: Create Application User**
```bash
sudo useradd -m -s /bin/bash financial-advisor
sudo mkdir -p /opt/financial-advisor
sudo chown financial-advisor:financial-advisor /opt/financial-advisor
```

### **Step 3: Transfer JAR File to Server**
```bash
# From your local machine
scp target/llm-api-1.0.0.jar user@your-server:/opt/financial-advisor/
```

### **Step 4: Create Systemd Service**
```bash
sudo nano /etc/systemd/system/financial-advisor.service
```

**Add this content:**
```ini
[Unit]
Description=Financial Advisor LLM API
After=network.target

[Service]
Type=simple
User=financial-advisor
WorkingDirectory=/opt/financial-advisor
ExecStart=/usr/bin/java -jar llm-api-1.0.0.jar
Restart=always
RestartSec=10
Environment=SPRING_PROFILES_ACTIVE=deploy
Environment=OPENAI_API_KEY=your-actual-api-key-here

[Install]
WantedBy=multi-user.target
```

### **Step 5: Start and Enable Service**
```bash
sudo systemctl daemon-reload
sudo systemctl enable financial-advisor
sudo systemctl start financial-advisor
```

### **Step 6: Check Service Status**
```bash
sudo systemctl status financial-advisor
```

### **Step 7: Configure Nginx (Optional)**
```bash
sudo apt install nginx -y
sudo nano /etc/nginx/sites-available/financial-advisor
```

**Add this content:**
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### **Step 8: Enable Nginx Site**
```bash
sudo ln -s /etc/nginx/sites-available/financial-advisor /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

**✅ SUCCESS!** Your application is running on a production server.

---

## 🔧 **Troubleshooting Guide**

### **Common Issues and Solutions**

#### **Issue 1: Port Already in Use**
```bash
# Find process using port 8080
sudo lsof -i :8080

# Kill the process
sudo kill -9 [PID]
```

#### **Issue 2: Java Version Issues**
```bash
# Check Java version
java -version

# Set JAVA_HOME (if needed)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

#### **Issue 3: Maven Build Fails**
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:resolve

# Build with debug info
mvn clean package -X
```

#### **Issue 4: Docker Build Fails**
```bash
# Clean Docker cache
docker system prune -a

# Build with no cache
docker build --no-cache -t financial-advisor-api .
```

#### **Issue 5: Application Won't Start**
```bash
# Check logs
tail -f logs/application.log

# Or for Docker
docker logs financial-advisor
```

---

## ✅ **Deployment Verification Checklist**

After deployment, verify these endpoints:

- [ ] **Web UI**: http://your-domain:8080/ (shows the financial advisor interface)
- [ ] **Health Check**: http://your-domain:8080/api/v1/llm/health (returns JSON status)
- [ ] **API Documentation**: http://your-domain:8080/swagger-ui/index.html (shows API docs)
- [ ] **Simple API Test**: 
  ```bash
  curl -X POST http://your-domain:8080/api/v1/financial-advisor/ask \
    -H "Content-Type: application/json" \
    -d '{"question": "Test"}'
  ```
- [ ] **UI Functionality**: Can ask questions through the web interface
- [ ] **Profile Form**: Can expand and fill the financial profile form

---

## 🎉 **Success!**

Choose the deployment method that best fits your needs:

- **🏠 Local Development**: Method 1 (JAR)
- **🐳 Containerized**: Method 2 (Docker) or Method 3 (Docker Compose)
- **☁️ Cloud Platforms**: Method 4 (Heroku) or Method 5 (AWS)
- **🏢 Production Server**: Method 6 (Systemd Service)

**Your Financial Advisor LLM API with integrated UI is now deployed and ready to help users with their financial questions! 💰📈**