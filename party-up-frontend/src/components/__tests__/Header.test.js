import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Header from '../Header/Header';

describe('Header Component', () => {
  test('Shows login and sign in buttons when user is not logged in', () => {
    render(<Header />);

    fireEvent.click(screen.getByText('Log out'));

    expect(screen.getByText('Login')).toBeInTheDocument();
    expect(screen.getByText('Sign Up')).toBeInTheDocument();
  });

  test('Shows profile avatar when user is logged in', () => {
    render(<Header />);

    expect(
      screen.getByRole('button', { name: /log out/i })
    ).toBeInTheDocument();
    expect(screen.queryByText('Login')).not.toBeInTheDocument();
    expect(screen.queryByText('Sign Up')).not.toBeInTheDocument();
  });

  test('Shows dropdown menu when profile avatar is clicked', () => {
    render(<Header />);

    const profileAvatarIcon = screen.getByLabelText('Profile Avatar');
    fireEvent.click(profileAvatarIcon);

    expect(screen.getByText('My Profile')).toBeInTheDocument();
    expect(screen.getByText('Settings')).toBeInTheDocument();
    expect(screen.getByText('Log out')).toBeInTheDocument();
  });

  test('Opens and closes hamburger menu when clicked on small screens', () => {
    render(<Header />);

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
