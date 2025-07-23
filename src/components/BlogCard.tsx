import { useState } from "react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Calendar, Clock, User, X, Volume2 } from "lucide-react";

export interface BlogPost {
  id: string;
  title: string;
  excerpt: string;
  author: string;
  date: string;
  readTime: number;
  category: string;
  image: string;
  content: string;
}

interface BlogCardProps {
  blog: BlogPost;
  readMoreText: string;
  onPlayAudio?: () => void;
  audioText?: string;
}

export const BlogCard = ({ blog, readMoreText, onPlayAudio, audioText }: BlogCardProps) => {
  const [showFullBlog, setShowFullBlog] = useState(false);

  return (
    <>
      <Dialog open={showFullBlog} onOpenChange={setShowFullBlog}>
        <DialogContent className="max-w-4xl max-h-[80vh]">
          <DialogHeader>
            <div className="flex items-center justify-between">
              <DialogTitle className="text-2xl pr-8">{blog.title}</DialogTitle>
              <Button 
                variant="ghost" 
                size="icon" 
                onClick={() => setShowFullBlog(false)}
              >
                <X className="h-4 w-4" />
              </Button>
            </div>
            <div className="flex items-center gap-4 text-sm text-muted-foreground pt-2">
              <div className="flex items-center gap-1">
                <User className="h-4 w-4" />
                {blog.author}
              </div>
              <div className="flex items-center gap-1">
                <Calendar className="h-4 w-4" />
                {blog.date}
              </div>
              <div className="flex items-center gap-1">
                <Clock className="h-4 w-4" />
                {blog.readTime} min
              </div>
            </div>
          </DialogHeader>
          <ScrollArea className="max-h-[60vh]">
            <div className="space-y-4">
              <img 
                src={blog.image} 
                alt={blog.title}
                className="w-full h-64 object-cover rounded-lg"
              />
              <div className="prose max-w-none text-foreground">
                {blog.content.split('\n\n').map((paragraph, index) => (
                  <p key={index} className="mb-4 leading-relaxed">
                    {paragraph}
                  </p>
                ))}
              </div>
            </div>
          </ScrollArea>
        </DialogContent>
      </Dialog>

      <Card className="shadow-card hover:shadow-elevated transition-all duration-300 overflow-hidden group">
      <div className="relative overflow-hidden">
        <img 
          src={blog.image} 
          alt={blog.title}
          className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
        />
        <Badge className="absolute top-4 left-4 bg-primary text-primary-foreground">
          {blog.category}
        </Badge>
      </div>
      <CardHeader>
        <CardTitle className="text-xl line-clamp-2 group-hover:text-primary transition-colors">
          {blog.title}
        </CardTitle>
        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <div className="flex items-center gap-1">
            <User className="h-4 w-4" />
            {blog.author}
          </div>
          <div className="flex items-center gap-1">
            <Calendar className="h-4 w-4" />
            {blog.date}
          </div>
          <div className="flex items-center gap-1">
            <Clock className="h-4 w-4" />
            {blog.readTime} min
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <CardDescription className="text-base line-clamp-3 mb-4">
          {blog.excerpt}
        </CardDescription>
        <div className="flex gap-2">
          <Button 
            variant="outline" 
            className="flex-1 group-hover:bg-primary group-hover:text-primary-foreground transition-colors"
            onClick={() => setShowFullBlog(true)}
          >
            {readMoreText}
          </Button>
          {onPlayAudio && (
            <Button 
              variant="outline" 
              size="icon"
              className="group-hover:bg-primary group-hover:text-primary-foreground transition-colors"
              onClick={onPlayAudio}
              title={audioText || "Play Audio"}
            >
              <Volume2 className="h-4 w-4" />
            </Button>
          )}
        </div>
        </CardContent>
      </Card>
    </>
  );
};