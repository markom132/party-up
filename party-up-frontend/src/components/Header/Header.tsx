import React, { useState } from 'react';
import styles from './Header.module.scss';

const Header: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(true);
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };
  const toggleProfileMenu = () => setIsProfileMenuOpen((prev) => !prev);

  return (
    <header className={styles.header}>
      <h1 className={styles.logo}>
        Par<span className={styles.accent}>t</span>y-
        <span className={styles['accent']}>Up</span>
      </h1>
      <nav className={styles['header-nav']}>
        <div
          className={`${styles['nav-links']} ${menuOpen ? styles.open : ''}`}
        >
          <a href="#home">Home</a>
          <a href="#events">Events</a>
          <a href="#explore">Explore</a>
        </div>
        <div className={styles['auth-buttons']}>
          {isLoggedIn ? (
            <div
              className={styles['profile-avatar']}
              onClick={toggleProfileMenu}
              aria-label='Profile Avatar'
            >
              <i className={`${styles['avatar-icon']} fas fa-user-circle`}></i>
              <div
                className={`${styles['dropdown-menu']} ${
                  isProfileMenuOpen ? styles.open : ''
                }`}
              >
                <a href="#profile">My Profile</a>
                <a href="#settings">Settings</a>
                <button onClick={() => setIsLoggedIn(false)}>Log out</button>
              </div>
            </div>
          ) : (
            <>
              <a href="#login" className={styles['login-link']}>
                Login
              </a>
              <button className={styles['signup-button']}>Sign Up</button>
            </>
          )}
        </div>
        <button className={styles['hamburger-menu']} onClick={toggleMenu}>
          &#9776;
        </button>
      </nav>
    </header>
  );
};

export default Header;
