import globals from 'globals';
import pluginJs from '@eslint/js';
import pluginReact from 'eslint-plugin-react';
import pluginPrettier from 'eslint-plugin-prettier';
import eslintConfigPrettier from 'eslint-config-prettier';
import pluginTypescript from '@typescript-eslint/eslint-plugin';
import parserTypescript from '@typescript-eslint/parser';
import pluginCypress from 'eslint-plugin-cypress';

export default [
  // Base Configuration
  {
    files: ['**/*.{js,mjs,cjs,jsx,ts,tsx}'],
    ignores: [
      'node_modules/**',
      'dist/**',
      '**/*.d.ts',
      '__mocks__/**/*',
      '**/*.cjs',
      'cypress/**/*.js',
      'cypress/**/*.ts',
      'cypress/**/*.tsx',
      '**/*.test.{js,ts,tsx}',
      '**/*.spec.{js,ts,tsx}',
      'coverage/**',
      '*.config.mjs',
      '*.config.js',
      'jest.setup.js',
      '**/__tests__/**',
    ],
    languageOptions: {
      parser: parserTypescript,
      parserOptions: {
        ecmaVersion: 2020,
        sourceType: 'module',
        ecmaFeatures: { jsx: true },
        project: './tsconfig.json',
      },
      globals: {
        ...globals.browser,
        require: 'readonly',
        module: 'readonly',
      },
    },
    plugins: {
      prettier: pluginPrettier,
      '@typescript-eslint': pluginTypescript,
    },
    rules: {
      'no-console': 'warn',
      'no-unused-vars': 'warn',
      '@typescript-eslint/no-unused-vars': ['warn'],
      '@typescript-eslint/explicit-module-boundary-types': 'off',
      '@typescript-eslint/no-explicit-any': 'warn',
      quotes: ['error', 'single'],
      semi: ['error', 'always'],
      'react/jsx-filename-extension': [1, { extensions: ['.tsx', '.jsx'] }],
      'react/prop-types': 'off',
      'prettier/prettier': 'error',
    },
  },
  // Test Files (Unit and Integration)
  {
    files: ['**/*.test.{js,ts,tsx}', '**/*.spec.{js,ts,tsx}'],
    languageOptions: {
      globals: {
        window: 'readonly',
        Event: 'readonly',
        jest: 'readonly',
        describe: 'readonly',
        it: 'readonly',
        test: 'readonly',
        expect: 'readonly',
        beforeEach: 'readonly',
        afterEach: 'readonly',
      },
    },
    rules: {
      'no-unused-vars': 'off',
      '@typescript-eslint/no-unused-vars': 'off',
      'no-undef': 'off',
    },
  },
  // Cypress Files
  {
    files: ['cypress/**/*.js', 'cypress/**/*.ts', 'cypress/**/*.tsx'],
    plugins: {
      cypress: pluginCypress,
    },
    languageOptions: {
      globals: {
        cy: 'readonly',
        Cypress: 'readonly',
        describe: 'readonly',
        it: 'readonly',
        beforeEach: 'readonly',
      },
    },
    rules: {
      'no-unused-vars': 'off',
      'no-undef': 'off',
    },
  },
  // React-Specific Settings
  {
    settings: {
      react: { version: 'detect' },
    },
  },
  // Prettier Compatibility
  eslintConfigPrettier,
  pluginJs.configs.recommended,
  pluginReact.configs.flat.recommended,
];
