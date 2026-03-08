# AI Code Helper

AI Code Helper 是一个基于 Spring Boot 的智能代码助手项目，集成了大模型（如 Qwen 3.5 Plus），支持全栈开发场景下的智能问答、代码生成与知识检索。

## 主要特性
- 支持 Qwen 3.5 Plus 等大模型的 API 接入
- 系统提示词预设为全栈开发专家，专业解答开发相关问题
- 消息持久化存储，支持多会话历史追踪
- 配置灵活，支持多环境（本地/生产）API Key 管理
- 可扩展的 RAG（检索增强生成）配置结构

## 项目结构
```
ai-code-helper/
├── src/
│   ├── main/
│   │   ├── java/art/qijian/aicodehelper/
│   │   │   ├── AiCodeHelperApplication.java         # Spring Boot 启动类
│   │   │   ├── ai/
│   │   │   │   ├── AiCodeHelper.java               # 核心AI服务逻辑
│   │   │   │   ├── PersistentChatMemoryStore.java  # 消息持久化存储实现
│   │   │   │   └── rag/
│   │   │   │       └── RagConfig.java              # RAG相关配置类
│   │   ├── resources/
│   │   │   ├── application.yml                     # 默认配置
│   │   │   └── application-local.yml               # 本地开发配置（含API Key）
│   └── test/
│       └── java/art/qijian/aicodehelper/
│           └── AiCodeHelperApplicationTests.java   # 测试类
├── pom.xml                                         # Maven 构建文件
```

## 快速开始

### 1. 克隆项目
```bash
git clone <repo-url>
cd ai-code-helper
```

### 2. 配置 API Key
在 `src/main/resources/application-local.yml` 中填写你的大模型 API Key：
```yaml
langchain4j:
  community:
    dashscope:
      chat-model:
        api-key: <你的API_KEY>
```

### 3. 启动项目
```bash
./mvnw spring-boot:run
```

### 4. 主要接口说明
- `AiCodeHelper.chatM()`：测试与大模型的连通性，使用系统提示词“全栈开发专家”
- 消息历史通过 `PersistentChatMemoryStore` 自动存储和获取

## 配置说明
- `application.yml`：通用配置（如模型名、环境）
- `application-local.yml`：本地开发专用配置（如 API Key），不会提交到生产环境

## 扩展与定制
- 可在 `AiCodeHelper` 中自定义系统提示词和业务逻辑
- `RagConfig` 可扩展用于知识检索增强

## 贡献
欢迎提交 issue 和 PR 参与项目改进！

## License
MIT

