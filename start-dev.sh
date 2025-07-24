#!/bin/bash

echo "🚀 Starting Full-Stack Development Environment..."
echo "======================================================="

# Function to kill background processes on exit
cleanup() {
    echo "🛑 Shutting down services..."
    kill $(jobs -p) 2>/dev/null
    exit
}

# Trap Ctrl+C
trap cleanup SIGINT SIGTERM

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js."
    exit 1
fi

echo "📦 Installing frontend dependencies..."
npm install

echo "🏗️  Starting backend on port 8081..."
cd llm-api
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8081" &
BACKEND_PID=$!
cd ..

# Wait a bit for backend to start
echo "⏳ Waiting for backend to start..."
sleep 10

echo "🌐 Starting frontend on port 8080..."
npm run dev &
FRONTEND_PID=$!

echo "======================================================="
echo "✅ Development servers started!"
echo "🌐 Frontend: http://localhost:8080"
echo "🔧 Backend:  http://localhost:8081"
echo "📖 API Docs: http://localhost:8081/swagger-ui.html"
echo "🗄️  H2 Console: http://localhost:8081/h2-console"
echo "======================================================="
echo "Press Ctrl+C to stop all services"

# Wait for user to terminate
wait