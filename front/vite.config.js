import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

/**
 * Vite 构建工具配置文件
 *
 * 职责：
 * - 加载 Vue 3 插件，支持 .vue 单文件组件
 * - 开发服务器监听 5173 端口
 * - 将 /api 前缀的请求代理到后端 Spring Boot 服务（8080 端口），
 *   解决开发环境下的浏览器同源策略限制
 *
 * 注意：changeOrigin:true 会将请求头中的 Host 替换为目标地址，
 * 与后端 CORS 配置共同保障跨域通信正常。
 */
export default defineConfig({
  // 注册 Vue 3 插件，支持 <template>/<script setup>/<style scoped>
  plugins: [vue()],
  server: {
    // 开发服务器端口
    port: 5173,
    proxy: {
      // 将所有 /api 开头的请求转发到后端，避免前端直接跨域
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})