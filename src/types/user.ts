export interface User {
  id: string;
  email: string;
  name: string;
  age: number;
  country: string;
  language: string;
  financialGoals: FinancialGoal[];
  createdAt: Date;
  lastLogin: Date;
}

export interface UserProfile {
  age: number;
  country: string;
  language: string;
  financialGoals: FinancialGoal[];
}

export type FinancialGoal = 
  | 'saving_for_retirement'
  | 'buying_a_house'
  | 'emergency_fund'
  | 'debt_reduction'
  | 'investment_growth'
  | 'education_funding'
  | 'starting_a_business'
  | 'travel_goals'
  | 'general_wealth_building';

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface SignupData {
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  age: number;
  country: string;
  language: string;
  financialGoals: FinancialGoal[];
}

export const FINANCIAL_GOALS: { value: FinancialGoal; label: string; description: string }[] = [
  {
    value: 'saving_for_retirement',
    label: 'Saving for Retirement',
    description: 'Building a nest egg for your golden years'
  },
  {
    value: 'buying_a_house',
    label: 'Buying a House',
    description: 'Saving for a down payment and homeownership'
  },
  {
    value: 'emergency_fund',
    label: 'Emergency Fund',
    description: 'Building a safety net for unexpected expenses'
  },
  {
    value: 'debt_reduction',
    label: 'Debt Reduction',
    description: 'Paying off loans, credit cards, and other debts'
  },
  {
    value: 'investment_growth',
    label: 'Investment Growth',
    description: 'Growing wealth through strategic investments'
  },
  {
    value: 'education_funding',
    label: 'Education Funding',
    description: 'Saving for your or your children\'s education'
  },
  {
    value: 'starting_a_business',
    label: 'Starting a Business',
    description: 'Building capital to launch your entrepreneurial venture'
  },
  {
    value: 'travel_goals',
    label: 'Travel Goals',
    description: 'Funding your dream vacations and travel experiences'
  },
  {
    value: 'general_wealth_building',
    label: 'General Wealth Building',
    description: 'Overall financial growth and prosperity'
  }
];

export const COUNTRIES = [
  'United States', 'Canada', 'United Kingdom', 'Germany', 'France', 'Italy', 'Spain',
  'Netherlands', 'Sweden', 'Norway', 'Denmark', 'Finland', 'Switzerland', 'Austria',
  'Belgium', 'Ireland', 'Portugal', 'Australia', 'New Zealand', 'Japan', 'South Korea',
  'Singapore', 'Hong Kong', 'India', 'China', 'Brazil', 'Mexico', 'Argentina', 'Chile',
  'South Africa', 'Nigeria', 'Kenya', 'Egypt', 'Morocco', 'Israel', 'UAE', 'Saudi Arabia',
  'Russia', 'Poland', 'Czech Republic', 'Hungary', 'Romania', 'Bulgaria', 'Croatia',
  'Greece', 'Turkey', 'Thailand', 'Malaysia', 'Indonesia', 'Philippines', 'Vietnam'
];

export const LANGUAGES = [
  'English', 'Spanish', 'French', 'German', 'Italian', 'Portuguese', 'Dutch',
  'Swedish', 'Norwegian', 'Danish', 'Finnish', 'Russian', 'Polish', 'Czech',
  'Hungarian', 'Romanian', 'Bulgarian', 'Croatian', 'Greek', 'Turkish',
  'Arabic', 'Hebrew', 'Hindi', 'Chinese (Mandarin)', 'Japanese', 'Korean',
  'Thai', 'Vietnamese', 'Indonesian', 'Malay', 'Tagalog'
];