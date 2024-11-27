import React, { useState } from 'react';
import styles from './SearchFilter.module.scss';

interface SearchFilterProps {
  // eslint-disable-next-line no-unused-vars
  onSearch: (filters: SearchFilters) => void;
}

export interface SearchFilters {
  searchTerm: string;
  location: string;
  category: string;
  date: string;
}

const SearchFilter: React.FC<SearchFilterProps> = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [location, setLocation] = useState<string>('');
  const [category, setCategory] = useState<string>('');
  const [date, setDate] = useState<string>('');

  const handleSearch = () => {
    onSearch({ searchTerm, location, category, date });
  };

  return (
    <div className={styles['search-filter']}>
      <h2>Search Events</h2>
      <div className={styles.fields}>
        {/* Search Term */}
        <input
          type="text"
          placeholder="Find event by name"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className={styles.input}
        />

        {/* Location */}
        <select
          value={location}
          onChange={(e) => setLocation(e.target.value)}
          className={styles.select}
          aria-label="Location"
        >
          <option value="">Select Location</option>
          <option value="near-me">Near Me</option>
          <option value="city">City</option>
          {/* Add real locations later through API calls or from front-end directly */}
        </select>

        {/* Category */}
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className={styles.select}
          aria-label="Category"
        >
          <option value="">Select Category</option>
          <option value="party">Party</option>
          <option value="meetup">Meetup</option>
          <option value="networking">Networking</option>
          <option value="movie-night">Movie Night</option>
          <option value="camping">Camping</option>
          <option value="festival">Festival</option>
          <option value="Food & Drinks">Food & Drinks</option>
          <option value="gaming">Gaming</option>
        </select>

        {/* Date */}
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
          className={styles.input}
          aria-label="date"
        />

        {/* Search Button */}
        <button onClick={handleSearch} className={styles.button}>
          Search
        </button>
      </div>
    </div>
  );
};

export default SearchFilter;
