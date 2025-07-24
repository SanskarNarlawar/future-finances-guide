// Configuration
const API_BASE_URL = '/api/v1';

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
    profileToggleBtn: document.getElementById('profile-toggle-btn'),
    profileForm: document.getElementById('profile-form'),
    
    // Loading
    loadingOverlay: document.getElementById('loading-overlay'),
    
    // Form elements
    age: document.getElementById('age'),
    incomeRange: document.getElementById('income-range'),
    riskTolerance: document.getElementById('risk-tolerance'),
    currentSavings: document.getElementById('current-savings'),
    monthlyExpenses: document.getElementById('monthly-expenses'),
    employmentStatus: document.getElementById('employment-status'),
    financialGoals: document.querySelectorAll('input[type="checkbox"][value]'),
    interests: document.querySelectorAll('input[type="checkbox"][value]')
};

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    initializeEventListeners();
    showWelcomeMessage();
});

// Event Listeners
function initializeEventListeners() {
    // Mode switching
    elements.modeTabs.forEach(tab => {
        tab.addEventListener('click', () => switchMode(tab.dataset.mode));
    });
    
    // Simple mode
    elements.simpleSendBtn.addEventListener('click', () => sendSimpleMessage());
    elements.simpleInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendSimpleMessage();
        }
    });
    
    // Suggestion buttons
    elements.suggestionBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const question = btn.dataset.question;
            elements.simpleInput.value = question;
            sendSimpleMessage();
        });
    });
    
    // Detailed mode
    elements.detailedSendBtn.addEventListener('click', () => sendDetailedMessage());
    elements.detailedInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendDetailedMessage();
        }
    });
    
    // Profile toggle
    elements.profileToggleBtn.addEventListener('click', toggleProfile);
}

// Mode Switching
function switchMode(mode) {
    currentMode = mode;
    
    // Update tab states
    elements.modeTabs.forEach(tab => {
        tab.classList.toggle('active', tab.dataset.mode === mode);
    });
    
    // Update container visibility
    elements.simpleModeContainer.classList.toggle('active', mode === 'simple');
    elements.detailedModeContainer.classList.toggle('active', mode === 'detailed');
    
    // Show welcome message for the new mode
    if (mode === 'simple' && elements.simpleMessages.children.length === 0) {
        showWelcomeMessage();
    } else if (mode === 'detailed' && elements.detailedMessages.children.length === 0) {
        showWelcomeMessage();
    }
}

// Welcome Messages
function showWelcomeMessage() {
    const container = currentMode === 'simple' ? elements.simpleMessages : elements.detailedMessages;
    
    const welcomeMessages = {
        simple: {
            text: "👋 Welcome to AI Financial Advisor! I'm here to help you with any financial questions. You can ask me about investments, loans, savings, retirement planning, or any other money-related topics. Just type your question or click on one of the suggestions above!",
            examples: [
                "💡 Try asking: 'How should I start investing?'",
                "🏠 Or: 'What do I need to know about home loans?'",
                "💰 Or: 'How much should I save each month?'"
            ]
        },
        detailed: {
            text: "🎯 Welcome to Personalized Financial Advisory! For the most tailored advice, you can optionally fill out your financial profile below. This helps me provide recommendations specific to your age, income, goals, and risk tolerance. Don't worry - you can also just ask questions without filling out the profile!",
            examples: [
                "📊 With profile: Get age-specific investment strategies",
                "🎯 With profile: Receive personalized risk assessments",
                "💼 With profile: Get income-appropriate recommendations"
            ]
        }
    };
    
    const welcome = welcomeMessages[currentMode];
    addMessage(container, 'assistant', welcome.text);
    
    // Add examples as a separate message
    setTimeout(() => {
        const exampleText = welcome.examples.join('\n');
        addMessage(container, 'assistant', exampleText);
    }, 1000);
}

// Message Handling
async function sendSimpleMessage() {
    const message = elements.simpleInput.value.trim();
    if (!message) return;
    
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
            },
            body: JSON.stringify({
                question: message
            })
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        addMessage(elements.simpleMessages, 'assistant', data.message);
        
    } catch (error) {
        console.error('Error sending simple message:', error);
        addMessage(elements.simpleMessages, 'assistant', 
            "I apologize, but I'm having trouble connecting to the server right now. Please check that the backend is running on http://localhost:8080 and try again. 🔧");
    } finally {
        elements.simpleSendBtn.disabled = false;
        hideLoading();
    }
}

