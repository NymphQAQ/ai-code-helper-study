<template>
  <!-- Landing Page -->
  <Landing v-if="!showChat" @start-chat="showChat = true" />

  <!-- 聊天界面 -->
  <div v-else class="chat-container">
    <!-- 标题栏 -->
    <header class="chat-header">
      <div class="header-left">
        <button class="back-btn" @click="showChat = false" title="返回首页">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
        </button>
        <h1>AI 代码助手</h1>
      </div>
      <button class="new-chat-btn" @click="startNewChat">新建对话</button>
    </header>

    <!-- 消息列表区域 -->
    <main class="chat-messages" ref="messagesContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <p>👋 你好！我是AI代码助手，有什么可以帮助你的吗？</p>
      </div>
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message', msg.role]"
      >
        <div class="message-avatar">
          {{ msg.role === 'user' ? '👤' : '🤖' }}
        </div>
        <div class="message-content">
          <div class="message-text" v-html="formatMessage(msg.content)"></div>
        </div>
      </div>
      <div v-if="isLoading" class="message assistant loading">
        <div class="message-avatar">🤖</div>
        <div class="message-content">
          <div class="message-text">
            <span class="typing-indicator">
              <span></span><span></span><span></span>
            </span>
          </div>
        </div>
      </div>
    </main>

    <!-- 输入区域 -->
    <footer class="chat-input">
      <textarea
        v-model="inputMessage"
        @keydown.enter.exact.prevent="sendMessage"
        placeholder="输入消息... (Enter发送)"
        rows="1"
        ref="inputArea"
      ></textarea>
      <button
        @click="sendMessage"
        :disabled="!inputMessage.trim() || isLoading"
        class="send-btn"
      >
        {{ isLoading ? '发送中...' : '发送' }}
      </button>
    </footer>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { marked } from 'marked'
import { chatStream } from './api/chat.js'
import Landing from './components/Landing.vue'

// 是否显示聊天界面
const showChat = ref(false)

// 配置 marked 选项
marked.setOptions({
  breaks: true,      // 支持 GitHub 风格的换行
  gfm: true          // 启用 GitHub Flavored Markdown
})

// 消息列表
const messages = ref([])
// 输入框内容
const inputMessage = ref('')
// 加载状态
const isLoading = ref(false)
// 会话ID（用于保持对话上下文），使用简单整数
const memoryId = ref(1)
// 消息容器引用
const messagesContainer = ref(null)
// 输入框引用
const inputArea = ref(null)

/**
 * 发送消息
 */
async function sendMessage() {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message
  })

  // 清空输入框
  inputMessage.value = ''

  // 滚动到底部
  await scrollToBottom()

  // 设置加载状态
  isLoading.value = true

  // 添加AI回复消息占位
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'assistant',
    content: ''
  })

  try {
    // 发送流式请求
    chatStream(memoryId.value, message, {
      onMessage: async (data) => {
        // 追加消息内容
        messages.value[aiMessageIndex].content += data
        // 滚动到底部
        await scrollToBottom()
      },
      onError: (error) => {
        console.error('聊天出错:', error)
        messages.value[aiMessageIndex].content = '抱歉，发生了错误，请稍后重试。'
        isLoading.value = false
      },
      onComplete: () => {
        isLoading.value = false
      }
    })
  } catch (error) {
    console.error('发送消息失败:', error)
    messages.value[aiMessageIndex].content = '抱歉，发送失败，请稍后重试。'
    isLoading.value = false
  }
}

/**
 * 开始新对话
 */
function startNewChat() {
  messages.value = []
  // 使用简单递增的会话ID
  memoryId.value = memoryId.value + 1
  inputMessage.value = ''
}

/**
 * 滚动到底部
 */
async function scrollToBottom() {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

/**
 * 格式化消息内容（Markdown 渲染）
 */
function formatMessage(content) {
  if (!content) return ''
  try {
    // 使用 marked 解析 Markdown，返回安全的 HTML
    return marked.parse(content)
  } catch (e) {
    console.error('Markdown 解析错误:', e)
    return content
  }
}

// 组件挂载后聚焦输入框
onMounted(() => {
  inputArea.value?.focus()
})
</script>

<style scoped>
/* 全局背景 - 玻璃拟态需要渐变背景才能显现效果 */
body {
  margin: 0;
  padding: 0;
}

.chat-container {
  --glass-bg: rgba(255, 255, 255, 0.15);
  --glass-border: rgba(255, 255, 255, 0.25);
  --glass-shadow: 0 8px 32px rgba(31, 38, 135, 0.15);
  --glass-blur: 16px;

  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 1000px;
  margin: 0 auto;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  position: relative;
  overflow: hidden;
}

/* 装饰性背景圆圈 */
.chat-container::before {
  content: '';
  position: absolute;
  width: 500px;
  height: 500px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  top: -200px;
  right: -150px;
  filter: blur(60px);
}

.chat-container::after {
  content: '';
  position: absolute;
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.08);
  border-radius: 50%;
  bottom: -150px;
  left: -100px;
  filter: blur(50px);
}

