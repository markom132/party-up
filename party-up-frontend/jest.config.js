// jest.config.js
module.exports = {
    testPathIgnorePatterns: [
        "/node_modules/",
        "/cypress/"
    ],
    testEnvironment: "jsdom",
    moduleNameMapper: {
      "\\.(css|less|scss|sass)$": "identity-obj-proxy"
    },
};