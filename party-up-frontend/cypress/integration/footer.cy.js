describe('Footer Component 2E2 Tests', () => {
    beforeEach(() => {
        cy.visit('/');
    });

    it('Renders the footer on the page', () => {
        // Check that footer is visible
        cy.get('footer').should('be.visible');
    });

    it('Displays the About section', () => {
        // Validate the presence of the About section
        cy.get('footer').within(() => {
            cy.contains('About').should('be.visible');
            cy.contains(
                'Party-Up is a social network dedicated to connecting people through shared events and experiences.'
            ).should('be.visible');
        });
    });

    it('Has all quick links with correct href attributes', () => {
        const links = [
            { text: 'Home', href: '#home' },
            { text: 'Events', href: '#events' },
            { text: 'Explore', href: '#explore' },
            { text: 'Support', href: '#support' },
            { text: 'Privacy Policy', href: '#privacy-policy' },
            { text: 'Terms of Service', href: '#terms-of-service' },
        ];

        links.forEach(({ text, href }) => {
            cy.get('footer')
            .contains(text)
            .should('have.attr', 'href', href);
        });
    });

    it('Has all social media icons with correct aria-labels', () => {
        const socialMedia = [
            { label: 'Facebook', href: '#facebook' },
            { label: 'Instagram', href: '#instagram' },
            { label: 'Twitter', href: '#twitter' },
            { label: 'LinkedIn', href: '#linkedin' },
        ];

        socialMedia.forEach(({ label, href }) => {
            cy.get('footer')
            .find(`[aria-label="${label}"]`)
            .should('have.attr', 'href', href)
            .and('be.visible');
        });
    });

    it('Displays the copyright information', () => {
        // Validate copyright information
        cy.get('footer').contains('© 2024 Party-Up. All rights reserved.').should('be.visible');
    })
})