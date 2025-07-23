#!/bin/bash

echo "🚀 Google Cloud Deployment Pre-check"
echo "======================================"

# Check if dist folder exists
if [ ! -d "dist" ]; then
    echo "❌ dist/ folder not found. Running build..."
    npm run build
    if [ $? -ne 0 ]; then
        echo "❌ Build failed. Please fix build errors before deploying."
        exit 1
    fi
else
    echo "✅ dist/ folder found"
fi

# Check if required files exist
required_files=("dist/index.html" "app.yaml" ".gcloudignore")
for file in "${required_files[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file found"
    else
        echo "❌ $file missing"
        exit 1
    fi
done

# Check if assets folder exists
if [ -d "dist/assets" ]; then
    echo "✅ dist/assets/ folder found"
    echo "📊 Asset files:"
    ls -la dist/assets/
else
    echo "❌ dist/assets/ folder missing"
    exit 1
fi

# Check app.yaml configuration
if grep -q "dist/assets" app.yaml; then
    echo "✅ app.yaml correctly configured for SPA"
else
    echo "❌ app.yaml may not be properly configured"
    exit 1
fi

echo ""
echo "🎉 All checks passed! Ready for deployment."
echo ""
echo "To deploy, run:"
echo "  gcloud app deploy app.yaml"
echo ""
echo "To view your app after deployment:"
echo "  gcloud app browse"