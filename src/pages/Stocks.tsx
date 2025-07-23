import { useState, useEffect } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts';
import { TrendingUp, TrendingDown, DollarSign, Search, Calendar, ExternalLink, MessageSquare, BookOpen, Bell, Star } from "lucide-react";
import { Link } from "react-router-dom";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";
import { useTranslations } from "@/hooks/useTranslations";
import { format } from "date-fns";

interface StockData {
  symbol: string;
  name: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
  marketCap: number;
  high52Week: number;
  low52Week: number;
  pe: number;
  lastUpdated: string;
}

interface ChartData {
  date: string;
  price: number;
  volume: number;
}

interface NewsItem {
  id: string;
  title: string;
  summary: string;
  source: string;
  publishedAt: string;
  url: string;
  sentiment: 'positive' | 'negative' | 'neutral';
}

const Stocks = () => {
  const { currentLanguage, setCurrentLanguage, t } = useTranslations();
  const [selectedStock, setSelectedStock] = useState<string>("AAPL");
  const [timeframe, setTimeframe] = useState<string>("1D");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [watchlist, setWatchlist] = useState<string[]>(["AAPL", "GOOGL", "MSFT", "TSLA"]);

  // Mock data - in a real app, this would come from APIs like Alpha Vantage, Finnhub, etc.
  const stocksData: Record<string, StockData> = {
    AAPL: {
      symbol: "AAPL",
      name: "Apple Inc.",
      price: 192.53,
      change: 2.47,
      changePercent: 1.3,
      volume: 45672893,
      marketCap: 2980000000000,
      high52Week: 199.62,
      low52Week: 164.08,
      pe: 29.4,
      lastUpdated: new Date().toISOString()
    },
    GOOGL: {
      symbol: "GOOGL",
      name: "Alphabet Inc.",
      price: 138.21,
      change: -0.85,
      changePercent: -0.61,
      volume: 28493021,
      marketCap: 1750000000000,
      high52Week: 153.78,
      low52Week: 102.21,
      pe: 25.8,
      lastUpdated: new Date().toISOString()
    },
    MSFT: {
      symbol: "MSFT", 
      name: "Microsoft Corporation",
      price: 418.34,
      change: 5.21,
      changePercent: 1.26,
      volume: 19847392,
      marketCap: 3100000000000,
      high52Week: 468.35,
      low52Week: 362.90,
      pe: 34.2,
      lastUpdated: new Date().toISOString()
    },
    TSLA: {
      symbol: "TSLA",
      name: "Tesla, Inc.",
      price: 248.98,
      change: -3.42,
      changePercent: -1.35,
      volume: 67283901,
      marketCap: 790000000000,
      high52Week: 278.98,
      low52Week: 152.37,
      pe: 67.3,
      lastUpdated: new Date().toISOString()
    }
  };

  // Mock chart data
  const generateChartData = (symbol: string, days: number): ChartData[] => {
    const data: ChartData[] = [];
    const basePrice = stocksData[symbol]?.price || 100;
    
    for (let i = days; i >= 0; i--) {
      const date = new Date();
      date.setDate(date.getDate() - i);
      const variation = (Math.random() - 0.5) * 10;
      const price = basePrice + variation - (i * 0.1);
      
      data.push({
        date: format(date, 'MMM dd'),
        price: Math.max(price, basePrice * 0.8),
        volume: Math.floor(Math.random() * 50000000) + 10000000
      });
    }
    return data;
  };

  const [chartData, setChartData] = useState<ChartData[]>([]);

  useEffect(() => {
    const days = timeframe === "1D" ? 1 : timeframe === "1W" ? 7 : timeframe === "1M" ? 30 : 365;
    setChartData(generateChartData(selectedStock, days));
  }, [selectedStock, timeframe]);

  // Mock news data
  const newsData: NewsItem[] = [
    {
      id: "1",
      title: "Apple Reports Strong Q4 Earnings, Beats Revenue Expectations",
      summary: "Apple Inc. reported better-than-expected quarterly revenue driven by strong iPhone sales and services growth.",
      source: "Financial Times",
      publishedAt: "2024-01-15T10:30:00Z",
      url: "#",
      sentiment: "positive"
    },
    {
      id: "2", 
      title: "Tech Stocks Rally on AI Optimism",
      summary: "Major technology stocks including Apple, Microsoft, and Google gained on renewed investor optimism about artificial intelligence applications.",
      source: "Bloomberg",
      publishedAt: "2024-01-15T08:45:00Z", 
      url: "#",
      sentiment: "positive"
    },
    {
      id: "3",
      title: "Market Volatility Expected as Fed Meeting Approaches",
      summary: "Analysts predict increased market volatility ahead of the Federal Reserve's upcoming interest rate decision.",
      source: "Reuters",
      publishedAt: "2024-01-14T16:20:00Z",
      url: "#",
      sentiment: "neutral"
    }
  ];

  const currentStock = stocksData[selectedStock];
  const isPositive = currentStock?.change >= 0;

  const formatNumber = (num: number): string => {
    if (num >= 1e12) return `$${(num / 1e12).toFixed(1)}T`;
    if (num >= 1e9) return `$${(num / 1e9).toFixed(1)}B`;
    if (num >= 1e6) return `$${(num / 1e6).toFixed(1)}M`;
    return `$${num.toLocaleString()}`;
  };

  const toggleWatchlist = (symbol: string) => {
    setWatchlist(prev => 
      prev.includes(symbol) 
        ? prev.filter(s => s !== symbol)
        : [...prev, symbol]
    );
  };

  return (
    <div className="min-h-screen bg-background">
      {/* Navigation */}
      <nav className="border-b bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center space-x-2">
            <TrendingUp className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">FinanceAI</span>
          </div>
          <div className="flex items-center space-x-4">
            <LanguageSwitcher 
              currentLanguage={currentLanguage}
              onLanguageChange={setCurrentLanguage}
            />
            <Link to="/dashboard">
              <Button variant="outline">
                <DollarSign className="mr-2 h-4 w-4" />
                {t('dashboard')}
              </Button>
            </Link>
            <Link to="/learning">
              <Button variant="outline">
                <BookOpen className="mr-2 h-4 w-4" />
                {t('learning')}
              </Button>
            </Link>
            <Link to="/chat">
              <Button variant="outline">
                <MessageSquare className="mr-2 h-4 w-4" />
                AI Advisor
              </Button>
            </Link>
          </div>
        </div>
      </nav>

      <div className="container mx-auto p-6">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-2">Stock Market</h1>
          <p className="text-muted-foreground text-lg">
            Live stock data, charts, and market insights
          </p>
        </div>

        {/* Search and Watchlist */}
        <div className="mb-6 flex flex-col md:flex-row gap-4">
          <div className="flex-1 relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
            <Input
              placeholder="Search stocks (AAPL, GOOGL, MSFT, TSLA...)"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="pl-10"
            />
          </div>
          <Select value={selectedStock} onValueChange={setSelectedStock}>
            <SelectTrigger className="w-full md:w-48">
              <SelectValue placeholder="Select a stock" />
            </SelectTrigger>
            <SelectContent>
              {Object.keys(stocksData).map(symbol => (
                <SelectItem key={symbol} value={symbol}>
                  {symbol} - {stocksData[symbol].name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {/* Watchlist */}
        <Card className="mb-6">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Star className="h-5 w-5" />
              Watchlist
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              {watchlist.map(symbol => {
                const stock = stocksData[symbol];
                if (!stock) return null;
                const isPos = stock.change >= 0;
                return (
                  <div 
                    key={symbol}
                    className={`p-4 rounded-lg border cursor-pointer transition-colors ${
                      selectedStock === symbol ? 'border-primary bg-primary/5' : 'border-border hover:border-primary/50'
                    }`}
                    onClick={() => setSelectedStock(symbol)}
                  >
                    <div className="flex justify-between items-start mb-2">
                      <div>
                        <div className="font-semibold">{symbol}</div>
                        <div className="text-sm text-muted-foreground truncate">{stock.name}</div>
                      </div>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation();
                          toggleWatchlist(symbol);
                        }}
                      >
                        <Star className="h-4 w-4 fill-primary" />
                      </Button>
                    </div>
                    <div className="text-xl font-bold">${stock.price.toFixed(2)}</div>
                    <div className={`text-sm flex items-center ${isPos ? 'text-green-600' : 'text-red-600'}`}>
                      {isPos ? <TrendingUp className="h-3 w-3 mr-1" /> : <TrendingDown className="h-3 w-3 mr-1" />}
                      ${Math.abs(stock.change).toFixed(2)} ({Math.abs(stock.changePercent).toFixed(2)}%)
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Main Stock Data */}
        {currentStock && (
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
            {/* Stock Info */}
            <Card className="lg:col-span-2">
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle className="text-2xl">{currentStock.symbol}</CardTitle>
                    <CardDescription className="text-lg">{currentStock.name}</CardDescription>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => toggleWatchlist(currentStock.symbol)}
                  >
                    <Star className={`h-4 w-4 ${watchlist.includes(currentStock.symbol) ? 'fill-primary' : ''}`} />
                  </Button>
                </div>
              </CardHeader>
              <CardContent>
                <div className="mb-6">
                  <div className="text-4xl font-bold mb-2">${currentStock.price.toFixed(2)}</div>
                  <div className={`text-xl flex items-center ${isPositive ? 'text-green-600' : 'text-red-600'}`}>
                    {isPositive ? <TrendingUp className="h-5 w-5 mr-2" /> : <TrendingDown className="h-5 w-5 mr-2" />}
                    ${Math.abs(currentStock.change).toFixed(2)} ({Math.abs(currentStock.changePercent).toFixed(2)}%)
                  </div>
                </div>

                {/* Chart Controls */}
                <div className="flex gap-2 mb-4">
                  {["1D", "1W", "1M", "1Y"].map(period => (
                    <Button
                      key={period}
                      variant={timeframe === period ? "default" : "outline"}
                      size="sm"
                      onClick={() => setTimeframe(period)}
                    >
                      {period}
                    </Button>
                  ))}
                </div>

                {/* Chart */}
                <div className="h-64 w-full">
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={chartData}>
                      <defs>
                        <linearGradient id="colorPrice" x1="0" y1="0" x2="0" y2="1">
                          <stop offset="5%" stopColor={isPositive ? "#10b981" : "#ef4444"} stopOpacity={0.3}/>
                          <stop offset="95%" stopColor={isPositive ? "#10b981" : "#ef4444"} stopOpacity={0}/>
                        </linearGradient>
                      </defs>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="date" />
                      <YAxis />
                      <Tooltip 
                        formatter={(value: number) => [`$${value.toFixed(2)}`, 'Price']}
                        labelFormatter={(label) => `Date: ${label}`}
                      />
                      <Area 
                        type="monotone" 
                        dataKey="price" 
                        stroke={isPositive ? "#10b981" : "#ef4444"}
                        fillOpacity={1}
                        fill="url(#colorPrice)"
                      />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
              </CardContent>
            </Card>

            {/* Stock Metrics */}
            <Card>
              <CardHeader>
                <CardTitle>Key Metrics</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Market Cap</span>
                  <span className="font-semibold">{formatNumber(currentStock.marketCap)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Volume</span>
                  <span className="font-semibold">{currentStock.volume.toLocaleString()}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">P/E Ratio</span>
                  <span className="font-semibold">{currentStock.pe}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">52W High</span>
                  <span className="font-semibold">${currentStock.high52Week.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">52W Low</span>
                  <span className="font-semibold">${currentStock.low52Week.toFixed(2)}</span>
                </div>
                <div className="pt-4 border-t">
                  <div className="text-xs text-muted-foreground">
                    Last updated: {format(new Date(currentStock.lastUpdated), 'MMM dd, HH:mm')}
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        )}

        {/* News Section */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Bell className="h-5 w-5" />
              Market News
            </CardTitle>
            <CardDescription>Latest news that may impact {currentStock?.symbol || 'the market'}</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {newsData.map(news => (
                <div key={news.id} className="border-b pb-4 last:border-b-0">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-semibold text-lg hover:text-primary cursor-pointer">
                      {news.title}
                    </h3>
                    <Badge 
                      variant={news.sentiment === 'positive' ? 'default' : news.sentiment === 'negative' ? 'destructive' : 'secondary'}
                    >
                      {news.sentiment}
                    </Badge>
                  </div>
                  <p className="text-muted-foreground mb-3">{news.summary}</p>
                  <div className="flex justify-between items-center text-sm text-muted-foreground">
                    <span>{news.source} • {format(new Date(news.publishedAt), 'MMM dd, HH:mm')}</span>
                    <Button variant="ghost" size="sm">
                      <ExternalLink className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Stocks;