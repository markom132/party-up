import React, { useState } from 'react';
import { registerUser, RegisterData } from '../../services/registerService';
import SuccessMessageModal from '../SuccessMessageModal/SuccessMessageModal';
import styles from './RegisterForm.module.scss';

interface FormError {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  birthDay: string;
}
const RegisterForm: React.FC = () => {
  const [firstName, setFirstName] = useState<string>('');
  const [lastName, setLastName] = useState<string>('');
  const [username, setUsername] = useState<string>('');
  const [email, setEmail] = useState<string>('');
  const [birthDay, setBirthDay] = useState<string>('');
  const [error, setError] = useState<FormError>({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    birthDay: '',
  });
  const [apiError, setApiError] = useState<string>('');
  const [isLoading, setIsLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    setIsLoading(true);
    setApiError('');
    setSuccessMessage('');
    let hasError = false;
    const newError: FormError = {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      birthDay: '',
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
    if (!birthDay) {
      newError.birthDay = 'Birth date is required';
      hasError = true;
    }

    setError(newError);
    if (!hasError) {
      console.log({ firstName, lastName, username, email, birthDay });
    }

    const registerData: RegisterData = {
      firstName,
      lastName,
      username,
      email,
      birthDay,
    };

    try {
      const response = await registerUser(registerData);

      if (response.success) {
        setSuccessMessage(response.message || 'Registration successful!');
        setIsModalOpen(true);
      } else {
        setApiError(response.message || 'Registration failed.');
      }
    } catch (error: any) {
      setApiError(error.message || 'An unexpected error occurred.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleResendEmail = () => {
    // Logic for resending email
    alert('Email poslat ponovo!');
  };

  return (
    <div className={styles['register-container']}>
      <div className={styles['form-header']}>
        <h1 className={styles.title}>Join Party-Up</h1>
        <p className={styles.subtitle}>
          Join the Party and find events near you.
        </p>
      </div>

      {/* Feedback Messages */}
      {apiError && <p className={styles['error-message']}>{apiError}</p>}
      {successMessage && (
        <p className={styles['success-message']}>{successMessage}</p>
      )}

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

        {/* Birth date */}
        <div className={styles['input-group']}>
          <label htmlFor="birthDay">Birth date</label>
          <input
            type="date"
            id="birthDay"
            value={birthDay}
            onChange={(e) => {
              setBirthDay(e.target.value);
            }}
            className={error.birthDay ? styles['error-input'] : ''}
          />
          {error.birthDay && (
            <p className={styles['error-text']}>{error.birthDay}</p>
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
        <button
          type="submit"
          className={styles['register-button']}
          disabled={isLoading}
        >
          {isLoading ? 'Registering...' : 'Sign Up'}
        </button>

        {/* Login Link */}
        <p className={styles['login-text']}>
          Already have an account?{' '}
          <a href="/login" className={styles['login-link']}>
            Log in
          </a>
        </p>
      </form>
      <SuccessMessageModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onResendEmail={handleResendEmail}
      />
    </div>
  );
};

export default RegisterForm;
