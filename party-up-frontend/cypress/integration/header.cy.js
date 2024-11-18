describe('Header Component E2E Tests', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('Shows all navigation links on large screens', () => {
    cy.viewport(1024, 768);

    cy.get('a[href="#home"]').should('be.visible');
    cy.get('a[href="#events"]').should('be.visible');
    cy.get('a[href="#explore"]').should('be.visible');
  });

  it('Opens and closes hamburger menu on small screens', () => {
    cy.viewport(500, 768);

    // Hamburger menu sgould be visible
    cy.get('[data-testid="hamburger-menu"]').should('be.visible');

    // Click on menu
    cy.get('[data-testid="hamburger-menu"]').click();

    // Nav links should be visible
    cy.get('a[href="#home"]').should('be.visible');
    cy.get('a[href="#events"]').should('be.visible');
    cy.get('a[href="#explore"]').should('be.visible');

    // Click again to close menu
    cy.get('[data-testid="hamburger-menu"]').click();

    // Nav links should be hidden
    cy.get('[data-testid="nav-links"]').should('have.css', 'display', 'none');
  });

  it('Shows Login and Sign Up buttons when user is not logged in', () => {
    cy.get('[data-testid="profile-avatar"]').click();
    cy.get('[data-testid="dropdown-menu"] button').contains('Log out').click();

    cy.get('[data-testid="login-link"]').should('be.visible');
    cy.get('[data-testid="signup-button"]').should('be.visible');
  });

  it('Shows Profile avatar and dropdown when user is logged in', () => {
    // Avatar should be visible
    cy.get('[data-testid="profile-avatar"]').should('be.visible');

    // Click on avatar to open dropdown
    cy.get('[data-testid="profile-avatar"]').click();
    cy.get('[data-testid="dropdown-menu"]').should('be.visible');

    // Check dropdown items
    cy.get('[data-testid="dropdown-menu"]')
      .contains('My Profile')
      .should('be.visible');

    cy.get('[data-testid="dropdown-menu"]')
      .contains('Settings')
      .should('be.visible');

    cy.get('[data-testid="dropdown-menu"] button')
      .contains('Log out')
      .should('be.visible');
  });

  it('Logs out the user when Log Out is clicked', () => {
    cy.get('[data-testid="profile-avatar"]').click();
    cy.get('[data-testid="dropdown-menu"]').contains('Log out').click();

    cy.get('[data-testid="login-link"]').should('be.visible');
    cy.get('[data-testid="signup-button"]').should('be.visible');
  });
});
