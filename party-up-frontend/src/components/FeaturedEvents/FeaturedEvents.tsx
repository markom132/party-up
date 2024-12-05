import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './FeaturedEvents.module.scss';

interface Event {
  id: number;
  title: string;
  date: string;
  location: string;
  image: string;
  description: string;
}

interface FeaturedEventsProps {
  events: Event[];
}

const FeaturedEvents: React.FC<FeaturedEventsProps> = ({ events }) => {
  const navigate = useNavigate();

  const handleDetailsClick = (id: number) => {
    navigate(`/event/${id}`);
  };
  return (
    <section
      className={styles['featured-events']}
      data-testid="featured-events"
    >
      <h2 className={styles['section-title']}>Featured Events</h2>
      <div className={styles['events-grid']}>
        {events.map((event) => (
          <div
            className={styles['event-card']}
            key={event.id}
            data-testid="event-card"
          >
            <img
              src={event.image}
              alt={event.title}
              className={styles['event-image']}
            />
            <h3 className={styles['event-title']} data-testid="event-title">
              {event.title}
            </h3>
            <p className={styles['event-date']}>{event.date}</p>
            <p className={styles['event-location']}>{event.location}</p>
            <p className={styles['event-description']}>{event.description}</p>
            <button
              className={styles['event-button']}
              onClick={() => handleDetailsClick(event.id)}
            >
              Details
            </button>
          </div>
        ))}
      </div>
    </section>
  );
};

export default FeaturedEvents;
