import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { logoutUser } from '../../services/authService'; // Update with your actual service file path
import { useAuth } from '../../context/AuthContext';
import styles from './Header.module.scss';

interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
  const [menuOpen, setMenuOpen] = useState<boolean>(false);
  const { isLoggedIn, setIsLoggedIn } = useAuth();
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState<boolean>(false);
  const navigate = useNavigate();

  const toggleMenu = (): void => {
    setMenuOpen((prev) => !prev);
  };

  const toggleProfileMenu = (): void => {
    setIsProfileMenuOpen((prev) => !prev);
  };

  const handleLogout = async () => {
    try {
      const message = await logoutUser(); // Call the centralized logout function
      console.log('Logout message:', message); // Log or handle the success message
      setIsLoggedIn(false); // Update local state
      navigate('/login'); // Redirect to login page
    } catch (error) {
      console.error('Error during logout:', error);
    }
  };

  const handleCreateAccount = (): void => {
    navigate('/create-account');
  };

  return (
    <header className={styles.header}>
      <h1 className={styles.logo}>
        Par<span className={styles.accent}>t</span>y-
        <span className={styles['accent']}>Up</span>
      </h1>
      <nav className={styles['header-nav']}>
        <div
          className={`${styles['nav-links']} ${menuOpen ? styles.open : ''}`}
          data-testid="nav-links"
        >
          {/* Dynamically set the Home link based on login state */}
          <Link
            to={isLoggedIn ? '/home' : '/welcome'}
            data-testid="nav-link-home"
          >
            Home
          </Link>
          {isLoggedIn && (
            <>
              <a href="/events" data-testid="nav-link-events">
                Events
              </a>
              <a href="/explore" data-testid="nav-link-explore">
                Explore
              </a>
            </>
          )}
        </div>
        <div className={styles['auth-buttons']}>
          {isLoggedIn ? (
            <div
              className={styles['profile-avatar']}
              onClick={toggleProfileMenu}
              aria-label="Profile Avatar"
              data-testid="profile-avatar"
            >
              <i className={`${styles['avatar-icon']} fas fa-user-circle`}></i>
              <div
                className={`${styles['dropdown-menu']} ${
                  isProfileMenuOpen ? styles.open : ''
                }`}
                data-testid="dropdown-menu"
              >
                <a href="/profile">My Profile</a>
                <a href="/settings">Settings</a>
                <button onClick={handleLogout}>Log out</button>
              </div>
            </div>
          ) : (
            <>
              <a
                href="/login"
                className={styles['login-link']}
                data-testid="login-link"
              >
                Login
              </a>
              <button
                className={styles['signup-button']}
                onClick={handleCreateAccount}
                data-testid="signup-button"
              >
                Sign Up
              </button>
            </>
          )}
        </div>
        <button
          className={styles['hamburger-menu']}
          onClick={toggleMenu}
          data-testid="hamburger-menu"
        >
          &#9776;
        </button>
      </nav>
    </header>
  );
};

export default Header;
