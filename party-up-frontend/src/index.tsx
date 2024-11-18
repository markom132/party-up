import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/global.scss'; // Import global styles
import App from './App';
import '@fortawesome/fontawesome-free/css/all.min.css'; // FontAwesome for icons

// Find the root element and ensure it exists
const rootElement = document.getElementById('root');

if (!rootElement) {
  throw new Error(
    'Root element not found. Please ensure index.html has a div with id="root".',
  );
}

const root = ReactDOM.createRoot(rootElement as HTMLElement);

// Render the application
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
);
