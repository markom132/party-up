import React, { useState } from 'react';
import { loginUser } from '../services/authService';
import '../assets/LoginForm.css';

const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState({ username: '', password: '', api: '' });
  const [isLaoding, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    let hasError = false;
    const newError = { username: '', password: '', api: '' };

    if (!username) {
      newError.username = 'Please enter your username';
      hasError = true;
    } else if (username.length < 5 || username.length > 30) {
      newError.username = 'Invalid username';
      hasError = true;
    }

    if (!password) {
      newError.password = 'Please enter your password';
      hasError = true;
    } else if (password.length < 8) {
      newError.password = 'Invalid password';
      hasError = true;
    }

    setError(newError);

    if (!hasError) {
      setIsLoading(true);
      try {
        const data = await loginUser(username, password);
        setSuccessMessage('Login successful');
        console.log('Logged in successfully:', data);
      } catch (error) {
        setError((prevError) => ({ ...prevError, api: error.message }));
        setSuccessMessage('');
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className="login-container">
      <div className="login-form">
        <h1 className="form-header">Login</h1>
        <form onSubmit={handleSubmit}>
          <div className="input-container">
            <i className="fas fa-user"></i>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              className={error.username ? 'error-input' : ''}
            />
            {error.username && <p className="error-text">{error.username}</p>}
          </div>
          <div className="input-container">
            <i className="fas fa-lock"></i>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              className={error.password ? 'error-input' : ''}
            />
            {error.password && <p className="error-text">{error.password}</p>}
          </div>
          {error.api && <p className="error-text">{error.api}</p>}
          {successMessage && <p className="success-text">{successMessage}</p>}
          <button type="submit" disabled={isLaoding}>
            {isLaoding ? <span className="spinner"></span> : 'Login'}
          </button>
          <p className="account-options">
            <a href="#forgot-password" className="forgot-password">
              Forgot Password?
            </a>
            <span className="divider">|</span>
            <a href="#create-account" className="create-account">
              Create Account
            </a>
          </p>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
