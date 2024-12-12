import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Header from '../Header/Header';
import { AuthContext } from '../../context/AuthContext'; // Import AuthContext and AuthProvider
import { MemoryRouter } from 'react-router-dom'; // Import MemoryRouter for routing context

describe('Header Component', () => {
  const renderWithProviders = (
    ui: React.ReactNode,
    { isLoggedIn }: { isLoggedIn: boolean },
  ) => {
    return render(
      <MemoryRouter>
        <AuthContext.Provider
          value={{
            isLoggedIn,
            setIsLoggedIn: jest.fn(), // Mock `setIsLoggedIn` for logging in/out
          }}
        >
          {ui}
        </AuthContext.Provider>
      </MemoryRouter>,
    );
  };

  test('Shows login and sign-in buttons when user is not logged in', () => {
    renderWithProviders(<Header />, { isLoggedIn: false });

    expect(screen.getByText('Login')).toBeInTheDocument();
    expect(screen.getByText('Sign Up')).toBeInTheDocument();
  });

  test('Shows profile avatar when user is logged in', () => {
    renderWithProviders(<Header />, { isLoggedIn: true });

    const logOutButton = screen.getByRole('button', { name: /log out/i });
    expect(logOutButton).toBeInTheDocument();
    expect(screen.queryByText('Login')).not.toBeInTheDocument();
    expect(screen.queryByText('Sign Up')).not.toBeInTheDocument();
  });

  test('Shows dropdown menu when profile avatar is clicked', () => {
    renderWithProviders(<Header />, { isLoggedIn: true });

    const profileAvatarIcon = screen.getByLabelText('Profile Avatar');
    fireEvent.click(profileAvatarIcon);

    expect(screen.getByText('My Profile')).toBeInTheDocument();
    expect(screen.getByText('Settings')).toBeInTheDocument();
    expect(screen.getByText('Log out')).toBeInTheDocument();
  });

  test('Opens and closes hamburger menu when clicked on small screens', () => {
    renderWithProviders(<Header />, { isLoggedIn: false });

    // Simulate small screen
    window.innerWidth = 500;
    window.dispatchEvent(new Event('resize'));

    const hamburgerMenu = screen.getByText('☰');
    fireEvent.click(hamburgerMenu);

    const homeLink = screen.getByText('Home');
    expect(homeLink).toBeInTheDocument();
    expect(homeLink.parentElement).toHaveClass('open');

    fireEvent.click(hamburgerMenu);
    expect(homeLink.parentElement).not.toHaveClass('open');
  });
});
