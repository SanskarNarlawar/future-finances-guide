import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { User, AuthState, LoginCredentials, SignupData } from '@/types/user';
import { useToast } from '@/hooks/use-toast';

interface AuthContextType extends AuthState {
  login: (credentials: LoginCredentials) => Promise<boolean>;
  signup: (data: SignupData) => Promise<boolean>;
  logout: () => void;
  updateProfile: (updates: Partial<User>) => Promise<boolean>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const STORAGE_KEY = 'financeApp_currentUser';

// Simulate API delay
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [authState, setAuthState] = useState<AuthState>({
    user: null,
    isAuthenticated: false,
    isLoading: true,
  });
  const { toast } = useToast();

  // Load user from localStorage on app start
  useEffect(() => {
    const loadUser = () => {
      try {
        const storedUser = localStorage.getItem(STORAGE_KEY);
        if (storedUser) {
          const user = JSON.parse(storedUser);
          // Convert date strings back to Date objects
          user.createdAt = new Date(user.createdAt);
          user.lastLogin = new Date(user.lastLogin);
          
          setAuthState({
            user,
            isAuthenticated: true,
            isLoading: false,
          });
        } else {
          setAuthState(prev => ({ ...prev, isLoading: false }));
        }
      } catch (error) {
        console.error('Error loading user from storage:', error);
        setAuthState(prev => ({ ...prev, isLoading: false }));
      }
    };

    loadUser();
  }, []);

  const login = async (credentials: LoginCredentials): Promise<boolean> => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true }));
      
      // Simulate API call
      await delay(1000);
      
      // Get all users from localStorage
      const usersData = localStorage.getItem('financeApp_users');
      const users: User[] = usersData ? JSON.parse(usersData) : [];
      
      // Find user by email
      const user = users.find(u => u.email === credentials.email);
      
      if (!user) {
        toast({
          title: "Login Failed",
          description: "No account found with this email address.",
          variant: "destructive",
        });
        setAuthState(prev => ({ ...prev, isLoading: false }));
        return false;
      }
      
      // In a real app, you'd verify the password hash
      // For demo purposes, we'll simulate password validation
      if (credentials.password.length < 6) {
        toast({
          title: "Login Failed",
          description: "Invalid credentials.",
          variant: "destructive",
        });
        setAuthState(prev => ({ ...prev, isLoading: false }));
        return false;
      }
      
      // Update last login
      user.lastLogin = new Date();
      
      // Update users array
      const updatedUsers = users.map(u => u.id === user.id ? user : u);
      localStorage.setItem('financeApp_users', JSON.stringify(updatedUsers));
      
      // Set current user
      localStorage.setItem(STORAGE_KEY, JSON.stringify(user));
      
      setAuthState({
        user,
        isAuthenticated: true,
        isLoading: false,
      });
      
      toast({
        title: "Welcome back!",
        description: `Logged in successfully as ${user.name}`,
      });
      
      return true;
    } catch (error) {
      console.error('Login error:', error);
      toast({
        title: "Login Failed",
        description: "An unexpected error occurred. Please try again.",
        variant: "destructive",
      });
      setAuthState(prev => ({ ...prev, isLoading: false }));
      return false;
    }
  };

  const signup = async (data: SignupData): Promise<boolean> => {
    try {
      setAuthState(prev => ({ ...prev, isLoading: true }));
      
      // Validate passwords match
      if (data.password !== data.confirmPassword) {
        toast({
          title: "Signup Failed",
          description: "Passwords do not match.",
          variant: "destructive",
        });
        setAuthState(prev => ({ ...prev, isLoading: false }));
        return false;
      }
      
      // Simulate API call
      await delay(1500);
      
      // Get existing users
      const usersData = localStorage.getItem('financeApp_users');
      const users: User[] = usersData ? JSON.parse(usersData) : [];
      
      // Check if email already exists
      if (users.some(u => u.email === data.email)) {
        toast({
          title: "Signup Failed",
          description: "An account with this email already exists.",
          variant: "destructive",
        });
        setAuthState(prev => ({ ...prev, isLoading: false }));
        return false;
      }
      
      // Create new user
      const newUser: User = {
        id: crypto.randomUUID(),
        email: data.email,
        name: data.name,
        age: data.age,
        country: data.country,
        language: data.language,
        financialGoals: data.financialGoals,
        createdAt: new Date(),
        lastLogin: new Date(),
      };
      
      // Save to users array
      users.push(newUser);
      localStorage.setItem('financeApp_users', JSON.stringify(users));
      
      // Set as current user
      localStorage.setItem(STORAGE_KEY, JSON.stringify(newUser));
      
      setAuthState({
        user: newUser,
        isAuthenticated: true,
        isLoading: false,
      });
      
      toast({
        title: "Account Created!",
        description: `Welcome ${newUser.name}! Your account has been created successfully.`,
      });
      
      return true;
    } catch (error) {
      console.error('Signup error:', error);
      toast({
        title: "Signup Failed",
        description: "An unexpected error occurred. Please try again.",
        variant: "destructive",
      });
      setAuthState(prev => ({ ...prev, isLoading: false }));
      return false;
    }
  };

  const logout = () => {
    localStorage.removeItem(STORAGE_KEY);
    setAuthState({
      user: null,
      isAuthenticated: false,
      isLoading: false,
    });
    
    toast({
      title: "Logged Out",
      description: "You have been successfully logged out.",
    });
  };

  const updateProfile = async (updates: Partial<User>): Promise<boolean> => {
    try {
      if (!authState.user) return false;
      
      setAuthState(prev => ({ ...prev, isLoading: true }));
      
      // Simulate API call
      await delay(800);
      
      const updatedUser = { ...authState.user, ...updates };
      
      // Update in users array
      const usersData = localStorage.getItem('financeApp_users');
      const users: User[] = usersData ? JSON.parse(usersData) : [];
      const updatedUsers = users.map(u => u.id === updatedUser.id ? updatedUser : u);
      localStorage.setItem('financeApp_users', JSON.stringify(updatedUsers));
      
      // Update current user
      localStorage.setItem(STORAGE_KEY, JSON.stringify(updatedUser));
      
      setAuthState({
        user: updatedUser,
        isAuthenticated: true,
        isLoading: false,
      });
      
      toast({
        title: "Profile Updated",
        description: "Your profile has been updated successfully.",
      });
      
      return true;
    } catch (error) {
      console.error('Profile update error:', error);
      toast({
        title: "Update Failed",
        description: "Failed to update profile. Please try again.",
        variant: "destructive",
      });
      setAuthState(prev => ({ ...prev, isLoading: false }));
      return false;
    }
  };

  const value: AuthContextType = {
    ...authState,
    login,
    signup,
    logout,
    updateProfile,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};