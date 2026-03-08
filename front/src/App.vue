<template>
  <div class="chat-container">
    <!-- 标题栏 -->
    <header class="chat-header">
      <h1>AI 代码助手</h1>
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
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  max-width: 900px;
  margin: 0 auto;
  background: #fff;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

/* 标题栏 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.chat-header h1 {
  font-size: 1.25rem;
  font-weight: 600;
}

.new-chat-btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-size: 0.875rem;
  transition: all 0.2s;
}

.new-chat-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

/* 消息列表 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: #f9fafb;
}

.empty-state {
  text-align: center;
  color: #6b7280;
  padding: 60px 20px;
}

.empty-state p {
  font-size: 1.125rem;
}

.message {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.message.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.message-content {
  max-width: 70%;
}

.message.user .message-content {
  align-items: flex-end;
}

.message-text {
  padding: 12px 16px;
  border-radius: 16px;
  line-height: 1.6;
  font-size: 0.9375rem;
}

.message.user .message-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.message.assistant .message-text {
  background: white;
  color: #1f2937;
  border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.message-text :deep(pre) {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 8px 0;
}

.message-text :deep(code) {
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 0.875rem;
}

.message-text :deep(code:not(pre code)) {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 6px;
  border-radius: 4px;
}

.message.user .message-text :deep(code:not(pre code)) {
  background: rgba(255, 255, 255, 0.2);
}

/* Markdown 列表样式 */
.message-text :deep(ul),
.message-text :deep(ol) {
  padding-left: 1.5em;
  margin: 8px 0;
}

.message-text :deep(li) {
  margin: 4px 0;
}

/* Markdown 标题样式 */
.message-text :deep(h1),
.message-text :deep(h2),
.message-text :deep(h3),
.message-text :deep(h4),
.message-text :deep(h5),
.message-text :deep(h6) {
  margin: 12px 0 8px 0;
  font-weight: 600;
}

.message-text :deep(h1) { font-size: 1.5em; }
.message-text :deep(h2) { font-size: 1.3em; }
.message-text :deep(h3) { font-size: 1.1em; }

/* Markdown 引用块样式 */
.message-text :deep(blockquote) {
  border-left: 4px solid #667eea;
  padding-left: 12px;
  margin: 8px 0;
  color: #6b7280;
}

/* Markdown 表格样式 */
.message-text :deep(table) {
  border-collapse: collapse;
  margin: 8px 0;
  width: 100%;
}

.message-text :deep(th),
.message-text :deep(td) {
  border: 1px solid #e5e7eb;
  padding: 8px 12px;
  text-align: left;
}

.message-text :deep(th) {
  background: #f3f4f6;
  font-weight: 600;
}

/* Markdown 水平线样式 */
.message-text :deep(hr) {
  border: none;
  border-top: 1px solid #e5e7eb;
  margin: 12px 0;
}

/* Markdown 图片样式 */
.message-text :deep(img) {
  max-width: 100%;
  border-radius: 8px;
}

/* Markdown 链接样式 */
.message-text :deep(a) {
  color: #667eea;
  text-decoration: none;
}

.message-text :deep(a:hover) {
  text-decoration: underline;
}

/* 加载动画 */
.typing-indicator {
  display: flex;
  gap: 4px;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #667eea;
  animation: bounce 1.4s infinite ease-in-out both;
}

.typing-indicator span:nth-child(1) {
  animation-delay: -0.32s;
}

.typing-indicator span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%, 80%, 100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

/* 输入区域 */
.chat-input {
  display: flex;
  gap: 12px;
  padding: 16px 24px;
  background: white;
  border-top: 1px solid #e5e7eb;
}

.chat-input textarea {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  font-size: 0.9375rem;
  resize: none;
  outline: none;
  font-family: inherit;
  line-height: 1.5;
  transition: border-color 0.2s;
}

.chat-input textarea:focus {
  border-color: #667eea;
}

.chat-input textarea::placeholder {
  color: #9ca3af;
}

.send-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 0.9375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.send-btn:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.send-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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
    padding: 12px 16px;
  }

  .chat-messages {
    padding: 16px;
  }

  .chat-input {
    padding: 12px 16px;
  }
}
</style>