import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { TrendingUp, BookOpen, Video, Award, ArrowLeft, MessageSquare } from "lucide-react";
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

  const handlePlayAudio = (text: string) => {
    if ('speechSynthesis' in window) {
      // Cancel any ongoing speech
      window.speechSynthesis.cancel();
      
      const utterance = new SpeechSynthesisUtterance(text);
      
      // Set language based on current language
      const languageMap: { [key: string]: string } = {
        'en': 'en-US',
        'es': 'es-ES',
        'fr': 'fr-FR',
        'de': 'de-DE',
        'hi': 'hi-IN',
        'zh': 'zh-CN',
        'ja': 'ja-JP'
      };
      
      utterance.lang = languageMap[currentLanguage] || 'en-US';
      utterance.rate = 0.9;
      utterance.pitch = 1;
      
      utterance.onstart = () => {
        toast({
          title: "Audio Started",
          description: "Playing blog content audio...",
        });
      };
      
      utterance.onend = () => {
        toast({
          title: "Audio Finished",
          description: "Finished playing audio content",
        });
      };
      
      utterance.onerror = () => {
        toast({
          title: "Audio Error",
          description: "Could not play audio. Please try again.",
          variant: "destructive",
        });
      };
      
      window.speechSynthesis.speak(utterance);
    } else {
      toast({
        title: "Not Supported",
        description: "Text-to-speech is not supported in your browser.",
        variant: "destructive",
      });
    }
  };

  // Multi-language blog content
  const blogContent = {
    en: [
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
        content: "Albert Einstein allegedly called compound interest 'the eighth wonder of the world,' and for good reason. Compound interest is the process by which your investments generate earnings, and those earnings then generate their own earnings, creating a snowball effect that can dramatically increase your wealth over time.\n\nThe mathematics of compound interest might seem simple, but its long-term effects are profound. When you invest money, you earn returns not just on your initial investment (the principal), but also on all the returns that investment has generated over time. This means your money grows at an accelerating rate rather than a linear one.\n\nTo illustrate this power, consider investing €10,000 at an annual return of 7%. After one year, you'd have €10,700. But after 30 years, assuming you never added another penny, you'd have over €76,000. The initial €10,000 would have generated more than €66,000 in compound returns.\n\nThe key factors that maximize compound interest are time, rate of return, and frequency of compounding. Starting early is crucial because compound interest works best over long periods. Even small amounts invested in your twenties can grow to substantial sums by retirement.\n\nThe rate of return also matters significantly. A difference of just 2-3% in annual returns can result in hundreds of thousands of euros difference over a 30-40 year investment horizon. This is why it's important to minimize fees and taxes that can erode your returns.\n\nTo harness compound interest effectively, start investing as early as possible, be consistent with your contributions, reinvest your dividends and interest payments, and be patient. Avoid the temptation to withdraw funds early, as this breaks the compounding cycle and significantly reduces long-term growth potential."
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
      },
      {
        id: "4",
        title: "ESG Investing: Sustainable Finance for the Future",
        excerpt: "Explore how Environmental, Social, and Governance factors are reshaping investment decisions.",
        author: "Dr. Lisa Rodriguez",
        date: "2024-01-08",
        readTime: 14,
        category: "Sustainable Finance",
        image: "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?w=500&h=300&fit=crop",
        content: "Environmental, Social, and Governance (ESG) investing has moved from a niche approach to mainstream investment strategy. ESG investing considers environmental impact, social responsibility, and corporate governance practices alongside traditional financial metrics when making investment decisions.\n\nEnvironmental factors include a company's carbon footprint, waste management, water usage, and commitment to renewable energy. Social factors encompass labor practices, human rights policies, community relations, and product safety. Governance factors involve board diversity, executive compensation, shareholder rights, and corporate transparency.\n\nESG investing isn't just about doing good – it's increasingly about doing well financially. Studies show that companies with strong ESG practices often outperform their peers over the long term. This is because ESG factors can be material risks that affect a company's financial performance.\n\nFor example, companies with poor environmental practices may face regulatory fines, cleanup costs, or stranded assets as the world transitions to cleaner energy. Companies with weak governance may be more prone to scandals, fraud, or poor strategic decisions that destroy shareholder value.\n\nThere are several ways to incorporate ESG into your investment strategy. ESG funds screen companies based on ESG criteria. Impact investing actively seeks to generate positive social or environmental impact alongside financial returns. Shareholder advocacy involves using your ownership rights to influence corporate behavior.\n\nHowever, ESG investing isn't without challenges. There's no standardized ESG rating system, making it difficult to compare investments. Some critics argue that ESG funds may underperform due to limited investment universe or higher fees.\n\nDespite these challenges, ESG investing continues to grow rapidly. As younger investors increasingly prioritize sustainability and social responsibility, ESG considerations are likely to become even more important in investment decision-making."
      },
      {
        id: "5",
        title: "Cryptocurrency: Digital Assets in Your Portfolio",
        excerpt: "Understanding the role of cryptocurrencies and blockchain technology in modern investment portfolios.",
        author: "Alex Thompson",
        date: "2024-01-05",
        readTime: 16,
        category: "Alternative Investments",
        image: "https://images.unsplash.com/photo-1518544866310-3ad75419f2ba?w=500&h=300&fit=crop",
        content: "Cryptocurrency has evolved from a niche technology experiment to a significant asset class that many investors consider for portfolio diversification. Understanding the fundamentals of digital assets is crucial for modern investors.\n\nCryptocurrencies are digital or virtual currencies secured by cryptography and typically based on blockchain technology. Bitcoin, the first and largest cryptocurrency, was created as a decentralized digital currency. Since then, thousands of cryptocurrencies have emerged, each with different purposes and technologies.\n\nThe investment case for cryptocurrency includes potential for high returns, portfolio diversification, inflation hedge properties, and exposure to blockchain technology innovation. Cryptocurrencies have shown low correlation with traditional assets during certain periods, potentially providing diversification benefits.\n\nHowever, cryptocurrency investing comes with significant risks. Price volatility can be extreme, with double-digit percentage moves common in single days. Regulatory uncertainty poses ongoing risks as governments worldwide develop cryptocurrency policies. Technical risks include exchange hacks, wallet security issues, and the permanent loss of private keys.\n\nBefore investing in cryptocurrency, consider your risk tolerance, investment timeline, and overall portfolio allocation. Most financial advisors recommend limiting cryptocurrency exposure to 5-10% of your total portfolio due to its high-risk nature.\n\nThere are several ways to gain cryptocurrency exposure: direct ownership through exchanges, cryptocurrency ETFs, blockchain-focused stocks, or cryptocurrency trusts. Each method has different risk profiles, costs, and tax implications.\n\nStay informed about regulatory developments, technological advances, and market trends. The cryptocurrency space evolves rapidly, and what's true today may not be tomorrow. Consider dollar-cost averaging to reduce the impact of volatility if you decide to invest.\n\nRemember that cryptocurrency is still a relatively new and experimental asset class. While it offers potential opportunities, it should be approached with caution and thorough research."
      },
      {
        id: "6",
        title: "Retirement Planning: Securing Your Financial Future",
        excerpt: "A comprehensive guide to planning and saving for a comfortable retirement.",
        author: "Jennifer Lee",
        date: "2024-01-03",
        readTime: 18,
        category: "Retirement Planning",
        image: "https://images.unsplash.com/photo-1559526324-593bc073d938?w=500&h=300&fit=crop",
        content: "Retirement planning is one of the most important financial goals you'll ever pursue. The earlier you start, the more time your money has to grow through compound interest, making your retirement savings journey significantly easier.\n\nThe first step in retirement planning is estimating how much money you'll need. A common rule of thumb suggests you'll need 70-80% of your pre-retirement income to maintain your lifestyle. However, this varies based on your expected expenses, healthcare costs, travel plans, and desired retirement lifestyle.\n\nThere are several retirement savings vehicles available. Employer-sponsored plans like 401(k)s often include company matching – this is essentially free money that you should always maximize. Individual Retirement Accounts (IRAs) offer additional tax-advantaged savings opportunities with different contribution limits and withdrawal rules.\n\nThe power of starting early cannot be overstated. Someone who starts saving €200 per month at age 25 will have significantly more at retirement than someone who starts saving €400 per month at age 35, assuming the same rate of return.\n\nAsset allocation becomes increasingly important as you approach retirement. The traditional rule was to subtract your age from 100 to determine your stock allocation percentage. However, with increasing lifespans, many advisors now recommend more aggressive allocations to combat inflation risk.\n\nDon't forget to plan for healthcare costs in retirement. Healthcare expenses typically increase with age, and Medicare doesn't cover everything. Consider Health Savings Accounts (HSAs) if available – they offer triple tax advantages for healthcare expenses.\n\nRegularly review and adjust your retirement plan. Life changes, market conditions, and new opportunities may require modifications to your strategy. Consider working with a financial advisor to ensure you're on track to meet your retirement goals.\n\nRemember that retirement planning isn't just about accumulating wealth – it's also about creating a plan for how you'll spend your time and find purpose in retirement."
      }
    ],
    hi: [
      {
        id: "1",
        title: "बाजार की अस्थिरता को समझना: एक शुरुआती गाइड",
        excerpt: "जानें कि बाजार में उतार-चढ़ाव आपके निवेश को कैसे प्रभावित करता है और अनिश्चित समय में आत्मविश्वास के साथ नेविगेट करने की रणनीतियां।",
        author: "सारा जॉनसन",
        date: "2024-01-15",
        readTime: 8,
        category: "बाजार विश्लेषण",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "बाजार की अस्थिरता निवेश के सबसे चुनौतीपूर्ण पहलुओं में से एक है जिसे हर निवेशक को समझना और नेविगेट करना सीखना चाहिए। अस्थिरता समय के साथ ट्रेडिंग कीमतों में भिन्नता की डिग्री को संदर्भित करती है, और यह वित्तीय बाजारों की एक प्राकृतिक विशेषता है।\n\nजब बाजार अस्थिर होते हैं, तो कीमतें छोटी अवधि में नाटकीय रूप से बदल सकती हैं। यह नए निवेशकों के लिए परेशान करने वाला हो सकता है, लेकिन अस्थिरता के कारणों और पैटर्न को समझना आपको अपने निवेश के बारे में अधिक सूचित निर्णय लेने में मदद कर सकता है।\n\nकई कारक बाजार की अस्थिरता में योगदान देते हैं। मुद्रास्फीति दर, रोजगार डेटा, और जीडीपी वृद्धि जैसे आर्थिक संकेतक बाजारों को मजबूत प्रतिक्रिया दे सकते हैं। राजनीतिक घटनाएं, प्राकृतिक आपदाएं, और वैश्विक संघर्ष भी अनिश्चितता पैदा करते हैं जो बढ़ी हुई अस्थिरता के रूप में प्रकट होती है।"
      },
      {
        id: "2",
        title: "लंबी अवधि के निवेश में चक्रवृद्धि ब्याज की शक्ति",
        excerpt: "जानें कि चक्रवृद्धि ब्याज कैसे समय के साथ मामूली निवेश को पर्याप्त धन में बदल सकता है।",
        author: "माइकल चेन",
        date: "2024-01-12",
        readTime: 12,
        category: "निवेश रणनीति",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
        content: "अल्बर्ट आइंस्टीन ने कथित तौर पर चक्रवृद्धि ब्याज को 'दुनिया का आठवां अजूबा' कहा था, और अच्छे कारण से। चक्रवृद्धि ब्याज वह प्रक्रिया है जिसके द्वारा आपके निवेश कमाई उत्पन्न करते हैं, और वे कमाई फिर अपनी खुद की कमाई उत्पन्न करती हैं, एक स्नोबॉल प्रभाव बनाती हैं जो समय के साथ आपकी संपत्ति को नाटकीय रूप से बढ़ा सकती हैं।"
      },
      {
        id: "3",
        title: "विविधीकरण: एक संतुलित पोर्टफोलियो का निर्माण",
        excerpt: "पोर्टफोलियो विविधीकरण की मूल बातें सीखें और विभिन्न एसेट क्लासों में जोखिम कैसे फैलाएं।",
        author: "एम्मा डेविस",
        date: "2024-01-10",
        readTime: 10,
        category: "पोर्टफोलियो प्रबंधन",
        image: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
        content: "विविधीकरण को अक्सर निवेश में एकमात्र मुफ्त दोपहर का भोजन कहा जाता है, और इस अवधारणा को समझना एक सफल दीर्घकालिक निवेश रणनीति बनाने के लिए महत्वपूर्ण है।"
      }
    ],
    te: [
      {
        id: "1",
        title: "మార్కెట్ అస్థిరతను అర్థం చేసుకోవడం: ప్రారంభకుల గైడ్",
        excerpt: "మార్కెట్ హెచ్చుతగ్గులు మీ పెట్టుబడులను ఎలా ప్రభావితం చేస్తాయో తెలుసుకోండి మరియు అనిశ్చిత సమయాల్లో విశ్వాసంతో నావిగేట్ చేసే వ్యూహాలను నేర్చుకోండి।",
        author: "సారా జాన్సన్",
        date: "2024-01-15",
        readTime: 8,
        category: "మార్కెట్ విశ్లేషణ",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "మార్కెట్ అస్థిరత అనేది పెట్టుబడి యొక్క అత్యంత సవాలుగా ఉండే అంశాలలో ఒకటి, దీనిని ప్రతి పెట్టుబడిదారుడు అర్థం చేసుకోవాలి మరియు నావిగేట్ చేయడం నేర్చుకోవాలి।"
      },
      {
        id: "2",
        title: "దీర్ఘకాలిక పెట్టుబడిలో కాంపౌండ్ ఇంట్రెస్ట్ యొక్క శక్తి",
        excerpt: "కాంపౌండ్ ఇంట్రెస్ట్ ఎలా కాలక్రమేణా మామూలు పెట్టుబడులను గణనీయమైన సంపదగా మార్చగలదో తెలుసుకోండి।",
        author: "మైఖేల్ చెన్",
        date: "2024-01-12",
        readTime: 12,
        category: "పెట్టుబడి వ్యూహం",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
        content: "ఆల్బర్ట్ ఐన్‌స్టీన్ కాంపౌండ్ ఇంట్రెస్ట్‌ను 'ప్రపంచంలోని ఎనిమిదవ అద్భుతం' అని పిలిచారు, మరియు మంచి కారణంతో."
      }
    ],
    kn: [
      {
        id: "1",
        title: "ಮಾರುಕಟ್ಟೆಯ ಅಸ್ಥಿರತೆಯನ್ನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುವುದು: ಆರಂಭಿಕರ ಮಾರ್ಗದರ್ಶಿ",
        excerpt: "ಮಾರುಕಟ್ಟೆಯ ಏರಿಳಿತಗಳು ನಿಮ್ಮ ಹೂಡಿಕೆಗಳನ್ನು ಹೇಗೆ ಪರಿಣಾಮ ಬೀರುತ್ತವೆ ಮತ್ತು ಅನಿಶ್ಚಿತ ಸಮಯಗಳಲ್ಲಿ ವಿಶ್ವಾಸದೊಂದಿಗೆ ಸಂಚರಿಸುವ ತಂತ್ರಗಳನ್ನು ಕಲಿಯಿರಿ.",
        author: "ಸಾರಾ ಜಾನ್ಸನ್",
        date: "2024-01-15",
        readTime: 8,
        category: "ಮಾರುಕಟ್ಟೆ ವಿಶ್ಲೇಷಣೆ",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "ಮಾರುಕಟ್ಟೆಯ ಅಸ್ಥಿರತೆಯು ಹೂಡಿಕೆಯ ಅತ್ಯಂತ ಸವಾಲಿನ ಅಂಶಗಳಲ್ಲಿ ಒಂದಾಗಿದೆ, ಇದನ್ನು ಪ್ರತಿಯೊಬ್ಬ ಹೂಡಿಕೆದಾರನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳಬೇಕು ಮತ್ತು ನ್ಯಾವಿಗೇಟ್ ಮಾಡಲು ಕಲಿಯಬೇಕು."
      }
    ],
    fr: [
      {
        id: "1",
        title: "Comprendre la volatilité du marché : Un guide pour débutants",
        excerpt: "Apprenez comment les fluctuations du marché affectent vos investissements et les stratégies pour naviguer en période d'incertitude avec confiance.",
        author: "Sarah Johnson",
        date: "2024-01-15",
        readTime: 8,
        category: "Analyse de marché",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "La volatilité du marché est l'un des aspects les plus difficiles de l'investissement que chaque investisseur doit comprendre et apprendre à naviguer. La volatilité fait référence au degré de variation des prix de trading au fil du temps, et c'est une caractéristique naturelle des marchés financiers."
      },
      {
        id: "2",
        title: "Le pouvoir des intérêts composés dans l'investissement à long terme",
        excerpt: "Découvrez comment les intérêts composés peuvent transformer des investissements modestes en richesse substantielle au fil du temps.",
        author: "Michael Chen",
        date: "2024-01-12",
        readTime: 12,
        category: "Stratégie d'investissement",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
        content: "Albert Einstein aurait appelé les intérêts composés 'la huitième merveille du monde', et pour une bonne raison."
      }
    ],
    de: [
      {
        id: "1",
        title: "Marktvolatilität verstehen: Ein Leitfaden für Anfänger",
        excerpt: "Lernen Sie, wie Marktschwankungen Ihre Investitionen beeinflussen und Strategien, um unsichere Zeiten mit Vertrauen zu navigieren.",
        author: "Sarah Johnson",
        date: "2024-01-15",
        readTime: 8,
        category: "Marktanalyse",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "Marktvolatilität ist einer der herausforderndsten Aspekte des Investierens, den jeder Investor verstehen und zu navigieren lernen muss."
      }
    ]
  };

  // Get blogs for current language (fallback to English if not available)
  const blogs: BlogPost[] = blogContent[currentLanguage as keyof typeof blogContent] || blogContent.en;

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
    },
    {
      id: "4",
      title: "The Little Book of Common Sense Investing",
      author: "John C. Bogle",
      description: "The founder of Vanguard explains why index fund investing is the smartest choice for most investors.",
      rating: 4.9,
      pages: 216,
      difficulty: "beginner",
      category: "Index Investing",
      cover: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=300&fit=crop"
    },
    {
      id: "5",
      title: "Rich Dad Poor Dad",
      author: "Robert T. Kiyosaki",
      description: "A powerful story about money and investing that challenges conventional wisdom about personal finance.",
      rating: 4.3,
      pages: 336,
      difficulty: "beginner",
      category: "Personal Finance",
      cover: "https://images.unsplash.com/photo-1553729459-efe14ef6055d?w=200&h=300&fit=crop"
    },
    {
      id: "6",
      title: "One Up On Wall Street",
      author: "Peter Lynch",
      description: "The legendary mutual fund manager shares his investment philosophy and stock-picking strategies.",
      rating: 4.5,
      pages: 352,
      difficulty: "intermediate",
      category: "Stock Analysis",
      cover: "https://images.unsplash.com/photo-1592496431122-2349e0fbc666?w=200&h=300&fit=crop"
    },
    {
      id: "7",
      title: "The Bogleheads' Guide to Investing",
      author: "Taylor Larimore",
      description: "A comprehensive guide to building wealth through simple, low-cost index fund investing strategies.",
      rating: 4.6,
      pages: 352,
      difficulty: "beginner",
      category: "Index Investing",
      cover: "https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=200&h=300&fit=crop"
    },
    {
      id: "8",
      title: "The Millionaire Next Door",
      author: "Thomas J. Stanley",
      description: "Research-based insights into the habits and characteristics of America's wealthy individuals.",
      rating: 4.4,
      pages: 258,
      difficulty: "beginner",
      category: "Wealth Building",
      cover: "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=200&h=300&fit=crop"
    }
  ];

  const videos: VideoType[] = [
    {
      id: "1",
      title: "Stock Market Basics for Beginners",
      description: "A comprehensive introduction to stock market investing covering key concepts, terminology, and getting started strategies.",
      duration: 18,
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
      title: "Understanding Bond Investments",
      description: "Learn about bonds, how they work, their risks and rewards, and their role in a diversified investment portfolio.",
      duration: 14,
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
              "The annual interest payment rate",
              "The maturity date",
              "The credit rating",
              "The face value"
            ],
            correctAnswer: 0
          },
          {
            id: "q3",
            question: "Which type of bond is generally considered safest?",
            options: [
              "Corporate bonds",
              "Municipal bonds",
              "Government treasury bonds",
              "High-yield bonds"
            ],
            correctAnswer: 2
          }
        ]
      }
    },
    {
      id: "3",
      title: "Portfolio Diversification Strategies", 
      description: "Explore advanced techniques for building a well-diversified portfolio to minimize risk and maximize returns.",
      duration: 22,
      category: "Advanced",
      youtubeId: "uSBzQB-UXkU",
      thumbnail: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is the main benefit of diversification?",
            options: [
              "Guaranteed higher returns",
              "Risk reduction without necessarily sacrificing returns",
              "Lower fees",
              "Simpler portfolio management"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "Which of these is NOT a type of diversification?",
            options: [
              "Geographic diversification",
              "Sector diversification",
              "Time diversification",
              "Price diversification"
            ],
            correctAnswer: 3
          },
          {
            id: "q3",
            question: "What is asset allocation?",
            options: [
              "Buying individual stocks",
              "Dividing investments among different asset categories",
              "Timing the market",
              "Choosing when to sell"
            ],
            correctAnswer: 1
          }
        ]
      }
    },
    {
      id: "4", 
      title: "Retirement Planning Essentials",
      description: "A complete guide to planning for retirement, including 401(k)s, IRAs, and investment strategies for different life stages.",
      duration: 25,
      category: "Planning",
      youtubeId: "gFKNLKdh-cI",
      thumbnail: "https://images.unsplash.com/photo-1559526324-593bc073d938?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "At what age can you start withdrawing from a 401(k) without penalties?",
            options: [
              "55",
              "59.5",
              "62",
              "65"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "What is the main advantage of a Roth IRA over a traditional IRA?",
            options: [
              "Higher contribution limits",
              "Tax-free withdrawals in retirement",
              "Immediate tax deduction",
              "No income restrictions"
            ],
            correctAnswer: 1
          }
        ]
      }
    },
    {
      id: "5",
      title: "Cryptocurrency Investment Basics",
      description: "Understanding digital currencies, blockchain technology, and how to evaluate cryptocurrency investments.",
      duration: 19,
      category: "Alternative Investments",
      youtubeId: "VYWc9dFqROI",
      thumbnail: "https://images.unsplash.com/photo-1518544866310-3ad75419f2ba?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is blockchain?",
            options: [
              "A type of cryptocurrency",
              "A distributed ledger technology",
              "A trading platform",
              "A wallet application"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "What percentage of your portfolio should typically be in cryptocurrency?",
            options: [
              "50% or more",
              "25-30%",
              "5-10%",
              "100%"
            ],
            correctAnswer: 2
          }
        ]
      }
    },
    {
      id: "6",
      title: "ESG Investing and Sustainable Finance",
      description: "Learn about Environmental, Social, and Governance investing and how to align your investments with your values.",
      duration: 16,
      category: "ESG",
      youtubeId: "F0J6zTyjvY4",
      thumbnail: "https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What does ESG stand for?",
            options: [
              "Economic, Social, Governance",
              "Environmental, Social, Governance", 
              "Ethical, Social, Growth",
              "Environmental, Sustainable, Green"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "Which is NOT typically an ESG consideration?",
            options: [
              "Carbon footprint",
              "Board diversity",
              "Stock price volatility",
              "Labor practices"
            ],
            correctAnswer: 2
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
                <Button variant="ghost">
                  <MessageSquare className="mr-2 h-4 w-4" />
                  AI Advisor
                </Button>
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
                    onPlayAudio={() => handlePlayAudio(blog.content)}
                    audioText="Play Audio"
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
                  Interactive video content with quizzes to test your knowledge.
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