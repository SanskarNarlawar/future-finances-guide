# Google Cloud App Engine Deployment Guide

## Prerequisites
1. Google Cloud Platform account
2. Google Cloud SDK installed
3. A valid project with billing enabled

## Step-by-Step Deployment

### 1. Install Google Cloud SDK
```bash
# For macOS
brew install google-cloud-sdk

# For Windows/Linux, download from:
# https://cloud.google.com/sdk/docs/install
```

### 2. Authenticate with Google Cloud
```bash
gcloud auth login
gcloud config set project YOUR_PROJECT_ID
```

### 3. Enable Required APIs
```bash
gcloud services enable appengine.googleapis.com
gcloud services enable cloudbuild.googleapis.com
```

### 4. Build Your Application
```bash
# Install dependencies and build
npm install
npm run build
```

### 5. Deploy to App Engine
```bash
# Deploy using the app.yaml configuration
gcloud app deploy app.yaml
```

### 6. View Your Application
```bash
# Open your deployed app in browser
gcloud app browse
```

## Configuration Files

### app.yaml (already created)
```yaml
runtime: nodejs20

env_variables:
  NODE_ENV: production

automatic_scaling:
  min_instances: 0
  max_instances: 10

handlers:
- url: /static
  static_dir: dist/assets
  secure: always

- url: /.*
  static_files: dist/index.html
  upload: dist/index.html
  secure: always
```

### Additional package.json for deployment
You may need to create a temporary package.json for deployment:

```json
{
  "name": "finance-ai-app",
  "version": "1.0.0",
  "description": "AI-powered financial dashboard",
  "scripts": {
    "build": "npm install && npm run build:app",
    "build:app": "vite build",
    "start": "serve -s dist -l $PORT"
  },
  "dependencies": {
    "serve": "^14.2.1"
  }
}
```

## Deployment Commands Summary

```bash
# 1. Build the project
npm run build

# 2. Deploy to App Engine
gcloud app deploy

# 3. Set traffic to new version (if needed)
gcloud app services set-traffic default --splits=VERSION_ID=1

# 4. View logs
gcloud app logs tail -s default
```

## Important Notes

1. **Static Site**: This is a frontend-only app, so we're deploying it as a static site
2. **Routing**: The app.yaml handles client-side routing by serving index.html for all routes
3. **Environment Variables**: Add any required environment variables in the app.yaml file
4. **Cost Management**: Set max_instances to control costs
5. **Custom Domain**: Configure custom domains in the App Engine settings

## Troubleshooting

- If build fails, ensure all dependencies are properly installed
- Check that the dist/ folder exists and contains the built files
- Verify your Google Cloud project has billing enabled
- Check the logs with `gcloud app logs tail -s default`