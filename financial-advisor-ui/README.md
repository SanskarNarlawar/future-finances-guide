# 💰 AI Financial Advisor - Web UI

A modern, responsive web interface for the AI Financial Advisor API, offering both simple and detailed financial advisory services.

## 🌟 Features

### 🚀 **Dual Mode Interface**
- **Quick Ask Mode**: Simple questions without any setup
- **Detailed Advice Mode**: Personalized recommendations with financial profile

### 💡 **Smart Features**
- **Quick Suggestions**: Popular financial questions with one-click
- **Financial Profile**: Optional detailed profile for personalized advice
- **Real-time Chat**: Instant responses from the AI advisor
- **Auto-save**: Your profile is saved locally for convenience
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile
- **Dark/Light Theme**: Automatic theme switching based on system preference

### 🎯 **Financial Advisory Categories**
- **Investments**: Stocks, mutual funds, SIPs, portfolio management
- **Real Estate**: Home loans, property buying, market analysis
- **Education**: College planning, education loans, savings strategies
- **Business**: Startup funding, business loans, financial planning
- **Retirement**: Long-term planning, pension, wealth building
- **General**: Savings, debt management, budgeting

## 🚀 Quick Start

### Prerequisites
- **Backend API**: The Spring Boot Financial Advisor API must be running on `http://localhost:8080`
- **Python 3**: For running the UI server (or any web server)
- **Modern Browser**: Chrome, Firefox, Safari, or Edge

### 1. Start the Backend API
```bash
cd /workspace/llm-api
mvn spring-boot:run
```

### 2. Start the UI Server
```bash
cd /workspace/financial-advisor-ui
python3 server.py
```

### 3. Open in Browser
Visit: **http://localhost:3000**

## 🎮 How to Use

### 🚀 Quick Ask Mode (Simple)
1. Click on **"Quick Ask"** tab
2. Type any financial question or click on suggestion buttons
3. Get instant, detailed advice

**Example Questions:**
- "How do I start investing with ₹10,000?"
- "What documents do I need for a home loan?"
- "Which is the best savings account in India?"

### 🎯 Detailed Advice Mode (Personalized)
1. Click on **"Detailed Advice"** tab
2. Optionally fill out your financial profile:
   - Age, income range, risk tolerance
   - Current savings, monthly expenses
   - Financial goals and interests
3. Ask questions to get personalized recommendations

**Profile Benefits:**
- Age-specific investment strategies
- Income-appropriate recommendations
- Risk-based portfolio suggestions
- Goal-oriented financial planning

## 🔧 Technical Details

### File Structure
```
financial-advisor-ui/
├── index.html          # Main HTML page
├── styles.css          # Modern CSS with responsive design
├── script.js           # JavaScript functionality
├── server.py           # Python HTTP server
└── README.md           # This file
```

### API Integration
The UI connects to these backend endpoints:
- **Simple Questions**: `POST /api/v1/financial-advisor/ask`
- **Detailed Chat**: `POST /api/v1/financial-advisor/chat`

### Browser Support
- ✅ Chrome 80+
- ✅ Firefox 75+
- ✅ Safari 13+
- ✅ Edge 80+

## 🎨 UI Features

### Modern Design
- **Clean Interface**: Minimalist, professional design
- **Smooth Animations**: Subtle transitions and loading states
- **Responsive Layout**: Perfect on all screen sizes
- **Accessibility**: Keyboard shortcuts and screen reader support

### Interactive Elements
- **Suggestion Buttons**: Quick access to popular questions
- **Profile Toggle**: Expandable financial profile form
- **Real-time Chat**: Instant message display with typing indicators
- **Loading States**: Visual feedback during API calls

### Keyboard Shortcuts
- **Ctrl/Cmd + 1**: Switch to Quick Ask mode
- **Ctrl/Cmd + 2**: Switch to Detailed Advice mode
- **Enter**: Send message
- **Escape**: Close profile form

## 🔒 Privacy & Security

- **No Data Storage**: Your conversations are not stored on the server
- **Local Storage**: Profile data is saved locally in your browser only
- **Secure Communication**: HTTPS-ready (when deployed with SSL)
- **No Tracking**: No analytics or tracking scripts

## 🛠️ Development

### Running Without Python
If you don't have Python, you can use any web server:

**Using Node.js:**
```bash
npx http-server -p 3000 --cors
```

**Using PHP:**
```bash
php -S localhost:3000
```

**Using Live Server (VS Code Extension):**
1. Install "Live Server" extension
2. Right-click on `index.html`
3. Select "Open with Live Server"

### Customization
- **Colors**: Edit CSS variables in `styles.css`
- **API URL**: Change `API_BASE_URL` in `script.js`
- **Port**: Modify `PORT` in `server.py`

## 🐛 Troubleshooting

### Common Issues

**❌ "Can't connect to server"**
- ✅ Ensure backend API is running on `http://localhost:8080`
- ✅ Check if the API health endpoint works: `http://localhost:8080/api/v1/llm/health`

**❌ "CORS Error"**
- ✅ Make sure you're using the provided server (not opening HTML directly)
- ✅ Backend has CORS enabled for all origins

**❌ "Profile not saving"**
- ✅ Check browser localStorage is enabled
- ✅ Try clearing browser cache and reload

**❌ "UI not responsive"**
- ✅ Try a different browser
- ✅ Check browser console for JavaScript errors

### API Status Check
Test if the backend is working:
```bash
curl http://localhost:8080/api/v1/llm/health
```

Should return: `{"status": "UP", "timestamp": "..."}`

## 📱 Mobile Experience

The UI is fully optimized for mobile devices:
- **Touch-friendly**: Large buttons and input areas
- **Responsive Text**: Readable on all screen sizes
- **Mobile Navigation**: Optimized tab switching
- **Gesture Support**: Swipe and tap interactions

## 🎯 Sample Conversations

### Quick Ask Examples
**User**: "How much should I invest monthly?"
**AI**: "💡 **Financial Advisory Response**

💰 **Essential Financial Planning:**
- Emergency fund: 6-12 months expenses
- Life insurance: 10-15x annual income
- Health insurance: ₹10-20L family coverage..."

### Detailed Profile Examples
**User**: "Investment advice for me" (with profile: Age 28, Tech worker, ₹75K income)
**AI**: "🎯 **Personalized advice based on your profile:**

📊 **Age-based Financial Goals (28 years):**
- Build emergency fund (6 months expenses)
- Start investing early for compounding
- Focus on career growth and skill development

🎯 **Investment Strategy (MODERATE Risk):**
- Balanced portfolio with diversified funds
- 60% equity, 40% debt allocation..."

## 🚀 Deployment

### Production Deployment
For production use:
1. Use a proper web server (Nginx, Apache)
2. Enable HTTPS
3. Configure proper CORS policies
4. Set up monitoring and logging

### Docker Deployment
```dockerfile
FROM nginx:alpine
COPY . /usr/share/nginx/html
EXPOSE 80
```

## 📞 Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Verify backend API is running correctly
3. Check browser console for error messages
4. Ensure you're using a supported browser

## 🎉 Enjoy Your Financial Advisory Experience!

Your AI Financial Advisor is ready to help you make smart money decisions. Whether you have quick questions or need detailed personalized advice, the interface adapts to your needs.

**Happy Financial Planning! 💰📈**