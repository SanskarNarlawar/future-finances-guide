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
  const [currentlyPlayingAudio, setCurrentlyPlayingAudio] = useState<string | null>(null);

  const handlePlayAudio = (blogId: string, text: string) => {
    if ('speechSynthesis' in window) {
      // If currently playing the same blog, stop it
      if (currentlyPlayingAudio === blogId) {
        window.speechSynthesis.cancel();
        setCurrentlyPlayingAudio(null);
        toast({
          title: "Audio Stopped",
          description: "Audio playback stopped",
        });
        return;
      }

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
        setCurrentlyPlayingAudio(blogId);
        toast({
          title: "Audio Started",
          description: "Playing blog content audio...",
        });
      };
      
      utterance.onend = () => {
        setCurrentlyPlayingAudio(null);
        toast({
          title: "Audio Finished",
          description: "Finished playing audio content",
        });
      };
      
      utterance.onerror = () => {
        setCurrentlyPlayingAudio(null);
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
        content: "बाजार की अस्थिरता निवेश के सबसे चुनौतीपूर्ण पहलुओं में से एक है जिसे हर निवेशक को समझना और नेविगेट करना सीखना चाहिए। अस्थिरता समय के साथ ट्रेडिंग मूल्यों में भिन्नता की डिग्री को संदर्भित करती है, और यह वित्तीय बाजारों की एक प्राकृतिक विशेषता है।\n\nजब बाजार अस्थिर होते हैं, तो कीमतें कम समय में नाटकीय रूप से बदल सकती हैं। यह नए निवेशकों के लिए परेशान करने वाला हो सकता है, लेकिन अस्थिरता के कारणों और पैटर्न को समझना आपको अपने निवेश के बारे में अधिक सूचित निर्णय लेने में मदद कर सकता है।\n\nकई कारक बाजार की अस्थिरता में योगदान करते हैं। मुद्रास्फीति दर, रोजगार डेटा, और GDP वृद्धि जैसे आर्थिक संकेतक बाजारों को मजबूती से प्रतिक्रिया करने का कारण बना सकते हैं। राजनीतिक घटनाएं, प्राकृतिक आपदाएं, और वैश्विक संघर्ष भी अनिश्चितता पैदा करते हैं जो बढ़ी हुई अस्थिरता के रूप में प्रकट होती है।\n\nअस्थिर बाजारों को सफलतापूर्वक नेविगेट करने के लिए, इन रणनीतियों पर विचार करें: विभिन्न परिसंपत्ति वर्गों और क्षेत्रों में एक विविधीकृत पोर्टफोलियो बनाए रखें, अल्पकालिक बाजार आंदोलनों के आधार पर भावनात्मक निर्णय लेने से बचें, और अपनी दीर्घकालिक निवेश योजना पर टिके रहें।\n\nडॉलर-कॉस्ट एवरेजिंग अस्थिर अवधि के दौरान एक और प्रभावी रणनीति है। नियमित रूप से एक निश्चित राशि निवेश करके, आप कम कीमतों पर अधिक शेयर खरीदते हैं और उच्च कीमतों पर कम, संभावित रूप से समय के साथ अपनी औसत लागत को कम करते हैं।\n\nअंत में, याद रखें कि अस्थिरता अस्थायी है, लेकिन बाजार में समय बाजार की टाइमिंग से अधिक महत्वपूर्ण है। सफल दीर्घकालिक निवेशक अस्थिरता को डरने के बजाय निवेश यात्रा के एक सामान्य हिस्से के रूप में देखना सीखते हैं।"
      },
      {
        id: "2",
        title: "दीर्घकालिक निवेश में चक्रवृद्धि ब्याज की शक्ति",
        excerpt: "जानें कि कैसे चक्रवृद्धि ब्याज समय के साथ मामूली निवेश को पर्याप्त धन में बदल सकता है।",
        author: "माइकल चेन",
        date: "2024-01-12",
        readTime: 12,
        category: "निवेश रणनीति",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
        content: "अल्बर्ट आइंस्टीन ने कथित तौर पर चक्रवृद्धि ब्याज को 'दुनिया का आठवां अजूबा' कहा था, और अच्छे कारण से। चक्रवृद्धि ब्याज वह प्रक्रिया है जिसके द्वारा आपके निवेश आय उत्पन्न करते हैं, और वे आय फिर अपनी खुद की आय उत्पन्न करती हैं, एक स्नोबॉल प्रभाव बनाते हैं जो समय के साथ आपकी संपत्ति को नाटकीय रूप से बढ़ा सकता है।\n\nचक्रवृद्धि ब्याज का गणित सरल लग सकता है, लेकिन इसके दीर्घकालिक प्रभाव गहरे हैं। जब आप पैसा निवेश करते हैं, तो आप न केवल अपने प्रारंभिक निवेश (मूलधन) पर रिटर्न अर्जित करते हैं, बल्कि उस निवेश द्वारा समय के साथ उत्पन्न सभी रिटर्न पर भी। इसका मतलब है कि आपका पैसा रैखिक दर के बजाय त्वरित दर से बढ़ता है।\n\nइस शक्ति को स्पष्ट करने के लिए, 7% की वार्षिक वापसी पर €10,000 निवेश करने पर विचार करें। एक साल बाद, आपके पास €10,700 होंगे। लेकिन 30 साल बाद, यह मानते हुए कि आपने कभी एक और पैसा नहीं जोड़ा, आपके पास €76,000 से अधिक होगा। प्रारंभिक €10,000 ने €66,000 से अधिक की चक्रवृद्धि वापसी उत्पन्न की होगी।\n\nचक्रवृद्धि ब्याज को अधिकतम करने वाले मुख्य कारक समय, वापसी की दर, और चक्रवृद्धि की आवृत्ति हैं। जल्दी शुरू करना महत्वपूर्ण है क्योंकि चक्रवृद्धि ब्याज लंबी अवधि में सबसे अच्छा काम करता है।\n\nचक्रवृद्धि ब्याज का प्रभावी रूप से उपयोग करने के लिए, जितनी जल्दी हो सके निवेश शुरू करें, अपने योगदान के साथ निरंतर रहें, अपने लाभांश और ब्याज भुगतान को पुनर्निवेश करें, और धैर्य रखें। जल्दी फंड निकालने के प्रलोभन से बचें, क्योंकि यह चक्रवृद्धि चक्र को तोड़ता है और दीर्घकालिक विकास क्षमता को काफी कम कर देता है।"
      },
      {
        id: "3",
        title: "विविधीकरण: एक संतुलित पोर्टफोलियो बनाना",
        excerpt: "पोर्टफोलियो विविधीकरण की मूलभूत बातें सीखें और जानें कि विभिन्न परिसंपत्ति वर्गों में जोखिम कैसे फैलाया जाए।",
        author: "एम्मा डेविस",
        date: "2024-01-10",
        readTime: 10,
        category: "पोर्टफोलियो प्रबंधन",
        image: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
        content: "विविधीकरण को अक्सर निवेश में एकमात्र मुफ्त भोजन कहा जाता है, और इस अवधारणा को समझना एक सफल दीर्घकालिक निवेश रणनीति बनाने के लिए महत्वपूर्ण है। इसके मूल में, विविधीकरण का मतलब है विभिन्न परिसंपत्तियों में अपने निवेश को फैलाना ताकि समग्र पोर्टफोलियो जोखिम को कम किया जा सके बिना जरूरी रूप से रिटर्न का त्याग किए।\n\nविविधीकरण के पीछे का सिद्धांत यह है कि विभिन्न निवेश अक्सर विभिन्न बाजार स्थितियों में अलग तरह से प्रदर्शन करते हैं। जब आपके पोर्टफोलियो में कुछ निवेश मूल्य में गिरावट कर रहे होते हैं, तो अन्य स्थिर या यहां तक कि बढ़ रहे हो सकते हैं, जो समग्र पोर्टफोलियो प्रदर्शन को चिकना बनाने और अस्थिरता को कम करने में मदद करते हैं।\n\nविचार करने के लिए कई प्रकार के विविधीकरण हैं। परिसंपत्ति वर्ग विविधीकरण में स्टॉक, बॉन्ड, रियल एस्टेट, कमोडिटीज, और नकदी में निवेश फैलाना शामिल है। भौगोलिक विविधीकरण का मतलब घरेलू और अंतर्राष्ट्रीय दोनों बाजारों में निवेश करना है।\n\nअपने पोर्टफोलियो के स्टॉक हिस्से के भीतर, आपको कंपनी के आकार (लार्ज-कैप, मिड-कैप, स्मॉल-कैप स्टॉक) और निवेश शैली (ग्रोथ बनाम वैल्यू स्टॉक) द्वारा भी विविधीकरण करना चाहिए।\n\nहालांकि, सावधान रहें कि विविधीकरण की सीमाएं हैं। प्रमुख बाजार गिरावट के दौरान, विभिन्न परिसंपत्तियों के बीच सहसंबंध अक्सर बढ़ जाता है, जिसका अर्थ है कि कई निवेश एक साथ गिर सकते हैं। एक अच्छी तरह से विविधीकृत पोर्टफोलियो में आमतौर पर युवा निवेशकों के लिए 60-80% स्टॉक और 20-40% बॉन्ड शामिल होते हैं।"
      }
    ],
    es: [
      {
        id: "1",
        title: "Entendiendo la Volatilidad del Mercado: Una Guía para Principiantes",
        excerpt: "Aprende cómo las fluctuaciones del mercado afectan tus inversiones y estrategias para navegar tiempos inciertos con confianza.",
        author: "Sarah Johnson",
        date: "2024-01-15",
        readTime: 8,
        category: "Análisis de Mercado",
        image: "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=500&h=300&fit=crop",
        content: "La volatilidad del mercado es uno de los aspectos más desafiantes de la inversión que todo inversor debe entender y aprender a navegar. La volatilidad se refiere al grado de variación en los precios de negociación a lo largo del tiempo, y es una característica natural de los mercados financieros.\n\nCuando los mercados son volátiles, los precios pueden oscilar dramáticamente en períodos cortos. Esto puede ser inquietante para los nuevos inversores, pero entender las causas y patrones de la volatilidad puede ayudarte a tomar decisiones más informadas sobre tus inversiones.\n\nVarios factores contribuyen a la volatilidad del mercado. Los indicadores económicos como las tasas de inflación, datos de empleo y crecimiento del PIB pueden causar que los mercados reaccionen fuertemente. Los eventos políticos, desastres naturales y conflictos globales también crean incertidumbre que se manifiesta como mayor volatilidad.\n\nPara navegar mercados volátiles exitosamente, considera estas estrategias: mantén un portafolio diversificado a través de diferentes clases de activos y sectores, evita tomar decisiones emocionales basadas en movimientos del mercado a corto plazo, y mantente fiel a tu plan de inversión a largo plazo.\n\nEl promedio de costo en dólares es otra estrategia efectiva durante períodos volátiles. Al invertir una cantidad fija regularmente, compras más acciones cuando los precios están bajos y menos cuando los precios están altos, potencialmente reduciendo tu costo promedio a lo largo del tiempo.\n\nFinalmente, recuerda que la volatilidad es temporal, pero el tiempo en el mercado es más importante que el timing del mercado. Los inversores exitosos a largo plazo aprenden a ver la volatilidad como una parte normal del viaje de inversión en lugar de algo a temer."
      },
      {
        id: "2",
        title: "El Poder del Interés Compuesto en la Inversión a Largo Plazo",
        excerpt: "Descubre cómo el interés compuesto puede transformar inversiones modestas en riqueza sustancial a lo largo del tiempo.",
        author: "Michael Chen",
        date: "2024-01-12",
        readTime: 12,
        category: "Estrategia de Inversión",
        image: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=500&h=300&fit=crop",
        content: "Albert Einstein supuestamente llamó al interés compuesto 'la octava maravilla del mundo', y por una buena razón. El interés compuesto es el proceso por el cual tus inversiones generan ganancias, y esas ganancias luego generan sus propias ganancias, creando un efecto bola de nieve que puede aumentar dramáticamente tu riqueza a lo largo del tiempo.\n\nLas matemáticas del interés compuesto pueden parecer simples, pero sus efectos a largo plazo son profundos. Cuando inviertes dinero, obtienes retornos no solo en tu inversión inicial (el principal), sino también en todos los retornos que esa inversión ha generado a lo largo del tiempo. Esto significa que tu dinero crece a una tasa acelerada en lugar de una lineal.\n\nPara ilustrar este poder, considera invertir €10,000 con un retorno anual del 7%. Después de un año, tendrías €10,700. Pero después de 30 años, asumiendo que nunca agregaste otro centavo, tendrías más de €76,000. Los €10,000 iniciales habrían generado más de €66,000 en retornos compuestos.\n\nLos factores clave que maximizan el interés compuesto son el tiempo, la tasa de retorno y la frecuencia de capitalización. Comenzar temprano es crucial porque el interés compuesto funciona mejor durante períodos largos. Incluso pequeñas cantidades invertidas en tus veintes pueden crecer a sumas sustanciales para la jubilación.\n\nPara aprovechar el interés compuesto efectivamente, comienza a invertir tan pronto como sea posible, sé consistente con tus contribuciones, reinvierte tus dividendos y pagos de intereses, y sé paciente. Evita la tentación de retirar fondos temprano, ya que esto rompe el ciclo de capitalización y reduce significativamente el potencial de crecimiento a largo plazo."
      },
      {
        id: "3",
        title: "Diversificación: Construyendo un Portafolio Equilibrado",
        excerpt: "Aprende los fundamentos de la diversificación de portafolio y cómo distribuir el riesgo a través de diferentes clases de activos.",
        author: "Emma Davis",
        date: "2024-01-10",
        readTime: 10,
        category: "Gestión de Portafolio",
        image: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
        content: "La diversificación a menudo se llama el único almuerzo gratis en la inversión, y entender este concepto es crucial para construir una estrategia de inversión exitosa a largo plazo. En su núcleo, la diversificación significa distribuir tus inversiones a través de varios activos para reducir el riesgo general del portafolio sin necesariamente sacrificar retornos.\n\nEl principio detrás de la diversificación es que diferentes inversiones a menudo se comportan de manera diferente bajo varias condiciones del mercado. Cuando algunas inversiones en tu portafolio están declinando en valor, otras pueden estar estables o incluso aumentando, ayudando a suavizar el rendimiento general del portafolio y reducir la volatilidad.\n\nHay varios tipos de diversificación a considerar. La diversificación de clases de activos implica distribuir inversiones a través de acciones, bonos, bienes raíces, materias primas y efectivo. La diversificación geográfica significa invertir en mercados tanto domésticos como internacionales.\n\nDentro de la porción de acciones de tu portafolio, también deberías diversificar por tamaño de empresa (acciones de gran, mediana y pequeña capitalización) y estilo de inversión (acciones de crecimiento vs. valor). Esto ayuda a asegurar que no dependas excesivamente del rendimiento de cualquier segmento único del mercado.\n\nSin embargo, ten en cuenta que la diversificación tiene limitaciones. Durante grandes caídas del mercado, las correlaciones entre diferentes activos a menudo aumentan, lo que significa que muchas inversiones pueden declinar juntas. Un portafolio bien diversificado típicamente incluye 60-80% acciones y 20-40% bonos para inversores jóvenes, con la asignación de bonos aumentando a medida que te acercas a la jubilación."
      }
    ]
  };

  // Get blogs based on current language
  const blogs: BlogPost[] = blogContent[currentLanguage as keyof typeof blogContent] || blogContent.en;

  // Difficulty labels for different languages
  const difficultyLabels = {
    en: { beginner: "Beginner", intermediate: "Intermediate", advanced: "Advanced" },
    hi: { beginner: "शुरुआती", intermediate: "मध्यम", advanced: "उन्नत" },
    es: { beginner: "Principiante", intermediate: "Intermedio", advanced: "Avanzado" },
    fr: { beginner: "Débutant", intermediate: "Intermédiaire", advanced: "Avancé" },
    de: { beginner: "Anfänger", intermediate: "Fortgeschritten", advanced: "Experte" }
  };

  // Quiz translations
  const quizTranslations = {
    question: t('question'),
    correct: t('correct'),
    incorrect: t('incorrect'),
    nextQuestion: t('nextQuestion'),
    quizComplete: t('quizComplete'),
    score: t('score'),
    retakeQuiz: t('retakeQuiz')
  };

  const books: Book[] = [
    {
      id: "1",
      title: "The Intelligent Investor",
      author: "Benjamin Graham",
      description: "Warren Buffett's mentor teaches the timeless principles of value investing and market psychology.",
      rating: 4.8,
      pages: 640,
      difficulty: "intermediate",
      category: "Value Investing",
      cover: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&h=300&fit=crop"
    },
    {
      id: "2",
      title: "A Random Walk Down Wall Street",
      author: "Burton G. Malkiel",
      description: "A comprehensive guide to investment theory and practice, advocating for index fund investing.",
      rating: 4.5,
      pages: 464,
      difficulty: "intermediate",
      category: "Investment Theory",
      cover: "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=200&h=300&fit=crop"
    },
    {
      id: "3",
      title: "Your Money or Your Life",
      author: "Vicki Robin",
      description: "A transformative approach to personal finance that emphasizes financial independence and conscious spending.",
      rating: 4.4,
      pages: 368,
      difficulty: "beginner",
      category: "Personal Finance",
      cover: "https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=200&h=300&fit=crop"
    },
    {
      id: "4",
      title: "Common Stocks and Uncommon Profits",
      author: "Philip A. Fisher",
      description: "Classic investment wisdom focusing on growth investing and understanding business fundamentals.",
      rating: 4.6,
      pages: 320,
      difficulty: "advanced",
      category: "Growth Investing",
      cover: "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=200&h=300&fit=crop"
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
    }
  ];

  // Updated videos with only working YouTube IDs
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
      id: "3",
      title: "Personal Finance Fundamentals",
      description: "Essential personal finance concepts including budgeting, saving, debt management, and building an emergency fund.",
      duration: 15,
      category: "Personal Finance",
      youtubeId: "HQzoZfc3GwQ",
      thumbnail: "https://images.unsplash.com/photo-1559526324-593bc073d938?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is the recommended emergency fund size?",
            options: [
              "1 month of expenses",
              "3-6 months of expenses",
              "1 year of expenses",
              "No emergency fund needed"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "What is the 50/30/20 budgeting rule?",
            options: [
              "50% savings, 30% needs, 20% wants",
              "50% needs, 30% wants, 20% savings",
              "50% wants, 30% needs, 20% savings",
              "50% investments, 30% savings, 20% spending"
            ],
            correctAnswer: 1
          }
        ]
      }
    },
    {
      id: "4",
      title: "Understanding ETFs and Index Funds",
      description: "Learn about Exchange-Traded Funds and Index Funds, their benefits, and how to choose the right ones for your portfolio.",
      duration: 12,
      category: "Investment Products",
      youtubeId: "fwe-PjrX23o",
      thumbnail: "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=500&h=300&fit=crop",
      quiz: {
        questions: [
          {
            id: "q1",
            question: "What is the main advantage of index funds?",
            options: [
              "Higher returns guaranteed",
              "Low fees and broad diversification",
              "Active management",
              "Tax benefits"
            ],
            correctAnswer: 1
          },
          {
            id: "q2",
            question: "What does ETF stand for?",
            options: [
              "Exchange-Traded Fund",
              "Electronic Trading Fund",
              "Equity Transfer Fund",
              "Earnings Tax Fund"
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
              <Link to="/stocks">
                <Button variant="ghost">Stocks</Button>
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
            <TabsList className="grid w-full grid-cols-3 max-w-md mx-auto mb-12">
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

            <TabsContent value="blogs" className="space-y-8">
              <div className="text-center mb-8">
                <h2 className="text-3xl font-bold mb-4">{t('blogs')}</h2>
                <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                  Stay updated with the latest insights and strategies in financial markets.
                </p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
                {blogs.map((blog) => (
                  <BlogCard 
                    key={blog.id} 
                    blog={blog} 
                    readMoreText={t('readMore')}
                    onPlayAudio={() => handlePlayAudio(blog.id, blog.content)}
                    audioText={currentlyPlayingAudio === blog.id ? "Stop Audio" : "Play Audio"}
                    isPlaying={currentlyPlayingAudio === blog.id}
                  />
                ))}
              </div>
            </TabsContent>

            <TabsContent value="books" className="space-y-8">
              <div className="text-center mb-8">
                <h2 className="text-3xl font-bold mb-4">{t('books')}</h2>
                <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                  Essential reading for investors at every level of experience.
                </p>
              </div>
              <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
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

            <TabsContent value="videos" className="space-y-8">
              <div className="text-center mb-8">
                <h2 className="text-3xl font-bold mb-4">{t('videos')}</h2>
                <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
                  Interactive video content with quizzes to test your knowledge.
                </p>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
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