async function sendDetailedMessage() {
    const message = elements.detailedInput.value.trim();
    if (!message) return;
    
    // Add user message to chat
    addMessage(elements.detailedMessages, 'user', message);
    elements.detailedInput.value = '';
    
    // Disable send button and show loading
    elements.detailedSendBtn.disabled = true;
    showLoading();
    
    try {
        // Build request payload
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
            },
            body: JSON.stringify(payload)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        
        // Add profile indicator if profile was used
        let responseMessage = data.message;
        if (data.profile_based) {
            responseMessage = "🎯 **Personalized advice based on your profile:**\n\n" + responseMessage;
        }
        
        addMessage(elements.detailedMessages, 'assistant', responseMessage);
        
    } catch (error) {
        console.error('Error sending detailed message:', error);
        addMessage(elements.detailedMessages, 'assistant', 
            "I apologize, but I'm having trouble connecting to the server right now. Please check that the backend is running on http://localhost:8080 and try again. 🔧");
    } finally {
        elements.detailedSendBtn.disabled = false;
        hideLoading();
    }
}

// Profile Management
function toggleProfile() {
    isProfileExpanded = !isProfileExpanded;
    
    if (isProfileExpanded) {
        elements.profileForm.classList.add('expanded');
        elements.profileToggleBtn.innerHTML = '<i class="fas fa-user-minus"></i> Hide Financial Profile';
    } else {
        elements.profileForm.classList.remove('expanded');
        elements.profileToggleBtn.innerHTML = '<i class="fas fa-user-plus"></i> Add Your Financial Profile (Optional)';
    }
}

function getFinancialProfile() {
    const profile = {};
    
    // Basic info
    if (elements.age.value) profile.age = parseInt(elements.age.value);
    if (elements.incomeRange.value) profile.income_range = elements.incomeRange.value;
    if (elements.riskTolerance.value) profile.risk_tolerance = elements.riskTolerance.value;
    if (elements.currentSavings.value) profile.current_savings = parseInt(elements.currentSavings.value);
    if (elements.monthlyExpenses.value) profile.monthly_expenses = parseInt(elements.monthlyExpenses.value);
    if (elements.employmentStatus.value) profile.employment_status = elements.employmentStatus.value;
    
    // Financial goals
    const selectedGoals = Array.from(document.querySelectorAll('input[type="checkbox"]:checked'))
        .map(cb => cb.value)
        .filter(value => ['HOME_PURCHASE', 'WEALTH_BUILDING', 'RETIREMENT_PLANNING', 'CHILD_EDUCATION', 'BUSINESS_INVESTMENT', 'DEBT_MANAGEMENT'].includes(value));
    
    if (selectedGoals.length > 0) {
        profile.financial_goals = selectedGoals;
    }
    
    // Interests
    const selectedInterests = Array.from(document.querySelectorAll('input[type="checkbox"]:checked'))
        .map(cb => cb.value)
        .filter(value => ['technology', 'healthcare', 'finance', 'real_estate', 'energy', 'consumer_goods'].includes(value));
    
    if (selectedInterests.length > 0) {
        profile.interests = selectedInterests;
    }
    
    return profile;
}

// UI Helper Functions
function addMessage(container, sender, text) {
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;
    
    const avatar = document.createElement('div');
    avatar.className = 'message-avatar';
    avatar.innerHTML = sender === 'user' ? '<i class="fas fa-user"></i>' : '<i class="fas fa-robot"></i>';
    
    const content = document.createElement('div');
    content.className = 'message-content';
    
    // Format the message text (convert markdown-like formatting to HTML)
    const formattedText = formatMessage(text);
    content.innerHTML = formattedText;
    
    if (sender === 'user') {
        messageDiv.appendChild(content);
        messageDiv.appendChild(avatar);
    } else {
        messageDiv.appendChild(avatar);
        messageDiv.appendChild(content);
    }
    
    container.appendChild(messageDiv);
    
    // Scroll to bottom
    container.scrollTop = container.scrollHeight;
    
    // Add typing animation for assistant messages
    if (sender === 'assistant') {
        content.style.opacity = '0';
        setTimeout(() => {
            content.style.opacity = '1';
            content.style.transition = 'opacity 0.5s ease';
        }, 100);
    }
}

