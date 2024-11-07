const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    specPattern: "cypress/integration/**/*.{js,jsx,ts,tsx}", // Update this to match your test files' path
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
