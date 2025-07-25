import { useState, useEffect, useCallback, useMemo } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, AreaChart, Area } from 'recharts';
import { TrendingUp, TrendingDown, DollarSign, Search, Calendar, ExternalLink, MessageSquare, BookOpen, Bell, Star, Settings, AlertCircle, Loader2 } from "lucide-react";
import { Link } from "react-router-dom";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";
import { UserMenu } from "@/components/UserMenu";
import { useTranslations } from "@/hooks/useTranslations";
import { format } from "date-fns";
import { Alert, AlertDescription } from "@/components/ui/alert";

interface StockData {
  symbol: string;
  name: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
  marketCap?: number;
  high52Week?: number;
  low52Week?: number;
  pe?: number;
  lastUpdated: string;
  previousClose: number;
  open: number;
  high: number;
  low: number;
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
  relevanceScore?: number;
}

interface ApiKeyConfig {
  apiKey: string;
  isConfigured: boolean;
}

const Stocks = () => {
  const { currentLanguage, setCurrentLanguage, t } = useTranslations();
  const [selectedStock, setSelectedStock] = useState<string>("AAPL");
  const [timeframe, setTimeframe] = useState<string>("1D");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [watchlist, setWatchlist] = useState<string[]>(["AAPL", "GOOGL", "MSFT", "TSLA"]);
  const [apiConfig, setApiConfig] = useState<ApiKeyConfig>({ apiKey: "", isConfigured: false });
  const [showApiKeyInput, setShowApiKeyInput] = useState(false);
  const [stocksData, setStocksData] = useState<Record<string, StockData>>({});
  const [chartData, setChartData] = useState<ChartData[]>([]);
  const [newsData, setNewsData] = useState<NewsItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [chartLoading, setChartLoading] = useState(false);
  const [newsLoading, setNewsLoading] = useState(false);

  // Load API key from localStorage on component mount
  useEffect(() => {
    const savedApiKey = localStorage.getItem('alphaVantageApiKey');
    if (savedApiKey) {
      setApiConfig({ apiKey: savedApiKey, isConfigured: true });
    } else {
      setShowApiKeyInput(true);
    }
  }, []);

  const handleApiKeySubmit = useCallback((key: string) => {
    if (key.trim()) {
      localStorage.setItem('alphaVantageApiKey', key.trim());
      setApiConfig({ apiKey: key.trim(), isConfigured: true });
      setShowApiKeyInput(false);
      setError(null);
    }
  }, []);

  // Memoize mock news data generator to prevent recreation
  const getMockNewsData = useMemo(() => (symbol: string): NewsItem[] => [
    {
      id: "1",
      title: `${symbol} Reports Strong Quarterly Earnings`,
      summary: `${symbol} reported better-than-expected quarterly results with strong revenue growth.`,
      source: "Financial News",
      publishedAt: new Date().toISOString(),
      url: "#",
      sentiment: "positive"
    },
    {
      id: "2",
      title: "Market Volatility Affects Tech Stocks",
      summary: "Recent market movements have impacted technology sector performance.",
      source: "Market Watch",
      publishedAt: new Date().toISOString(),
      url: "#",
      sentiment: "neutral"
    }
  ], []);

  // Fix generateMockChartData to not depend on stocksData directly
  const generateMockChartData = useCallback((symbol: string, timeframe: string, basePrice?: number) => {
    const currentPrice = basePrice || 150;
    const days = timeframe === "1D" ? 78 : timeframe === "1W" ? 7 : timeframe === "1M" ? 30 : 52;
    
    const data: ChartData[] = [];
    for (let i = days; i >= 0; i--) {
      const date = new Date();
      if (timeframe === "1D") {
        date.setMinutes(date.getMinutes() - (i * 5));
      } else if (timeframe === "1W") {
        date.setDate(date.getDate() - i);
      } else {
        date.setDate(date.getDate() - i);
      }
      
      const variation = (Math.random() - 0.5) * 5;
      const price = currentPrice + variation - (i * 0.01);
      
      data.push({
        date: timeframe === "1D" ? format(date, 'HH:mm') : format(date, 'MMM dd'),
        price: Math.max(price, currentPrice * 0.9),
        volume: Math.floor(Math.random() * 10000000) + 1000000
      });
    }
    setChartData(data);
  }, []);



  // Fetch stock data when API key is configured or selected stock changes
  useEffect(() => {
    if (!apiConfig.isConfigured || !selectedStock) return;

    const fetchData = async () => {
      if (!apiConfig.apiKey) return;
      
      // Fetch stock data
      setLoading(true);
      setError(null);
      
      try {
        // Fetch global quote
        const quoteResponse = await fetch(
          `https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${selectedStock}&apikey=${apiConfig.apiKey}`
        );
        const quoteData = await quoteResponse.json();

        // Check for API errors
        if (quoteData['Error Message']) {
          throw new Error('Invalid stock symbol or API limit exceeded');
        }
        
        if (quoteData['Note']) {
          throw new Error('API rate limit exceeded. Please wait a moment and try again.');
        }

        const quote = quoteData['Global Quote'];
        if (!quote) {
          throw new Error('No data available for this symbol');
        }

        // Fetch company overview for additional data
        const overviewResponse = await fetch(
          `https://www.alphavantage.co/query?function=OVERVIEW&symbol=${selectedStock}&apikey=${apiConfig.apiKey}`
        );
        const overviewData = await overviewResponse.json();

        const price = parseFloat(quote['05. price']) || 0;
        const previousClose = parseFloat(quote['08. previous close']) || 0;
        const change = parseFloat(quote['09. change']) || 0;
        const changePercent = parseFloat(quote['10. change percent']?.replace('%', '')) || 0;

        const stockData: StockData = {
          symbol: quote['01. symbol'] || selectedStock,
          name: overviewData['Name'] || selectedStock,
          price: price,
          change: change,
          changePercent: changePercent,
          volume: parseInt(quote['06. volume']) || 0,
          marketCap: overviewData['MarketCapitalization'] ? parseInt(overviewData['MarketCapitalization']) : undefined,
          high52Week: overviewData['52WeekHigh'] ? parseFloat(overviewData['52WeekHigh']) : undefined,
          low52Week: overviewData['52WeekLow'] ? parseFloat(overviewData['52WeekLow']) : undefined,
          pe: overviewData['PERatio'] ? parseFloat(overviewData['PERatio']) : undefined,
          lastUpdated: quote['07. latest trading day'] || new Date().toISOString(),
          previousClose: previousClose,
          open: parseFloat(quote['02. open']) || 0,
          high: parseFloat(quote['03. high']) || 0,
          low: parseFloat(quote['04. low']) || 0,
        };

        setStocksData(prev => ({ ...prev, [selectedStock]: stockData }));
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to fetch stock data');
      } finally {
        setLoading(false);
      }

      // Fetch chart data
      setChartLoading(true);
      
      try {
        let apiFunction = 'TIME_SERIES_DAILY';
        let outputSize = 'compact';
        
        if (timeframe === '1W' || timeframe === '1M') {
          apiFunction = 'TIME_SERIES_DAILY';
          outputSize = 'compact';
        } else if (timeframe === '1Y') {
          apiFunction = 'TIME_SERIES_WEEKLY';
          outputSize = 'full';
        } else if (timeframe === '1D') {
          apiFunction = 'TIME_SERIES_INTRADAY';
        }

        let url = '';
        if (apiFunction === 'TIME_SERIES_INTRADAY') {
          url = `https://www.alphavantage.co/query?function=${apiFunction}&symbol=${selectedStock}&interval=5min&apikey=${apiConfig.apiKey}`;
        } else {
          url = `https://www.alphavantage.co/query?function=${apiFunction}&symbol=${selectedStock}&outputsize=${outputSize}&apikey=${apiConfig.apiKey}`;
        }

        const response = await fetch(url);
        const data = await response.json();

        if (data['Error Message'] || data['Note']) {
          generateMockChartData(selectedStock, timeframe, 150);
          setChartLoading(false);
          return;
        }

        let timeSeries: any = {};
        if (apiFunction === 'TIME_SERIES_INTRADAY') {
          timeSeries = data['Time Series (5min)'] || {};
        } else if (apiFunction === 'TIME_SERIES_DAILY') {
          timeSeries = data['Time Series (Daily)'] || {};
        } else if (apiFunction === 'TIME_SERIES_WEEKLY') {
          timeSeries = data['Weekly Time Series'] || {};
        }

        const chartPoints: ChartData[] = [];
        const dates = Object.keys(timeSeries).sort();
        
        let filteredDates = dates;
        if (timeframe === '1W') {
          filteredDates = dates.slice(-7);
        } else if (timeframe === '1M') {
          filteredDates = dates.slice(-30);
        } else if (timeframe === '1D') {
          filteredDates = dates.slice(-78);
        }

        filteredDates.forEach(date => {
          const dayData = timeSeries[date];
          chartPoints.push({
            date: timeframe === '1D' ? format(new Date(date), 'HH:mm') : format(new Date(date), 'MMM dd'),
            price: parseFloat(dayData['4. close']),
            volume: parseInt(dayData['5. volume']) || 0
          });
        });

        setChartData(chartPoints);
      } catch (err) {
        generateMockChartData(selectedStock, timeframe, 150);
      } finally {
        setChartLoading(false);
      }

      // Fetch news data
      setNewsLoading(true);
      
      try {
        const response = await fetch(
          `https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers=${selectedStock}&apikey=${apiConfig.apiKey}`
        );
        const data = await response.json();

        if (data['Error Message'] || data['Note']) {
          setNewsData(getMockNewsData(selectedStock));
          setNewsLoading(false);
          return;
        }

        const newsItems: NewsItem[] = (data.feed || []).slice(0, 5).map((item: any, index: number) => ({
          id: index.toString(),
          title: item.title,
          summary: item.summary,
          source: item.source,
          publishedAt: item.time_published,
          url: item.url,
          sentiment: item.overall_sentiment_label?.toLowerCase() || 'neutral',
          relevanceScore: item.relevance_score
        }));

        setNewsData(newsItems);
      } catch (err) {
        setNewsData(getMockNewsData(selectedStock));
      } finally {
        setNewsLoading(false);
      }
    };

    fetchData();
  }, [apiConfig.isConfigured, selectedStock, timeframe, apiConfig.apiKey, generateMockChartData, getMockNewsData]);

  // Removed the duplicate useEffect for chart data since timeframe is already handled above

  const currentStock = stocksData[selectedStock];
  const isPositive = currentStock?.change >= 0;

  const formatNumber = (num: number): string => {
    if (num >= 1e12) return `$${(num / 1e12).toFixed(1)}T`;
    if (num >= 1e9) return `$${(num / 1e9).toFixed(1)}B`;
    if (num >= 1e6) return `$${(num / 1e6).toFixed(1)}M`;
    return `$${num.toLocaleString()}`;
  };

  const toggleWatchlist = useCallback((symbol: string) => {
    setWatchlist(prev => 
      prev.includes(symbol) 
        ? prev.filter(s => s !== symbol)
        : [...prev, symbol]
    );
  }, []);

  const addToWatchlist = useCallback(() => {
    if (searchQuery.trim() && !watchlist.includes(searchQuery.toUpperCase())) {
      const symbol = searchQuery.toUpperCase();
      setWatchlist(prev => [...prev, symbol]);
      setSelectedStock(symbol);
      setSearchQuery("");
    }
  }, [searchQuery, watchlist]);

  // API Key Input Component
  if (showApiKeyInput || !apiConfig.isConfigured) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center p-6">
        <Card className="w-full max-w-md">
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Settings className="h-5 w-5" />
              Alpha Vantage API Configuration
            </CardTitle>
            <CardDescription>
              Enter your Alpha Vantage API key to access live stock data
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <Input
                type="password"
                placeholder="Enter your Alpha Vantage API key"
                value={apiConfig.apiKey}
                onChange={(e) => setApiConfig(prev => ({ ...prev, apiKey: e.target.value }))}
                onKeyPress={(e) => e.key === 'Enter' && handleApiKeySubmit(apiConfig.apiKey)}
              />
            </div>
            <Button 
              onClick={() => handleApiKeySubmit(apiConfig.apiKey)} 
              className="w-full"
              disabled={!apiConfig.apiKey.trim()}
            >
              Configure API Key
            </Button>
            <div className="text-sm text-muted-foreground space-y-2">
              <p>Don't have an API key? Get one free at:</p>
              <a 
                href="https://www.alphavantage.co/support/#api-key" 
                target="_blank" 
                rel="noopener noreferrer"
                className="text-primary hover:underline"
              >
                alphavantage.co/support/#api-key
              </a>
              <p className="text-xs">Free tier: 25 requests per day</p>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

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
            <Button
              variant="ghost"
              size="sm"
              onClick={() => setShowApiKeyInput(true)}
            >
              <Settings className="h-4 w-4" />
            </Button>
            <UserMenu />
          </div>
        </div>
      </nav>

      <div className="container mx-auto p-6">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-2">Stock Market</h1>
          <p className="text-muted-foreground text-lg">
            Live stock data powered by Alpha Vantage
          </p>
        </div>

        {/* Error Alert */}
        {error && (
          <Alert className="mb-6" variant="destructive">
            <AlertCircle className="h-4 w-4" />
            <AlertDescription>{error}</AlertDescription>
          </Alert>
        )}

        {/* Search and Stock Selection */}
        <div className="mb-6 flex flex-col md:flex-row gap-4">
          <div className="flex-1 flex gap-2">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                placeholder="Enter stock symbol (e.g., AAPL, GOOGL, MSFT...)"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && addToWatchlist()}
                className="pl-10"
              />
            </div>
            <Button onClick={addToWatchlist} disabled={!searchQuery.trim()}>
              Add
            </Button>
          </div>
          <Select value={selectedStock} onValueChange={setSelectedStock}>
            <SelectTrigger className="w-full md:w-48">
              <SelectValue placeholder="Select a stock" />
            </SelectTrigger>
            <SelectContent>
              {watchlist.map(symbol => (
                <SelectItem key={symbol} value={symbol}>
                  {symbol} {stocksData[symbol] ? `- ${stocksData[symbol].name}` : ''}
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
                const isPos = stock?.change >= 0;
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
                        <div className="text-sm text-muted-foreground truncate">
                          {stock?.name || 'Loading...'}
                        </div>
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
                    {stock ? (
                      <>
                        <div className="text-xl font-bold">${stock.price.toFixed(2)}</div>
                        <div className={`text-sm flex items-center ${isPos ? 'text-green-600' : 'text-red-600'}`}>
                          {isPos ? <TrendingUp className="h-3 w-3 mr-1" /> : <TrendingDown className="h-3 w-3 mr-1" />}
                          ${Math.abs(stock.change).toFixed(2)} ({Math.abs(stock.changePercent).toFixed(2)}%)
                        </div>
                      </>
                    ) : (
                      <div className="flex items-center text-muted-foreground">
                        <Loader2 className="h-4 w-4 animate-spin mr-2" />
                        Loading...
                      </div>
                    )}
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
                    <CardTitle className="text-2xl flex items-center gap-2">
                      {currentStock.symbol}
                      {loading && <Loader2 className="h-5 w-5 animate-spin" />}
                    </CardTitle>
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
                      disabled={chartLoading}
                    >
                      {period}
                    </Button>
                  ))}
                </div>

                {/* Chart */}
                <div className="h-64 w-full">
                  {chartLoading ? (
                    <div className="flex items-center justify-center h-full">
                      <Loader2 className="h-8 w-8 animate-spin" />
                    </div>
                  ) : (
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
                          labelFormatter={(label) => `${timeframe === '1D' ? 'Time' : 'Date'}: ${label}`}
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
                  )}
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
                  <span className="text-muted-foreground">Previous Close</span>
                  <span className="font-semibold">${currentStock.previousClose.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Open</span>
                  <span className="font-semibold">${currentStock.open.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Day Range</span>
                  <span className="font-semibold">${currentStock.low.toFixed(2)} - ${currentStock.high.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Volume</span>
                  <span className="font-semibold">{currentStock.volume.toLocaleString()}</span>
                </div>
                {currentStock.marketCap && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">Market Cap</span>
                    <span className="font-semibold">{formatNumber(currentStock.marketCap)}</span>
                  </div>
                )}
                {currentStock.pe && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">P/E Ratio</span>
                    <span className="font-semibold">{currentStock.pe}</span>
                  </div>
                )}
                {currentStock.high52Week && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">52W High</span>
                    <span className="font-semibold">${currentStock.high52Week.toFixed(2)}</span>
                  </div>
                )}
                {currentStock.low52Week && (
                  <div className="flex justify-between">
                    <span className="text-muted-foreground">52W Low</span>
                    <span className="font-semibold">${currentStock.low52Week.toFixed(2)}</span>
                  </div>
                )}
                <div className="pt-4 border-t">
                  <div className="text-xs text-muted-foreground">
                    Last updated: {format(new Date(currentStock.lastUpdated), 'MMM dd, yyyy')}
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
              {newsLoading && <Loader2 className="h-4 w-4 animate-spin" />}
            </CardTitle>
            <CardDescription>Latest news for {currentStock?.symbol || 'the market'}</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {newsData.length > 0 ? (
                newsData.map(news => (
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
                      <Button variant="ghost" size="sm" asChild>
                        <a href={news.url} target="_blank" rel="noopener noreferrer">
                          <ExternalLink className="h-4 w-4" />
                        </a>
                      </Button>
                    </div>
                  </div>
                ))
              ) : (
                <div className="text-center text-muted-foreground py-8">
                  No news available. This may be due to API rate limits.
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default Stocks;