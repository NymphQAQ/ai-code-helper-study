package art.qijian.aicodehelper.ai;

import art.qijian.aicodehelper.ai.guardrail.SafeInputGuardrail;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.guardrail.InputGuardrails;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import java.util.List;

// AiCodeHelperService 接口定义了与大模型交互的多种方法，支持不同的返回类型和系统提示词配置
//@AiService // 若启用，表示该接口为AI服务，由Spring自动实现
@InputGuardrails({SafeInputGuardrail.class})
public interface AiCodeHelperService {

    /**
     * 基础对话接口，传入用户问题，返回AI回复。
     * @param prompt 用户输入的问题
     * @return AI回复内容
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String prompt);

    /**
     * 对话并返回结构化报告，包含名称和消息列表。
     * @param prompt 用户输入的问题
     * @return Report结构体，含AI分析结果
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatReport(String prompt);

    /**
     * 基于RAG（检索增强生成）的对话接口，返回带置信度的AI回复。
     * @param prompt 用户输入的问题
     * @return Result<String>，含AI回复及相关元信息
     */
    @SystemMessage(fromResource = "system-prompt.txt")
    Result<String> chatWithRag(String prompt);

    /**
     * Report 结构体，包含名称和消息列表。
     */
    record Report(String name, List<String> messages) {
    }

    @SystemMessage(fromResource = "system-prompt.txt")
    Flux<String> chatStream(@MemoryId int memoryId,@UserMessage String message);
}
