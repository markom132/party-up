describe('Home Page E2E Tests', () => {
  beforeEach(() => {
    cy.visit('/home');
  });

  // Hero Section
  it('Displays Hero Section with proper content', () => {
    cy.get('h1').contains('Connect. Organize. Attend.').should('be.visible');
    cy.get('p').contains('Party-Up helps you').should('be.visible');
    cy.get('a').contains('Explore Events').should('be.visible');
    cy.get('a').contains('Create Event').should('be.visible');

    // Click on buttons
    cy.get('a').contains('Explore Events').click();
    cy.url().should('include', '/events');
    cy.go('back');
  });

  // Featured Events
  it('Displays Featured Events and navigates to details', () => {
    cy.get('[data-testid="featured-events"]').within(() => {
      cy.get('[data-testid="event-card"]').should('have.length.greaterThan', 0);
      cy.get('[data-testid="event-card"]')
        .first()
        .within(() => {
          cy.get('[data-testid="event-title"]').should('be.visible');
          cy.get('button').contains('Details').click();
        });
    });
    cy.url().should('include', '/event/');
    cy.go('back');
  });

  // Search filter
  it('Executes search with proper filters', () => {
    cy.get('[data-testid="search-filter"]').within(() => {
      cy.get('input[placeholder="Find event by name"]').should('be.visible');
      cy.get('select').first().should('be.visible');
      cy.get('select').last().should('be.visible');
      cy.get('input[type="date"]').should('be.visible');
      cy.get('button').contains('Search').should('be.visible');
    });
  });

  //Social Feed
  it('Interacts with Social Feed', () => {
    cy.get('[data-testid="social-feed"]').within(() => {
      cy.get('[data-testid="post"]').should('have.length.greaterThan', 0);
      cy.get('[data-testid="post"]')
        .first()
        .within(() => {
          cy.get('button').contains('Like').should('be.visible');
          cy.get('button').contains('Comment').should('be.visible');
        });

      // Click on Load More
      cy.get('button').contains('Load More').click();
      cy.wait(2000);
      cy.get('[data-testid="post"]').should('have.length.greaterThan', 3);
    });
  });

  // Top Reviews
  it('Display Top Reviews and navigate to review', () => {
    cy.get('[data-testid="top-reviews"]').within(() => {
      cy.get('[data-testid="place-card"]').should('have.length.greaterThan', 0);
      cy.get('[data-testid="place-card"]')
        .first()
        .within(() => {
          cy.get('button').contains('Read Reviews').click();
        });
    });
    cy.url().should('include', '/reviews/');
  });
});
