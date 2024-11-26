import React from 'react';
import HeroSection from '../components/HeroSection/HeroSection';

const HomePage: React.FC = () => {
  const userName: string = 'Marko';
  return (
    <>
      <HeroSection userName={userName} />
    </>
  );
};

export default HomePage;
