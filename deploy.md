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

### app.yaml (Updated for SPA Support)
```yaml
runtime: nodejs20

env_variables:
  NODE_ENV: production

automatic_scaling:
  min_instances: 0
  max_instances: 10

handlers:
# Serve static assets (CSS, JS, images, etc.) with proper caching
- url: /assets/(.*)
  static_files: dist/assets/\1
  upload: dist/assets/.*
  secure: always
  expiration: "1h"

# Serve favicon and other root-level static files
- url: /(favicon\.ico|robots\.txt|placeholder\.svg)
  static_files: dist/\1
  upload: dist/(favicon\.ico|robots\.txt|placeholder\.svg)
  secure: always
  expiration: "1h"

# Serve index.html for all other routes (SPA fallback)
- url: /.*
  static_files: dist/index.html
  upload: dist/index.html
  secure: always
  expiration: "0"
```

### .gcloudignore (Created)
This file ensures only necessary files are deployed:
- Excludes source files, node_modules, and development files
- Only deploys the built `dist/` folder and essential config files

## Deployment Commands Summary

```bash
# 1. Build the project
npm install
npm run build

# 2. Deploy to App Engine
gcloud app deploy

# 3. Set traffic to new version (if needed)
gcloud app services set-traffic default --splits=VERSION_ID=1

# 4. View logs
gcloud app logs tail -s default
```

## Key Fixes Applied

1. **SPA Routing Support**: Updated handlers to properly serve React Router routes
2. **Asset Optimization**: Proper caching headers for static assets
3. **File Organization**: Optimized deployment with .gcloudignore
4. **Production Build**: Ensured proper Vite build configuration

## Important Notes

1. **Single Page Application**: Configured for React Router client-side routing
2. **Asset Caching**: Static assets cached for 1 hour, index.html not cached
3. **Security**: All routes served over HTTPS
4. **Cost Management**: Auto-scaling from 0 to 10 instances
5. **File Size**: Built bundle is optimized for production

## Troubleshooting

- **Blank Page**: Ensure `npm run build` completes successfully
- **404 Errors**: Check that dist/ folder contains index.html and assets/
- **Routing Issues**: Verify app.yaml handlers are correctly configured
- **Build Failures**: Run `npm install` before `npm run build`
- **Deployment Size**: .gcloudignore excludes unnecessary files

## Performance Tips

- Assets are cached for 1 hour to improve load times
- Consider implementing code splitting for larger applications
- Monitor bundle size and use dynamic imports for optimization