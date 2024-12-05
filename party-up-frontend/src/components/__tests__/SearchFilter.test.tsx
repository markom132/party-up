import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import SearchFilter from '../SearchFilter/SearchFilter';

describe('SearchFilter Component', () => {
  const mockOnSearch = jest.fn();

  beforeEach(() => {
    mockOnSearch.mockClear();
  });

  test('renders all fields and the search button', () => {
    render(<SearchFilter onSearch={mockOnSearch} />);

    // Check if all fields are present
    expect(
      screen.getByPlaceholderText('Find event by name'),
    ).toBeInTheDocument();
    expect(screen.getByText('Select Location')).toBeInTheDocument();
    expect(screen.getByText('Select Category')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
  });

  test('calls onSearch with correct filters when search button is clicked', () => {
    render(<SearchFilter onSearch={mockOnSearch} />);

    // Fill out the form
    fireEvent.change(screen.getByPlaceholderText('Find event by name'), {
      target: { value: 'Concert' },
    });
    fireEvent.change(screen.getByRole('combobox', { name: /location/i }), {
      target: { value: 'city' },
    });
    fireEvent.change(screen.getByRole('combobox', { name: /category/i }), {
      target: { value: 'party' },
    });
    fireEvent.change(screen.getByLabelText('date'), {
      target: { value: '2024-12-31' },
    });

    // Click the search button
    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    // Verify onSearch was called with correct filters
    expect(mockOnSearch).toHaveBeenCalledTimes(1);
    expect(mockOnSearch).toHaveBeenCalledWith({
      searchTerm: 'Concert',
      location: 'city',
      category: 'party',
      date: '2024-12-31',
    });
  });

  test('calls onSearch with empty values if no filters are selected', () => {
    render(<SearchFilter onSearch={mockOnSearch} />);

    // Click the search button without entering any data
    fireEvent.click(screen.getByRole('button', { name: /search/i }));

    // Verify onSearch was called with empty filters
    expect(mockOnSearch).toHaveBeenCalledTimes(1);
    expect(mockOnSearch).toHaveBeenCalledWith({
      searchTerm: '',
      location: '',
      category: '',
      date: '',
    });
  });
});
