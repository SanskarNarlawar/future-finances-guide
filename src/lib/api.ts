import axios from 'axios';

// API Configuration
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api/v1';

// Create axios instance with default configuration
export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000, // 30 seconds timeout
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging (can be extended for auth tokens)
apiClient.interceptors.request.use(
  (config) => {
    console.log(`API Request: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('API Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
apiClient.interceptors.response.use(
  (response) => {
    console.log(`API Response: ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error('API Response Error:', error.response?.data || error.message);
    
    // Handle different error types
    if (error.response?.status === 404) {
      console.error('API endpoint not found');
    } else if (error.response?.status === 500) {
      console.error('Internal server error');
    } else if (error.code === 'ECONNREFUSED') {
      console.error('Cannot connect to backend server. Is it running?');
    }
    
    return Promise.reject(error);
  }
);

export default apiClient;