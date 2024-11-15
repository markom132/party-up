import React from 'react';
import styles from './Footer.module.scss';

const Footer: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles['footer-container']}>
        <div className={`${styles['footer-section']} ${styles.about}`}>
          <h4>About</h4>
          <p>
            Party-Up is a social network dedicated to connecting people through
            shared events and experiences. Find friends, create events, enjoy
            unforgettable moments.
          </p>
        </div>

        <div className={`${styles['footer-section']} ${styles['quick-links']}`}>
          <h4>Links</h4>
          <div className={styles['links-columns']}>
            <a href="#home">Home</a>
            <a href="#events">Events</a>
            <a href="#explore">Explore</a>
            <a href="#support">Support</a>
            <a href="#privacy-policy">Privacy Policy</a>
            <a href="#terms-of-service">Terms of Service</a>
          </div>
        </div>

        <div className={`${styles['footer-section']} ${styles['social-media']}`}>
          <h4>Follow Us</h4>
          <div className={styles['social-icons']}>
            <a href="#facebook" className={styles['social-icon']} aria-label="Facebook">
              <i className="fab fa-facebook-f"></i>
            </a>
            <a href="#instagram" className={styles['social-icon']} aria-label="Instagram">
              <i className="fab fa-instagram"></i>
            </a>
            <a href="#twitter" className={styles['social-icon']} aria-label="Twitter">
              <i className="fab fa-twitter"></i>
            </a>
            <a href="#linkedin" className={styles['social-icon']} aria-label="LinkedIn">
              <i className="fab fa-linkedin-in"></i>
            </a>
          </div>
        </div>
      </div>

      <div className={styles['footer-bottom']}>
        <p>&copy; 2024 Party-Up. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
