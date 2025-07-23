#!/bin/bash
set -e

echo "🚀 Starting deployment process..."

# Ensure we're in the right directory
echo "📂 Current directory: $(pwd)"

# Clean and build locally
echo "🧹 Cleaning previous build..."
rm -rf dist/

echo "📦 Installing dependencies..."
npm install

echo "🔨 Building application..."
npm run build

# Verify build
if [ ! -f "dist/index.html" ]; then
    echo "❌ Build failed - dist/index.html not found"
    exit 1
fi

echo "✅ Build completed successfully!"
echo "📊 Build output:"
ls -la dist/
echo ""
ls -la dist/assets/

echo "🚀 Deploying to Google Cloud App Engine..."
gcloud app deploy app.yaml --quiet

echo "🎉 Deployment completed! Opening your app..."
gcloud app browse