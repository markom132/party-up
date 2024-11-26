import React from "react";
import { render, screen } from '@testing-library/react';
import HeroSection from "../HeroSection/HeroSection";

describe('HeroSection Component', () => {
    test('Renders HeroSection', () => {
        const userName: string = 'Marko';
        render(<HeroSection userName={userName} />);

        expect(screen.getByText('Welcome back, Marko! Ready to party?'));
        expect(screen.getByText('Connect. Organize. Attend.'));
        expect(screen.getByText('Party-Up helps you find nearby events, meet people and share unforgettable moments.'));
        expect(screen.getByText('Explore Events'));
        expect(screen.getByText('Create Event'));
        expect(screen.getByText('View Profile'));
    });
});