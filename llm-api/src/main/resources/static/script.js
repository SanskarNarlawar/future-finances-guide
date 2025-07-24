// Configuration
const API_BASE_URL = '/api/v1';
const APP_VERSION = 'v2.0-cors-fixed'; // Version identifier

// State Management
let currentMode = 'simple';
let isProfileExpanded = false;

// DOM Elements
const elements = {
    // Mode switching
    modeTabs: document.querySelectorAll('.mode-tab'),
    simpleModeContainer: document.getElementById('simple-mode'),
    detailedModeContainer: document.getElementById('detailed-mode'),
    
    // Simple mode
    simpleInput: document.getElementById('simple-input'),
    simpleSendBtn: document.getElementById('simple-send'),
    simpleMessages: document.getElementById('simple-messages'),
    suggestionBtns: document.querySelectorAll('.suggestion-btn'),
    
    // Detailed mode
    detailedInput: document.getElementById('detailed-input'),
    detailedSendBtn: document.getElementById('detailed-send'),
    detailedMessages: document.getElementById('detailed-messages'),
    
    // Profile
    profileToggleBtn: document.getElementById('profile-toggle-btn'),
    profileForm: document.getElementById('profile-form'),
    
    // Loading
    loadingOverlay: document.getElementById('loading-overlay')
};

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    console.log(`Financial Advisor App ${APP_VERSION} - Initializing...`);
    initializeEventListeners();
    loadProfileFromStorage();
    addWelcomeMessage();
});

// Event Listeners
function initializeEventListeners() {
    // Mode switching
    elements.modeTabs.forEach(tab => {
        tab.addEventListener('click', () => switchMode(tab.dataset.mode));
    });

    // Simple mode
    elements.simpleSendBtn.addEventListener('click', sendSimpleMessage);
    elements.simpleInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendSimpleMessage();
        }
    });

    // Detailed mode
    elements.detailedSendBtn.addEventListener('click', sendDetailedMessage);
    elements.detailedInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendDetailedMessage();
        }
    });

    // Profile toggle
    elements.profileToggleBtn.addEventListener('click', toggleProfile);

    // Suggestion buttons
    elements.suggestionBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const question = btn.textContent.trim();
            elements.simpleInput.value = question;
            sendSimpleMessage();
        });
    });
}

// Mode switching
function switchMode(mode) {
    currentMode = mode;
    
    // Update tabs
    elements.modeTabs.forEach(tab => {
        tab.classList.toggle('active', tab.dataset.mode === mode);
    });
    
    // Update containers
    elements.simpleModeContainer.classList.toggle('active', mode === 'simple');
    elements.detailedModeContainer.classList.toggle('active', mode === 'detailed');
}

// Loading states
function showLoading() {
    elements.loadingOverlay.classList.add('show');
}

function hideLoading() {
    elements.loadingOverlay.classList.remove('show');
}

// Add welcome message
function addWelcomeMessage() {
    const welcomeMessage = `
🎯 **Welcome to Your AI Financial Advisor!** (${APP_VERSION})

I'm here to help you with:
• **Investment Planning** - Stocks, SIPs, Mutual Funds
• **Home & Car Buying** - Loans, EMI calculations
• **Education Planning** - College costs, education loans
• **Retirement Planning** - Long-term financial goals
• **Budgeting & Savings** - Personal finance management

**Quick Ask**: Just type your question
**Detailed Mode**: Add your financial profile for personalized advice

What would you like to know about your finances today?
    `;
    
    addMessage(elements.simpleMessages, 'assistant', welcomeMessage);
}

