import React from 'react';
import styles from './HeroSection.module.scss';

// Props
interface HeroSectionProps {
  userName: string; // User must have name
}

const HeroSection: React.FC<HeroSectionProps> = ({ userName }) => {
  return (
    <section className={styles.hero}>
      <div className={styles.overlay}></div>
      <div className={styles.content}>
        <h1 className={styles.title}>Connect. Organize. Attend.</h1>
        <h4 className={styles.subtitle}>
          Welcome back, {userName}! Ready to party?
        </h4>
        <p className={styles.subtitle}>
          Party-Up helps you find nearby events, meet people and share
          unforgettable moments.
        </p>
        <div className={styles.buttons}>
          <a href="#explore" className={styles['primary-button']}>
            Explore Events
          </a>
          <a href="#create-event" className={styles['secondary-button']}>
            Create Event
          </a>
          <a href="#profile" className={styles['secondary-button']}>
            View Profile
          </a>
        </div>
      </div>
    </section>
  );
};

export default HeroSection;
