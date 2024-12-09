import React, { useState } from 'react';
import styles from './Header.module.scss';

// Defining a Props interface in case it will be needed to pass props to the Header in the future
interface HeaderProps {}

const Header: React.FC<HeaderProps> = () => {
  const [menuOpen, setMenuOpen] = useState<boolean>(false);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(true);
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState<boolean>(false);

  const toggleMenu = (): void => {
    setMenuOpen((prev) => !prev);
  };

  const toggleProfileMenu = (): void => {
    setIsProfileMenuOpen((prev) => !prev);
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
          <a href="#home" data-testid="nav-link-home">
            Home
          </a>
          <a href="#events">Events</a>
          <a href="#explore">Explore</a>
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
                <a href="#profile">My Profile</a>
                <a href="#settings">Settings</a>
                <button onClick={() => setIsLoggedIn(false)}>Log out</button>
              </div>
            </div>
          ) : (
            <>
              <a
                href="#login"
                className={styles['login-link']}
                data-testid="login-link"
              >
                Login
              </a>
              <button
                className={styles['signup-button']}
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
