# 🌩️ **Google Cloud App Engine Deployment Guide**
## **End-to-End Steps via GCP Console**

This guide will walk you through deploying your Financial Advisor LLM API on Google Cloud App Engine from start to finish.

---

## 📋 **Prerequisites**

Before starting, ensure you have:
- ✅ Google Cloud account (free tier available)
- ✅ Credit card for billing setup (required even for free tier)
- ✅ Your project files ready in `/workspace/llm-api`

---

## 🎯 **Step 1: Create Google Cloud Project**

### **1.1 Go to Google Cloud Console**
1. Open your browser and go to: https://console.cloud.google.com
2. Sign in with your Google account

### **1.2 Create New Project**
1. Click on the **project selector** at the top of the page
2. Click **"NEW PROJECT"**
3. Enter project details:
   - **Project name**: `financial-advisor-api`
   - **Project ID**: `financial-advisor-api-123456` (must be globally unique)
   - **Location**: Leave as default or select your organization
4. Click **"CREATE"**
5. Wait for project creation (usually takes 30-60 seconds)
6. **Select your new project** from the project selector

### **1.3 Enable Billing**
1. Go to **Navigation Menu** (☰) → **Billing**
2. Click **"Link a billing account"**
3. Create a new billing account or select existing one
4. Add your credit card information
5. Click **"SET ACCOUNT"**

**💡 Note**: Google provides $300 free credits for new accounts, which is more than enough for this deployment.

---

## 🔧 **Step 2: Enable Required APIs**

### **2.1 Enable App Engine Admin API**
1. Go to **Navigation Menu** (☰) → **APIs & Services** → **Library**
2. Search for **"App Engine Admin API"**
3. Click on it and click **"ENABLE"**

### **2.2 Enable Cloud Build API**
1. In the same **APIs & Services** → **Library**
2. Search for **"Cloud Build API"**
3. Click on it and click **"ENABLE"**

### **2.3 Enable Cloud Resource Manager API**
1. Search for **"Cloud Resource Manager API"**
2. Click on it and click **"ENABLE"**

**⏱️ Wait**: APIs may take 1-2 minutes to be fully enabled.

---

## 🚀 **Step 3: Set Up Cloud Shell**

### **3.1 Open Cloud Shell**
1. Click the **Cloud Shell** icon (>_) in the top-right corner of the console
2. Wait for Cloud Shell to initialize (30-60 seconds)
3. You'll see a terminal at the bottom of the screen

### **3.2 Verify Project Selection**
```bash
gcloud config get-value project
```
**Expected Output**: Your project ID (e.g., `financial-advisor-api-123456`)

If not correct, set it:
```bash
gcloud config set project YOUR_PROJECT_ID
```

---

## 📁 **Step 4: Upload Your Project Files**

### **4.1 Option A: Upload via Cloud Shell Editor**
1. In Cloud Shell, click **"Open Editor"** button
2. In the editor, click **"File"** → **"Upload Files"**
3. Navigate to your local `/workspace/llm-api` folder
4. Select ALL files and folders:
   - `src/` folder
   - `target/` folder (if exists)
   - `pom.xml`
   - `app.yaml`
   - `Dockerfile`
   - `docker-compose.yml`
   - All `.md` files
   - `deploy.sh`
5. Click **"Upload"**
6. Wait for upload to complete

### **4.2 Option B: Clone from Git (if you have a repository)**
```bash
git clone YOUR_REPOSITORY_URL
cd financial-advisor-api
```

### **4.3 Verify Files**
```bash
ls -la
```
**You should see**: `pom.xml`, `app.yaml`, `src/` folder, etc.

---

## 🏗️ **Step 5: Initialize App Engine**

### **5.1 Initialize App Engine Application**
```bash
gcloud app create
```

**You'll be prompted to choose a region. Recommended regions:**
- `us-central1` (Iowa) - Good for North America
- `europe-west1` (Belgium) - Good for Europe
- `asia-northeast1` (Tokyo) - Good for Asia

**Example:**
```
Please choose the region where you want your App Engine application located:
[1] us-central1 (supports standard and flexible)
[2] us-east1 (supports standard and flexible)
...
Please enter your numeric choice: 1
```

### **5.2 Verify App Engine Creation**
```bash
gcloud app describe
```
**Expected Output**: Details about your App Engine application

---

## 🔨 **Step 6: Build Your Application**

### **6.1 Navigate to Project Directory**
```bash
cd ~/financial-advisor-api  # or wherever you uploaded files
```

### **6.2 Build with Maven**
```bash
mvn clean package -DskipTests
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 2-3 minutes
```

### **6.3 Verify JAR File**
```bash
ls -la target/
```
**Look for**: `llm-api-1.0.0.jar` (should be 50-80 MB)

---

## 🌟 **Step 7: Configure Environment Variables (Optional)**

### **7.1 Edit app.yaml for OpenAI API Key**
If you have an OpenAI API key:
```bash
nano app.yaml
```

Find this line:
```yaml
# OPENAI_API_KEY: "your-openai-api-key-here"
```

Uncomment and replace with your actual key:
```yaml
OPENAI_API_KEY: "sk-your-actual-openai-api-key-here"
```

Save and exit (Ctrl+X, then Y, then Enter)

---

## 🚀 **Step 8: Deploy to App Engine**

### **8.1 Deploy the Application**
```bash
gcloud app deploy
```

**You'll see prompts like:**
```
Services to deploy:
descriptor:      [/home/user/financial-advisor-api/app.yaml]
source:          [/home/user/financial-advisor-api]
target project:  [financial-advisor-api-123456]
target service:  [default]
target version:  [20241218t123456]
target url:      [https://financial-advisor-api-123456.uc.r.appspot.com]

Do you want to continue (Y/n)? Y
```

