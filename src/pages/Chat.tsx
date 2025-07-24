import { useState, useRef, useEffect } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { Send, Bot, User, TrendingUp, AlertCircle, Mic, MicOff, Volume2, VolumeX } from "lucide-react";
import { Link } from "react-router-dom";
import { UserMenu } from "@/components/UserMenu";
import { useToast } from "@/hooks/use-toast";
import { useVoice } from "@/hooks/useVoice";
import { useLanguageContext } from "@/contexts/LanguageContext";

interface Message {
  id: string;
  content: string;
  sender: 'user' | 'ai';
  timestamp: Date;
  language: string;
}

const Chat = () => {
  const [messages, setMessages] = useState<Message[]>([
    {
      id: '1',
      content: "Hello! I'm your AI Financial Advisor. I can help you with portfolio analysis, investment strategies, budgeting tips, and financial planning. What would you like to discuss today?",
      sender: 'ai',
      timestamp: new Date(),
      language: 'en'
    }
  ]);
  const [inputMessage, setInputMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [apiKey, setApiKey] = useState("");
  const [showApiKeyInput, setShowApiKeyInput] = useState(true);
  const [currentSpeakingMessage, setCurrentSpeakingMessage] = useState<string | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const { toast } = useToast();
  const { conversationLanguage, setConversationLanguage, detectLanguage } = useLanguageContext();

  // Initialize voice hooks for the current conversation language
  const voiceForInput = useVoice({
    language: conversationLanguage,
    onTranscriptReceived: (transcript: string) => {
      setInputMessage(transcript);
    }
  });

  const voiceForOutput = useVoice({
    language: conversationLanguage
  });

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

  // Multi-language responses
  const getLocalizedResponses = (language: string) => {
    const responses = {
      en: {
        "diversify": "Portfolio diversification is crucial for managing risk. I recommend spreading investments across different asset classes: 60% stocks (mix of domestic and international), 20% bonds, 10% real estate (REITs), and 10% alternative investments like commodities or gold. This helps protect against market volatility.",
        "emergency": "A good emergency fund should cover 3-6 months of living expenses. Keep this in a high-yield savings account for easy access. Based on average household expenses, aim for $15,000-30,000. This provides financial security without needing to liquidate investments during emergencies.",
        "gold": "Gold can be a good hedge against inflation and economic uncertainty. Currently, with market volatility, allocating 5-10% of your portfolio to precious metals could be beneficial. Consider gold ETFs for easier liquidity, or physical gold for long-term wealth preservation.",
        "risk": "To reduce investment risk: 1) Diversify across asset classes and sectors, 2) Use dollar-cost averaging for regular investments, 3) Rebalance quarterly, 4) Avoid emotional trading, 5) Keep some cash reserves, and 6) Consider your time horizon - longer periods allow for more aggressive growth strategies.",
        "stocks": "For 2024, focus on companies with strong fundamentals: technology leaders (AI, cloud computing), healthcare innovators, renewable energy companies, and emerging market leaders. Consider ETFs for broader exposure: QQQ for tech, VTI for total market, or VXUS for international diversification.",
        "default": "Based on your question about \"{question}\", I'd recommend focusing on your long-term financial goals. Consider factors like your risk tolerance, time horizon, and current financial situation. Would you like me to analyze your specific portfolio or provide more targeted advice? Feel free to share more details about your financial situation."
      },
      es: {
        "diversify": "La diversificación del portafolio es crucial para gestionar el riesgo. Recomiendo distribuir las inversiones en diferentes clases de activos: 60% acciones (mezcla doméstica e internacional), 20% bonos, 10% bienes raíces (REITs), y 10% inversiones alternativas como materias primas u oro. Esto ayuda a proteger contra la volatilidad del mercado.",
        "emergency": "Un buen fondo de emergencia debe cubrir 3-6 meses de gastos de vida. Manténgalo en una cuenta de ahorros de alto rendimiento para fácil acceso. Basado en gastos domésticos promedio, apunte a $15,000-30,000. Esto proporciona seguridad financiera sin necesidad de liquidar inversiones durante emergencias.",
        "gold": "El oro puede ser una buena cobertura contra la inflación e incertidumbre económica. Actualmente, con la volatilidad del mercado, asignar 5-10% de su portafolio a metales preciosos podría ser beneficioso. Considere ETFs de oro para mayor liquidez, o oro físico para preservación de riqueza a largo plazo.",
        "risk": "Para reducir el riesgo de inversión: 1) Diversifique entre clases de activos y sectores, 2) Use promedio de costo en dólares para inversiones regulares, 3) Rebalancee trimestralmente, 4) Evite el trading emocional, 5) Mantenga algunas reservas en efectivo, y 6) Considere su horizonte temporal - períodos más largos permiten estrategias de crecimiento más agresivas.",
        "stocks": "Para 2024, enfóquese en empresas con fundamentos sólidos: líderes tecnológicos (IA, computación en la nube), innovadores de salud, empresas de energía renovable, y líderes de mercados emergentes. Considere ETFs para mayor exposición: QQQ para tecnología, VTI para mercado total, o VXUS para diversificación internacional.",
        "default": "Basado en su pregunta sobre \"{question}\", recomendaría enfocarse en sus objetivos financieros a largo plazo. Considere factores como su tolerancia al riesgo, horizonte temporal y situación financiera actual. ¿Le gustaría que analice su portafolio específico o proporcione consejos más dirigidos? Siéntase libre de compartir más detalles sobre su situación financiera."
      },
      fr: {
        "diversify": "La diversification du portefeuille est cruciale pour gérer le risque. Je recommande de répartir les investissements sur différentes classes d'actifs : 60% d'actions (mélange domestique et international), 20% d'obligations, 10% d'immobilier (REITs), et 10% d'investissements alternatifs comme les matières premières ou l'or. Cela aide à protéger contre la volatilité du marché.",
        "emergency": "Un bon fonds d'urgence devrait couvrir 3-6 mois de dépenses de vie. Gardez-le dans un compte d'épargne à haut rendement pour un accès facile. Basé sur les dépenses moyennes des ménages, visez 15 000-30 000 $. Cela fournit une sécurité financière sans avoir besoin de liquider des investissements pendant les urgences.",
        "gold": "L'or peut être une bonne couverture contre l'inflation et l'incertitude économique. Actuellement, avec la volatilité du marché, allouer 5-10% de votre portefeuille aux métaux précieux pourrait être bénéfique. Considérez les ETF d'or pour une liquidité plus facile, ou l'or physique pour la préservation de richesse à long terme.",
        "risk": "Pour réduire le risque d'investissement : 1) Diversifiez entre les classes d'actifs et secteurs, 2) Utilisez la moyenne des coûts en dollars pour les investissements réguliers, 3) Rééquilibrez trimestriellement, 4) Évitez le trading émotionnel, 5) Gardez quelques réserves en espèces, et 6) Considérez votre horizon temporel - des périodes plus longues permettent des stratégies de croissance plus agressives.",
        "stocks": "Pour 2024, concentrez-vous sur les entreprises avec des fondamentaux solides : leaders technologiques (IA, informatique en nuage), innovateurs de santé, entreprises d'énergie renouvelable, et leaders des marchés émergents. Considérez les ETF pour une exposition plus large : QQQ pour la technologie, VTI pour le marché total, ou VXUS pour la diversification internationale.",
        "default": "Basé sur votre question à propos de \"{question}\", je recommanderais de vous concentrer sur vos objectifs financiers à long terme. Considérez des facteurs comme votre tolérance au risque, horizon temporel, et situation financière actuelle. Aimeriez-vous que j'analyse votre portefeuille spécifique ou fournisse des conseils plus ciblés ? N'hésitez pas à partager plus de détails sur votre situation financière."
      }
    };

    return responses[language as keyof typeof responses] || responses.en;
  };

  const generateAIResponse = async (userMessage: string, messageLanguage: string): Promise<string> => {
    const responses = getLocalizedResponses(messageLanguage);
    
    const lowerMessage = userMessage.toLowerCase();
    for (const [key, response] of Object.entries(responses)) {
      if (key !== 'default' && lowerMessage.includes(key)) {
        return response;
      }
    }

    return responses.default.replace('{question}', userMessage);
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

    // Detect language from the first user message to set conversation language
    const detectedLanguage = detectLanguage(inputMessage);
    if (messages.length === 1) { // Only initial AI message exists
      setConversationLanguage(detectedLanguage);
    }

    const userMessage: Message = {
      id: Date.now().toString(),
      content: inputMessage,
      sender: 'user',
      timestamp: new Date(),
      language: conversationLanguage
    };

    setMessages(prev => [...prev, userMessage]);
    setInputMessage("");
    setIsLoading(true);

    try {
      const aiResponse = await generateAIResponse(inputMessage, conversationLanguage);
      
      const aiMessage: Message = {
        id: (Date.now() + 1).toString(),
        content: aiResponse,
        sender: 'ai',
        timestamp: new Date(),
        language: conversationLanguage
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

  const handlePlayMessage = (messageId: string, content: string) => {
    if (currentSpeakingMessage === messageId) {
      voiceForOutput.stopSpeaking();
      setCurrentSpeakingMessage(null);
    } else {
      if (currentSpeakingMessage) {
        voiceForOutput.stopSpeaking();
      }
      setCurrentSpeakingMessage(messageId);
      voiceForOutput.speak(content);
      
      // Reset speaking state when speech ends
      setTimeout(() => {
        setCurrentSpeakingMessage(null);
      }, (content.length * 100)); // Rough estimate of speech duration
    }
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
          {conversationLanguage !== 'en' && (
            <p className="text-sm text-muted-foreground mt-2">
              Conversation language: {conversationLanguage.toUpperCase()}
            </p>
          )}
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
                  {voiceForInput.isSupported && (
                    <div className="ml-auto flex items-center space-x-2">
                      <span className="text-sm text-muted-foreground">Voice input:</span>
                      <Button
                        variant={voiceForInput.isListening ? "default" : "outline"}
                        size="sm"
                        onClick={voiceForInput.toggleListening}
                        className={voiceForInput.isListening ? "bg-red-500 hover:bg-red-600" : ""}
                      >
                        {voiceForInput.isListening ? (
                          <MicOff className="h-4 w-4" />
                        ) : (
                          <Mic className="h-4 w-4" />
                        )}
                      </Button>
                    </div>
                  )}
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
                            <div className="flex-1">
                              <p className="text-sm">{message.content}</p>
                              <div className="flex items-center justify-between mt-1">
                                <p className={`text-xs ${
                                  message.sender === 'user' ? 'text-primary-foreground/70' : 'text-muted-foreground'
                                }`}>
                                  {message.timestamp.toLocaleTimeString()}
                                </p>
                                {voiceForOutput.isSpeechSupported && (
                                  <Button
                                    variant="ghost"
                                    size="sm"
                                    className={`h-6 px-2 ml-2 ${
                                      message.sender === 'user' 
                                        ? 'hover:bg-primary-foreground/20' 
                                        : 'hover:bg-muted-foreground/20'
                                    }`}
                                    onClick={() => handlePlayMessage(message.id, message.content)}
                                  >
                                    {currentSpeakingMessage === message.id ? (
                                      <VolumeX className="h-3 w-3" />
                                    ) : (
                                      <Volume2 className="h-3 w-3" />
                                    )}
                                  </Button>
                                )}
                              </div>
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
                      placeholder="Ask about investments, budgeting, or financial planning... (or use voice input)"
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
                  {voiceForInput.isListening && (
                    <p className="text-sm text-muted-foreground mt-2 animate-pulse">
                      🎤 Listening... Speak now or click the microphone to stop.
                    </p>
                  )}
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
                <CardTitle>Voice Features</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3 text-sm">
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">🎤 Voice Input</h4>
                  <p className="text-muted-foreground">
                    {voiceForInput.isSupported 
                      ? "Click the microphone to speak your question"
                      : "Voice input not supported in this browser"
                    }
                  </p>
                </div>
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">🔊 Audio Playback</h4>
                  <p className="text-muted-foreground">
                    {voiceForOutput.isSpeechSupported
                      ? "Click the speaker icon next to any message to hear it"
                      : "Audio playback not supported in this browser"
                    }
                  </p>
                </div>
                <div className="p-3 bg-gradient-chart rounded-lg">
                  <h4 className="font-semibold mb-1">🌍 Multi-language</h4>
                  <p className="text-muted-foreground">
                    Conversation continues in the language you start with
                  </p>
                </div>
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