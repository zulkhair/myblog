const withLess = require('next-with-less');

module.exports = async (phase) => {
  /** @type {import('next').NextConfig} */
  const config = { lessLoaderOptions: {} };
  return withLess(config);
};
