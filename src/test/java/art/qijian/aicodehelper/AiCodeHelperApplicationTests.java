package art.qijian.aicodehelper;

import art.qijian.aicodehelper.ai.AiCodeHelper;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeHelperApplicationTests {

    @Resource
    private AiCodeHelper aiCodeHelper;
    @Test
    void chat() {
        aiCodeHelper.chat("你好");
    }

    @Test
    void chatM() throws NoApiKeyException, InputRequiredException {
        aiCodeHelper.chatM();
    }
}