function formatMessage(text) {
    return text
        // Bold text
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        // Italic text
        .replace(/\*(.*?)\*/g, '<em>$1</em>')
        // Line breaks
        .replace(/\n/g, '<br>')
        // Bullet points
        .replace(/^• /gm, '• ')
        .replace(/^- /gm, '• ')
        // Currency formatting
        .replace(/₹(\d+)/g, '<span style="color: var(--secondary-color); font-weight: 600;">₹$1</span>')
        // Percentage formatting
        .replace(/(\d+(?:\.\d+)?)%/g, '<span style="color: var(--accent-color); font-weight: 600;">$1%</span>')
        // Emoji spacing
        .replace(/([\u{1F300}-\u{1F9FF}])/gu, '$1 ');
}

function showLoading() {
    elements.loadingOverlay.classList.add('active');
}

function hideLoading() {
    elements.loadingOverlay.classList.remove('active');
}

// Utility Functions
function generateSessionId() {
    return `session-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
}

function validateForm() {
    // Basic validation for financial profile
    const age = elements.age.value;
    if (age && (age < 18 || age > 100)) {
        alert('Please enter a valid age between 18 and 100');
        return false;
    }
    
    const savings = elements.currentSavings.value;
    if (savings && savings < 0) {
        alert('Current savings cannot be negative');
        return false;
    }
    
    const expenses = elements.monthlyExpenses.value;
    if (expenses && expenses < 0) {
        alert('Monthly expenses cannot be negative');
        return false;
    }
    
    return true;
}

// Error Handling
window.addEventListener('error', function(e) {
    console.error('Global error:', e.error);
    hideLoading();
});

// Network Status
window.addEventListener('online', function() {
    console.log('Network connection restored');
});

window.addEventListener('offline', function() {
    console.log('Network connection lost');
    hideLoading();
    alert('Network connection lost. Please check your internet connection.');
});

// Keyboard Shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl/Cmd + 1 for simple mode
    if ((e.ctrlKey || e.metaKey) && e.key === '1') {
        e.preventDefault();
        switchMode('simple');
    }
    
    // Ctrl/Cmd + 2 for detailed mode
    if ((e.ctrlKey || e.metaKey) && e.key === '2') {
        e.preventDefault();
        switchMode('detailed');
    }
    
    // Escape to close profile form
    if (e.key === 'Escape' && isProfileExpanded) {
        toggleProfile();
    }
});

// Auto-save profile data to localStorage
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
            if (profile.age) elements.age.value = profile.age;
            if (profile.income_range) elements.incomeRange.value = profile.income_range;
            if (profile.risk_tolerance) elements.riskTolerance.value = profile.risk_tolerance;
            if (profile.current_savings) elements.currentSavings.value = profile.current_savings;
            if (profile.monthly_expenses) elements.monthlyExpenses.value = profile.monthly_expenses;
            if (profile.employment_status) elements.employmentStatus.value = profile.employment_status;
            
            // Set checkboxes
            if (profile.financial_goals) {
                profile.financial_goals.forEach(goal => {
                    const checkbox = document.querySelector(`input[type="checkbox"][value="${goal}"]`);
                    if (checkbox) checkbox.checked = true;
                });
            }
            
            if (profile.interests) {
                profile.interests.forEach(interest => {
                    const checkbox = document.querySelector(`input[type="checkbox"][value="${interest}"]`);
                    if (checkbox) checkbox.checked = true;
                });
            }
        }
    } catch (error) {
        console.error('Error loading profile from storage:', error);
    }
}

// Auto-save on form changes
document.addEventListener('change', function(e) {
    if (e.target.closest('#profile-form')) {
        saveProfileToStorage();
    }
});

// Load saved profile on page load
document.addEventListener('DOMContentLoaded', function() {
    loadProfileFromStorage();
});

// Export for debugging
if (typeof window !== 'undefined') {
    window.FinancialAdvisorUI = {
        switchMode,
        sendSimpleMessage,
        sendDetailedMessage,
        getFinancialProfile,
        addMessage,
        showLoading,
        hideLoading
    };
}