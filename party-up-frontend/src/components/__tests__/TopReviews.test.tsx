import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import userEvent from '@testing-library/user-event';
import TopReviews from '../TopReviews/TopReviews';

const mockPlaces = [
  {
    id: 1,
    name: 'Blue Lagoon Bar',
    rating: 4.8,
    description: 'The best cocktails in town with amazing sea views.',
    image: '',
  },
  {
    id: 2,
    name: 'Downtown Cafe',
    rating: 4.5,
    description: 'Cozy cafe with delicious coffee and snacks.',
    image: '',
  },
];

describe('TopReviews Component', () => {
  test('renders top reviews section with places', () => {
    render(
      <MemoryRouter>
        <TopReviews places={mockPlaces} />
      </MemoryRouter>,
    );
    expect(screen.getByText('Top Rated Places')).toBeInTheDocument();
    expect(screen.getByText('Blue Lagoon Bar')).toBeInTheDocument();
    expect(screen.getByText('Downtown Cafe')).toBeInTheDocument();
  });

  test('renders review button for each place', () => {
    render(
      <MemoryRouter>
        <TopReviews places={mockPlaces} />
      </MemoryRouter>,
    );
    const buttons = screen.getAllByRole('button', { name: /read reviews/i });
    expect(buttons).toHaveLength(mockPlaces.length);
  });

  test('calls action when "Read Reviews" button is clicked', async () => {
    render(
      <MemoryRouter>
        <TopReviews places={mockPlaces} />
      </MemoryRouter>,
    );
    const buttons = screen.getAllByRole('button', { name: /read reviews/i });
    await userEvent.click(buttons[0]);
  });
});
