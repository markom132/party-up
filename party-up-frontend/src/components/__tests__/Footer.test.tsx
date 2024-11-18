import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Footer from '../Footer/Footer';
import { text } from 'stream/consumers';

describe('Footer Component', () => {
    test('Renders the footer without crashing', () => {
        render(<Footer />);
        expect(screen.getByRole('contentinfo')).toBeInTheDocument();
    });

    test('Displays the About section with text', () => {
        render(<Footer />);
        expect(screen.getByText(/Party-Up is a social network/i)).toBeInTheDocument();
        expect(screen.getByText(/connecting people through shared events and experiences/i)).toBeInTheDocument();
    });

    test('Renders quick links with correct text and href attributes', () => {
        render(<Footer />);
        const links = [
            {text: 'Home', href: '#home'},
            {text: 'Events', href: '#events'},
            {text: 'Explore', href: '#explore'},
            {text: 'Support', href: '#support'},
            {text: 'Privacy Policy', href: '#privacy-policy'},
            {text: 'Terms of Service', href: '#terms-of-service'},
        ];

        links.forEach(({ text, href }) => {
            const link = screen.getByText(text);
            expect(link).toBeInTheDocument();
            expect(link).toHaveAttribute('href', href);
        });
    });

    test('Renders social media icons with correct aria-labels', () => {
        render(<Footer />);
        const socialMediaIcons = [
            { label: 'Facebook', href: '#facebook' },
            { label: 'Instagram', href: '#instagram' },
            { label: 'Twitter', href: '#twitter' },
            { label: 'LinkedIn', href: '#linkedin'},
        ];

        socialMediaIcons.forEach(({ label, href }) => {
            const icon = screen.getByLabelText(label);
            expect(icon).toBeInTheDocument();
            expect(icon.closest('a')).toHaveAttribute('href', href);
        });
    });

    test('Display the copyright information', () => {
        render(<Footer />);
        expect(screen.getByText(/© 2024 Party-Up. All rights reserved./i)).toBeInTheDocument();
    });
})