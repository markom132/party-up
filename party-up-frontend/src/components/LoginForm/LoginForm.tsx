import React, { useState } from 'react';
import { loginUser } from '../../services/authService';
import styles from './LoginForm.module.scss';

interface ErrorState {
  username: string;
  password: string;
  api: string;
}

const LoginForm: React.FC = () => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<ErrorState>({
    username: '',
    password: '',
    api: '',
  });
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [successMessage, setSuccessMessage] = useState<string>('');

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    let hasError = false;
    const newError: ErrorState = { username: '', password: '', api: '' };

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
      } catch (error: any) {
        setError((prevError) => ({ ...prevError, api: error.message }));
        setSuccessMessage('');
      } finally {
        setIsLoading(false);
      }
    }
  };

  return (
    <div className={styles['login-container']}>
      <div className={styles['login-form']}>
        <h1 className={styles['form-header']}>Login</h1>
        <form onSubmit={handleSubmit}>
          <div className={styles['input-container']}>
            <i className="fas fa-user"></i>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              className={error.username ? styles['error-input'] : ''}
            />
            {error.username && (
              <p className={styles['error-text']}>{error.username}</p>
            )}
          </div>
          <div className={styles['input-container']}>
            <i className="fas fa-lock"></i>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              className={error.password ? styles['error-input'] : ''}
            />
            {error.password && (
              <p className={styles['error-text']}>{error.password}</p>
            )}
          </div>
          {error.api && <p className={styles['error-text']}>{error.api}</p>}
          {successMessage && (
            <p className={styles['success-text']}>{successMessage}</p>
          )}
          <button type="submit" disabled={isLoading}>
            {isLoading ? <span className={styles['spinner']}></span> : 'Login'}
          </button>
          <p className={styles['account-options']}>
            <a href="#forgot-password" className={styles['forgot-password']}>
              Forgot Password?
            </a>
            <span className={styles['divider']}>|</span>
            <a href="/create-account" className={styles['create-account']}>
              Create Account
            </a>
          </p>
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
