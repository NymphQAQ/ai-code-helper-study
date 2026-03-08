/**
 * AI聊天API服务
 * 使用SSE(Server-Sent Events)实现流式输出
 */

// API基础路径
const API_BASE = '/api/ai'

/**
 * 发送聊天消息并获取流式响应
 * @param {number} memoryId - 会话ID，用于保持对话上下文
 * @param {string} message - 用户消息
 * @param {function} onMessage - 收到消息时的回调函数
 * @param {function} onError - 发生错误时的回调函数
 * @param {function} onComplete - 流式响应完成时的回调函数
 * @returns {function} 取消请求的函数
 */
export function chatStream(memoryId, message, { onMessage, onError, onComplete }) {
  // 构建请求URL
  const url = `${API_BASE}/chat?memoryId=${encodeURIComponent(memoryId)}&message=${encodeURIComponent(message)}`

  // 创建EventSource连接
  const eventSource = new EventSource(url)

  // 监听消息事件
  eventSource.onmessage = (event) => {
    if (event.data) {
      onMessage && onMessage(event.data)
    }
  }

  // 监听错误事件
  eventSource.onerror = (error) => {
    // 任何错误都关闭连接，防止自动重连
    eventSource.close()
    // 检查是否是正常关闭（readyState为2表示已关闭）
    if (eventSource.readyState === EventSource.CLOSED) {
      // 连接正常关闭，表示流式响应完成
      onComplete && onComplete()
    } else {
      // 发生错误
      onError && onError(error)
    }
  }

  // 返回取消函数
  return () => {
    eventSource.close()
  }
}

/**
 * 发送聊天消息（Promise方式）
 * 收集所有流式消息后一次性返回
 * @param {number} memoryId - 会话ID
 * @param {string} message - 用户消息
 * @returns {Promise<string>} 完整的AI回复
 */
export function chat(memoryId, message) {
  return new Promise((resolve, reject) => {
    let fullResponse = ''

    chatStream(memoryId, message, {
      onMessage: (data) => {
        fullResponse += data
      },
      onError: (error) => {
        reject(error)
      },
      onComplete: () => {
        resolve(fullResponse)
      }
    })
  })
}