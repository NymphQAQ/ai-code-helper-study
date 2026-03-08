package art.qijian.aicodehelper.ai;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * AiCodeHelper 是核心AI服务类，负责与大模型进行对话、消息生成和日志记录。
 */
@Service
@Slf4j
public class AiCodeHelper {

    // 注入Qwen大模型的ChatModel实例
    private final ChatModel qwenChatModel;
    // 系统提示词，设定AI身份为全栈开发专家
    private static final String SYSTEM_PROMPT = "你是一名资深的全栈开发专家，精通前端、后端、数据库、云计算与DevOps，能够为各种软件开发问题提供专业、详细、实用的解答。请用简洁、准确、专业的语言回复用户的问题。";

    // 从配置文件注入API Key和模型名
    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String dashscopeApiKey;
    @Value("${langchain4j.community.dashscope.chat-model.model-name}")
    private String dashscopeModelName;

    // 构造方法注入ChatModel
    public AiCodeHelper(ChatModel qwenChatModel) {
        this.qwenChatModel = qwenChatModel;
    }

    /**
     * 基础对话方法，传入用户问题，返回AI回复。
     * @param prompt 用户输入
     * @return AI回复内容
     */
    public String chat(String prompt) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_PROMPT);
        UserMessage userMessage = UserMessage.from(prompt);
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage,userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("AI回复: "+aiMessage.toString());
        return aiMessage.text();
    }

    /**
     * 测试与Qwen大模型的连通性，返回固定问题的AI回复。
     * @return AI回复内容
     * @throws NoApiKeyException 未配置API Key时抛出
     * @throws InputRequiredException 输入参数异常时抛出
     */
    public String chatM() throws NoApiKeyException, InputRequiredException {
        // 构造系统消息
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(SYSTEM_PROMPT)
                .build();
        // 构造用户消息
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("你是谁？")
                .build();
        // 创建Generation对象并设置参数
        Generation generation = new Generation();
        GenerationParam param = GenerationParam.builder()
                .apiKey(dashscopeApiKey)
                .model(dashscopeModelName)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();
        // 调用大模型API并记录日志
        log.info(JsonUtils.toJson(generation.call(param)));
        return "123";
    }
}
