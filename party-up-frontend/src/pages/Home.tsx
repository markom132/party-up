import React from 'react';
import HeroSection from '../components/HeroSection/HeroSection';
import FeaturedEvents from '../components/FeaturedEvents/FeaturedEvents';
import SearchFilter, {
  SearchFilters,
} from '../components/SearchFilter/SearchFilter';
import SocialFeed from '../components/SocialFeed/SocialFeed';
import TopReviews from '../components/TopReviews/TopReviews';

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

  const handleSearch = (filters: SearchFilters) => {
    console.log('Searching with filters: ', filters);
  };

  const mockPosts = [
    {
      id: 1,
      username: 'JohnDoe',
      profilePicture: '/images/john.jpg',
      content: 'Had an amazing time at the concert last night!',
      image: '/images/concert.jpg',
      likes: 42,
      comments: 5,
    },
    {
      id: 2,
      username: 'JaneSmith',
      profilePicture: '/images/jane.jpg',
      content: 'Can’t wait for the next meetup! Who’s joining?',
      likes: 27,
      comments: 8,
    },
  ];

  const handleLike = (postId: number) => {
    console.log(`Liked post ${postId}`);
  };

  const handleComment = (postId: number) => {
    console.log(`Commented on post ${postId}`);
  };

  const mockPlaces = [
    {
      id: 1,
      name: 'Blue Lagoon Bar',
      rating: 4.8,
      description: 'The best cocktails in town with amazing sea views.',
      image: '/images/bar.jpg',
    },
    {
      id: 2,
      name: 'Downtown Cafe',
      rating: 4.3,
      description: 'Cozy cafe with delicious coffee and snacks.',
      image: '/images/caffe.jpg',
    },
    {
      id: 3,
      name: 'Sunset Park',
      rating: 4.7,
      description: 'Perfect place for evening walks and beautiful sunsets.',
      image: '/images/park.jpg',
    },
  ];

  return (
    <div>
      <section>
        <HeroSection userName={userName} />
      </section>
      <section>
        <FeaturedEvents events={events} />
      </section>
      <section>
        <SearchFilter onSearch={handleSearch} />
      </section>
      <section>
        <SocialFeed
          posts={mockPosts}
          onLike={handleLike}
          onComment={handleComment}
        />
      </section>
      <section>
        <TopReviews places={mockPlaces} />
      </section>
    </div>
  );
};

export default HomePage;
