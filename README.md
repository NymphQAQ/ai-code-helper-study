# AI Code Helper

AI Code Helper 是一个基于 Spring Boot 的智能代码助手项目，集成了大模型（如 Qwen 3.5 Plus），支持全栈开发场景下的智能问答、代码生成、知识检索、网络搜索和输入安全防护。

## 主要特性
- 支持 Qwen 3.5 Plus 等大模型的 API 接入
- 系统提示词预设为全栈开发专家，专业解答开发相关问题
- 支持 LangChain4j 工具调用，可在回答中自动触发网络搜索
- 消息持久化存储，支持多会话历史追踪
- 配置灵活，支持多环境（本地/生产）API Key 管理
- 可扩展的 RAG（检索增强生成）配置结构
- 输入安全防护（Guardrail），可拦截敏感或危险请求
- 所有核心代码均含详细中文注释，便于理解和二次开发

## 项目结构
```
ai-code-helper/
├── src/
│   ├── main/
│   │   ├── java/art/qijian/aicodehelper/
│   │   │   ├── AiCodeHelperApplication.java            # Spring Boot 启动类
│   │   │   ├── ai/
│   │   │   │   ├── AiCodeHelper.java                  # 核心AI服务逻辑，含详细中文注释
│   │   │   │   ├── AiCodeHelperService.java           # AI服务接口定义，含详细中文注释
│   │   │   │   ├── AiCodeHelperServiceFactory.java    # AiService 工厂，负责注册 RAG、工具和Guardrail
│   │   │   │   ├── PersistentChatMemoryStore.java     # 消息持久化存储实现，含详细中文注释
│   │   │   │   ├── guardrail/
│   │   │   │   │   └── SafeInputGuardrail.java        # 输入安全防护实现
│   │   │   │   ├── rag/
│   │   │   │   │   └── RagConfig.java                 # RAG相关配置类，含详细中文注释
│   │   │   │   └── tools/
│   │   │   │       ├── InterviewQuestionTool.java     # 预留工具类
│   │   │   │       └── WebSearchTool.java             # 网络搜索工具，支持 LangChain4j Tool 调用
│   │   ├── resources/
│   │   │   ├── application.yml                        # 默认配置
│   │   │   ├── application-local.yml                  # 本地开发配置（含API Key）
│   │   │   └── system-prompt.txt                      # 系统提示词，已加入工具调用策略
│   └── test/
│       └── java/art/qijian/aicodehelper/
│           ├── AiCodeHelperApplicationTests.java      # 应用测试
│           └── ai/tools/WebSearchToolTest.java        # 网络搜索工具单元测试
├── pom.xml                                            # Maven 构建文件
```

## 输入安全防护（Guardrail）

### 功能说明
- 项目内置 `SafeInputGuardrail`，可对用户输入进行安全检查，防止敏感、危险操作（如“删除文件”“删除代码”等）被提交到大模型。
- 拦截命中敏感词的请求并返回错误提示，保障AI助手的安全边界。

### 配置与扩展
- 你可以在 `SafeInputGuardrail.java` 中自定义更多敏感词或危险操作规则。
- Guardrail 可在 `AiCodeHelperServiceFactory` 注册到 LangChain4j 服务链路中。
- 如需更复杂的安全策略，可扩展为正则、黑名单、上下文感知等多种方式。

## 其他主要能力说明
- `AiCodeHelper.chatM()`：测试与大模型的连通性
- `AiCodeHelperService`：统一 AI 服务接口
- `PersistentChatMemoryStore`：自动保存多轮对话历史
- `RagConfig`：负责 RAG 检索增强能力接入
- `WebSearchTool`：负责执行网络搜索，并以工具调用的方式提供给大模型

## 配置说明
- `application.yml`：通用配置（如模型名、默认网络搜索参数）
- `application-local.yml`：本地开发专用配置（如 API Key、RAG 参数、网络搜索参数）
- 若线上环境不希望启用网络搜索，可将 `web-search.enabled` 设置为 `false`

## 测试与验证
- 源码编译通过（JDK 21）
- `WebSearchToolTest` 单元测试通过
- Guardrail 可通过自定义测试用例验证拦截效果

## 代码注释说明
- `AiCodeHelper.java`
- `AiCodeHelperService.java`
- `AiCodeHelperServiceFactory.java`
- `PersistentChatMemoryStore.java`
- `RagConfig.java`
- `WebSearchTool.java`
- `SafeInputGuardrail.java`

以上核心文件均已补充详细中文注释，方便理解和二次开发。

## 扩展与定制
- 可替换 `web-search.base-url` 对接自建搜索引擎或 SearXNG
- 可在 `WebSearchTool` 中增加结果过滤、摘要截断、站点白名单等能力
- 可在 `AiCodeHelperService` 中继续扩展更多工具和业务方法
- Guardrail 可扩展为更复杂的安全策略

## 贡献
欢迎提交 issue 和 PR 参与项目改进！

## License
MIT
