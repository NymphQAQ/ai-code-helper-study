package art.qijian.aicodehelper.ai.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;


public class SafeInputGuardrail implements InputGuardrail {

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String s = userMessage.singleText();
        if (s.contains("删除") || s.contains("删除文件") || s.contains("删除代码") || s.contains("删除项目")) {
            return fatal("请求包含敏感词，已被拒绝");
        }
        return success();
    }
}
