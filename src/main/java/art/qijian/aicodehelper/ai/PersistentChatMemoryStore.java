package art.qijian.aicodehelper.ai;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * PersistentChatMemoryStore 实现了 ChatMemoryStore 接口，
 * 用于持久化存储每个会话的消息历史，支持多会话并发安全。
 */
@Service
public class PersistentChatMemoryStore implements ChatMemoryStore {
    // 使用线程安全的Map存储每个会话的消息列表，key为会话id，value为消息列表
    private final Map<Object, List<ChatMessage>> memory = new ConcurrentHashMap<>();

    /**
     * 获取指定会话的消息历史。
     * @param o 会话id
     * @return 消息列表，若无则返回空列表
     */
    @Override
    public List<ChatMessage> getMessages(Object o) {
        return memory.getOrDefault(o, List.of());
    }

    /**
     * 更新指定会话的消息历史。
     * @param o 会话id
     * @param list 新的消息列表
     */
    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {
        // 存储或覆盖该会话的消息列表
        memory.put(o, new ArrayList<>(list));
    }

    /**
     * 删除指定会话的消息历史。
     * @param o 会话id
     */
    @Override
    public void deleteMessages(Object o) {
        memory.remove(o);
    }
}
