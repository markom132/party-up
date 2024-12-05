import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { useNavigate } from 'react-router-dom';
import '@testing-library/jest-dom';
import FeaturedEvents from '../FeaturedEvents/FeaturedEvents';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

// Mock data for deatured events
const mockEvent = [
  {
    id: 1,
    title: 'Concert Night',
    date: '2024-11-25',
    location: 'New York City',
    image: '/images/concert.jpg',
    description: 'A night full of music and fun!',
  },
  {
    id: 2,
    title: 'Art Exhibition',
    date: '2024-12-05',
    location: 'Los Angeles',
    image: '/images/art.jpg',
    description: 'Explore the latest in modern art.',
  },
];

describe('FeaturedEvents Component', () => {
  test('Renders the correct number of event cards', () => {
    render(<FeaturedEvents events={mockEvent} />);

    const eventCards = screen.getAllByTestId('event-card');
    expect(eventCards.length).toBe(mockEvent.length);
  });

  test('Displays event details correctly', () => {
    render(<FeaturedEvents events={mockEvent} />);

    mockEvent.forEach((event) => {
      expect(screen.getByText(event.title)).toBeInTheDocument();
      expect(screen.getByText(event.date)).toBeInTheDocument();
      expect(screen.getByText(event.location)).toBeInTheDocument();
      expect(screen.getByText(event.description)).toBeInTheDocument();
    });
  });

  test('Details button navigates to event details', () => {
    const mockNavigate = jest.fn();
    // eslint-disable-next-line no-undef
    (useNavigate as jest.Mock).mockImplementation(() => mockNavigate);

    render(<FeaturedEvents events={mockEvent} />);

    // Find all "Details" buttons
    const detailButtons = screen.getAllByRole('button', { name: /details/i });

    // Simulate click on the second "Details" button
    fireEvent.click(detailButtons[1]);

    // Check if navigation was called with correct URL
    expect(mockNavigate).toHaveBeenCalledWith(`/event/${mockEvent[1].id}`);
    expect(mockNavigate).toHaveBeenCalledTimes(1);

    // Verify that the second button was clicked
    expect(detailButtons[1]).toBeVisible();
  });
});
