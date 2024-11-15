import React from 'react';
import '../assets/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section about">
          <h4>About</h4>
          <p>
            Parti-Up is a social network dedicated to connecting people through
            shared events and experiences. Find friends, create events, enjoy
            unforgettable moments.
          </p>
        </div>

        <div className="footer-section quick-links">
          <h4>Links</h4>
          <div className="links-columns">
            <a href="#home">Home</a>
            <a href="#events">Events</a>
            <a href="#explore">Explore</a>
            <a href="#support">Support</a>
            <a href="#privacy-policy">Privacy Policy</a>
            <a href="#terms-of-service">Terms of Service</a>
          </div>
        </div>

        <div className="footer-section social-media">
          <h4>Follow Us</h4>
          <div className="social-icons">
            <a href="#facebook" className="social-icon facebook">
              <i className="fab fa-facebook-f"></i>
            </a>
            <a href="#instagram" className="social-icon instagram">
              <i className="fab fa-instagram"></i>
            </a>
            <a href="#twitter" className="social-icon twitter">
              <i className="fab fa-twitter"></i>
            </a>
            <a href="#linkedin" className="social-icon linkedin">
              <i className="fab fa-linkedin-in"></i>
            </a>
          </div>
        </div>
      </div>

      <div className="footer-bottom">
        <p>&copy; 2024 Party-Up. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
