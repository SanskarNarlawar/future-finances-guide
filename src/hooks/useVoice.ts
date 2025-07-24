import { useState, useEffect, useCallback } from 'react';
import { useToast } from '@/hooks/use-toast';

interface UseVoiceProps {
  language?: string;
  onTranscriptReceived?: (transcript: string) => void;
}

interface VoiceSettings {
  lang: string;
  rate: number;
  pitch: number;
  volume: number;
}

const languageMap: Record<string, string> = {
  en: 'en-US',
  es: 'es-ES',
  fr: 'fr-FR',
  de: 'de-DE',
  hi: 'hi-IN',
  te: 'te-IN',
  kn: 'kn-IN',
  zh: 'zh-CN',
  ja: 'ja-JP'
};

export const useVoice = ({ language = 'en', onTranscriptReceived }: UseVoiceProps = {}) => {
  const { toast } = useToast();
  const [isListening, setIsListening] = useState(false);
  const [isSupported, setIsSupported] = useState(false);
  const [isSpeechSupported, setIsSpeechSupported] = useState(false);
  const [recognition, setRecognition] = useState<SpeechRecognition | null>(null);
  const [currentUtterance, setCurrentUtterance] = useState<SpeechSynthesisUtterance | null>(null);
  const [isSpeaking, setIsSpeaking] = useState(false);

  const voiceSettings: VoiceSettings = {
    lang: languageMap[language] || 'en-US',
    rate: 0.9,
    pitch: 1.0,
    volume: 0.8
  };

  // Initialize speech recognition
  useEffect(() => {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    
    if (SpeechRecognition) {
      setIsSupported(true);
      const recognitionInstance = new SpeechRecognition();
      
      recognitionInstance.continuous = false;
      recognitionInstance.interimResults = false;
      recognitionInstance.lang = voiceSettings.lang;

      recognitionInstance.onresult = (event) => {
        const transcript = event.results[0][0].transcript;
        onTranscriptReceived?.(transcript);
        setIsListening(false);
      };

      recognitionInstance.onerror = (event) => {
        console.error('Speech recognition error:', event.error);
        setIsListening(false);
        
        let errorMessage = 'Speech recognition failed';
        switch (event.error) {
          case 'no-speech':
            errorMessage = 'No speech detected. Please try again.';
            break;
          case 'audio-capture':
            errorMessage = 'Microphone not accessible. Please check permissions.';
            break;
          case 'not-allowed':
            errorMessage = 'Microphone permission denied.';
            break;
          case 'network':
            errorMessage = 'Network error occurred during speech recognition.';
            break;
        }
        
        toast({
          title: 'Voice Recognition Error',
          description: errorMessage,
          variant: 'destructive'
        });
      };

      recognitionInstance.onend = () => {
        setIsListening(false);
      };

      setRecognition(recognitionInstance);
    } else {
      setIsSupported(false);
    }

    // Check text-to-speech support
    if ('speechSynthesis' in window) {
      setIsSpeechSupported(true);
    }
  }, [language, onTranscriptReceived, toast, voiceSettings.lang]);

  const startListening = useCallback(() => {
    if (!recognition || isListening) return;

    try {
      setIsListening(true);
      recognition.start();
      
      toast({
        title: 'Listening...',
        description: 'Speak now. Click the microphone again to stop.',
      });
    } catch (error) {
      console.error('Error starting speech recognition:', error);
      setIsListening(false);
      toast({
        title: 'Error',
        description: 'Could not start voice recognition. Please try again.',
        variant: 'destructive'
      });
    }
  }, [recognition, isListening, toast]);

  const stopListening = useCallback(() => {
    if (recognition && isListening) {
      recognition.stop();
      setIsListening(false);
    }
  }, [recognition, isListening]);

  const speak = useCallback((text: string) => {
    if (!isSpeechSupported || !text.trim()) return;

    // Stop any ongoing speech
    if (currentUtterance) {
      window.speechSynthesis.cancel();
      setCurrentUtterance(null);
      setIsSpeaking(false);
    }

    const utterance = new SpeechSynthesisUtterance(text);
    utterance.lang = voiceSettings.lang;
    utterance.rate = voiceSettings.rate;
    utterance.pitch = voiceSettings.pitch;
    utterance.volume = voiceSettings.volume;

    // Try to find a voice that matches the language
    const voices = window.speechSynthesis.getVoices();
    const matchingVoice = voices.find(voice => 
      voice.lang.startsWith(voiceSettings.lang.split('-')[0])
    );
    
    if (matchingVoice) {
      utterance.voice = matchingVoice;
    }

    utterance.onstart = () => {
      setIsSpeaking(true);
      setCurrentUtterance(utterance);
    };

    utterance.onend = () => {
      setIsSpeaking(false);
      setCurrentUtterance(null);
    };

    utterance.onerror = (event) => {
      console.error('Speech synthesis error:', event.error);
      setIsSpeaking(false);
      setCurrentUtterance(null);
      toast({
        title: 'Speech Error',
        description: 'Could not play audio. Please try again.',
        variant: 'destructive'
      });
    };

    window.speechSynthesis.speak(utterance);
  }, [isSpeechSupported, currentUtterance, voiceSettings, toast]);

  const stopSpeaking = useCallback(() => {
    if (currentUtterance && isSpeaking) {
      window.speechSynthesis.cancel();
      setIsSpeaking(false);
      setCurrentUtterance(null);
    }
  }, [currentUtterance, isSpeaking]);

  const toggleListening = useCallback(() => {
    if (isListening) {
      stopListening();
    } else {
      startListening();
    }
  }, [isListening, startListening, stopListening]);

  const toggleSpeaking = useCallback((text: string) => {
    if (isSpeaking) {
      stopSpeaking();
    } else {
      speak(text);
    }
  }, [isSpeaking, speak, stopSpeaking]);

  return {
    isListening,
    isSupported,
    isSpeechSupported,
    isSpeaking,
    startListening,
    stopListening,
    toggleListening,
    speak,
    stopSpeaking,
    toggleSpeaking
  };
};