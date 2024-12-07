import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import RegisterForm from '../../components/RegisterForm/RegisterForm';
import * as service from '../../services/registerService';

jest.mock('../../services/registerService');

describe('RegisterForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders all input fields and submit button', () => {
    render(<RegisterForm />);

    expect(screen.getByLabelText(/First Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Last Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Birth date/i)).toBeInTheDocument();
    expect(
      screen.getByRole('button', { name: /Sign Up/i }),
    ).toBeInTheDocument();
  });

  it('shows validation errors for empty fields', async () => {
    render(<RegisterForm />);

    fireEvent.click(screen.getByRole('button', { name: /Sign Up/i }));

    expect(
      await screen.findByText(/First name is required/i),
    ).toBeInTheDocument();
    expect(screen.getByText(/Last name is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Username is required/i)).toBeInTheDocument();
    expect(screen.getByText(/A valid email is required/i)).toBeInTheDocument();
    expect(screen.getByText(/Birth date is required/i)).toBeInTheDocument();
  });

  it('displays API errors returned by the backend', async () => {
    // eslint-disable-next-line no-undef
    const mockRegisterUser = service.registerUser as jest.Mock;
    mockRegisterUser.mockResolvedValueOnce({
      success: false,
      errors: { email: 'Registration failed.' },
    });

    render(<RegisterForm />);

    fireEvent.change(screen.getByLabelText(/First Name/i), {
      target: { value: 'John' },
    });
    fireEvent.change(screen.getByLabelText(/Last Name/i), {
      target: { value: 'Doe' },
    });
    fireEvent.change(screen.getByLabelText(/Username/i), {
      target: { value: 'johndoe' },
    });
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: 'john.doe@example.com' },
    });
    fireEvent.change(screen.getByLabelText(/Birth date/i), {
      target: { value: '1990-01-01' },
    });

    fireEvent.click(screen.getByRole('button', { name: /Sign Up/i }));

    await waitFor(() => expect(mockRegisterUser).toHaveBeenCalledTimes(1));

    expect(
      await screen.findByText(/Registration failed./i),
    ).toBeInTheDocument();
  });

  it('disables the submit button when the form is submitting', async () => {
    // eslint-disable-next-line no-undef
    const mockRegisterUser = service.registerUser as jest.Mock;
    mockRegisterUser.mockResolvedValueOnce({ success: true });

    render(<RegisterForm />);

    fireEvent.change(screen.getByLabelText(/First Name/i), {
      target: { value: 'John' },
    });
    fireEvent.change(screen.getByLabelText(/Last Name/i), {
      target: { value: 'Doe' },
    });
    fireEvent.change(screen.getByLabelText(/Username/i), {
      target: { value: 'johndoe' },
    });
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: 'john.doe@example.com' },
    });
    fireEvent.change(screen.getByLabelText(/Birth date/i), {
      target: { value: '1990-01-01' },
    });

    fireEvent.click(screen.getByRole('button', { name: /Sign Up/i }));

    expect(
      screen.getByRole('button', { name: /Registering.../i }),
    ).toBeDisabled();
  });
});
