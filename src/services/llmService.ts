import { apiClient } from '@/lib/api';

export interface ChatMessage {
  id: string;
  content: string;
  sender: 'user' | 'ai';
  timestamp: Date;
  language: string;
}

export interface ChatRequest {
  message: string;
  session_id?: string;
  model_name?: string;
  max_tokens?: number;
  temperature?: number;
  top_p?: number;
  frequency_penalty?: number;
  presence_penalty?: number;
}

export interface ChatResponse {
  id: string;
  session_id: string;
  message: string;
  model_name: string;
  created_at: string;
  token_count?: number;
  total_tokens?: number;
  prompt_tokens?: number;
  completion_tokens?: number;
}

export interface ApiHealthResponse {
  status: string;
  service: string;
  version: string;
}

export interface ModelsResponse {
  models: Array<{
    id: string;
    name: string;
    description: string;
  }>;
  default: string;
}

class LlmService {
  private baseEndpoint = '/llm';

  // Simple question endpoint - most user-friendly
  async askQuestion(question: string): Promise<ChatResponse> {
    try {
      const response = await apiClient.post(`${this.baseEndpoint}/ask`, {
        question: question
      });
      return response.data;
    } catch (error) {
      console.error('Error asking question:', error);
      throw this.handleError(error);
    }
  }

  // Full chat endpoint with all parameters
  async sendChatMessage(request: ChatRequest): Promise<ChatResponse> {
    try {
      // Set defaults if not provided
      const chatRequest: ChatRequest = {
        session_id: `session-${Date.now()}`,
        model_name: 'gpt-3.5-turbo',
        max_tokens: 1500,
        temperature: 0.7,
        ...request
      };

      const response = await apiClient.post(`${this.baseEndpoint}/simple-chat`, chatRequest);
      return response.data;
    } catch (error) {
      console.error('Error sending chat message:', error);
      throw this.handleError(error);
    }
  }

  // Get chat history for a session
  async getChatHistory(sessionId: string): Promise<ChatMessage[]> {
    try {
      const response = await apiClient.get(`${this.baseEndpoint}/chat/history/${sessionId}`);
      return response.data.map((msg: any) => ({
        id: msg.id.toString(),
        content: msg.content,
        sender: msg.role.toLowerCase() === 'user' ? 'user' : 'ai',
        timestamp: new Date(msg.created_at),
        language: 'en' // Default, could be enhanced
      }));
    } catch (error) {
      console.error('Error getting chat history:', error);
      throw this.handleError(error);
    }
  }

  // Clear chat history for a session
  async clearChatHistory(sessionId: string): Promise<void> {
    try {
      await apiClient.delete(`${this.baseEndpoint}/chat/history/${sessionId}`);
    } catch (error) {
      console.error('Error clearing chat history:', error);
      throw this.handleError(error);
    }
  }

  // Check API health
  async checkHealth(): Promise<ApiHealthResponse> {
    try {
      const response = await apiClient.get(`${this.baseEndpoint}/health`);
      return response.data;
    } catch (error) {
      console.error('Error checking health:', error);
      throw this.handleError(error);
    }
  }

  // Get available models
  async getModels(): Promise<ModelsResponse> {
    try {
      const response = await apiClient.get(`${this.baseEndpoint}/models`);
      return response.data;
    } catch (error) {
      console.error('Error getting models:', error);
      throw this.handleError(error);
    }
  }

  // Test connection to backend
  async testConnection(): Promise<boolean> {
    try {
      await this.checkHealth();
      return true;
    } catch (error) {
      console.error('Backend connection test failed:', error);
      return false;
    }
  }

  private handleError(error: any): Error {
    if (error.response) {
      // Server responded with error status
      const message = error.response.data?.message || error.response.data || 'Server error occurred';
      return new Error(`API Error (${error.response.status}): ${message}`);
    } else if (error.request) {
      // Network error
      return new Error('Network error: Cannot connect to the backend server. Please ensure it is running.');
    } else {
      // Other error
      return new Error(`Request error: ${error.message}`);
    }
  }
}

export const llmService = new LlmService();
export default llmService;