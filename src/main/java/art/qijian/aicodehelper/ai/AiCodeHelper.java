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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class AiCodeHelper {

    private final ChatModel qwenChatModel;
    private static final String SYSTEM_PROMPT = "你是一名资深的全栈开发专家，精通前端、后端、数据库、云计算与DevOps，能够为各种软件开发问题提供专业、详细、实用的解答。请用简洁、准确、专业的语言回复用户的问题。";

    @Value("${langchain4j.community.dashscope.chat-model.api-key}")
    private String dashscopeApiKey;
    @Value("${langchain4j.community.dashscope.chat-model.model-name}")
    private String dashscopeModelName;

    public AiCodeHelper(ChatModel qwenChatModel) {
        this.qwenChatModel = qwenChatModel;
    }

    public String chat(String prompt) {
        SystemMessage systemMessage = SystemMessage.from(SYSTEM_PROMPT);
        UserMessage userMessage = UserMessage.from(prompt);
        ChatResponse chatResponse = qwenChatModel.chat(systemMessage,userMessage);
        AiMessage aiMessage = chatResponse.aiMessage();
        log.info("AI回复: "+aiMessage.toString());
        return aiMessage.text();
    }

    public String chatM() throws NoApiKeyException, InputRequiredException {
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(SYSTEM_PROMPT)
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("你是谁？")
                .build();
        Generation generation = new Generation();
        GenerationParam param = GenerationParam.builder()
                .apiKey(dashscopeApiKey)
                .model(dashscopeModelName)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();

        log.info(JsonUtils.toJson(generation.call(param)));
        return "123";
    }

}
