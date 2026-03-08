import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Vite配置文件
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 代理API请求到后端
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})