// Message Handling - COMPLETELY REWRITTEN
async function sendSimpleMessage() {
    const message = elements.simpleInput.value.trim();
    if (!message) return;
    
    console.log(`${APP_VERSION}: Sending message:`, message);
    
    // Add user message to chat
    addMessage(elements.simpleMessages, 'user', message);
    elements.simpleInput.value = '';
    
    // Disable send button and show loading
    elements.simpleSendBtn.disabled = true;
    showLoading();
    
    try {
        const response = await fetch(`${API_BASE_URL}/financial-advisor/ask`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-App-Version': APP_VERSION
            },
            body: JSON.stringify({
                question: message
            })
        });
        
        console.log(`${APP_VERSION}: Response status:`, response.status);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        console.log(`${APP_VERSION}: Response received successfully`);
        addMessage(elements.simpleMessages, 'assistant', data.message);
        
    } catch (error) {
        console.error(`${APP_VERSION}: Error sending message:`, error);
        
        // COMPLETELY NEW ERROR HANDLING - NO OLD MESSAGES
        let errorMessage = `🚨 **Unable to Connect** (${APP_VERSION})\n\n`;
        
        if (error.message.includes('Failed to fetch') || error.name === 'TypeError') {
            errorMessage += `**Network Error**: Cannot reach the financial advisor service.\n\n`;
            errorMessage += `**Possible causes:**\n`;
            errorMessage += `• Internet connection issues\n`;
            errorMessage += `• Server is starting up (please wait 2-3 minutes)\n`;
            errorMessage += `• CORS configuration still propagating\n\n`;
            errorMessage += `**Solutions:**\n`;
            errorMessage += `• Check your internet connection\n`;
            errorMessage += `• Wait a moment and try again\n`;
            errorMessage += `• Refresh the page (Ctrl+R)\n`;
            errorMessage += `• Try in incognito/private browsing mode`;
        } else if (error.message.includes('404')) {
            errorMessage += `**API Not Found**: The financial advisor endpoint is not available.\n`;
            errorMessage += `Please refresh the page and try again.`;
        } else if (error.message.includes('500')) {
            errorMessage += `**Server Error**: The financial advisor service encountered an error.\n`;
            errorMessage += `Please try again in a moment.`;
        } else {
            errorMessage += `**Unexpected Error**: ${error.message}\n`;
            errorMessage += `Please refresh the page and try again.`;
        }
        
        addMessage(elements.simpleMessages, 'assistant', errorMessage);
        
    } finally {
        elements.simpleSendBtn.disabled = false;
        hideLoading();
    }
}

// Detailed message handling - COMPLETELY REWRITTEN
async function sendDetailedMessage() {
    const message = elements.detailedInput.value.trim();
    if (!message) return;
    
    console.log(`${APP_VERSION}: Sending detailed message:`, message);
    
    // Add user message to chat
    addMessage(elements.detailedMessages, 'user', message);
    elements.detailedInput.value = '';
    
    // Disable send button and show loading
    elements.detailedSendBtn.disabled = true;
    showLoading();
    
    try {
        const payload = { 
            message: message, 
            session_id: `detailed-${Date.now()}` 
        };
        
        // Add financial profile if form is filled
        const profile = getFinancialProfile();
        if (profile && Object.keys(profile).length > 0) {
            payload.financial_profile = profile;
            payload.advisory_mode = "GENERAL";
        }
        
        const response = await fetch(`${API_BASE_URL}/financial-advisor/chat`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-App-Version': APP_VERSION
            },
            body: JSON.stringify(payload)
        });
        
        console.log(`${APP_VERSION}: Detailed response status:`, response.status);
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        console.log(`${APP_VERSION}: Detailed response received successfully`);
        
        // Add profile indicator if profile was used
        let responseMessage = data.message;
        if (data.profile_based) {
            responseMessage = "🎯 **Personalized advice based on your profile:**\n\n" + responseMessage;
        }
        
        addMessage(elements.detailedMessages, 'assistant', responseMessage);
        
    } catch (error) {
        console.error(`${APP_VERSION}: Error sending detailed message:`, error);
        
        // COMPLETELY NEW ERROR HANDLING - NO OLD MESSAGES
        let errorMessage = `🚨 **Unable to Connect** (${APP_VERSION})\n\n`;
        
        if (error.message.includes('Failed to fetch') || error.name === 'TypeError') {
            errorMessage += `**Network Error**: Cannot reach the financial advisor service.\n\n`;
            errorMessage += `**Possible causes:**\n`;
            errorMessage += `• Internet connection issues\n`;
            errorMessage += `• Server is starting up (please wait 2-3 minutes)\n`;
            errorMessage += `• CORS configuration still propagating\n\n`;
            errorMessage += `**Solutions:**\n`;
            errorMessage += `• Check your internet connection\n`;
            errorMessage += `• Wait a moment and try again\n`;
            errorMessage += `• Refresh the page (Ctrl+R)\n`;
            errorMessage += `• Try in incognito/private browsing mode`;
        } else if (error.message.includes('404')) {
            errorMessage += `**API Not Found**: The financial advisor endpoint is not available.\n`;
            errorMessage += `Please refresh the page and try again.`;
        } else if (error.message.includes('500')) {
            errorMessage += `**Server Error**: The financial advisor service encountered an error.\n`;
            errorMessage += `Please try again in a moment.`;
        } else {
            errorMessage += `**Unexpected Error**: ${error.message}\n`;
            errorMessage += `Please refresh the page and try again.`;
        }
        
        addMessage(elements.detailedMessages, 'assistant', errorMessage);
        
    } finally {
        elements.detailedSendBtn.disabled = false;
        hideLoading();
    }
}

