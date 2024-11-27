import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import SocialFeed from '../SocialFeed/SocialFeed';

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
    image: '',
    likes: 27,
    comments: 8,
  },
];

const handleLike = jest.fn();
const handleComment = jest.fn();

describe('SocialFeed Component', () => {
  test('renders all posts correctly', () => {
    render(
      <SocialFeed
        posts={mockPosts}
        onLike={handleLike}
        onComment={handleComment}
      />,
    );

    // Check that each post is rendered
    expect(screen.getByText('JohnDoe')).toBeInTheDocument();
    expect(
      screen.getByText('Had an amazing time at the concert last night!'),
    ).toBeInTheDocument();
    expect(screen.getByAltText('Post content')).toBeInTheDocument();

    expect(screen.getByText('JaneSmith')).toBeInTheDocument();
    expect(
      screen.getByText('Can’t wait for the next meetup! Who’s joining?'),
    ).toBeInTheDocument();
  });

  test('displays correct number of likes and comments', () => {
    render(
      <SocialFeed
        posts={mockPosts}
        onLike={handleLike}
        onComment={handleComment}
      />,
    );

    // Check likes and comments for the first post
    expect(screen.getByText('Like (42)')).toBeInTheDocument();
    expect(screen.getByText('Comment (5)')).toBeInTheDocument();

    // Check likes and comments for the second post
    expect(screen.getByText('Like (27)')).toBeInTheDocument();
    expect(screen.getByText('Comment (8)')).toBeInTheDocument();
  });

  test('handles like button clicks', () => {
    const handleLike = jest.fn();
    render(
      <SocialFeed
        posts={mockPosts}
        onLike={handleLike}
        onComment={handleComment}
      />,
    );

    const likeButtons = screen.getAllByText(/Like/i);

    // Simulate click on the first post's like button
    fireEvent.click(likeButtons[0]);
    expect(handleLike).toHaveBeenCalledTimes(1);
    expect(handleLike).toHaveBeenCalledWith(1); // Post ID
  });

  test('handles comment button clicks', () => {
    const handleComment = jest.fn();
    render(
      <SocialFeed
        posts={mockPosts}
        onLike={handleLike}
        onComment={handleComment}
      />,
    );

    const commentButtons = screen.getAllByText(/Comment/i);

    // Simulate click on the second post's comment button
    fireEvent.click(commentButtons[1]);
    expect(handleComment).toHaveBeenCalledTimes(1);
    expect(handleComment).toHaveBeenCalledWith(2); // Post ID
  });

  test('renders profile image and image correctly', () => {
    render(
      <SocialFeed
        posts={mockPosts}
        onLike={handleLike}
        onComment={handleComment}
      />,
    );

    // Check profile image for the first post
    const avatar1 = screen.getByAltText(`JohnDoe's profile`);
    expect(avatar1).toHaveAttribute('src', '/images/john.jpg');

    // Check image for the first post
    const image1 = screen.getByAltText('Post content');
    expect(image1).toHaveAttribute('src', '/images/concert.jpg');
  });
});
