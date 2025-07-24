# Google Cloud Deployment Instructions - Cloud Shell

This guide will help you deploy the React/TypeScript SPA from the `cursor/deploy-to-google-cloud-using-cloud-shell-132b` branch to Google Cloud App Engine using Google Cloud Shell.

## Prerequisites

1. **Google Cloud Project**: A Google Cloud Project with billing enabled
2. **Google Cloud Shell**: Access to Google Cloud Console and Cloud Shell
3. **App Engine API**: Will be enabled during deployment

## Step-by-Step Deployment in Cloud Shell

### 1. Open Google Cloud Shell

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Click the **Cloud Shell** icon (>_) in the top-right corner
3. Wait for Cloud Shell to initialize

### 2. Set Up Your Project

```bash
# Set your project ID (replace YOUR_PROJECT_ID with your actual project ID)
export PROJECT_ID="YOUR_PROJECT_ID"
gcloud config set project $PROJECT_ID

# Verify project is set correctly
gcloud config get-value project
```

### 3. Enable Required APIs

```bash
# Enable App Engine and Cloud Build APIs
gcloud services enable appengine.googleapis.com
gcloud services enable cloudbuild.googleapis.com

# Verify APIs are enabled
gcloud services list --enabled --filter="name:(appengine.googleapis.com OR cloudbuild.googleapis.com)"
```

### 4. Initialize App Engine (First Time Only)

If this is your first App Engine deployment in this project:

```bash
# Create App Engine application (choose your preferred region)
gcloud app create --region=us-central1

# Alternative regions: us-east1, europe-west1, asia-northeast1, etc.
# See: https://cloud.google.com/appengine/docs/locations
```

### 5. Clone and Prepare the Repository

```bash
# Clone your repository (replace with your actual repository URL)
git clone YOUR_REPOSITORY_URL
cd YOUR_REPOSITORY_NAME

# Switch to the deployment branch
git checkout cursor/deploy-to-google-cloud-using-cloud-shell-132b

# Verify you're on the correct branch
git branch --show-current
```

### 6. Install Node.js and Dependencies

Cloud Shell comes with Node.js, but let's ensure we're using the right version:

```bash
# Check Node.js version (should be >= 18.0.0)
node --version

# If you need to update Node.js in Cloud Shell:
# nvm install 20
# nvm use 20

# Install project dependencies
npm install
```

### 7. Build the Application

```bash
# Build the production version
npm run build

# Verify build completed successfully
ls -la dist/
```

You should see output similar to:
```
drwxr-xr-x 3 user user  4096 date time assets/
-rw-r--r-- 1 user user   xxx date time index.html
```

### 8. Deploy to App Engine

```bash
# Deploy using the configured app.yaml
gcloud app deploy app.yaml

# When prompted:
# - Confirm the deployment by typing 'Y' or pressing Enter
# - Wait for deployment to complete (usually 2-5 minutes)
```

### 9. View Your Deployed Application

```bash
# Open your app in the browser
gcloud app browse

# Or get the URL to share
gcloud app describe --format="value(defaultHostname)"
```

## Alternative Deployment Methods

### Option 1: Using the Deployment Script

```bash
# Make the script executable and run it
chmod +x deploy.sh
./deploy.sh
```

### Option 2: Using Cloud Build (Automated)

```bash
# Submit build to Cloud Build
gcloud builds submit --config cloudbuild.yaml

# Then deploy the built artifacts
gcloud app deploy app.yaml
```

## Monitoring and Management

### View Logs
```bash
# Stream live logs
gcloud app logs tail -s default

# View recent logs
gcloud app logs read -s default --limit 100
```

### Manage Versions
```bash
# List all versions
gcloud app versions list

# Set traffic to a specific version
gcloud app services set-traffic default --splits=VERSION_ID=1

# Delete old versions (optional)
gcloud app versions delete VERSION_ID
```

### View Application Details
```bash
# Get app information
gcloud app describe

# Check current configuration
gcloud app versions describe VERSION_ID --service=default
```

## Project Structure

This React SPA includes:
- **Frontend**: React 18 + TypeScript + Vite
- **UI Library**: ShadCN UI components with Tailwind CSS
- **Routing**: React Router for client-side navigation
- **State Management**: TanStack Query for server state
- **Build Tool**: Vite for fast builds and hot reloading

## Configuration Files

- **`app.yaml`**: App Engine configuration for SPA routing
- **`.gcloudignore`**: Excludes source files from deployment
- **`cloudbuild.yaml`**: Optional Cloud Build configuration
- **`deploy.sh`**: Automated deployment script

## Troubleshooting

### Common Issues

1. **"Build failed" error**:
   ```bash
   rm -rf dist/ node_modules/
   npm install
   npm run build
   ```

2. **"App Engine API not enabled"**:
   ```bash
   gcloud services enable appengine.googleapis.com
   ```

3. **"Project not found" error**:
   ```bash
   gcloud projects list
   gcloud config set project YOUR_CORRECT_PROJECT_ID
   ```

4. **404 errors on client-side routes**:
   - This is handled by the SPA fallback in `app.yaml`
   - Ensure `dist/index.html` exists after build

5. **Deployment takes too long**:
   - Check `.gcloudignore` is properly configured
   - Ensure only `dist/` folder is being deployed

### Performance Optimization

- Static assets are cached for 1 hour
- HTML files are not cached to ensure updates are immediate
- Auto-scaling configured from 0-10 instances for cost efficiency

## Security Considerations

- All traffic is served over HTTPS (enforced in `app.yaml`)
- Environment variables can be added to `app.yaml` if needed
- No sensitive data should be in the client-side bundle

## Cost Management

- **Auto-scaling**: Scales to 0 instances when not in use
- **Static hosting**: Efficient for SPA applications
- **Free tier**: App Engine provides free quota for small applications

## Support

- [App Engine Documentation](https://cloud.google.com/appengine/docs)
- [Cloud Shell Documentation](https://cloud.google.com/shell/docs)
- [Troubleshooting Guide](https://cloud.google.com/appengine/docs/standard/troubleshooting)

---

**Note**: Replace `YOUR_PROJECT_ID` and `YOUR_REPOSITORY_URL` with your actual values. The branch `cursor/deploy-to-google-cloud-using-cloud-shell-132b` contains all necessary configuration files for deployment.