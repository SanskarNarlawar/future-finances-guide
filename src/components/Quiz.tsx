import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { CheckCircle, XCircle, ArrowLeft, ArrowRight } from "lucide-react";

interface Question {
  id: string;
  question: string;
  options: string[];
  correctAnswer: number;
}

interface QuizProps {
  questions: Question[];
  onComplete: (score: number, total: number) => void;
  onClose: () => void;
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

export const Quiz = ({ questions, onComplete, onClose, translations }: QuizProps) => {
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState<number[]>(new Array(questions.length).fill(-1));
  const [showResults, setShowResults] = useState(false);
  const [quizScore, setQuizScore] = useState(0);

  const handleAnswerSelect = (answerIndex: number) => {
    const newAnswers = [...selectedAnswers];
    newAnswers[currentQuestion] = answerIndex;
    setSelectedAnswers(newAnswers);
  };

  const handleNext = () => {
    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion(currentQuestion + 1);
    }
  };

  const handlePrevious = () => {
    if (currentQuestion > 0) {
      setCurrentQuestion(currentQuestion - 1);
    }
  };

  const handleSubmit = () => {
    const score = selectedAnswers.reduce((acc, answer, index) => {
      return acc + (answer === questions[index].correctAnswer ? 1 : 0);
    }, 0);
    setQuizScore(score);
    setShowResults(true);
  };

  const handleTryAgain = () => {
    setCurrentQuestion(0);
    setSelectedAnswers(new Array(questions.length).fill(-1));
    setShowResults(false);
    setQuizScore(0);
  };

  const handleFinish = () => {
    onComplete(quizScore, questions.length);
  };

  const progress = ((currentQuestion + 1) / questions.length) * 100;
  const allAnswered = selectedAnswers.every(answer => answer !== -1);

  if (showResults) {
    return (
      <Card className="max-w-2xl mx-auto shadow-elevated">
        <CardHeader className="text-center">
          <CardTitle className="text-2xl">{translations.quiz} {translations.score}</CardTitle>
          <div className="text-4xl font-bold text-primary my-4">
            {quizScore}/{questions.length}
          </div>
          <CardDescription className="text-lg">
            {quizScore >= questions.length * 0.7 ? (
              <div className="flex items-center justify-center gap-2 text-success">
                <CheckCircle className="h-5 w-5" />
                {translations.correct}
              </div>
            ) : (
              <div className="flex items-center justify-center gap-2 text-destructive">
                <XCircle className="h-5 w-5" />
                {translations.incorrect}
              </div>
            )}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="space-y-4">
            {questions.map((question, index) => (
              <Card key={question.id} className="p-4">
                <div className="flex items-start gap-2">
                  {selectedAnswers[index] === question.correctAnswer ? (
                    <CheckCircle className="h-5 w-5 text-success mt-0.5" />
                  ) : (
                    <XCircle className="h-5 w-5 text-destructive mt-0.5" />
                  )}
                  <div className="flex-1">
                    <p className="font-medium mb-2">{question.question}</p>
                    <p className="text-sm text-muted-foreground">
                      {translations.correct}: {question.options[question.correctAnswer]}
                    </p>
                    {selectedAnswers[index] !== question.correctAnswer && (
                      <p className="text-sm text-destructive">
                        Your answer: {question.options[selectedAnswers[index]]}
                      </p>
                    )}
                  </div>
                </div>
              </Card>
            ))}
          </div>
          <div className="flex gap-2 mt-6">
            <Button onClick={handleTryAgain} variant="outline" className="flex-1">
              {translations.tryAgain}
            </Button>
            <Button onClick={handleFinish} className="flex-1">
              Continue Learning
            </Button>
          </div>
        </CardContent>
      </Card>
    );
  }

  const question = questions[currentQuestion];

  return (
    <Card className="max-w-2xl mx-auto shadow-elevated">
      <CardHeader>
        <div className="flex items-center justify-between">
          <Button variant="ghost" size="sm" onClick={onClose}>
            <ArrowLeft className="h-4 w-4 mr-2" />
            Back
          </Button>
          <Badge variant="outline">
            {translations.question} {currentQuestion + 1} {translations.of} {questions.length}
          </Badge>
        </div>
        <Progress value={progress} className="w-full" />
        <CardTitle className="text-xl mt-4">{question.question}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-3 mb-6">
          {question.options.map((option, index) => (
            <Button
              key={index}
              variant={selectedAnswers[currentQuestion] === index ? "default" : "outline"}
              className="w-full text-left justify-start h-auto p-4"
              onClick={() => handleAnswerSelect(index)}
            >
              <span className="mr-3 w-6 h-6 rounded-full border-2 flex items-center justify-center text-sm">
                {String.fromCharCode(65 + index)}
              </span>
              {option}
            </Button>
          ))}
        </div>
        
        <div className="flex justify-between">
          <Button
            variant="outline"
            onClick={handlePrevious}
            disabled={currentQuestion === 0}
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            {translations.previousQuestion}
          </Button>
          
          {currentQuestion === questions.length - 1 ? (
            <Button
              onClick={handleSubmit}
              disabled={!allAnswered}
              className="bg-success hover:bg-success/90"
            >
              {translations.submitQuiz}
            </Button>
          ) : (
            <Button
              onClick={handleNext}
              disabled={selectedAnswers[currentQuestion] === -1}
            >
              {translations.nextQuestion}
              <ArrowRight className="h-4 w-4 ml-2" />
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
};