package art.qijian.aicodehelper.ai;

import art.qijian.aicodehelper.ai.rag.RagConfig;
import art.qijian.aicodehelper.ai.tools.WebSearchTool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AiCodeHelperServiceFactory 是核心 AI 服务的装配工厂类（Spring Configuration）。
 *
 * <p>负责将以下组件组装成一个完整的 LangChain4j AiService 实例并注册为 Spring Bean：
 * <ul>
 *   <li>{@link ChatModel}：普通同步对话模型（qwen-plus）</li>
 *   <li>{@link StreamingChatModel}：流式输出对话模型（qwen-plus 流式版）</li>
 *   <li>{@link PersistentChatMemoryStore}：多会话内存持久化存储</li>
 *   <li>{@link ContentRetriever}：RAG 内容检索器（见 {@link RagConfig}）</li>
 *   <li>{@link WebSearchTool}：网络实时搜索工具</li>
 * </ul>
 *
 * <p>通过 {@link AiServices#builder(Class)} 构建，框架会自动代理接口方法，
 * 将系统提示、历史记忆、RAG 检索、工具调用整合为完整调用链路。
 */
@Configuration
public class AiCodeHelperServiceFactory {

    /** 通义千问同步对话模型，用于普通请求响应 */
    private final ChatModel qwenChatModel;
    /** 多会话消息持久化存储，保证对话上下文不丢失 */
    private final PersistentChatMemoryStore persistentChatMemoryStore;
    /** RAG 内容检索器，负责从本地向量库检索相关文档片段 */
    private final ContentRetriever contentRetriever;
    /** 通义千问流式对话模型，用于 SSE 流式输出 */
    private final StreamingChatModel streamingChatModel;

    /**
     * 构造方法，通过 Spring 依赖注入所有组件。
     *
     * @param qwenChatModel             同步对话模型
     * @param persistentChatMemoryStore 会话记忆存储
     * @param contentRetriever          RAG 检索器
     * @param streamingChatModel        流式对话模型
     */
    @Autowired
    public AiCodeHelperServiceFactory(
            ChatModel qwenChatModel,
            PersistentChatMemoryStore persistentChatMemoryStore,
            ContentRetriever contentRetriever, StreamingChatModel streamingChatModel
    ) {
        this.qwenChatModel = qwenChatModel;
        this.persistentChatMemoryStore = persistentChatMemoryStore;
        this.contentRetriever = contentRetriever;
        this.streamingChatModel = streamingChatModel;
    }

    /**
     * 构建并注册 {@link AiCodeHelperService} Bean。
     *
     * <p>装配策略说明：
     * <ul>
     *   <li>流式模型：用于 {@code chatStream} 方法，返回 {@code Flux<String>} 实时推送</li>
     *   <li>会话记忆：每个 memoryId 独立维护最多 10 条消息窗口</li>
     *   <li>RAG 检索：每次请求前自动检索向量库，将相关文档注入上下文</li>
     *   <li>工具调用：注册 {@link WebSearchTool}，模型可自主决定是否调用搜索</li>
     * </ul>
     *
     * @return 完整功能的 AiCodeHelperService 代理实例
     */
    @Bean
    public AiCodeHelperService getAiCodeHelperService() {
        return AiServices.builder(AiCodeHelperService.class)
                // 流式输出模型，支持 Flux<String> 推流
                .streamingChatModel(streamingChatModel)
                // 会话记忆提供者：每个 memoryId 对应一个独立的滑动窗口记忆（最多10条消息）
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .maxMessages(10)
                        .chatMemoryStore(persistentChatMemoryStore)
                        .id(memoryId)
                        .build())
                // 同步对话模型，用于非流式接口
                .chatModel(qwenChatModel)
                // RAG 检索器，自动从本地向量库补充上下文
                .contentRetriever(contentRetriever)
                // 网络搜索工具，模型可按需调用
                .tools(new WebSearchTool())
                .build();
    }
}
