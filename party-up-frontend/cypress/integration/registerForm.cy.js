describe('User Registration Flow', () => {
  beforeEach(() => {
    cy.visit('/create-account');
  });

  it('should register a user and show the success message modal', () => {
    cy.intercept('POST', 'http://localhost:8080/api/create-user', {
      statusCode: 200,
      body: { success: true, message: 'User created successfully' },
    }).as('createUser'); // Set up mock and alias

    cy.get('input#firstName').type('John');
    cy.get('input#lastName').type('Doe');
    cy.get('input#username').type('johndoe');
    cy.get('input#email').type('john.doe@example.com');
    cy.get('input#birthDay').type('1990-01-01');
    cy.get('input[type="checkbox"]').check();

    cy.get('button[type="submit"]').click();

    // Ensure the API request is completed
    cy.wait('@createUser');

    // Assert modal appears (extend timeout if needed)
    cy.get('[data-testid="success-modal"]', { timeout: 10000 })
      .should('be.visible')
      .contains('Thanks for registering!');
  });

  it('should navigate to the email client on clicking contact support', () => {
    cy.intercept('POST', 'http://localhost:8080/api/create-user', {
      statusCode: 200,
      body: { success: true, message: 'User created successfully' },
    }).as('createUser'); // Set up mock and alias

    cy.get('input#firstName').type('John');
    cy.get('input#lastName').type('Doe');
    cy.get('input#username').type('johndoe');
    cy.get('input#email').type('john.doe@example.com');
    cy.get('input#birthDay').type('1990-01-01');
    cy.get('input[type="checkbox"]').check();

    cy.get('button[type="submit"]').click();

    // Ensure the API request is completed
    cy.wait('@createUser');

    cy.get('[data-testid="success-modal"]').should('be.visible'); // Ensure the modal is open

    cy.get('[data-testid="contact-support"]') // Assuming this is the test ID for the "Contact Us" link
      .should('have.attr', 'href', 'mailto:support@partyup.com'); // Verify the email link
  });

  it('should close the modal and navigate to the login page', () => {
    cy.intercept('POST', 'http://localhost:8080/api/create-user', {
      statusCode: 200,
      body: { success: true, message: 'User created successfully' },
    }).as('createUser'); // Set up mock and alias

    cy.get('input#firstName').type('John');
    cy.get('input#lastName').type('Doe');
    cy.get('input#username').type('johndoe');
    cy.get('input#email').type('john.doe@example.com');
    cy.get('input#birthDay').type('1990-01-01');
    cy.get('input[type="checkbox"]').check();

    cy.get('button[type="submit"]').click();

    // Ensure the API request is completed
    cy.wait('@createUser');

    cy.get('[data-testid="success-modal"]').should('be.visible'); // Ensure the modal is open

    cy.get('[data-testid="return-login"]') // Assuming this is the test ID for the button
      .should('contain', 'Return to login page') // Verify the content
      .click();

    // Assert navigation to the login page
    cy.url().should('include', '/login');
  });

  it('shows validation errors when mandatory fields are empty', () => {
    cy.get('input[type="checkbox"]').check();
    cy.get('button[type="submit"]').click();

    // Check for error messages on all required fields
    cy.get('#firstName + [data-testid="first-name-error"]').should(
      'contain',
      'First name is required',
    );
    cy.get('#lastName + [data-testid="last-name-error"]').should(
      'contain',
      'Last name is required',
    );
    cy.get('#username + [data-testid="username-error"]').should(
      'contain',
      'Username is required',
    );
    cy.get('#email + [data-testid="email-error"]').should(
      'contain',
      'A valid email is required',
    );
    cy.get('#birthDay + [data-testid="birth-day-error"]').should(
      'contain',
      'Birth date is required',
    );
  });

  it('shows an error for invalid email address', () => {
    // Check the Terms and Conditions box
    cy.get('input[type="checkbox"]').check();

    // Type an invalid email
    cy.get('#email').type('invalid-email');

    // Attempt to submit the form
    cy.get('button[type="submit"]').click();

    // Assert that the email input is invalid
    cy.get('#email').then(($email) => {
      cy.expect($email[0].validity.valid).to.be.false;

      cy.expect($email[0].validationMessage).to.eq(
        "Please include an '@' in the email address. 'invalid-email' is missing an '@'.",
      );
    });
  });

  it('disables submission without accepting terms and conditions', () => {
    // Fill in all the required fields
    cy.get('input#firstName').type('John');
    cy.get('input#lastName').type('Doe');
    cy.get('input#username').type('johndoe');
    cy.get('input#email').type('john.doe@example.com');
    cy.get('input#birthDay').type('1990-01-01');

    // Ensure the checkbox is not checked
    cy.get('input[type="checkbox"]').should('not.be.checked');

    // Verify that the submit button is disabled
    cy.get('button[type="submit"]').should('be.disabled');

    // Check the checkbox to enable the button
    cy.get('input[type="checkbox"]').check();

    // Verify that the submit button is enabled
    cy.get('button[type="submit"]').should('not.be.disabled');
  });
});
