import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Calendar, Clock, User } from "lucide-react";

export interface BlogPost {
  id: string;
  title: string;
  excerpt: string;
  author: string;
  date: string;
  readTime: number;
  category: string;
  image: string;
}

interface BlogCardProps {
  blog: BlogPost;
  readMoreText: string;
}

export const BlogCard = ({ blog, readMoreText }: BlogCardProps) => {
  return (
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
        <Button variant="outline" className="w-full group-hover:bg-primary group-hover:text-primary-foreground transition-colors">
          {readMoreText}
        </Button>
      </CardContent>
    </Card>
  );
};