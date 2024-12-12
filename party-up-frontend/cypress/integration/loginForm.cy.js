describe('LoginForm Component E2E Tests', () => {
  beforeEach(() => {
    // Intercept login request to mock a successful response
    cy.intercept('POST', 'http://localhost:8080/api/auth/login', {
      statusCode: 200,
      body: {
        token: 'mocked-jwt-token',
        message: 'Login successful',
      },
    }).as('loginRequest');

    cy.visit('/login');
  });

  it('Shows login form', () => {
    cy.get('h1').contains('Login').should('be.visible');
    cy.get('input[placeholder="Enter your username"]').should('be.visible');
    cy.get('input[placeholder="Enter your password"]').should('be.visible');
    cy.get('button').contains('Login').should('be.visible');
  });

  it('Shows errors for empty fields', () => {
    cy.get('button').contains('Login').click();
    cy.contains('Please enter your username').should('be.visible');
    cy.contains('Please enter your password').should('be.visible');
  });

  it('Redirects to /home after successful login', () => {
    // Fill in username and password
    cy.get('input[placeholder="Enter your username"]').type('testuser');
    cy.get('input[placeholder="Enter your password"]').type('password123');
    cy.get('button').contains('Login').click();

    // Wait for the login request to complete
    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

    // Assert that the user is redirected to /home
    cy.url().should('include', '/home');
  });

  it('Login failed', () => {
    // Mock for failed login
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: 'Invalid credentials',
    }).as('loginRequest');

    // Fill the form
    cy.get('input[placeholder="Enter your username"]').type('wronguser');
    cy.get('input[placeholder="Enter your password"]').type('wrongpassword');
    cy.get('button').contains('Login').click();

    // Checking error
    cy.wait('@loginRequest');
    cy.contains('Invalid credentials').should('be.visible');
  });
});
