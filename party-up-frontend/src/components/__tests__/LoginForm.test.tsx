import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import LoginForm from '../LoginForm/LoginForm';
import { loginUser } from '../../services/authService';

// Mock for loginUser function
jest.mock('../../services/authService', () => ({
  loginUser: jest.fn(),
}));

// eslint-disable-next-line no-undef
const mockedLoginUser = loginUser as jest.MockedFunction<typeof loginUser>;

describe('LoginForm Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('Shows component without errors', () => {
    render(<LoginForm />);
    expect(screen.getByRole('heading', { name: /login/i })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument();
  });

  test('Shows errors for empty fields', async () => {
    render(<LoginForm />);
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    expect(
      await screen.findByText('Please enter your username'),
    ).toBeInTheDocument();
    expect(screen.getByText('Please enter your password')).toBeInTheDocument();
  });

  test('Shows API error message when login request failed', async () => {
    // Mock for loginUser which will throw error
    mockedLoginUser.mockRejectedValueOnce(new Error('Invalid credentials'));

    render(<LoginForm />);

    // Fill input fields and send form
    fireEvent.change(screen.getByPlaceholderText('Enter your username'), {
      target: { value: 'testuser' },
    });

    fireEvent.change(screen.getByPlaceholderText('Enter your password'), {
      target: { value: 'password123' },
    });

    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    expect(await screen.findByText('Invalid credentials')).toBeInTheDocument();
  });
});
