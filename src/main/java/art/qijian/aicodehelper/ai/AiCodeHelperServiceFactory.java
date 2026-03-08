package art.qijian.aicodehelper.ai;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiCodeHelperServiceFactory {

    private final ChatModel qwenChatModel;
    private final PersistentChatMemoryStore persistentChatMemoryStore;
    public AiCodeHelperServiceFactory(ChatModel qwenChatModel, PersistentChatMemoryStore persistentChatMemoryStore) {
        this.qwenChatModel = qwenChatModel;
        this.persistentChatMemoryStore = persistentChatMemoryStore;
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
                .build();
    }
}
