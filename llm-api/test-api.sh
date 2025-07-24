#!/bin/bash

echo "=== LLM API Test Script ==="
echo

# Test health endpoint
echo "1. Testing Health Endpoint:"
curl -s -X GET http://localhost:8080/api/v1/llm/health
echo -e "\n"

# Test chat endpoint
echo "2. Testing Chat Endpoint:"
curl -s -X POST http://localhost:8080/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello, tell me a fun fact about AI!",
    "session_id": "demo-session",
    "max_tokens": 100,
    "temperature": 0.8
  }'
echo -e "\n"

# Test another message in the same session
echo "3. Testing Follow-up Message:"
curl -s -X POST http://localhost:8080/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Can you explain that in simpler terms?",
    "session_id": "demo-session"
  }'
echo -e "\n"

# Test chat history
echo "4. Testing Chat History:"
curl -s -X GET "http://localhost:8080/api/v1/llm/history/demo-session"
echo -e "\n"

# Test clearing history
echo "5. Testing Clear History:"
curl -s -X DELETE "http://localhost:8080/api/v1/llm/history/demo-session"
echo -e "\n"

# Test history after clearing
echo "6. Testing History After Clear:"
curl -s -X GET "http://localhost:8080/api/v1/llm/history/demo-session"
echo -e "\n"

echo "=== API Test Complete ==="
echo
echo "Available endpoints:"
echo "- Health: GET http://localhost:8080/api/v1/llm/health"
echo "- Chat: POST http://localhost:8080/api/v1/llm/chat"
echo "- History: GET http://localhost:8080/api/v1/llm/history/{sessionId}"
echo "- Clear History: DELETE http://localhost:8080/api/v1/llm/history/{sessionId}"
echo "- Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "- H2 Console: http://localhost:8080/h2-console"