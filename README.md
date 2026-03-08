# AI Code Helper

> 基于 **Spring Boot 4 + LangChain4j + Vue 3** 的全栈 AI 代码助手，集成阿里云 DashScope（通义千问）大模型，支持流式对话、RAG 检索增强、Web 实时搜索与输入安全防护。

---

## 目录

- [主要特性](#主要特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [核心能力说明](#核心能力说明)
- [输入安全防护](#输入安全防护)
- [测试](#测试)
- [扩展与定制](#扩展与定制)
- [文档](#文档)
- [License](#license)

---

## 主要特性

| 特性 | 说明 |
|------|------|
| 🤖 大模型接入 | 集成阿里云 DashScope Qwen-Plus，支持同步/流式对话 |
| 💬 多轮对话记忆 | 基于 `memoryId` 的多会话隔离，内存窗口保留最近 10 条 |
| 📡 SSE 流式输出 | 后端 `Flux<ServerSentEvent>` 驱动，前端 `EventSource` 实时接收 |
| 🔍 RAG 检索增强 | 本地文档向量化存储，每次对话自动注入相关知识 |
| 🌐 网络实时搜索 | `WebSearchTool` 爬取百度搜索结果，大模型按需调用 |
| 🛡️ 输入安全防护 | `SafeInputGuardrail` 拦截敏感词，保障 AI 安全边界 |
| 🔧 全中文注释 | 所有核心代码均含详细中文注释，易于理解与二次开发 |

---

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 4.0.3 | 应用框架 |
| LangChain4j | 1.11.0 | AI 服务框架 |
| LangChain4j DashScope | 1.11.0-beta19 | 通义千问模型适配 |
| LangChain4j Reactor | 1.1.0-beta7 | 响应式流式支持 |
| LangChain4j MCP | 1.1.0-beta7 | MCP 协议支持 |
| Jsoup | 1.20.1 | HTML 解析/网络爬取 |
| Lombok | latest | 代码简化 |
| Java | 21 | 运行环境 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue 3 | ^3.4.0 | 前端框架 |
| Vite | ^5.0.0 | 构建工具 |
| EventSource (SSE) | 浏览器原生 | 流式消息接收 |

---

## 项目结构

```
ai-code-helper/
├── front/                                             # 前端 Vue 3 项目
│   ├── src/
│   │   ├── App.vue                                   # 主聊天界面组件
│   │   ├── main.js                                   # 应用入口
│   │   └── api/
│   │       └── chat.js                               # SSE 聊天 API 封装
│   ├── index.html                                    # 入口 HTML
│   ├── vite.config.js                                # Vite 配置（含反向代理）
│   └── package.json                                  # 前端依赖
├── src/
│   ├── main/
│   │   ├── java/art/qijian/aicodehelper/
│   │   │   ├── AiCodeHelperApplication.java          # Spring Boot 启动类 + CORS 配置
│   │   │   └── ai/
│   │   │       ├── AiCodeHelper.java                 # 基础 AI 服务（同步对话 + 连通性测试）
│   │   │       ├── AiCodeHelperService.java          # AI 服务接口（流式/同步/RAG）
│   │   │       ├── AiCodeHelperServiceFactory.java   # AI 服务工厂（组装全部能力）
│   │   │       ├── PersistentChatMemoryStore.java    # 内存会话历史存储
│   │   │       ├── controller/
│   │   │       │   └── AiController.java             # HTTP 控制器（SSE 接口）
│   │   │       ├── guardrail/
│   │   │       │   └── SafeInputGuardrail.java       # 输入安全防护
│   │   │       ├── rag/
│   │   │       │   └── RagConfig.java                # RAG 检索增强配置
│   │   │       └── tools/
│   │   │           └── WebSearchTool.java            # 网络搜索工具
│   │   └── resources/
│   │       ├── application.yml                       # 主配置（模型名、端口等）
│   │       ├── application-local.yml                 # 本地开发配置（API Key）
│   │       ├── system-prompt.txt                     # 系统提示词
│   │       └── docs/                                 # RAG 知识库文档目录
│   └── test/                                         # 单元测试
├── pom.xml                                           # Maven 构建文件
└── README.md                                         # 本文件
```

---

## 快速开始

### 前置要求

- JDK 21+
- Maven 3.9+（或使用自带 `mvnw`）
- Node.js 18+ & pnpm（前端）
- 阿里云 DashScope API Key

### 1. 配置 API Key

编辑 `src/main/resources/application-local.yml`，填入你的 API Key：

```yaml
langchain4j:
  community:
    dashscope:
      chat-model:
        api-key: sk-xxxxxxxxxxxxxxxxxxxx
      embedding-model:
        api-key: sk-xxxxxxxxxxxxxxxxxxxx
      streaming-chat-model:
        api-key: sk-xxxxxxxxxxxxxxxxxxxx
```

> ⚠️ 请勿将 API Key 提交到代码仓库，建议通过环境变量注入。

### 2. 启动后端

```bash
./mvnw spring-boot:run
```

后端服务启动后监听：`http://localhost:8080/api`

### 3. 启动前端

```bash
cd front
pnpm install
pnpm dev
```

前端服务启动后访问：`http://localhost:5173`

---

## 配置说明

### application.yml（主配置）

```yaml
spring:
  application:
    name: ai-code-helper
  profiles:
    active: local               # 激活 local 环境配置

langchain4j:
  community:
    dashscope:
      chat-model:
        model-name: qwen-plus   # 使用的对话模型
      embedding-model:
        model-name: text-embedding-v3   # 向量化模型（用于 RAG）
      streaming-chat-model:
        model-name: qwen-plus   # 流式对话模型

server:
  port: 8080
  servlet:
    context-path: /api          # 所有接口以 /api 为前缀
```

### application-local.yml（本地开发配置）

```yaml
langchain4j:
  community:
    dashscope:
      chat-model:
        api-key: <YOUR_API_KEY>

rag:
  docs-path: src/main/resources/docs   # RAG 知识库文档路径
  split-size: 1000                     # 文档切分最大字符数
  split-overlap: 200                   # 相邻段落重叠字符数
  max-results: 5                       # RAG 最多返回结果数
  min-score: 0.75                      # RAG 最小相似度阈值
```

---

## 核心能力说明

### 多轮对话记忆

- 每个 `memoryId` 维护独立的会话历史
- 使用滑动窗口策略，最多保留最近 **10 条**消息
- `PersistentChatMemoryStore` 使用 `ConcurrentHashMap` 保证并发安全
- 前端每次「新建对话」时递增 `memoryId`，实现会话隔离

### RAG 检索增强生成

- 启动时自动加载 `src/main/resources/docs/` 目录下的所有文档
- 使用 `text-embedding-v3` 对文档进行向量化并存入嵌入式向量库
- 每次对话前检索最相关的 5 个文档片段（相似度 ≥ 0.75），自动注入 Prompt
- 文档片段附带文件名前缀，便于模型追溯来源

### 网络实时搜索

- `WebSearchTool` 通过爬取百度搜索页面提取结构化结果
- 工具调用由大模型自主决策，无需手动触发
- 支持通过配置文件调整 User-Agent、超时时间、最大结果数

### SSE 流式输出

- 后端使用 Project Reactor 的 `Flux<String>` 逐 token 推送
- 封装为 `ServerSentEvent` 格式，兼容标准 EventSource API
- 前端使用 `EventSource` 原生实现，无需额外依赖

---

## 输入安全防护

项目内置 `SafeInputGuardrail`，在消息发送给大模型前进行安全校验：

- **当前规则**：拦截包含「删除」「删除文件」「删除代码」「删除项目」等危险词的请求
- **拦截后**：返回 `fatal` 错误结果，阻止请求继续传递
- **扩展方式**：在 `SafeInputGuardrail.validate()` 中添加正则匹配规则或接入外部内容安全 API

---

## 测试

```bash
# 运行所有测试
./mvnw test
```

---

## 扩展与定制

- **替换搜索引擎**：修改 `WebSearchTool` 中的 URL，对接 SearXNG 或自建搜索服务
- **扩展知识库**：将 Markdown/TXT 文档放入 `src/main/resources/docs/` 即可自动被 RAG 索引
- **新增工具**：实现新的 `@Tool` 方法类，在 `AiCodeHelperServiceFactory` 的 `.tools()` 中注册
- **更换模型**：修改 `application.yml` 中的 `model-name` 即可切换
- **持久化记忆**：将 `PersistentChatMemoryStore` 改为 Redis/数据库后端实现

---

## 文档

| 文档 | 路径 |
|------|------|
| 开发文档 | src/main/resources/docs/开发文档.md |
| 接口文档 | src/main/resources/docs/接口文档.md |
| 部署文档 | src/main/resources/docs/部署文档.md |
| 项目总结 | src/main/resources/docs/项目总结.md |

---

## 贡献

欢迎提交 Issue 和 PR 参与项目改进！请遵循 Conventional Commits 提交规范。

---

## License

MIT
