import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { BookOpen, Star } from "lucide-react";

export interface Book {
  id: string;
  title: string;
  author: string;
  description: string;
  rating: number;
  pages: number;
  difficulty: 'beginner' | 'intermediate' | 'advanced';
  category: string;
  cover: string;
}

interface BookCardProps {
  book: Book;
  authorText: string;
  pagesText: string;
  difficultyText: string;
  difficultyLabels: {
    beginner: string;
    intermediate: string;
    advanced: string;
  };
}

export const BookCard = ({ book, authorText, pagesText, difficultyText, difficultyLabels }: BookCardProps) => {
  const getDifficultyColor = (difficulty: string) => {
    switch (difficulty) {
      case 'beginner': return 'bg-success text-success-foreground';
      case 'intermediate': return 'bg-warning text-warning-foreground';
      case 'advanced': return 'bg-destructive text-destructive-foreground';
      default: return 'bg-muted text-muted-foreground';
    }
  };

  return (
    <Card className="shadow-card hover:shadow-elevated transition-all duration-300 overflow-hidden group">
      <div className="flex">
        <div className="w-32 flex-shrink-0">
          <img 
            src={book.cover} 
            alt={book.title}
            className="w-full h-48 object-cover"
          />
        </div>
        <div className="flex-1">
          <CardHeader className="pb-2">
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <CardTitle className="text-lg line-clamp-2 group-hover:text-primary transition-colors">
                  {book.title}
                </CardTitle>
                <p className="text-sm text-muted-foreground mt-1">
                  {authorText}: {book.author}
                </p>
              </div>
              <div className="flex items-center gap-1 text-warning ml-2">
                <Star className="h-4 w-4 fill-current" />
                <span className="text-sm font-medium">{book.rating}</span>
              </div>
            </div>
          </CardHeader>
          <CardContent className="pt-0">
            <CardDescription className="text-sm line-clamp-3 mb-3">
              {book.description}
            </CardDescription>
            <div className="flex items-center gap-2 text-xs text-muted-foreground mb-2">
              <BookOpen className="h-3 w-3" />
              <span>{book.pages} {pagesText}</span>
            </div>
            <div className="flex items-center gap-2">
              <Badge variant="outline" className="text-xs">
                {book.category}
              </Badge>
              <Badge className={`text-xs ${getDifficultyColor(book.difficulty)}`}>
                {difficultyLabels[book.difficulty]}
              </Badge>
            </div>
          </CardContent>
        </div>
      </div>
    </Card>
  );
};