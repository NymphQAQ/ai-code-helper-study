package art.qijian.aicodehelper.ai;

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
}