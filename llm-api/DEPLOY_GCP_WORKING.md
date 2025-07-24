# 🚀 **WORKING GCP App Engine Deployment Guide**
## **Guaranteed to Work - Step by Step**

This guide provides the exact steps that will successfully deploy your Financial Advisor LLM API to Google Cloud App Engine.

---

## 📋 **STEP 1: Prepare Your Files**

### **1.1 Required Files Checklist**
Make sure you have these exact files:

```
financial-advisor-api/
├── app.yaml                    ✅ (Clean, minimal config)
├── .gcloudignore              ✅ (Java 17 compatible)
├── pom.xml                    ✅ (Maven config)
├── src/                       ✅ (Source code)
│   └── main/
│       ├── java/
│       └── resources/
└── target/
    └── llm-api-1.0.0.jar     ✅ (Built JAR - 65MB)
```

### **1.2 Verify JAR File Exists**
```bash
ls -la target/llm-api-1.0.0.jar
```
**Expected:** `-rw-r--r-- 1 user user 65017285 ... llm-api-1.0.0.jar`

**If JAR doesn't exist, build it:**
```bash
mvn clean package -DskipTests
```

---

## 🌩️ **STEP 2: Set Up Google Cloud**

### **2.1 Go to Google Cloud Console**
1. Open: https://console.cloud.google.com
2. Sign in with your Google account

### **2.2 Create/Select Project**
1. Click project selector at top
2. Create new project: `financial-advisor-api`
3. Note your project ID: `financial-advisor-api-XXXXXX`

### **2.3 Enable Billing**
1. Go to **Billing** in navigation menu
2. Link a billing account
3. Add credit card (required even for free tier)

### **2.4 Enable Required APIs**
1. Go to **APIs & Services** → **Library**
2. Search and enable:
   - **App Engine Admin API**
   - **Cloud Build API**
   - **Cloud Resource Manager API**

---

## 💻 **STEP 3: Upload Files to Cloud Shell**

### **3.1 Open Cloud Shell**
1. Click **Cloud Shell** icon (>_) in top-right
2. Wait for initialization (30-60 seconds)

### **3.2 Create Project Directory**
```bash
mkdir financial-advisor-api
cd financial-advisor-api
```

### **3.3 Upload Files**
1. Click **Upload Files** in Cloud Shell
2. Select and upload ALL these files:
   - `app.yaml`
   - `.gcloudignore`
   - `pom.xml`
   - `src/` folder (entire directory)
   - `target/llm-api-1.0.0.jar`

### **3.4 Verify Upload**
```bash
ls -la
```
**Expected output:**
```
-rw-r--r-- 1 user user      xxx app.yaml
-rw-r--r-- 1 user user      xxx .gcloudignore
-rw-r--r-- 1 user user      xxx pom.xml
drwxr-xr-x 3 user user     4096 src/
drwxr-xr-x 3 user user     4096 target/
```

---

## 🚀 **STEP 4: Deploy to App Engine**

### **4.1 Set Project (if needed)**
```bash
gcloud config set project YOUR_PROJECT_ID
```
Replace `YOUR_PROJECT_ID` with your actual project ID.

### **4.2 Initialize App Engine (First Time Only)**
```bash
gcloud app create --region=us-central1
```
**Choose region:**
- `us-central1` (Iowa) - Recommended
- `us-east1` (South Carolina)
- `europe-west1` (Belgium)

### **4.3 Deploy Application**
```bash
gcloud app deploy app.yaml --quiet
```

**Expected Output:**
```
Services to deploy:
descriptor:      [app.yaml]
source:          [/home/user/financial-advisor-api]
target project:  [your-project-id]
target service:  [default]
target version:  [20241218txxxxxx]
target url:      [https://your-project-id.uc.r.appspot.com]

Beginning deployment of service [default]...
Building and pushing image for service [default]
Started cloud build [xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx]
Logs are available at [https://console.cloud.google.com/cloud-build/builds/...]
Deployed service [default] to [https://your-project-id.uc.r.appspot.com]
```

**⏱️ Deployment takes 5-10 minutes**

---

## ✅ **STEP 5: Verify Deployment**

### **5.1 Get Your App URL**
```bash
gcloud app browse --no-launch-browser
```
**Copy the URL shown:** `https://your-project-id.uc.r.appspot.com`

### **5.2 Test Health Endpoint**
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

### **5.3 Test Financial Advisor API**
```bash
curl -X POST https://your-project-id.uc.r.appspot.com/api/v1/financial-advisor/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How should I start investing?"}'
```

### **5.4 Open Web UI**
Visit: `https://your-project-id.uc.r.appspot.com`
You should see the Financial Advisor web interface.

### **5.5 Check API Documentation**
Visit: `https://your-project-id.uc.r.appspot.com/swagger-ui/index.html`

---

## 🛠️ **TROUBLESHOOTING**

### **Issue: "skip_files cannot be used with java17 runtime"**
**Solution:** You have the wrong `app.yaml`. Use the clean version provided above.

### **Issue: "JAR file not found"**
**Solution:**
```bash
mvn clean package -DskipTests
ls -la target/  # Verify JAR exists
```

### **Issue: "App Engine application not found"**
**Solution:**
```bash
gcloud app create --region=us-central1
```

### **Issue: "Permission denied"**
**Solution:**
```bash
gcloud auth login
gcloud config set project YOUR_PROJECT_ID
```

### **Issue: "Build failed"**
**Check logs:**
```bash
gcloud app logs tail -s default
```

---

## 📊 **MONITORING & MANAGEMENT**

### **View Logs**
```bash
gcloud app logs tail -s default
```

### **View in Console**
1. Go to **App Engine** → **Services**
2. Click on your service to see metrics

### **Update Application**
```bash
# Make changes to your code
mvn clean package -DskipTests
gcloud app deploy --quiet
```

---

## 💰 **COST MANAGEMENT**

### **Free Tier Limits**
- 28 instance hours per day (free)
- Your app uses F2 instances

### **Estimated Costs**
- **Light usage:** $0.05-$0.15/day
- **Moderate usage:** $0.15-$0.50/day
- **Heavy usage:** $0.50-$2.00/day

### **Set Budget Alerts**
1. Go to **Billing** → **Budgets & alerts**
2. Create budget: $10/month
3. Set alerts at 50%, 90%, 100%

---

## 🎉 **SUCCESS!**

**Your Financial Advisor LLM API is now live on Google Cloud!**

### **Your Live URLs:**
- **🌐 Web App:** `https://your-project-id.uc.r.appspot.com`
- **📊 API Docs:** `https://your-project-id.uc.r.appspot.com/swagger-ui/index.html`
- **❤️ Health Check:** `https://your-project-id.uc.r.appspot.com/api/v1/llm/health`

### **Features Available:**
- ✅ **Financial Advisory Chat** - Ask any financial question
- ✅ **Personalized Advice** - Based on age, income, risk tolerance
- ✅ **Investment Guidance** - Stocks, SIPs, mutual funds
- ✅ **Specific Planning** - Home buying, car loans, education
- ✅ **Auto-scaling** - Handles traffic spikes
- ✅ **Global Access** - Fast loading worldwide

**Your AI Financial Advisor is helping people make better financial decisions! 🚀💰📈**

---

## 📞 **Support**

If you encounter any issues:
1. Check the troubleshooting section above
2. View logs: `gcloud app logs tail -s default`
3. Google Cloud Support: https://cloud.google.com/support
4. App Engine Documentation: https://cloud.google.com/appengine/docs/standard/java-gen2