#!/bin/bash

# 🌩️ Financial Advisor LLM API - GCP App Engine Deployment Script
# This script automates the GCP App Engine deployment process

set -e  # Exit on any error

echo "🌩️ Financial Advisor LLM API - GCP App Engine Deployment"
echo "========================================================"
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
print_status "Checking prerequisites..."

# Check if gcloud is installed
if ! command -v gcloud &> /dev/null; then
    print_error "Google Cloud SDK (gcloud) is not installed."
    print_status "Please install it from: https://cloud.google.com/sdk/docs/install"
    exit 1
fi
print_success "Google Cloud SDK check passed"

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven 3.6 or higher."
    exit 1
fi
print_success "Maven check passed"

# Check if we're in the right directory
if [ ! -f "pom.xml" ]; then
    print_error "pom.xml not found. Please run this script from the llm-api directory."
    exit 1
fi

if [ ! -f "app.yaml" ]; then
    print_error "app.yaml not found. Please ensure the App Engine configuration file exists."
    exit 1
fi

if [ ! -f ".gcloudignore" ]; then
    print_warning ".gcloudignore not found. This file is required for Java 17 runtime."
    print_status "Creating .gcloudignore file..."
    cat > .gcloudignore << 'EOF'
# .gcloudignore for Java 17 App Engine
*.class
*.log
.git/
.github/
Dockerfile*
docker-compose*.yml
deploy*.sh
src/test/
target/test-classes/
*.md
.idea/
.vscode/
*~
.DS_Store
EOF
    print_success ".gcloudignore file created"
fi
print_success "Project directory check passed"

echo ""
print_status "🎯 GCP App Engine Deployment Options:"
echo "1. Quick Deploy (build and deploy)"
echo "2. Build Only (prepare for manual deployment)"
echo "3. Deploy Only (assumes already built)"
echo "4. Full Setup (initialize App Engine + deploy)"

read -p "Choose option (1-4): " -n 1 -r
echo