/* 标题栏 - 玻璃效果 */
.chat-header {
  position: relative;
  z-index: 10;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 28px;
  background: var(--glass-bg);
  backdrop-filter: blur(var(--glass-blur));
  -webkit-backdrop-filter: blur(var(--glass-blur));
  border-bottom: 1px solid var(--glass-border);
  color: white;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

/* 返回按钮 - 玻璃拟态 */
.back-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  color: white;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.back-btn:active {
  transform: translateY(0);
}

.chat-header h1 {
  font-size: 1.3rem;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 新建对话按钮 - 玻璃拟态 */
.new-chat-btn {
  padding: 10px 20px;
  background: rgba(255, 255, 255, 0.12);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.25);
  border-radius: 14px;
  color: white;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.new-chat-btn:hover {
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.4);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.new-chat-btn:active {
  transform: translateY(0);
}

/* 消息列表 - 玻璃效果 */
.chat-messages {
  position: relative;
  z-index: 5;
  flex: 1;
  overflow-y: auto;
  padding: 28px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.empty-state {
  text-align: center;
  color: rgba(255, 255, 255, 0.9);
  padding: 60px 20px;
}

.empty-state p {
  font-size: 1.2rem;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  padding: 20px 30px;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: inline-block;
}

.message {
  display: flex;
  gap: 14px;
  margin-bottom: 24px;
  animation: slideIn 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(15px) scale(0.98);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.3rem;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.message-content {
  max-width: 70%;
}

.message.user .message-content {
  align-items: flex-end;
}

/* 消息气泡 - 玻璃效果 */
.message-text {
  padding: 14px 20px;
  border-radius: 20px;
  line-height: 1.7;
  font-size: 0.95rem;
}

.message.user .message-text {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  color: white;
  border-bottom-right-radius: 6px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.message.assistant .message-text {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  color: #1f2937;
  border-bottom-left-radius: 6px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

/* Markdown 样式优化 */
.message-text :deep(pre) {
  background: rgba(30, 30, 30, 0.95);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  color: #d4d4d4;
  padding: 16px;
  border-radius: 12px;
  overflow-x: auto;
  margin: 10px 0;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.message-text :deep(code) {
  font-family: 'JetBrains Mono', 'Consolas', 'Monaco', monospace;
  font-size: 0.875rem;
}

.message-text :deep(code:not(pre code)) {
  background: rgba(102, 126, 234, 0.15);
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 0.9em;
}

.message.user .message-text :deep(code:not(pre code)) {
  background: rgba(255, 255, 255, 0.25);
}

.message-text :deep(ul),
.message-text :deep(ol) {
  padding-left: 1.5em;
  margin: 10px 0;
}

.message-text :deep(li) {
  margin: 6px 0;
}

.message-text :deep(h1),
.message-text :deep(h2),
.message-text :deep(h3),
.message-text :deep(h4),
.message-text :deep(h5),
.message-text :deep(h6) {
  margin: 14px 0 10px 0;
  font-weight: 600;
}

.message-text :deep(h1) { font-size: 1.5em; }
.message-text :deep(h2) { font-size: 1.3em; }
.message-text :deep(h3) { font-size: 1.1em; }

.message-text :deep(blockquote) {
  border-left: 4px solid rgba(102, 126, 234, 0.6);
  padding-left: 14px;
  margin: 10px 0;
  color: #6b7280;
  background: rgba(102, 126, 234, 0.05);
  border-radius: 0 8px 8px 0;
  padding: 8px 14px;
}

.message-text :deep(table) {
  border-collapse: collapse;
  margin: 10px 0;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

.message-text :deep(th),
.message-text :deep(td) {
  border: 1px solid rgba(0, 0, 0, 0.1);
  padding: 10px 14px;
  text-align: left;
}

.message-text :deep(th) {
  background: rgba(102, 126, 234, 0.1);
  font-weight: 600;
}

.message-text :deep(hr) {
  border: none;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  margin: 14px 0;
}

.message-text :deep(img) {
  max-width: 100%;
  border-radius: 12px;
}

.message-text :deep(a) {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
}

.message-text :deep(a:hover) {
  text-decoration: underline;
}

/* 加载动画 - 玻璃效果 */
.typing-indicator {
  display: flex;
  gap: 6px;
  padding: 4px;
}

.typing-indicator span {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.6);
  animation: pulse 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }

@keyframes pulse {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区域 - 玻璃效果 */
.chat-input {
  position: relative;
  z-index: 10;
  display: flex;
  gap: 14px;
  padding: 20px 28px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(var(--glass-blur));
  -webkit-backdrop-filter: blur(var(--glass-blur));
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.chat-input textarea {
  flex: 1;
  padding: 14px 20px;
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  font-size: 0.95rem;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.6;
  color: white;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.chat-input textarea:focus {
  background: rgba(255, 255, 255, 0.22);
  border-color: rgba(255, 255, 255, 0.5);
  box-shadow: 0 0 0 4px rgba(255, 255, 255, 0.1);
}

.chat-input textarea::placeholder {
  color: rgba(255, 255, 255, 0.6);
}

/* 发送按钮 - 玻璃拟态效果 */
.send-btn {
  padding: 14px 28px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 16px;
  color: white;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

/* 按钮光效 */
.send-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.5s ease;
}

.send-btn:hover::before {
  left: 100%;
}

.send-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.35);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-3px);
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.send-btn:active:not(:disabled) {
  transform: translateY(-1px);
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 滚动条美化 */
.chat-messages::-webkit-scrollbar {
  width: 8px;
}

.chat-messages::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 响应式 */
@media (max-width: 768px) {
  .chat-container {
    max-width: 100%;
  }

  .message-content {
    max-width: 85%;
  }

  .chat-header {
    padding: 14px 18px;
  }

  .chat-messages {
    padding: 18px;
  }

  .chat-input {
    padding: 14px 18px;
    gap: 10px;
  }

  .send-btn {
    padding: 14px 20px;
  }

  .back-btn {
    width: 36px;
    height: 36px;
  }

  .new-chat-btn {
    padding: 8px 14px;
    font-size: 0.85rem;
  }
}

@media (max-width: 480px) {
  .chat-header h1 {
    font-size: 1.1rem;
  }

  .message-avatar {
    width: 36px;
    height: 36px;
    font-size: 1.1rem;
  }

  .message-text {
    padding: 12px 16px;
    font-size: 0.9rem;
  }
}
</style>