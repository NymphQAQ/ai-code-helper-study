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

@Configuration
public class AiCodeHelperServiceFactory {

    private final ChatModel qwenChatModel;
    private final PersistentChatMemoryStore persistentChatMemoryStore;
    private final ContentRetriever contentRetriever;
    private final StreamingChatModel streamingChatModel;

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

    @Bean
    public AiCodeHelperService getAiCodeHelperService() {
        return AiServices.builder(AiCodeHelperService.class)
                .streamingChatModel(streamingChatModel)//流式输出
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .maxMessages(10)
                        .chatMemoryStore(persistentChatMemoryStore)
                        .id(memoryId)
                        .build())
                .chatModel(qwenChatModel)
                .contentRetriever(contentRetriever)
                .tools(new WebSearchTool())
                .build();
    }
}
