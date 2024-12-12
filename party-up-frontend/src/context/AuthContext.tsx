import React, { createContext, useState, useContext } from 'react';

interface AuthContextType {
  isLoggedIn: boolean;
  // eslint-disable-next-line no-unused-vars
  setIsLoggedIn: (value: boolean) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const savedLoginState = localStorage.getItem('isLoggedIn') === 'true';
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(savedLoginState);

  const updateLoginState = (value: boolean) => {
    setIsLoggedIn(value);
    localStorage.setItem('isLoggedIn', value.toString());
  };

  return (
    <AuthContext.Provider
      value={{ isLoggedIn, setIsLoggedIn: updateLoginState }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the AuthContext
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

// Export AuthContext for testing purposes
export { AuthContext };
