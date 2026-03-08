package art.qijian.aicodehelper.ai;

import art.qijian.aicodehelper.ai.rag.RagConfig;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    private final ChatModel qwenChatModel;
    private final PersistentChatMemoryStore persistentChatMemoryStore;
    private final ContentRetriever contentRetriever;
    public AiCodeHelperServiceFactory(ChatModel qwenChatModel, PersistentChatMemoryStore persistentChatMemoryStore, RagConfig ragConfig, ContentRetriever contentRetriever) {
        this.qwenChatModel = qwenChatModel;
        this.persistentChatMemoryStore = persistentChatMemoryStore;
        this.contentRetriever = contentRetriever;
    }

    @Bean
    public AiCodeHelperService getAiCodeHelperService() {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(persistentChatMemoryStore)
                .build();
        return AiServices.builder(AiCodeHelperService.class)
                .chatMemory(chatMemory)
                .chatModel(qwenChatModel)
                .contentRetriever(contentRetriever)
                .build();
    }
}
