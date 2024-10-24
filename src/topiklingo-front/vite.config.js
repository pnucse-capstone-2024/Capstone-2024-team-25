import react from '@vitejs/plugin-react';
import { defineConfig, transformWithEsbuild } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    {
      name: 'treat-js-files-as-jsx',
      async transform(code, id) {
        if (!id.match(/src\/.*\.js$/))  return null

        // Use the exposed transform from vite, instead of directly
        // transforming with esbuild
        return transformWithEsbuild(code, id, {
          loader: 'jsx',
          jsx: 'automatic',
        })
      },
    },
    react(),
  ],
  optimizeDeps: {
    esbuildOptions: {
      loader: {
        '.js': 'jsx',
      },
    },
  },
  resolve: {
    alias: [
      { find: '@assets', replacement: '/src/assets' },
      { find: '@components', replacement: '/src/components' },
      { find: '@constants', replacement: '/src/constants' },
      { find: '@pages', replacement: '/src/pages' },
      { find: '@utils', replacement: '/src/utils' },
      { find: '@api', replacement: '/src/api' },
      { find: '@hooks', replacement: '/src/hooks' },
      { find: '@atom', replacement: '/src/atom.js' },
    ],
  },
  server: {
    host: '0.0.0.0',
    hmr: {overlay: false},
    port: 5173,
    proxy: {
      '/s3-bucket': {
        target: 'https://topikkorea.s3.amazonaws.com',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/s3-bucket/, ''),
      },
    },
  },
});