Type **Y** and press Enter.

### **8.2 Wait for Deployment**
**Deployment typically takes 5-10 minutes. You'll see:**
```
Beginning deployment of service [default]...
Building and pushing image for service [default]
Started cloud build [abc123-def456-ghi789]
...
Deployed service [default] to [https://your-project-id.uc.r.appspot.com]
```

### **8.3 Get Your Application URL**
```bash
gcloud app browse
```
**This will show your app URL**: `https://your-project-id.uc.r.appspot.com`

---

## ✅ **Step 9: Verify Deployment**

### **9.1 Test in Browser**
1. Open the URL from Step 8.3 in your browser
2. You should see the **Financial Advisor UI**

### **9.2 Test API Health**
```bash
curl https://your-project-id.uc.r.appspot.com/api/v1/llm/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "timestamp": "2024-01-18T12:34:56.789Z",
  "service": "Financial Advisor LLM API",
  "version": "1.0.0"
}
```

### **9.3 Test Financial Advisor API**
```bash
curl -X POST https://your-project-id.uc.r.appspot.com/api/v1/financial-advisor/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How should I start investing?"}'
```

### **9.4 Test Swagger Documentation**
Visit: `https://your-project-id.uc.r.appspot.com/swagger-ui/index.html`

---

## 🎯 **Step 10: Monitor and Manage**

### **10.1 View Application in Console**
1. Go to **Navigation Menu** (☰) → **App Engine** → **Services**
2. You'll see your deployed service with traffic allocation

### **10.2 View Logs**
1. Go to **Navigation Menu** (☰) → **Logging** → **Logs Explorer**
2. Filter by resource: **App Engine Application**
3. You can see real-time application logs

### **10.3 Monitor Performance**
1. Go to **Navigation Menu** (☰) → **App Engine** → **Services**
2. Click on your service name
3. View metrics like requests, latency, errors

---

## 🔧 **Step 11: Custom Domain (Optional)**

### **11.1 Set Up Custom Domain**
1. Go to **Navigation Menu** (☰) → **App Engine** → **Settings**
2. Click **"Custom domains"** tab
3. Click **"Add a custom domain"**
4. Follow the verification process
5. Update your DNS records as instructed

---

## 💰 **Step 12: Cost Management**

### **12.1 Set Up Billing Alerts**
1. Go to **Navigation Menu** (☰) → **Billing**
2. Click **"Budgets & alerts"**
3. Click **"CREATE BUDGET"**
4. Set budget amount (e.g., $10/month)
5. Set alert thresholds (50%, 90%, 100%)

### **12.2 Monitor Usage**
- **Free Tier**: 28 instance hours per day
- **Your app**: Uses F2 instances (more powerful)
- **Estimated cost**: $0.10-$0.30 per day for moderate usage

---

## 🔄 **Step 13: Updates and Redeployment**

### **13.1 To Update Your Application**
1. Make changes to your code locally
2. Upload updated files to Cloud Shell
3. Build: `mvn clean package -DskipTests`
4. Deploy: `gcloud app deploy`

### **13.2 Version Management**
```bash
# Deploy new version without promoting
gcloud app deploy --no-promote

# List versions
gcloud app versions list

# Promote a version to receive traffic
gcloud app versions migrate VERSION_ID
```

---

## 🛠️ **Troubleshooting**

### **Issue 1: Deployment Fails**
```bash
# Check build logs
gcloud app logs tail -s default

# Check detailed deployment logs
gcloud builds list
gcloud builds log BUILD_ID
```

### **Issue 2: Application Won't Start**
```bash
# Check application logs
gcloud app logs tail -s default

# Check health endpoint
curl https://your-project-id.uc.r.appspot.com/api/v1/llm/health
```

### **Issue 3: Out of Memory**
Edit `app.yaml`:
```yaml
instance_class: F4  # Upgrade from F2 to F4 (more memory)
```

### **Issue 4: Billing Issues**
1. Check **Navigation Menu** (☰) → **Billing**
2. Ensure billing account is active
3. Check if you've exceeded free tier limits

---

## 🎉 **SUCCESS! Your App is Live**

**🌟 Congratulations! Your Financial Advisor LLM API is now deployed on Google Cloud App Engine!**

### **📋 What You Have:**
- ✅ **Live Web Application**: `https://your-project-id.uc.r.appspot.com`
- ✅ **API Documentation**: `https://your-project-id.uc.r.appspot.com/swagger-ui/index.html`
- ✅ **Auto-scaling**: Handles traffic spikes automatically
- ✅ **Health Monitoring**: Built-in health checks
- ✅ **Global CDN**: Fast loading worldwide
- ✅ **HTTPS**: Secure by default

### **📱 Share Your App:**
- **Web UI**: `https://your-project-id.uc.r.appspot.com`
- **API Health**: `https://your-project-id.uc.r.appspot.com/api/v1/llm/health`
- **API Docs**: `https://your-project-id.uc.r.appspot.com/swagger-ui/index.html`

### **🎯 Next Steps:**
1. **Test thoroughly** with different financial questions
2. **Monitor costs** in the billing console
3. **Set up alerts** for usage and errors
4. **Consider adding** a custom domain
5. **Share** with users for feedback

**Your AI Financial Advisor is now helping people make better financial decisions worldwide! 🚀💰📈**

---

## 📞 **Support**

If you encounter issues:
1. **Google Cloud Support**: https://cloud.google.com/support
2. **App Engine Documentation**: https://cloud.google.com/appengine/docs
3. **Stack Overflow**: Tag with `google-app-engine` and `java`

**Happy Deploying! 🌟**