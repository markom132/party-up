import React from 'react';
import HeroSection from '../components/HeroSection/HeroSection';
import FeaturedEvents from '../components/FeaturedEvents/FeaturedEvents';

const HomePage: React.FC = () => {
  const userName: string = 'Marko';
  const events = [
    {
      id: 1,
      title: 'Summcer Beach Party',
      date: 'July 20, 2024',
      location: 'Miami Beach',
      image: '/images/beach-party.jpg',
      description:
        'Join us for an unforgettable beach party with live muysic, great food, and awsome vibes!',
    },
    {
      id: 2,
      title: 'Tech Meetup 2024',
      date: 'August 10, 2024',
      location: 'San Francisco',
      image: '/images/tech-meetup.jpg',
      description:
        'Connect with tech entusiasts and professionals at this exciting meetup.',
    },
  ];

  return (
    <>
      <HeroSection userName={userName} />
      <FeaturedEvents events={events} />
    </>
  );
};

export default HomePage;