// Profile Management
function toggleProfile() {
    isProfileExpanded = !isProfileExpanded;
    elements.profileForm.classList.toggle('collapsed', !isProfileExpanded);
    
    const icon = elements.profileToggleBtn.querySelector('i');
    const text = elements.profileToggleBtn.querySelector('span') || elements.profileToggleBtn;
    
    if (isProfileExpanded) {
        icon.className = 'fas fa-user-minus';
        text.textContent = 'Hide Financial Profile';
    } else {
        icon.className = 'fas fa-user-plus';
        text.textContent = 'Add Your Financial Profile (Optional)';
    }
}

function getFinancialProfile() {
    const profile = {};
    
    // Get all form elements
    const formElements = elements.profileForm.querySelectorAll('input, select');
    
    formElements.forEach(element => {
        if (element.value && element.value.trim() !== '') {
            let value = element.value.trim();
            
            // Convert numeric fields
            if (element.type === 'number') {
                value = parseFloat(value);
            }
            
            profile[element.name] = value;
        }
    });
    
    return profile;
}

// Storage functions
function saveProfileToStorage() {
    const profile = getFinancialProfile();
    localStorage.setItem('financialProfile', JSON.stringify(profile));
}

function loadProfileFromStorage() {
    try {
        const saved = localStorage.getItem('financialProfile');
        if (saved) {
            const profile = JSON.parse(saved);
            
            // Populate form fields
            Object.keys(profile).forEach(key => {
                const element = elements.profileForm.querySelector(`[name="${key}"]`);
                if (element) {
                    element.value = profile[key];
                }
            });
        }
    } catch (error) {
        console.error('Error loading profile from storage:', error);
    }
}

// Save profile when form changes
document.addEventListener('change', function(e) {
    if (e.target.closest('#profile-form')) {
        saveProfileToStorage();
    }
});

// Message display
function addMessage(container, type, content) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${type}`;
    
    const avatar = document.createElement('div');
    avatar.className = 'message-avatar';
    avatar.innerHTML = type === 'user' ? 
        '<i class="fas fa-user"></i>' : 
        '<i class="fas fa-robot"></i>';
    
    const messageContent = document.createElement('div');
    messageContent.className = 'message-content';
    messageContent.innerHTML = formatMessage(content);
    
    messageDiv.appendChild(avatar);
    messageDiv.appendChild(messageContent);
    
    container.appendChild(messageDiv);
    container.scrollTop = container.scrollHeight;
}

// Format message content
function formatMessage(content) {
    return content
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        .replace(/\n/g, '<br>')
        .replace(/•/g, '•')
        .replace(/₹/g, '₹')
        .replace(/(\d+)%/g, '$1%');
}