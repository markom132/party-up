import React from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
} from 'react-router-dom';
import LoginForm from './components/LoginForm/LoginForm';
import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import HomePage from './pages/Home';
import RegisterForm from './components/RegisterForm/RegisterForm';
import { AuthProvider } from './context/AuthContext';

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Header />
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<LoginForm />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/create-account" element={<RegisterForm />} />
        </Routes>
        <Footer />
      </Router>
    </AuthProvider>
  );
};

export default App;
