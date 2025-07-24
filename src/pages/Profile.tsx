import React from 'react';
import { UserProfile } from '@/components/UserProfile';

const Profile: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900">Your Profile</h1>
          <p className="text-gray-600 mt-2">
            Manage your account information and financial preferences
          </p>
        </div>
        <UserProfile />
      </div>
    </div>
  );
};

export default Profile;