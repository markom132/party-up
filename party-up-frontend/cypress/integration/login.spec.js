describe('Basic Page Load Test', () => {
  it('should load the homepage successfully', () => {
    cy.visit('http://localhost:3000');

    cy.contains('Welcome');
  });
});
