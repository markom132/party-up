const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    specPattern: 'cypress/integration/**/*.{js,jsx,ts,tsx}', // Update this to match your test files' path
    setupNodeEvents() {
      // implement node event listeners here
    },
  },
});
