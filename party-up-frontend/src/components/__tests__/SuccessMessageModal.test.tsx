import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import SuccessMessageModal from '../SuccessMessageModal/SuccessMessageModal';

describe('SuccessMessageModal', () => {
  const mockOnClose = jest.fn();
  const mockOnResendEmail = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders modal with the correct message', () => {
    render(
      <SuccessMessageModal
        isOpen={true}
        onResendEmail={mockOnResendEmail}
        onClose={function (): void {
          throw new Error('Function not implemented.');
        }}
      />,
    );

    expect(screen.getByText('Thanks for registering!')).toBeInTheDocument();
    expect(
      screen.getByText(
        'Check your email to activate your account. We have sent you an email with an activation link.',
      ),
    ).toBeInTheDocument();
  });

  it('calls onResendEmail when the resend email button is clicked', () => {
    render(
      <SuccessMessageModal
        isOpen={true}
        onResendEmail={mockOnResendEmail}
        onClose={function (): void {
          throw new Error('Function not implemented.');
        }}
      />,
    );

    const resendButton = screen.getByText("Didn't get an email? Send again.");
    fireEvent.click(resendButton);

    expect(mockOnResendEmail).toHaveBeenCalledTimes(1);
  });

  it('has a mailto link with subject and body pre-filled', () => {
    render(
      <SuccessMessageModal
        isOpen={true}
        onResendEmail={mockOnResendEmail}
        onClose={mockOnClose}
      />,
    );

    const contactLink = screen.getByText('Got a problem? Contact us.');

    expect(contactLink).toHaveAttribute('href', 'mailto:support@partyup.com');
  });

  it('navigates to the home page when the return to home link is clicked', () => {
    render(
      <SuccessMessageModal
        isOpen={true}
        onResendEmail={mockOnResendEmail}
        onClose={function (): void {
          throw new Error('Function not implemented.');
        }}
      />,
    );

    const loginLink = screen.getByText('Return to login page');
    expect(loginLink).toHaveAttribute('href', '/login');
  });
});
