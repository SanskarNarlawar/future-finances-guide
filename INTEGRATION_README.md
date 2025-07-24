# Frontend-Backend Integration Guide

## Overview
This application consists of a React TypeScript frontend and a Java Spring Boot backend that have been integrated to work together seamlessly with proper CORS handling.

## Architecture
- **Frontend**: React + TypeScript + Vite (Port 8080)
- **Backend**: Spring Boot + Java 17 (Port 8081)
- **Database**: H2 in-memory database
- **CORS**: Properly configured for cross-origin requests

## Quick Start

### Option 1: Use the Development Script (Recommended)
```bash
./start-dev.sh
```

This script will:
- Install frontend dependencies
- Start the backend on port 8081
- Start the frontend on port 8080
- Provide all necessary URLs

### Option 2: Manual Setup

#### Start Backend
```bash
cd llm-api
mvn spring-boot:run
```

#### Start Frontend
```bash
npm install
npm run dev
```

## Access Points
- **Frontend Application**: http://localhost:8080
- **Backend API**: http://localhost:8081/api/v1
- **API Documentation**: http://localhost:8081/swagger-ui.html
- **H2 Database Console**: http://localhost:8081/h2-console

## Features

### Frontend
- Real-time connection status indicator
- Automatic fallback to demo mode if backend is unavailable
- Voice input and text-to-speech capabilities
- Multi-language support
- Responsive UI with shadcn/ui components

### Backend
- RESTful API endpoints for chat functionality
- Swagger/OpenAPI documentation
- CORS configuration for frontend integration
- H2 database for session management
- Multiple LLM service endpoints

### Integration Features
- Automatic backend connectivity testing
- Graceful error handling and fallbacks
- Real-time API communication
- Proxy configuration for development

## API Endpoints

### Main Chat Endpoints
- `POST /api/v1/llm/ask` - Simple question endpoint
- `POST /api/v1/llm/simple-chat` - Full chat with parameters
- `GET /api/v1/llm/chat/history/{sessionId}` - Get chat history
- `DELETE /api/v1/llm/chat/history/{sessionId}` - Clear chat history
- `GET /api/v1/llm/health` - Health check
- `GET /api/v1/llm/models` - Available models

## CORS Configuration
The backend is configured to accept requests from:
- `http://localhost:*`
- `http://127.0.0.1:*`
- `https://*.vercel.app`
- `https://*.netlify.app`
- `https://*.appspot.com`

## Environment Variables
- `REACT_APP_API_URL`: Override default backend URL
- `OPENAI_API_KEY`: OpenAI API key for the backend (optional)

## Troubleshooting

### Backend Not Starting
- Ensure Java 17+ is installed
- Ensure Maven is installed
- Check port 8081 is not already in use

### Frontend Not Connecting
- Verify backend is running on port 8081
- Check browser console for CORS errors
- Ensure the API base URL is correct

### CORS Issues
- Backend includes comprehensive CORS configuration
- All necessary headers are allowed
- Preflight requests are handled automatically

## Development Notes

### File Structure
```
src/
  lib/
    api.ts              # Axios configuration and interceptors
  services/
    llmService.ts       # Backend API service layer
  pages/
    Chat.tsx            # Main chat interface with backend integration

llm-api/
  src/main/java/com/llmapi/
    config/
      CorsConfig.java   # CORS configuration
      SecurityConfig.java # Security and CORS integration
    controller/
      LlmController.java # Main API endpoints
```

### Key Integration Points
1. **API Client**: Centralized axios configuration in `src/lib/api.ts`
2. **Service Layer**: Abstracted backend calls in `src/services/llmService.ts`
3. **CORS Handling**: Comprehensive configuration in backend
4. **Error Handling**: Graceful fallbacks and user feedback
5. **Development Proxy**: Vite proxy for seamless development

## Production Deployment
For production deployment:
1. Build the frontend: `npm run build`
2. Serve frontend static files from the backend or CDN
3. Update CORS configuration for production domains
4. Configure environment variables appropriately