case $REPLY in
    1)
        print_status "Starting Quick Deploy..."
        
        # Check if user is authenticated
        if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | head -n 1 > /dev/null; then
            print_warning "You need to authenticate with Google Cloud"
            print_status "Running: gcloud auth login"
            gcloud auth login
        fi
        
        # Get current project
        PROJECT_ID=$(gcloud config get-value project 2>/dev/null)
        if [ -z "$PROJECT_ID" ]; then
            print_error "No project set. Please set your project ID:"
            read -p "Enter your GCP Project ID: " PROJECT_ID
            gcloud config set project $PROJECT_ID
        fi
        print_success "Using project: $PROJECT_ID"
        
        # Build the application
        print_status "Building application..."
        mvn clean package -DskipTests -q
        
        if [ $? -eq 0 ]; then
            print_success "Build completed successfully"
        else
            print_error "Build failed. Please check the error messages above."
            exit 1
        fi
        
        # Deploy to App Engine
        print_status "Deploying to App Engine..."
        gcloud app deploy --quiet
        
        if [ $? -eq 0 ]; then
            print_success "✅ Deployment completed successfully!"
            
            # Get the app URL
            APP_URL=$(gcloud app browse --no-launch-browser 2>&1 | grep -o 'https://[^[:space:]]*')
            if [ -n "$APP_URL" ]; then
                echo ""
                print_success "🌟 Your app is live at: $APP_URL"
                echo "   🌐 Web UI: $APP_URL"
                echo "   📊 API Docs: $APP_URL/swagger-ui/index.html"
                echo "   ❤️  Health Check: $APP_URL/api/v1/llm/health"
            fi
        else
            print_error "Deployment failed. Check the error messages above."
            exit 1
        fi
        ;;
        
    2)
        print_status "Building application only..."
        mvn clean package -DskipTests
        
        if [ $? -eq 0 ]; then
            print_success "✅ Build completed successfully!"
            echo "   📦 JAR file: target/llm-api-1.0.0.jar"
            echo "   🚀 To deploy: gcloud app deploy"
        else
            print_error "Build failed. Please check the error messages above."
            exit 1
        fi
        ;;
        
    3)
        print_status "Deploying to App Engine (assuming already built)..."
        
        # Check if JAR exists
        if [ ! -f "target/llm-api-1.0.0.jar" ]; then
            print_error "JAR file not found. Please build first with option 2 or run 'mvn clean package -DskipTests'"
            exit 1
        fi
        
        gcloud app deploy --quiet
        
        if [ $? -eq 0 ]; then
            print_success "✅ Deployment completed successfully!"
            
            # Get the app URL
            APP_URL=$(gcloud app browse --no-launch-browser 2>&1 | grep -o 'https://[^[:space:]]*')
            if [ -n "$APP_URL" ]; then
                echo ""
                print_success "🌟 Your app is live at: $APP_URL"
                echo "   🌐 Web UI: $APP_URL"
                echo "   📊 API Docs: $APP_URL/swagger-ui/index.html"
                echo "   ❤️  Health Check: $APP_URL/api/v1/llm/health"
            fi
        else
            print_error "Deployment failed. Check the error messages above."
            exit 1
        fi
        ;;
        
    4)
        print_status "Full Setup - Initialize App Engine and Deploy..."
        
        # Check if user is authenticated
        if ! gcloud auth list --filter=status:ACTIVE --format="value(account)" | head -n 1 > /dev/null; then
            print_warning "You need to authenticate with Google Cloud"
            print_status "Running: gcloud auth login"
            gcloud auth login
        fi
        
        # Get current project
        PROJECT_ID=$(gcloud config get-value project 2>/dev/null)
        if [ -z "$PROJECT_ID" ]; then
            print_error "No project set. Please set your project ID:"
            read -p "Enter your GCP Project ID: " PROJECT_ID
            gcloud config set project $PROJECT_ID
        fi
        print_success "Using project: $PROJECT_ID"
        
        # Check if App Engine app exists
        if ! gcloud app describe &>/dev/null; then
            print_status "App Engine application not found. Creating..."
            print_status "Available regions:"
            echo "  1. us-central1 (Iowa, USA)"
            echo "  2. us-east1 (South Carolina, USA)"
            echo "  3. europe-west1 (Belgium)"
            echo "  4. asia-northeast1 (Tokyo, Japan)"
            read -p "Choose region (1-4): " -n 1 -r
            echo
            
            case $REPLY in
                1) REGION="us-central1" ;;
                2) REGION="us-east1" ;;
                3) REGION="europe-west1" ;;
                4) REGION="asia-northeast1" ;;
                *) REGION="us-central1" ;;
            esac
            
            print_status "Creating App Engine application in region: $REGION"
            gcloud app create --region=$REGION
            
            if [ $? -eq 0 ]; then
                print_success "App Engine application created successfully"
            else
                print_error "Failed to create App Engine application"
                exit 1
            fi
        else
            print_success "App Engine application already exists"
        fi
        
        # Build the application
        print_status "Building application..."
        mvn clean package -DskipTests -q
        
        if [ $? -eq 0 ]; then
            print_success "Build completed successfully"
        else
            print_error "Build failed. Please check the error messages above."
            exit 1
        fi
        
        # Deploy to App Engine
        print_status "Deploying to App Engine..."
        gcloud app deploy --quiet
        
        if [ $? -eq 0 ]; then
            print_success "✅ Full deployment completed successfully!"
            
            # Get the app URL
            APP_URL=$(gcloud app browse --no-launch-browser 2>&1 | grep -o 'https://[^[:space:]]*')
            if [ -n "$APP_URL" ]; then
                echo ""
                print_success "🌟 Your app is live at: $APP_URL"
                echo "   🌐 Web UI: $APP_URL"
                echo "   📊 API Docs: $APP_URL/swagger-ui/index.html"
                echo "   ❤️  Health Check: $APP_URL/api/v1/llm/health"
            fi
        else
            print_error "Deployment failed. Check the error messages above."
            exit 1
        fi
        ;;
        
    *)
        print_error "Invalid option selected."
        exit 1
        ;;
esac

echo ""
print_success "🎉 GCP App Engine deployment process completed!"

# Provide helpful information
if [ "$REPLY" = "1" ] || [ "$REPLY" = "3" ] || [ "$REPLY" = "4" ]; then
    echo ""
    print_status "📋 Useful Commands:"
    echo "   # View logs"
    echo "   gcloud app logs tail -s default"
    echo ""
    echo "   # View app in browser"
    echo "   gcloud app browse"
    echo ""
    echo "   # Check app status"
    echo "   gcloud app versions list"
    echo ""
    echo "   # Monitor in console"
    echo "   https://console.cloud.google.com/appengine/services"
    echo ""
    print_status "💰 Monitor costs at: https://console.cloud.google.com/billing"
fi

print_status "🌟 Your Financial Advisor AI is now live on Google Cloud!"