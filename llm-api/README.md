# LLM API - Large Language Model Backend Service

A comprehensive Spring Boot REST API for interacting with Large Language Models (LLMs) like OpenAI's GPT models.

## Features

- 🤖 **LLM Integration**: Support for OpenAI GPT models (GPT-3.5, GPT-4)
- 💬 **Chat API**: Send messages and get AI responses
- 📝 **Conversation History**: Persistent chat sessions with context
- 🔧 **Configurable**: Easy configuration for different LLM providers
- 📊 **Mock Responses**: Works without API keys for testing
- 🔒 **Security**: Basic security configuration
- 📚 **API Documentation**: Swagger/OpenAPI documentation
- 🗄️ **Database**: H2 in-memory database for development

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- (Optional) OpenAI API key for real LLM responses

### Installation

1. **Clone and navigate to the project:**
   ```bash
   cd llm-api
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:
   ```bash
   java -jar target/llm-api-1.0.0.jar
   ```

4. **Access the application:**
   - API Base URL: `http://localhost:8080/api/v1/llm`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - H2 Console: `http://localhost:8080/h2-console`

### Configuration

#### OpenAI API Key (Optional)

To enable real LLM responses, set your OpenAI API key:

**Environment Variable:**
```bash
export OPENAI_API_KEY=your-api-key-here
mvn spring-boot:run
```

**Or via application properties:**
```yaml
llm:
  openai:
    api-key: your-api-key-here
```

#### Custom LLM Provider

You can configure a different OpenAI-compatible API:

```yaml
llm:
  openai:
    api-key: your-api-key
    base-url: https://your-custom-api.com/v1
```

## API Endpoints

### 1. Chat with LLM

**POST** `/api/v1/llm/chat`

Send a message to the LLM and get a response.

**Request Body:**
```json
{
  "message": "Hello, how are you?",
  "session_id": "optional-session-id",
  "model_name": "gpt-3.5-turbo",
  "max_tokens": 1000,
  "temperature": 0.7
}
```

**Response:**
```json
{
  "id": "response-id",
  "session_id": "session-id",
  "message": "Hello! I'm doing well, thank you for asking. How can I help you today?",
  "model_name": "gpt-3.5-turbo",
  "created_at": "2024-01-20T10:30:00",
  "token_count": 25,
  "finish_reason": "stop"
}
```

### 2. Get Chat History

**GET** `/api/v1/llm/chat/history/{sessionId}`

Retrieve the conversation history for a specific session.

**Response:**
```json
[
  {
    "id": 1,
    "session_id": "session-123",
    "role": "USER",
    "content": "Hello",
    "created_at": "2024-01-20T10:30:00",
    "token_count": 1
  },
  {
    "id": 2,
    "session_id": "session-123",
    "role": "ASSISTANT",
    "content": "Hello! How can I help you?",
    "created_at": "2024-01-20T10:30:05",
    "token_count": 6
  }
]
```

### 3. Clear Chat History

**DELETE** `/api/v1/llm/chat/history/{sessionId}`

Clear the conversation history for a specific session.

### 4. List Available Models

**GET** `/api/v1/llm/models`

Get list of available LLM models.

### 5. Health Check

**GET** `/api/v1/llm/health`

Check if the API is running.

## Example Usage

### Using cURL

```bash
# Send a chat message
curl -X POST http://localhost:8080/api/v1/llm/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is Spring Boot?",
    "session_id": "demo-session"
  }'

# Get chat history
curl http://localhost:8080/api/v1/llm/chat/history/demo-session

# Check health
curl http://localhost:8080/api/v1/llm/health
```

### Using JavaScript/Fetch

```javascript
// Send a chat message
const response = await fetch('http://localhost:8080/api/v1/llm/chat', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    message: 'Hello, world!',
    session_id: 'my-session',
    temperature: 0.8
  })
});

const data = await response.json();
console.log(data.message);
```

## Development

### Database

The application uses H2 in-memory database for development. You can access the H2 console at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:llmdb`
- Username: `sa`
- Password: `password`

### Testing

Run tests with:
```bash
mvn test
```

### Building for Production

```bash
mvn clean package -DskipTests
```

## Configuration Options

| Property | Description | Default |
|----------|-------------|---------|
| `server.port` | Server port | `8080` |
| `llm.openai.api-key` | OpenAI API key | Empty (uses mock responses) |
| `llm.openai.base-url` | OpenAI API base URL | `https://api.openai.com/v1` |
| `spring.datasource.url` | Database URL | `jdbc:h2:mem:llmdb` |

## Mock Responses

Without an OpenAI API key, the service provides intelligent mock responses:
- Greetings for "hello" messages
- Time responses for time-related queries
- Weather responses for weather queries
- Generic helpful responses for other messages

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For questions or issues, please create an issue in the repository.