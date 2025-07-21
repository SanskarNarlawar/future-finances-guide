import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Play, Clock, CheckCircle } from "lucide-react";
import { Quiz } from "./Quiz";

export interface Video {
  id: string;
  title: string;
  description: string;
  duration: number;
  thumbnail: string;
  category: string;
  quiz: {
    questions: {
      id: string;
      question: string;
      options: string[];
      correctAnswer: number;
    }[];
  };
}

interface VideoCardProps {
  video: Video;
  minutesText: string;
  watchVideoText: string;
  takeQuizText: string;
  onQuizComplete: (score: number, total: number) => void;
  translations: {
    quiz: string;
    nextQuestion: string;
    previousQuestion: string;
    submitQuiz: string;
    score: string;
    correct: string;
    incorrect: string;
    tryAgain: string;
    question: string;
    of: string;
  };
}

export const VideoCard = ({ 
  video, 
  minutesText, 
  watchVideoText, 
  takeQuizText, 
  onQuizComplete,
  translations 
}: VideoCardProps) => {
  const [isWatched, setIsWatched] = useState(false);
  const [showQuiz, setShowQuiz] = useState(false);
  const [quizCompleted, setQuizCompleted] = useState(false);

  const handleWatchVideo = () => {
    setIsWatched(true);
  };

  const handleTakeQuiz = () => {
    setShowQuiz(true);
  };

  const handleQuizComplete = (score: number, total: number) => {
    setQuizCompleted(true);
    setShowQuiz(false);
    onQuizComplete(score, total);
  };

  if (showQuiz) {
    return (
      <Quiz 
        questions={video.quiz.questions}
        onComplete={handleQuizComplete}
        onClose={() => setShowQuiz(false)}
        translations={translations}
      />
    );
  }

  return (
    <Card className="shadow-card hover:shadow-elevated transition-all duration-300 overflow-hidden group">
      <div className="relative">
        <img 
          src={video.thumbnail} 
          alt={video.title}
          className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
        />
        <div className="absolute inset-0 bg-black/20 group-hover:bg-black/10 transition-colors" />
        <Badge className="absolute top-4 left-4 bg-primary text-primary-foreground">
          {video.category}
        </Badge>
        <div className="absolute bottom-4 right-4 bg-black/70 text-white px-2 py-1 rounded text-sm flex items-center gap-1">
          <Clock className="h-3 w-3" />
          {video.duration} {minutesText}
        </div>
        {!isWatched && (
          <Button
            size="icon"
            className="absolute inset-0 m-auto w-16 h-16 rounded-full bg-primary/90 hover:bg-primary shadow-glow"
            onClick={handleWatchVideo}
          >
            <Play className="h-8 w-8 ml-1" />
          </Button>
        )}
        {isWatched && (
          <div className="absolute top-4 right-4 bg-success text-success-foreground p-2 rounded-full">
            <CheckCircle className="h-4 w-4" />
          </div>
        )}
      </div>
      <CardHeader>
        <CardTitle className="text-xl line-clamp-2 group-hover:text-primary transition-colors">
          {video.title}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <CardDescription className="text-base line-clamp-3 mb-4">
          {video.description}
        </CardDescription>
        <div className="flex gap-2">
          {!isWatched ? (
            <Button 
              onClick={handleWatchVideo}
              className="flex-1"
            >
              <Play className="mr-2 h-4 w-4" />
              {watchVideoText}
            </Button>
          ) : (
            <Button 
              onClick={handleTakeQuiz}
              variant={quizCompleted ? "secondary" : "default"}
              className="flex-1"
              disabled={quizCompleted}
            >
              {quizCompleted ? (
                <CheckCircle className="mr-2 h-4 w-4" />
              ) : null}
              {takeQuizText}
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
};