module.exports = {
  "root": true,
  "env": {
    "node": true
  },
  "plugins": ["prettier"],
  "extends": [
    "plugin:vue/recommended",
    "eslint:recommended",
    "plugin:prettier/recommended",
    "prettier/vue",
  ],
  "rules": {
    "no-console": "off",
    "prettier/prettier": "error"
  },
  "parserOptions": {
    "parser": "babel-eslint"
  }
}