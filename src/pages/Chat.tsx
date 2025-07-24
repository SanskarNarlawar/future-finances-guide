import { useState, useRef, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { Send, Bot, User, TrendingUp, AlertCircle } from "lucide-react";
import { Link } from "react-router-dom";
import { UserMenu } from "@/components/UserMenu";
import { useToast } from "@/hooks/use-toast";

interface Message {
  id: string;
  content: string;
  sender: 'user' | 'ai';
  timestamp: Date;
}

const Chat = () => {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      content: "Hello! I'm your AI Financial Advisor. I can help you with portfolio analysis, investment strategies, budgeting tips, and financial planning. What would you like to discuss today?",
      sender: 'ai',
      timestamp: new Date()
    }
  ]);
  const [inputMessage, setInputMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [apiKey, setApiKey] = useState("");
  const [showApiKeyInput, setShowApiKeyInput] = useState(true);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const { toast } = useToast();

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const financialPrompts = [
    "How should I diversify my portfolio?",
    "What's a good emergency fund amount?",
    "Should I invest in gold right now?",
    "How do I reduce investment risk?",
    "What are the best growth stocks for 2024?"
  ];

  const generateAIResponse = async (userMessage: string): Promise<string> => {
    // Simulated AI responses for demonstration
    // In production, replace with actual LLM API call
    
    const responses = {
      "diversify": "Portfolio diversification is crucial for managing risk. I recommend spreading investments across different asset classes: 60% stocks (mix of domestic and international), 20% bonds, 10% real estate (REITs), and 10% alternative investments like commodities or gold. This helps protect against market volatility.",
      
      "emergency": "A good emergency fund should cover 3-6 months of living expenses. Keep this in a high-yield savings account for easy access. Based on average household expenses, aim for $15,000-30,000. This provides financial security without needing to liquidate investments during emergencies.",
      
      "gold": "Gold can be a good hedge against inflation and economic uncertainty. Currently, with market volatility, allocating 5-10% of your portfolio to precious metals could be beneficial. Consider gold ETFs for easier liquidity, or physical gold for long-term wealth preservation.",
      
      "risk": "To reduce investment risk: 1) Diversify across asset classes and sectors, 2) Use dollar-cost averaging for regular investments, 3) Rebalance quarterly, 4) Avoid emotional trading, 5) Keep some cash reserves, and 6) Consider your time horizon - longer periods allow for more aggressive growth strategies.",
      
      "stocks": "For 2024, focus on companies with strong fundamentals: technology leaders (AI, cloud computing), healthcare innovators, renewable energy companies, and emerging market leaders. Consider ETFs for broader exposure: QQQ for tech, VTI for total market, or VXUS for international diversification."
    };

    // Simple keyword matching for demo
    const lowerMessage = userMessage.toLowerCase();
    for (const [key, response] of Object.entries(responses)) {
      if (lowerMessage.includes(key)) {
        return response;
      }
    }

    // Default financial advice response
    return `Based on your question about "${userMessage}", I'd recommend focusing on your long-term financial goals. Consider factors like your risk tolerance, time horizon, and current financial situation. Would you like me to analyze your specific portfolio or provide more targeted advice? Feel free to share more details about your financial situation.`;
  };

  const handleSendMessage = async () => {
    if (!inputMessage.trim() || isLoading) return;

    if (showApiKeyInput && !apiKey.trim()) {
      toast({
        title: "API Key Required",
        description: "Please enter your OpenAI API key to use the AI advisor, or use the demo mode.",
        variant: "destructive"
      });
      return;
    }

    const userMessage: Message = {
      id: Date.now().toString(),
      content: inputMessage,
      sender: 'user',
      timestamp: new Date()
    };

    setMessages(prev => [...prev, userMessage]);
    setInputMessage("");
    setIsLoading(true);

    try {
      // In demo mode or if API key is provided, generate response
      const aiResponse = await generateAIResponse(inputMessage);
      
      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: aiResponse,
        sender: 'ai',
        timestamp: new Date()
      };

      setMessages(prev => [...prev, aiMessage]);
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to get AI response. Please try again.",
        variant: "destructive"
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSendMessage();
    }
  };

  const useDemoMode = () => {
    setShowApiKeyInput(false);
    toast({
      title: "Demo Mode Active",
      description: "Using simulated AI responses. For real AI integration, add your API key.",
    });
  };

  return (
    <div className="min-h-screen bg-gradient-hero">
      {/* Navigation */}
      <nav className="border-b bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <Link to="/" className="flex items-center space-x-2">
            <TrendingUp className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">FinanceAI</span>
          </Link>
          <div className="flex items-center space-x-4">
            <Link to="/dashboard">
              <Button variant="outline">Dashboard</Button>
            </Link>
            <Link to="/stocks">
              <Button variant="outline">Stocks</Button>
            </Link>
            <Link to="/learning">
              <Button variant="outline">Learning</Button>
            </Link>
            <UserMenu />
          </div>
        </div>
      </nav>

      <div className="container mx-auto p-6 max-w-4xl">
        <div className="mb-6">
          <h1 className="text-4xl font-bold mb-2">AI Financial Advisor</h1>
          <p className="text-muted-foreground text-lg">
            Get personalized financial advice powered by artificial intelligence
          </p>
        </div>

        {/* API Key Input */}
        {showApiKeyInput && (
          <Card className="mb-6 shadow-card border-warning">
            <CardHeader>
              <CardTitle className="flex items-center">
                <AlertCircle className="mr-2 h-5 w-5 text-warning" />
                API Configuration
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <p className="text-sm text-muted-foreground">
                To use the AI advisor with real responses, enter your OpenAI API key. 
                Or try our demo mode with simulated responses.
              </p>
              <div className="flex space-x-2">
                <Input
                  type="password"
                  placeholder="Enter your OpenAI API key (optional)"
                  value={apiKey}
                  onChange={(e) => setApiKey(e.target.value)}
                  className="flex-1"
                />
                <Button onClick={() => setShowApiKeyInput(false)}>
                  Use API Key
                </Button>
                <Button variant="outline" onClick={useDemoMode}>
                  Demo Mode
                </Button>
              </div>
            </CardContent>
          </Card>
        )}

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
          {/* Chat Interface */}
          <div className="lg:col-span-3">
            <Card className="shadow-elevated h-[600px] flex flex-col">
              <CardHeader>
                <CardTitle className="flex items-center">
                  <Bot className="mr-2 h-5 w-5 text-primary" />
                  Financial Advisor Chat
                </CardTitle>
              </CardHeader>
              
              <CardContent className="flex-1 flex flex-col p-0">
                <ScrollArea className="flex-1 p-4">
                  <div className="space-y-4">
                    {messages.map((message) => (
                      <div
                        key={message.id}
                        className={`flex ${message.sender === 'user' ? 'justify-end' : 'justify-start'}`}
                      >
                        <div
                          className={`max-w-[80%] p-3 rounded-lg ${
                            message.sender === 'user'
                              ? 'bg-gradient-primary text-primary-foreground'
                              : 'bg-muted'
                          }`}
                        >
                          <div className="flex items-start space-x-2">
                            {message.sender === 'ai' && (
                              <Bot className="h-4 w-4 mt-1 text-primary" />
                            )}
                            {message.sender === 'user' && (
                              <User className="h-4 w-4 mt-1" />
                            )}
                            <div>
                              <p className="text-sm">{message.content}</p>
                              <p className={`text-xs mt-1 ${
                                message.sender === 'user' ? 'text-primary-foreground/70' : 'text-muted-foreground'
                              }`}>
                                {message.timestamp.toLocaleTimeString()}
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                    
                    {isLoading && (
                      <div className="flex justify-start">
                        <div className="bg-muted p-3 rounded-lg max-w-[80%]">
                          <div className="flex items-center space-x-2">
                            <Bot className="h-4 w-4 text-primary animate-pulse" />
                            <p className="text-sm">AI is thinking...</p>
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                  <div ref={messagesEndRef} />
                </ScrollArea>
                
                <Separator />
                
                <div className="p-4">
                  <div className="flex space-x-2">
                    <Input
                      placeholder="Ask about investments, budgeting, or financial planning..."
                      value={inputMessage}
                      onChange={(e) => setInputMessage(e.target.value)}
                      onKeyPress={handleKeyPress}
                      disabled={isLoading}
                      className="flex-1"
                    />
                    <Button 
                      onClick={handleSendMessage} 
                      disabled={isLoading || !inputMessage.trim()}
                    >
                      <Send className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Quick Actions */}
          <div className="space-y-6">
            <Card className="shadow-card">
              <CardHeader>
                <CardTitle>Quick Questions</CardTitle>
              </CardHeader>
              <CardContent className="space-y-2">
                {financialPrompts.map((prompt, index) => (
                  <Button
                    key={index}
                    variant="outline"
                    size="sm"
                    className="w-full text-left justify-start text-wrap h-auto p-3"
                    onClick={() => setInputMessage(prompt)}
                  >
                    {prompt}
                  </Button>
                ))}
              </CardContent>
            </Card>

            <Card className="shadow-card">
              <CardHeader>
                <CardTitle>Financial Tips</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-sm">
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">Diversification</h4>
                  <p className="text-muted-foreground">Spread risk across different asset classes</p>
                </div>
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">Emergency Fund</h4>
                  <p className="text-muted-foreground">Keep 3-6 months of expenses saved</p>
                </div>
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">Long-term Focus</h4>
                  <p className="text-muted-foreground">Time in market beats timing the market</p>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Chat;