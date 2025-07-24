#!/bin/bash

# 🚀 Financial Advisor LLM API - Quick Deployment Script
# This script automates the simplest deployment method (JAR)

set -e  # Exit on any error

echo "🚀 Financial Advisor LLM API - Quick Deployment"
echo "=============================================="
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

# Check Java
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 17 or higher."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt "17" ]; then
    print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi
print_success "Java version check passed"

# Check Maven
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
print_success "Project directory check passed"

echo ""
print_status "Starting deployment process..."

# Step 1: Clean and build
print_status "Step 1: Cleaning and building the application..."
mvn clean package -DskipTests -q

if [ $? -eq 0 ]; then
    print_success "Build completed successfully"
else
    print_error "Build failed. Please check the error messages above."
    exit 1
fi

# Step 2: Check if JAR file exists
JAR_FILE="target/llm-api-1.0.0.jar"
if [ ! -f "$JAR_FILE" ]; then
    print_error "JAR file not found at $JAR_FILE"
    exit 1
fi

JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
print_success "JAR file created successfully (Size: $JAR_SIZE)"

# Step 3: Check if port 8080 is available
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    print_warning "Port 8080 is already in use. The application might already be running."
    read -p "Do you want to continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_status "Deployment cancelled."
        exit 0
    fi
fi

echo ""
print_status "🎯 Deployment Options:"
echo "1. Run in foreground (you'll see logs, Ctrl+C to stop)"
echo "2. Run in background (daemon mode)"
echo "3. Just build (don't run)"

read -p "Choose option (1-3): " -n 1 -r
echo

case $REPLY in
    1)
        print_status "Starting application in foreground mode..."
        echo ""
        print_success "✅ Application starting! Access it at:"
        echo "   🌐 Web UI: http://localhost:8080"
        echo "   📊 API Docs: http://localhost:8080/swagger-ui/index.html"
        echo "   ❤️  Health Check: http://localhost:8080/api/v1/llm/health"
        echo ""
        print_status "Press Ctrl+C to stop the application"
        echo ""
        java -jar "$JAR_FILE"
        ;;
    2)
        print_status "Starting application in background mode..."
        nohup java -jar "$JAR_FILE" > application.log 2>&1 &
        APP_PID=$!
        echo $APP_PID > application.pid
        
        # Wait a moment for the application to start
        sleep 5
        
        # Check if the process is still running
        if kill -0 $APP_PID 2>/dev/null; then
            print_success "✅ Application started successfully in background!"
            echo "   🌐 Web UI: http://localhost:8080"
            echo "   📊 API Docs: http://localhost:8080/swagger-ui/index.html"
            echo "   ❤️  Health Check: http://localhost:8080/api/v1/llm/health"
            echo ""
            print_status "Process ID: $APP_PID"
            print_status "Logs: tail -f application.log"
            print_status "Stop: kill $APP_PID (or kill \$(cat application.pid))"
        else
            print_error "Application failed to start. Check application.log for details."
            exit 1
        fi
        ;;
    3)
        print_success "✅ Build completed successfully!"
        echo "   📦 JAR file: $JAR_FILE"
        echo "   🚀 To run: java -jar $JAR_FILE"
        ;;
    *)
        print_error "Invalid option selected."
        exit 1
        ;;
esac

echo ""
print_success "🎉 Deployment completed!"

# Provide helpful information
if [ "$REPLY" != "3" ]; then
    echo ""
    print_status "📋 Quick Test Commands:"
    echo "   # Test API health"
    echo "   curl http://localhost:8080/api/v1/llm/health"
    echo ""
    echo "   # Test financial advisor"
    echo '   curl -X POST http://localhost:8080/api/v1/financial-advisor/ask \'
    echo '     -H "Content-Type: application/json" \'
    echo '     -d '"'"'{"question": "How do I start investing?"}'"'"
    echo ""
    print_status "🌐 Open http://localhost:8080 in your browser to use the web interface!"
fi