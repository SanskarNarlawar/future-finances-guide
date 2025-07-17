import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts';
import { TrendingUp, TrendingDown, DollarSign, Coins, Building, PiggyBank, Plus, MessageSquare } from "lucide-react";
import { Link } from "react-router-dom";

interface Asset {
  name: string;
  value: number;
  type: 'stocks' | 'gold' | 'cash' | 'crypto';
}

const Dashboard = () => {
  const [assets, setAssets] = useState<Asset[]>([
    { name: "Apple Stock", value: 15000, type: "stocks" },
    { name: "Gold Investment", value: 8000, type: "gold" },
    { name: "Emergency Fund", value: 5000, type: "cash" },
    { name: "Bitcoin", value: 3000, type: "crypto" }
  ]);

  const [newAsset, setNewAsset] = useState({ name: "", value: "", type: "stocks" as Asset['type'] });

  const totalValue = assets.reduce((sum, asset) => sum + asset.value, 0);

  const pieData = assets.map(asset => ({
    name: asset.name,
    value: asset.value,
    percentage: ((asset.value / totalValue) * 100).toFixed(1)
  }));

  const assetsByType = assets.reduce((acc, asset) => {
    const existing = acc.find(item => item.type === asset.type);
    if (existing) {
      existing.value += asset.value;
    } else {
      acc.push({ type: asset.type, value: asset.value });
    }
    return acc;
  }, [] as { type: string; value: number }[]);

  const performanceData = [
    { month: 'Jan', value: 25000 },
    { month: 'Feb', value: 27000 },
    { month: 'Mar', value: 26500 },
    { month: 'Apr', value: 29000 },
    { month: 'May', value: 31000 },
    { month: 'Jun', value: totalValue }
  ];

  const COLORS = {
    stocks: '#3B82F6',
    gold: '#F59E0B', 
    cash: '#10B981',
    crypto: '#8B5CF6'
  };

  const addAsset = () => {
    if (newAsset.name && newAsset.value) {
      setAssets([...assets, { 
        name: newAsset.name, 
        value: parseFloat(newAsset.value), 
        type: newAsset.type 
      }]);
      setNewAsset({ name: "", value: "", type: "stocks" });
    }
  };

  const getPortfolioHealth = () => {
    const diversificationScore = assetsByType.length * 25; // Max 100 for 4 types
    const cashRatio = (assetsByType.find(a => a.type === 'cash')?.value || 0) / totalValue;
    const emergencyScore = cashRatio >= 0.1 ? 100 : cashRatio * 1000; // 10% emergency fund
    
    const totalScore = Math.min(100, (diversificationScore + emergencyScore) / 2);
    return {
      score: Math.round(totalScore),
      status: totalScore >= 70 ? 'Excellent' : totalScore >= 50 ? 'Good' : 'Needs Improvement'
    };
  };

  const health = getPortfolioHealth();

  return (
    <div className="min-h-screen bg-gradient-hero">
      {/* Navigation */}
      <nav className="border-b bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <Link to="/" className="flex items-center space-x-2">
            <TrendingUp className="h-8 w-8 text-primary" />
            <span className="text-2xl font-bold text-foreground">FinanceAI</span>
          </Link>
          <div className="flex space-x-4">
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
          <h1 className="text-4xl font-bold mb-2">Financial Dashboard</h1>
          <p className="text-muted-foreground text-lg">
            Track your investments and get AI-powered insights
          </p>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
          <Card className="shadow-card">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Total Portfolio</CardTitle>
              <DollarSign className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">${totalValue.toLocaleString()}</div>
              <p className="text-xs text-success flex items-center">
                <TrendingUp className="h-3 w-3 mr-1" />
                +12.5% from last month
              </p>
            </CardContent>
          </Card>

          <Card className="shadow-card">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Assets Count</CardTitle>
              <Building className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{assets.length}</div>
              <p className="text-xs text-muted-foreground">
                Across {assetsByType.length} categories
              </p>
            </CardContent>
          </Card>

          <Card className="shadow-card">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Portfolio Health</CardTitle>
              <PiggyBank className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{health.score}/100</div>
              <p className={`text-xs ${health.score >= 70 ? 'text-success' : health.score >= 50 ? 'text-warning' : 'text-destructive'}`}>
                {health.status}
              </p>
            </CardContent>
          </Card>

          <Card className="shadow-card">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Monthly Growth</CardTitle>
              <TrendingUp className="h-4 w-4 text-primary" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">+8.3%</div>
              <p className="text-xs text-success">
                Above market average
              </p>
            </CardContent>
          </Card>
        </div>

        <Tabs defaultValue="overview" className="space-y-6">
          <TabsList className="grid w-full grid-cols-3">
            <TabsTrigger value="overview">Overview</TabsTrigger>
            <TabsTrigger value="assets">Manage Assets</TabsTrigger>
            <TabsTrigger value="analytics">Analytics</TabsTrigger>
          </TabsList>

          <TabsContent value="overview" className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Portfolio Distribution */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>Portfolio Distribution</CardTitle>
                  <CardDescription>Your asset allocation breakdown</CardDescription>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                      <Pie
                        data={pieData}
                        cx="50%"
                        cy="50%"
                        innerRadius={60}
                        outerRadius={100}
                        dataKey="value"
                      >
                        {pieData.map((entry, index) => (
                          <Cell key={`cell-${index}`} fill={COLORS[assets[index]?.type] || '#8884d8'} />
                        ))}
                      </Pie>
                      <Tooltip formatter={(value: number) => [`$${value.toLocaleString()}`, 'Value']} />
                    </PieChart>
                  </ResponsiveContainer>
                  <div className="mt-4 space-y-2">
                    {pieData.map((entry, index) => (
                      <div key={entry.name} className="flex justify-between items-center">
                        <div className="flex items-center">
                          <div 
                            className="w-3 h-3 rounded mr-2" 
                            style={{ backgroundColor: COLORS[assets[index]?.type] || '#8884d8' }}
                          />
                          <span className="text-sm">{entry.name}</span>
                        </div>
                        <span className="text-sm font-medium">{entry.percentage}%</span>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>

              {/* Performance Chart */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>Portfolio Performance</CardTitle>
                  <CardDescription>6-month portfolio value trend</CardDescription>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <LineChart data={performanceData}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="month" />
                      <YAxis />
                      <Tooltip formatter={(value: number) => [`$${value.toLocaleString()}`, 'Portfolio Value']} />
                      <Line 
                        type="monotone" 
                        dataKey="value" 
                        stroke="#3B82F6" 
                        strokeWidth={3}
                        dot={{ fill: '#3B82F6', strokeWidth: 2, r: 4 }}
                      />
                    </LineChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>
            </div>
          </TabsContent>

          <TabsContent value="assets" className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Add New Asset */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>Add New Asset</CardTitle>
                  <CardDescription>Expand your portfolio tracking</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div>
                    <Label htmlFor="assetName">Asset Name</Label>
                    <Input
                      id="assetName"
                      value={newAsset.name}
                      onChange={(e) => setNewAsset({...newAsset, name: e.target.value})}
                      placeholder="e.g., Tesla Stock, Silver Coins"
                    />
                  </div>
                  <div>
                    <Label htmlFor="assetValue">Current Value ($)</Label>
                    <Input
                      id="assetValue"
                      type="number"
                      value={newAsset.value}
                      onChange={(e) => setNewAsset({...newAsset, value: e.target.value})}
                      placeholder="10000"
                    />
                  </div>
                  <div>
                    <Label htmlFor="assetType">Asset Type</Label>
                    <select 
                      className="w-full p-2 border rounded-md"
                      value={newAsset.type}
                      onChange={(e) => setNewAsset({...newAsset, type: e.target.value as Asset['type']})}
                    >
                      <option value="stocks">Stocks</option>
                      <option value="gold">Gold/Precious Metals</option>
                      <option value="cash">Cash/Savings</option>
                      <option value="crypto">Cryptocurrency</option>
                    </select>
                  </div>
                  <Button onClick={addAsset} className="w-full">
                    <Plus className="mr-2 h-4 w-4" />
                    Add Asset
                  </Button>
                </CardContent>
              </Card>

              {/* Current Assets */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>Your Assets</CardTitle>
                  <CardDescription>Currently tracked investments</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {assets.map((asset, index) => (
                      <div key={index} className="flex justify-between items-center p-3 border rounded-lg">
                        <div>
                          <p className="font-medium">{asset.name}</p>
                          <p className="text-sm text-muted-foreground capitalize">{asset.type}</p>
                        </div>
                        <div className="text-right">
                          <p className="font-bold">${asset.value.toLocaleString()}</p>
                          <p className="text-sm text-muted-foreground">
                            {((asset.value / totalValue) * 100).toFixed(1)}%
                          </p>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </div>
          </TabsContent>

          <TabsContent value="analytics" className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              {/* Asset Type Breakdown */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>Asset Category Analysis</CardTitle>
                  <CardDescription>Value distribution by asset type</CardDescription>
                </CardHeader>
                <CardContent>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={assetsByType}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="type" />
                      <YAxis />
                      <Tooltip formatter={(value: number) => [`$${value.toLocaleString()}`, 'Value']} />
                      <Bar dataKey="value" fill="#3B82F6" />
                    </BarChart>
                  </ResponsiveContainer>
                </CardContent>
              </Card>

              {/* Portfolio Insights */}
              <Card className="shadow-card">
                <CardHeader>
                  <CardTitle>AI Insights</CardTitle>
                  <CardDescription>Automated portfolio analysis</CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="p-4 border rounded-lg bg-gradient-chart">
                    <h4 className="font-semibold mb-2">Portfolio Health: {health.status}</h4>
                    <p className="text-sm text-muted-foreground mb-2">Score: {health.score}/100</p>
                    <div className="w-full bg-muted rounded-full h-2">
                      <div 
                        className="bg-gradient-primary h-2 rounded-full transition-all duration-300"
                        style={{ width: `${health.score}%` }}
                      />
                    </div>
                  </div>
                  
                  <div className="space-y-2">
                    <h4 className="font-semibold">Recommendations:</h4>
                    <ul className="text-sm space-y-1 text-muted-foreground">
                      <li>• Consider increasing your emergency fund to 10% of portfolio</li>
                      <li>• Your portfolio shows good diversification across asset types</li>
                      <li>• Consider taking profits on outperforming assets</li>
                      <li>• Review asset allocation quarterly for optimal balance</li>
                    </ul>
                  </div>

                  <Link to="/chat">
                    <Button className="w-full mt-4">
                      <MessageSquare className="mr-2 h-4 w-4" />
                      Get Detailed AI Analysis
                    </Button>
                  </Link>
                </CardContent>
              </Card>
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default Dashboard;