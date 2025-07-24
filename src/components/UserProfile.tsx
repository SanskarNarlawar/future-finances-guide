import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from '@/components/ui/form';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Checkbox } from '@/components/ui/checkbox';
import { Badge } from '@/components/ui/badge';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Separator } from '@/components/ui/separator';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { 
  User, 
  Edit, 
  Globe, 
  Languages, 
  Target, 
  Calendar,
  Mail,
  Loader2,
  Save,
  X
} from 'lucide-react';
import { useAuth } from '@/contexts/AuthContext';
import { User as UserType, FINANCIAL_GOALS, COUNTRIES, LANGUAGES, FinancialGoal } from '@/types/user';
import { format } from 'date-fns';

const profileUpdateSchema = z.object({
  name: z.string().min(2, 'Name must be at least 2 characters'),
  age: z.number().min(13, 'You must be at least 13 years old').max(120, 'Please enter a valid age'),
  country: z.string().min(1, 'Please select your country'),
  language: z.string().min(1, 'Please select your language'),
  financialGoals: z.array(z.string()).min(1, 'Please select at least one financial goal'),
});

type ProfileUpdateData = z.infer<typeof profileUpdateSchema>;

export const UserProfile: React.FC = () => {
  const { user, updateProfile, isLoading } = useAuth();
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false);

  const form = useForm<ProfileUpdateData>({
    resolver: zodResolver(profileUpdateSchema),
    defaultValues: {
      name: user?.name || '',
      age: user?.age || 18,
      country: user?.country || '',
      language: user?.language || '',
      financialGoals: user?.financialGoals || [],
    },
  });

  const onSubmit = async (data: ProfileUpdateData) => {
    const success = await updateProfile(data);
    if (success) {
      setIsEditDialogOpen(false);
    }
  };

  if (!user) {
    return null;
  }

  const getInitials = (name: string) => {
    return name
      .split(' ')
      .map(word => word.charAt(0))
      .join('')
      .toUpperCase()
      .slice(0, 2);
  };

  const getFinancialGoalLabels = (goals: FinancialGoal[]) => {
    return goals.map(goal => 
      FINANCIAL_GOALS.find(g => g.value === goal)?.label || goal
    );
  };

  return (
    <Card className="w-full max-w-2xl mx-auto">
      <CardHeader className="text-center">
        <div className="flex flex-col items-center space-y-4">
          <Avatar className="h-20 w-20">
            <AvatarImage src={undefined} alt={user.name} />
            <AvatarFallback className="text-lg font-semibold">
              {getInitials(user.name)}
            </AvatarFallback>
          </Avatar>
          <div>
            <CardTitle className="text-2xl">{user.name}</CardTitle>
            <p className="text-muted-foreground flex items-center justify-center mt-1">
              <Mail className="h-4 w-4 mr-1" />
              {user.email}
            </p>
          </div>
          <Dialog open={isEditDialogOpen} onOpenChange={setIsEditDialogOpen}>
            <DialogTrigger asChild>
              <Button variant="outline" size="sm">
                <Edit className="h-4 w-4 mr-2" />
                Edit Profile
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
              <DialogHeader>
                <DialogTitle>Edit Profile</DialogTitle>
                <DialogDescription>
                  Update your profile information and financial goals.
                </DialogDescription>
              </DialogHeader>
              <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
                  {/* Basic Information */}
                  <div className="space-y-4">
                    <h3 className="text-lg font-medium">Basic Information</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <FormField
                        control={form.control}
                        name="name"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Full Name</FormLabel>
                            <FormControl>
                              <Input {...field} disabled={isLoading} />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="age"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Age</FormLabel>
                            <FormControl>
                              <Input 
                                {...field} 
                                type="number" 
                                min="13"
                                max="120"
                                disabled={isLoading}
                                onChange={(e) => field.onChange(parseInt(e.target.value) || 0)}
                              />
                            </FormControl>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                    </div>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      <FormField
                        control={form.control}
                        name="country"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Country</FormLabel>
                            <Select onValueChange={field.onChange} defaultValue={field.value} disabled={isLoading}>
                              <FormControl>
                                <SelectTrigger>
                                  <SelectValue placeholder="Select your country" />
                                </SelectTrigger>
                              </FormControl>
                              <SelectContent>
                                {COUNTRIES.map((country) => (
                                  <SelectItem key={country} value={country}>
                                    {country}
                                  </SelectItem>
                                ))}
                              </SelectContent>
                            </Select>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                      <FormField
                        control={form.control}
                        name="language"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel>Language</FormLabel>
                            <Select onValueChange={field.onChange} defaultValue={field.value} disabled={isLoading}>
                              <FormControl>
                                <SelectTrigger>
                                  <SelectValue placeholder="Select your language" />
                                </SelectTrigger>
                              </FormControl>
                              <SelectContent>
                                {LANGUAGES.map((language) => (
                                  <SelectItem key={language} value={language}>
                                    {language}
                                  </SelectItem>
                                ))}
                              </SelectContent>
                            </Select>
                            <FormMessage />
                          </FormItem>
                        )}
                      />
                    </div>
                  </div>

                  {/* Financial Goals */}
                  <div className="space-y-4">
                    <h3 className="text-lg font-medium">Financial Goals</h3>
                    <FormField
                      control={form.control}
                      name="financialGoals"
                      render={() => (
                        <FormItem>
                          <FormDescription>
                            Update your financial goals to get personalized recommendations.
                          </FormDescription>
                          <div className="grid grid-cols-1 gap-3">
                            {FINANCIAL_GOALS.map((goal) => (
                              <FormField
                                key={goal.value}
                                control={form.control}
                                name="financialGoals"
                                render={({ field }) => {
                                  return (
                                    <FormItem
                                      key={goal.value}
                                      className="flex flex-row items-start space-x-3 space-y-0 p-3 border rounded-lg hover:bg-muted/50 transition-colors"
                                    >
                                      <FormControl>
                                        <Checkbox
                                          checked={field.value?.includes(goal.value)}
                                          onCheckedChange={(checked) => {
                                            const currentGoals = field.value || [];
                                            if (checked) {
                                              field.onChange([...currentGoals, goal.value]);
                                            } else {
                                              field.onChange(
                                                currentGoals.filter((value) => value !== goal.value)
                                              );
                                            }
                                          }}
                                          disabled={isLoading}
                                        />
                                      </FormControl>
                                      <div className="space-y-1 leading-none">
                                        <FormLabel className="text-sm font-medium">
                                          {goal.label}
                                        </FormLabel>
                                        <p className="text-xs text-muted-foreground">
                                          {goal.description}
                                        </p>
                                      </div>
                                    </FormItem>
                                  );
                                }}
                              />
                            ))}
                          </div>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>

                  <div className="flex justify-end space-x-2">
                    <Button 
                      type="button" 
                      variant="outline" 
                      onClick={() => setIsEditDialogOpen(false)}
                      disabled={isLoading}
                    >
                      <X className="h-4 w-4 mr-2" />
                      Cancel
                    </Button>
                    <Button type="submit" disabled={isLoading}>
                      {isLoading ? (
                        <>
                          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                          Saving...
                        </>
                      ) : (
                        <>
                          <Save className="h-4 w-4 mr-2" />
                          Save Changes
                        </>
                      )}
                    </Button>
                  </div>
                </form>
              </Form>
            </DialogContent>
          </Dialog>
        </div>
      </CardHeader>
      <CardContent className="space-y-6">
        {/* Personal Information */}
        <div>
          <h3 className="text-lg font-medium mb-3 flex items-center">
            <User className="h-5 w-5 mr-2" />
            Personal Information
          </h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="flex items-center space-x-2">
              <Calendar className="h-4 w-4 text-muted-foreground" />
              <span className="text-sm">Age: {user.age}</span>
            </div>
            <div className="flex items-center space-x-2">
              <Globe className="h-4 w-4 text-muted-foreground" />
              <span className="text-sm">Country: {user.country}</span>
            </div>
            <div className="flex items-center space-x-2">
              <Languages className="h-4 w-4 text-muted-foreground" />
              <span className="text-sm">Language: {user.language}</span>
            </div>
          </div>
        </div>

        <Separator />

        {/* Financial Goals */}
        <div>
          <h3 className="text-lg font-medium mb-3 flex items-center">
            <Target className="h-5 w-5 mr-2" />
            Financial Goals
          </h3>
          <div className="flex flex-wrap gap-2">
            {getFinancialGoalLabels(user.financialGoals).map((goal, index) => (
              <Badge key={index} variant="secondary">
                {goal}
              </Badge>
            ))}
          </div>
        </div>

        <Separator />

        {/* Account Information */}
        <div>
          <h3 className="text-lg font-medium mb-3">Account Information</h3>
          <div className="space-y-2 text-sm text-muted-foreground">
            <p>Member since: {format(user.createdAt, 'MMMM d, yyyy')}</p>
            <p>Last login: {format(user.lastLogin, 'MMMM d, yyyy \'at\' h:mm a')}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};