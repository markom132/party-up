describe('Header Component E2E Tests', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Shows all navigation links on large screens', () => {
    cy.viewport(1024, 768);

    cy.get('[data-testid="nav-link-home"]').should('be.visible');
    cy.get('[data-testid="nav-link-events"]').should('be.visible');
    cy.get('[data-testid="nav-link-explore"]').should('be.visible');
  });

  it('Opens and closes hamburger menu on small screens', () => {
    cy.viewport(500, 768);

    cy.get('[data-testid="hamburger-menu"]').should('be.visible');
    cy.get('[data-testid="hamburger-menu"]').click();
    cy.get('a[href="#home"]').should('be.visible');
    cy.get('a[href="#events"]').should('be.visible');
    cy.get('a[href="#explore"]').should('be.visible');

    cy.get('[data-testid="hamburger-menu"]').click();
    // Nav links should be hidden
    cy.get('[data-testid="nav-links"]').should('have.css', 'display', 'none');
  });

  it('Shows login and sign-up buttons when user is not logged in', () => {
    // Ensure the user is logged out
    cy.window().then((win) => {
      win.localStorage.setItem('isLoggedIn', 'false');
    });
    cy.reload();

    cy.get('[data-testid="login-link"]').should('be.visible');
    cy.get('[data-testid="signup-button"]').should('be.visible');
  });

  it('Shows profile avatar when user is logged in', () => {
    // Simulate login
    cy.window().then((win) => {
      win.localStorage.setItem('isLoggedIn', 'true');
    });
    cy.reload();

    cy.get('[data-testid="profile-avatar"]').should('be.visible');
    cy.get('[data-testid="login-link"]').should('not.exist');
    cy.get('[data-testid="signup-button"]').should('not.exist');
  });

  it('Logs out the user when Log Out is clicked', () => {
    // Mock the API call for logout
    cy.intercept('POST', 'http://localhost:8080/api/auth/logout', {
      statusCode: 200,
      body: {
        message: 'Logged out successfully',
      },
    }).as('mockedLogout'); // Alias for the mocked call

    // Simulate login state
    cy.window().then((win) => {
      win.localStorage.setItem('isLoggedIn', 'true');
    });
    cy.reload();

    // Simulate log out flow
    cy.get('[data-testid="profile-avatar"]').click();
    cy.get('[data-testid="dropdown-menu"]').contains('Log out').click();

    // Ensure the mocked API call was made
    cy.wait('@mockedLogout').its('response.statusCode').should('eq', 200);

    // Validate UI changes after logout
    cy.get('[data-testid="login-link"]').should('be.visible');
    cy.get('[data-testid="signup-button"]').should('be.visible');
  });
});
