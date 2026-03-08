package art.qijian.aicodehelper.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class PersistentChatMemoryStore implements ChatMemoryStore {
    // 使用线程安全的Map存储每个会话的消息列表
    private final Map<Object, List<ChatMessage>> memory = new ConcurrentHashMap<>();

    @Override
    public List<ChatMessage> getMessages(Object o) {
        return memory.getOrDefault(o, List.of());
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        // 存储或覆盖该会话的消息列表
        memory.put(o, new ArrayList<>(list));
    }

    @Override
    public void deleteMessages(Object o) {
        memory.remove(o);
    }
}
