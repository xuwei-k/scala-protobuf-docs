"use strict";

const js = require("@eslint/js");
const globals = require("globals");

module.exports = [
  js.configs.recommended,
  {
    languageOptions: {
      ecmaVersion: 2015,
      sourceType: "module",
      globals: {
        ...globals.node,
        ...globals.mocha,
      },
    },
    rules: {
      "no-multi-spaces": 0,
      "no-invalid-this": 0,
      "indent": [2, 2],

      // ES6 RULES
      "arrow-parens": 2,
      "arrow-spacing": 2,
      "constructor-super": 2,
      "generator-star-spacing": 2,
      "no-class-assign": 2,
      "no-const-assign": 2,
      "no-dupe-class-members": 2,
      "no-this-before-super": 2,
      "no-var": 2,
      "object-shorthand": 2,
      "prefer-arrow-callback": 1,
      "prefer-const": 0,
      "prefer-spread": 2,
      "prefer-template": 1,
      "require-yield": 2,
    },
  },
];
