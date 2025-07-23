#!/bin/bash
set -e

echo "Starting build process..."

# Ensure we're in the right directory
cd /workspace

# Install dependencies if needed
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
fi

# Clean dist directory
rm -rf dist

# Build the application
echo "Building application..."
npm run build

# Verify build output
if [ ! -f "dist/index.html" ]; then
    echo "Error: Build failed - dist/index.html not found"
    exit 1
fi

echo "Build completed successfully!"
ls -la dist/