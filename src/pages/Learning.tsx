import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { TrendingUp, BookOpen, Video, Award, ArrowLeft } from "lucide-react";
import { Link } from "react-router-dom";
import { BlogCard, BlogPost } from "@/components/BlogCard";
import { BookCard, Book } from "@/components/BookCard";
import { VideoCard, Video as VideoType } from "@/components/VideoCard";
import { LanguageSwitcher } from "@/components/LanguageSwitcher";
import { useTranslations } from "@/hooks/useTranslations";
import { useToast } from "@/hooks/use-toast";

const Learning = () => {
  const { currentLanguage, setCurrentLanguage, t } = useTranslations();
  const { toast } = useToast();

  // Sample data - in a real app, this would come from an API
  const blogs: BlogPost[] = [
    {
      id: "1",
      title: "Understanding Market Volatility: A Beginner's Guide",
      excerpt: "Learn how market fluctuations affect your investments and strategies to navigate uncertain times with confidence.",
      author: "Sarah Johnson",
      date: "2024-01-15",
      readTime: 8,
      category: "Market Analysis",
      image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
      content: "Market volatility is one of the most challenging aspects of investing that every investor must understand and learn to navigate. Volatility refers to the degree of variation in trading prices over time, and it's a natural characteristic of financial markets.\n\nWhen markets are volatile, prices can swing dramatically in short periods. This can be unsettling for new investors, but understanding the causes and patterns of volatility can help you make more informed decisions about your investments.\n\nSeveral factors contribute to market volatility. Economic indicators such as inflation rates, employment data, and GDP growth can cause markets to react strongly. Political events, natural disasters, and global conflicts also create uncertainty that manifests as increased volatility.\n\nTo navigate volatile markets successfully, consider these strategies: maintain a diversified portfolio across different asset classes and sectors, avoid making emotional decisions based on short-term market movements, and stick to your long-term investment plan. Remember that volatility works in both directions – while it can create losses, it also presents opportunities for gains.\n\nDollar-cost averaging is another effective strategy during volatile periods. By investing a fixed amount regularly, you buy more shares when prices are low and fewer when prices are high, potentially reducing your average cost over time.\n\nFinally, remember that volatility is temporary, but time in the market is more important than timing the market. Successful long-term investors learn to view volatility as a normal part of the investment journey rather than something to fear."
    },
    {
      id: "2", 
      title: "The Power of Compound Interest in Long-term Investing",
      excerpt: "Discover how compound interest can transform modest investments into substantial wealth over time.",
      author: "Michael Chen",
      date: "2024-01-12",
      readTime: 12,
      category: "Investment Strategy", 
      image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
      content: "Albert Einstein allegedly called compound interest 'the eighth wonder of the world,' and for good reason. Compound interest is the process by which your investments generate earnings, and those earnings then generate their own earnings, creating a snowball effect that can dramatically increase your wealth over time.\n\nThe mathematics of compound interest might seem simple, but its long-term effects are profound. When you invest money, you earn returns not just on your initial investment (the principal), but also on all the returns that investment has generated over time. This means your money grows at an accelerating rate rather than a linear one.\n\nTo illustrate this power, consider investing $10,000 at an annual return of 7%. After one year, you'd have $10,700. But after 30 years, assuming you never added another penny, you'd have over $76,000. The initial $10,000 would have generated more than $66,000 in compound returns.\n\nThe key factors that maximize compound interest are time, rate of return, and frequency of compounding. Starting early is crucial because compound interest works best over long periods. Even small amounts invested in your twenties can grow to substantial sums by retirement.\n\nThe rate of return also matters significantly. A difference of just 2-3% in annual returns can result in hundreds of thousands of dollars difference over a 30-40 year investment horizon. This is why it's important to minimize fees and taxes that can erode your returns.\n\nTo harness compound interest effectively, start investing as early as possible, be consistent with your contributions, reinvest your dividends and interest payments, and be patient. Avoid the temptation to withdraw funds early, as this breaks the compounding cycle and significantly reduces long-term growth potential."
    },
    {
      id: "3",
      title: "Diversification: Building a Balanced Portfolio",
      excerpt: "Learn the fundamentals of portfolio diversification and how to spread risk across different asset classes.",
      author: "Emma Davis",
      date: "2024-01-10",
      readTime: 10,
      category: "Portfolio Management",
      image: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
      content: "Diversification is often called the only free lunch in investing, and understanding this concept is crucial for building a successful long-term investment strategy. At its core, diversification means spreading your investments across various assets to reduce overall portfolio risk without necessarily sacrificing returns.\n\nThe principle behind diversification is that different investments often perform differently under various market conditions. When some investments in your portfolio are declining in value, others may be stable or even increasing, helping to smooth out overall portfolio performance and reduce volatility.\n\nThere are several types of diversification to consider. Asset class diversification involves spreading investments across stocks, bonds, real estate, commodities, and cash. Geographic diversification means investing in both domestic and international markets. Sector diversification involves investing across different industries like technology, healthcare, finance, and consumer goods.\n\nWithin the stock portion of your portfolio, you should also diversify by company size (large-cap, mid-cap, small-cap stocks) and investment style (growth vs. value stocks). This helps ensure you're not overly dependent on any single market segment's performance.\n\nBond diversification is equally important. Consider mixing government bonds, corporate bonds, and international bonds with different maturities and credit qualities. This helps protect against interest rate risk and credit risk.\n\nHowever, be aware that diversification has limitations. During major market downturns, correlations between different assets often increase, meaning many investments may decline together. Also, over-diversification can lead to mediocre returns if you spread your investments too thin.\n\nA well-diversified portfolio typically includes 60-80% stocks and 20-40% bonds for younger investors, with the bond allocation increasing as you approach retirement. Regular rebalancing helps maintain your target allocation and can actually boost returns over time by forcing you to sell high-performing assets and buy underperforming ones."
    }
  ];

  const books: Book[] = [
    {
      id: "1",
      title: "The Intelligent Investor",
      author: "Benjamin Graham",
      description: "The definitive book on value investing. A book of practical counsel that teaches you how to develop a sound investment policy.",
      rating: 4.8,
      pages: 640,
      difficulty: "intermediate",
      category: "Value Investing",
      cover: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=200&h=300&fit=crop"
    },
    {
      id: "2",
      title: "A Random Walk Down Wall Street",
      author: "Burton Malkiel",
      description: "A comprehensive guide to investment strategies including efficient market theory and modern portfolio theory.",
      rating: 4.6,
      pages: 448,
      difficulty: "beginner",
      category: "Investment Theory",
      cover: "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=200&h=300&fit=crop"
    },
    {
      id: "3",
      title: "Common Stocks and Uncommon Profits",
      author: "Philip Fisher",
      description: "A classic guide to growth investing and the importance of understanding the companies you invest in.",
      rating: 4.7,
      pages: 288,
      difficulty: "advanced",
      category: "Growth Investing",
      cover: "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=200&h=300&fit=crop"
    }
  ];

  const videos: VideoType[] = [
    {
      id: "1",
      title: "Stock Market Basics: How to Start Investing",
      description: "A comprehensive introduction to stock market investing covering key concepts, terminology, and getting started.",
      duration: 15,
      category: "Beginner",
      youtubeId: "p7HKvqRI_Bo",
      thumbnail: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is a stock?",
            options: [
              "A type of bond",
              "A share of ownership in a company",
              "A government security",
              "A type of currency"
            ],
            correctAnswer: 1
          },
          {
            id: "q2", 
            question: "What does P/E ratio stand for?",
            options: [
              "Price to Earnings",
              "Profit to Expenses",
              "Price to Equity",
              "Profit to Equity"
            ],
            correctAnswer: 0
          },
          {
            id: "q3",
            question: "Which of these is generally considered a safer investment?",
            options: [
              "Individual stocks",
              "Cryptocurrency",
              "Diversified index funds",
              "Penny stocks"
            ],
            correctAnswer: 2
          }
        ]
      }
    },
    {
      id: "2",
      title: "Understanding Bond Markets and Fixed Income",
      description: "Learn about bonds, how they work, and their role in a diversified investment portfolio.",
      duration: 12,
      category: "Intermediate",
      youtubeId: "IuyejHOGCro",
      thumbnail: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What happens to bond prices when interest rates rise?",
            options: [
              "They increase",
              "They decrease",
              "They stay the same",
              "They become more volatile"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "What is the coupon rate on a bond?",
            options: [
              "The annual interest payment",
              "The maturity date",
              "The credit rating",
              "The face value"
            ],
            correctAnswer: 0
          }
        ]
      }
    },
    {
      id: "3",
      title: "Advanced Portfolio Optimization Strategies",
      description: "Explore sophisticated techniques for optimizing your investment portfolio including Modern Portfolio Theory.",
      duration: 20,
      category: "Advanced",
      youtubeId: "5VEWCtPmRXg",
      thumbnail: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is the efficient frontier?",
            options: [
              "The maximum return for any level of risk",
              "The minimum risk for any level of return",
              "Both A and B",
              "Neither A nor B"
            ],
            correctAnswer: 2
          },
          {
            id: "q2",
            question: "What does beta measure?",
            options: [
              "Total return",
              "Systematic risk relative to the market",
              "Dividend yield",
              "Price volatility"
            ],
            correctAnswer: 1
          },
          {
            id: "q3",
            question: "What is the Sharpe ratio used for?",
            options: [
              "Measuring risk-adjusted returns",
              "Calculating compound interest",
              "Determining market cap",
              "Analyzing cash flow"
            ],
            correctAnswer: 0
          }
        ]
      }
    }
  ];

  const handleQuizComplete = (score: number, total: number) => {
    const percentage = (score / total) * 100;
    toast({
      title: `${t('quiz')} ${t('score')}: ${score}/${total}`,
      description: `You scored ${percentage.toFixed(0)}%!`,
    });
  };

  const difficultyLabels = {
    beginner: t('beginner'),
    intermediate: t('intermediate'),
    advanced: t('advanced')
  };

  const quizTranslations = {
    quiz: t('quiz'),
    nextQuestion: t('nextQuestion'),
    previousQuestion: t('previousQuestion'),
    submitQuiz: t('submitQuiz'),
    score: t('score'),
    correct: t('correct'),
    incorrect: t('incorrect'),
    tryAgain: t('tryAgain'),
    question: t('question'),
    of: t('of')
  };

  return (
    <div className="min-h-screen bg-gradient-hero">
      {/* Navigation */}
      <nav className="border-b bg-card/50 backdrop-blur-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center space-x-4">
            <Link to="/" className="flex items-center space-x-2">
              <TrendingUp className="h-8 w-8 text-primary" />
              <span className="text-2xl font-bold text-foreground">FinanceAI</span>
            </Link>
            <div className="hidden md:flex items-center space-x-4 ml-8">
              <Link to="/dashboard">
                <Button variant="ghost">Dashboard</Button>
              </Link>
              <Link to="/chat">
                <Button variant="ghost">AI Advisor</Button>
              </Link>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <LanguageSwitcher 
              currentLanguage={currentLanguage}
              onLanguageChange={setCurrentLanguage}
            />
            <Link to="/dashboard">
              <Button variant="outline" size="sm">
                <ArrowLeft className="h-4 w-4 mr-2" />
                Back
              </Button>
            </Link>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section className="py-12 px-4">
        <div className="container mx-auto text-center">
          <div className="max-w-3xl mx-auto">
            <h1 className="text-4xl font-bold mb-4 bg-gradient-primary bg-clip-text text-transparent">
              {t('learning')}
            </h1>
            <p className="text-xl text-muted-foreground mb-8">
              Expand your financial knowledge with our comprehensive collection of blogs, books, and interactive video content.
            </p>
          </div>
        </div>
      </section>

      {/* Main Content */}
      <section className="pb-16 px-4">
        <div className="container mx-auto">
          <Tabs defaultValue="blogs" className="w-full">
            <TabsList className="grid w-full grid-cols-3 max-w-md mx-auto mb-8">
              <TabsTrigger value="blogs" className="flex items-center gap-2">
                <BookOpen className="h-4 w-4" />
                <span className="hidden sm:inline">{t('blogs')}</span>
              </TabsTrigger>
              <TabsTrigger value="books" className="flex items-center gap-2">
                <Award className="h-4 w-4" />
                <span className="hidden sm:inline">{t('books')}</span>
              </TabsTrigger>
              <TabsTrigger value="videos" className="flex items-center gap-2">
                <Video className="h-4 w-4" />
                <span className="hidden sm:inline">{t('videos')}</span>
              </TabsTrigger>
            </TabsList>

            <TabsContent value="blogs">
              <div className="mb-6">
                <h2 className="text-2xl font-bold mb-2">{t('blogs')}</h2>
                <p className="text-muted-foreground">
                  Stay updated with the latest insights and strategies in financial markets.
                </p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {blogs.map((blog) => (
                  <BlogCard 
                    key={blog.id} 
                    blog={blog} 
                    readMoreText={t('readMore')}
                  />
                ))}
              </div>
            </TabsContent>

            <TabsContent value="books">
              <div className="mb-6">
                <h2 className="text-2xl font-bold mb-2">{t('books')}</h2>
                <p className="text-muted-foreground">
                  Essential reading for investors at every level of experience.
                </p>
              </div>
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {books.map((book) => (
                  <BookCard 
                    key={book.id} 
                    book={book}
                    authorText={t('author')}
                    pagesText={t('pages')}
                    difficultyText={t('difficulty')}
                    difficultyLabels={difficultyLabels}
                  />
                ))}
              </div>
            </TabsContent>

            <TabsContent value="videos">
              <div className="mb-6">
                <h2 className="text-2xl font-bold mb-2">{t('videos')}</h2>
                <p className="text-muted-foreground">
                  Learn through engaging video content with interactive quizzes to test your knowledge.
                </p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {videos.map((video) => (
                  <VideoCard 
                    key={video.id} 
                    video={video}
                    minutesText={t('minutes')}
                    watchVideoText={t('watchVideo')}
                    takeQuizText={t('takeQuiz')}
                    onQuizComplete={handleQuizComplete}
                    translations={quizTranslations}
                  />
                ))}
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </section>
    </div>
  );
};

export default Learning;