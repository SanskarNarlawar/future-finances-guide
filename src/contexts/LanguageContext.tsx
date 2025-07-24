import React, { createContext, useContext, useState, ReactNode } from 'react';

interface LanguageContextType {
  conversationLanguage: string;
  setConversationLanguage: (language: string) => void;
  detectLanguage: (text: string) => string;
}

const LanguageContext = createContext<LanguageContextType | undefined>(undefined);

export const useLanguageContext = () => {
  const context = useContext(LanguageContext);
  if (!context) {
    throw new Error('useLanguageContext must be used within a LanguageProvider');
  }
  return context;
};

interface LanguageProviderProps {
  children: ReactNode;
}

// Simple language detection based on common patterns
const detectLanguageFromText = (text: string): string => {
  const lowerText = text.toLowerCase();
  
  // English patterns
  if (/\b(the|and|or|but|in|on|at|to|for|of|with|by)\b/.test(lowerText)) {
    return 'en';
  }
  
  // Spanish patterns
  if (/\b(el|la|los|las|un|una|y|o|pero|en|con|por|para|de)\b/.test(lowerText) ||
      /[ñáéíóúü]/.test(lowerText)) {
    return 'es';
  }
  
  // French patterns
  if (/\b(le|la|les|un|une|et|ou|mais|dans|avec|par|pour|de)\b/.test(lowerText) ||
      /[àâäçéèêëïîôùûüÿ]/.test(lowerText)) {
    return 'fr';
  }
  
  // German patterns
  if (/\b(der|die|das|und|oder|aber|in|mit|von|zu|für)\b/.test(lowerText) ||
      /[äöüß]/.test(lowerText)) {
    return 'de';
  }
  
  // Hindi patterns (Devanagari script)
  if (/[\u0900-\u097F]/.test(text)) {
    return 'hi';
  }
  
  // Telugu patterns
  if (/[\u0C00-\u0C7F]/.test(text)) {
    return 'te';
  }
  
  // Kannada patterns
  if (/[\u0C80-\u0CFF]/.test(text)) {
    return 'kn';
  }
  
  // Chinese patterns (simplified and traditional)
  if (/[\u4e00-\u9fff]/.test(text)) {
    return 'zh';
  }
  
  // Japanese patterns (hiragana, katakana, kanji)
  if (/[\u3040-\u309f\u30a0-\u30ff\u4e00-\u9fff]/.test(text)) {
    return 'ja';
  }
  
  // Default to English if no pattern matches
  return 'en';
};

export const LanguageProvider: React.FC<LanguageProviderProps> = ({ children }) => {
  const [conversationLanguage, setConversationLanguage] = useState<string>('en');

  const detectLanguage = (text: string): string => {
    return detectLanguageFromText(text);
  };

  return (
    <LanguageContext.Provider
      value={{
        conversationLanguage,
        setConversationLanguage,
        detectLanguage
      }}
    >
      {children}
    </LanguageContext.Provider>
  );
};