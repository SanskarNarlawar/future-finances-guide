import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { TrendingUp, DollarSign, PieChart, MessageSquare, Shield, BarChart3 } from "lucide-react";
import { Link } from "react-router-dom";

const Index = () => {
  const features = [
    {
      icon: PieChart,
      title: "Portfolio Tracking",
      description: "Track your stocks, gold, cash, and other investments in one place"
    },
    {
      icon: MessageSquare,
      title: "AI Financial Advisor", 
      description: "Get personalized financial advice from our intelligent chatbot"
    },
    {
      icon: BarChart3,
      title: "Analytics & Insights",
      description: "Visualize your portfolio performance with advanced charts"
    },
    {
      icon: Shield,
      title: "Portfolio Health Check",
      description: "Assess your portfolio diversification and risk levels"
    }
  ];

  return (
    <div className="min-h-screen bg-gradient-hero">
      {/* Navigation */}
      <nav className="border-b bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center space-x-2">
            <TrendingUp className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">FinanceAI</span>
          </div>
          <div className="flex space-x-4">
            <Link to="/dashboard">
              <Button variant="outline">Dashboard</Button>
            </Link>
            <Link to="/learning">
              <Button variant="outline">Learning</Button>
            </Link>
            <Link to="/chat">
              <Button>AI Advisor</Button>
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="py-20 px-4">
        <div className="container mx-auto text-center">
          <div className="max-w-4xl mx-auto">
            <h1 className="text-5xl font-bold mb-6 bg-gradient-primary bg-clip-text text-transparent">
              Master Your Financial Future with AI
            </h1>
            <p className="text-xl text-muted-foreground mb-8 max-w-2xl mx-auto">
              Take control of your investments with intelligent portfolio tracking, 
              AI-powered financial advice, and comprehensive analytics.
            </p>
            <div className="flex justify-center space-x-4">
              <Link to="/dashboard">
                <Button size="lg" className="shadow-glow">
                  Get Started
                  <DollarSign className="ml-2 h-5 w-5" />
                </Button>
              </Link>
              <Link to="/chat">
                <Button variant="outline" size="lg">
                  Try AI Advisor
                  <MessageSquare className="ml-2 h-5 w-5" />
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16 px-4">
        <div className="container mx-auto">
          <h2 className="text-3xl font-bold text-center mb-12">
            Everything You Need for Financial Success
          </h2>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {features.map((feature, index) => (
              <Card key={index} className="shadow-card hover:shadow-elevated transition-all duration-300">
                <CardHeader>
                  <feature.icon className="h-12 w-12 text-primary mb-4" />
                  <CardTitle className="text-xl">{feature.title}</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardDescription className="text-base">
                    {feature.description}
                  </CardDescription>
                </CardContent>
              </Card>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 px-4 bg-gradient-primary text-primary-foreground">
        <div className="container mx-auto text-center">
          <h2 className="text-3xl font-bold mb-4">
            Ready to Transform Your Financial Life?
          </h2>
          <p className="text-xl mb-8 opacity-90">
            Join thousands of users who are already building wealth with FinanceAI
          </p>
          <Link to="/dashboard">
            <Button size="lg" variant="secondary" className="shadow-lg">
              Start Your Journey
            </Button>
          </Link>
        </div>
      </section>
    </div>
  );
};

export default Index;
