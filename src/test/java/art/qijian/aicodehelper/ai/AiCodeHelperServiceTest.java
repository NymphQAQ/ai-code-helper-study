package art.qijian.aicodehelper.ai;

import dev.langchain4j.service.Result;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AiCodeHelperServiceTest {

    @Resource
    private AiCodeHelperService aiCodeHelperService;

    @Test
    void chat() {
        String chat = aiCodeHelperService.chat("你好,我是汤汤");
        System.out.println(chat);
        String chat1 = aiCodeHelperService.chat("我是谁");
        System.out.println(chat1);
    }

    @Test
    void chatReport() {
        AiCodeHelperService.Report report = aiCodeHelperService.chatReport("你好,我是汤汤,请帮我写一份健身计划");
        System.out.println(report.toString());
    }

    @Test
    void chatReportRetriever() {
        AiCodeHelperService.Report report = aiCodeHelperService.chatReport("这个项目都用了什么技术栈");
        System.out.println(report.toString());
    }

    @Test
    void chatWithRag() {
        Result<String> withRag = aiCodeHelperService.chatWithRag("这个项目都用了什么技术栈");
        System.out.println(withRag.toString());
    }
}