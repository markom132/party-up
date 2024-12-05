import React, { useState } from 'react';
import styles from './RegisterForm.module.scss';

interface FormError {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}
const RegisterForm: React.FC = () => {
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [username, setUsername] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [passwordStrength, setPasswordStrength] = useState<string>('');
  const [error, setError] = useState<FormError>({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });

  const validatePassword = (password: string): string => {
    if (password.length < 8) return 'Weak';
    if (
      /[A-Z]/.test(password) &&
      /[0-9]/.test(password) &&
      /[@$!%*?&#]/.test(password)
    ) {
      return 'Strong';
    }
    return 'Moderate';
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>): void => {
    e.preventDefault();

    let hasError = false;
    const newError: FormError = {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    };

    if (!firstName) {
      newError.firstName = 'First name is required';
      hasError = true;
    }
    if (!lastName) {
      newError.lastName = 'Last name is required';
      hasError = true;
    }
    if (!username) {
      newError.username = 'Username is required';
      hasError = true;
    }
    if (!email || !/\S+@\S+\.\S+/.test(email)) {
      newError.email = 'A valid email is required';
      hasError = true;
    }
    if (!password) {
      newError.password = 'Password is required';
      hasError = true;
    } else if (password.length < 8) {
      newError.password = 'Password is week';
      hasError = true;
    } else if (password !== confirmPassword) {
      newError.confirmPassword = 'Passwords do not match';
      hasError = true;
    }

    setError(newError);
    if (!hasError) {
      console.log({ firstName, lastName, username, email, password });
    }
  };

  return (
    <div className={styles['register-container']}>
      <div className={styles['form-header']}>
        <h1 className={styles.title}>Join Party-Up</h1>
        <p className={styles.subtitle}>
          Join the Party and find events near you.
        </p>
      </div>
      <form onSubmit={handleSubmit} className={styles['register-form']}>
        {/* First Name */}
        <div className={styles['input-group']}>
          <label htmlFor="firstName">First Name</label>
          <input
            type="text"
            id="firstName"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            placeholder="Enter your first name"
            className={error.firstName ? styles['error-input'] : ''}
          />
          {error.firstName && (
            <p className={styles['error-text']}>{error.firstName}</p>
          )}
        </div>

        {/* Last Name */}
        <div className={styles['input-group']}>
          <label htmlFor="lastName">Last Name</label>
          <input
            type="text"
            id="lastName"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            placeholder="Enter your last name"
            className={error.lastName ? styles['error-input'] : ''}
          />
          {error.lastName && (
            <p className={styles['error-text']}>{error.lastName}</p>
          )}
        </div>

        {/* Username */}
        <div className={styles['input-group']}>
          <label htmlFor="username">Username</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Enter your username"
            className={error.username ? styles['error-input'] : ''}
          />
          {error.username && (
            <p className={styles['error-text']}>{error.username}</p>
          )}
        </div>

        {/* Email */}
        <div className={styles['input-group']}>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Enter your email"
            className={error.email ? styles['error-input'] : ''}
          />
          {error.email && <p className={styles['error-text']}>{error.email}</p>}
        </div>

        {/* Password */}
        <div className={styles['input-group']}>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
              setPasswordStrength(validatePassword(e.target.value));
            }}
            placeholder="Enter your password"
            className={error.password ? styles['error-input'] : ''}
          />
          {error.password && (
            <p className={styles['error-text']}>{error.password}</p>
          )}
          {password && (
            <p className={styles['password-strength']}>
              Strength: {passwordStrength}
            </p>
          )}
        </div>

        {/* Confirm Password */}
        <div className={styles['input-group']}>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            placeholder="Confirm your password"
            className={error.confirmPassword ? styles['error-input'] : ''}
          />
          {error.confirmPassword && (
            <p className={styles['error-text']}>{error.confirmPassword}</p>
          )}
        </div>

        {/* Terms and Conditions */}
        <div className={styles['terms-container']}>
          <label className={styles['checkbox']}>
            <input type="checkbox" required />
            <span>I agree to the Terms of Service and Privacy Policy</span>
          </label>
        </div>

        {/* Submit Button */}
        <button type="submit" className={styles['register-button']}>
          Sign Up
        </button>

        {/* Login Link */}
        <p className={styles['login-text']}>
          Already have an account?{' '}
          <a href="/login" className={styles['login-link']}>
            Log in
          </a>
        </p>
      </form>
    </div>
  );
};

export default RegisterForm;
