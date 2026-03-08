package art.qijian.aicodehelper.ai.controller;

import art.qijian.aicodehelper.ai.AiCodeHelperService;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * AiController 是 AI 对话功能的 HTTP 入口控制器。
 *
 * <p>提供基于 SSE（Server-Sent Events）的流式对话接口，
 * 前端通过 EventSource 连接该接口，实现打字机效果的实时流式输出。
 *
 * <p>接口基础路径：{@code /ai}（加上 context-path 后为 {@code /api/ai}）
 *
 * <p>所有接口的请求已通过 {@link AiCodeHelperApplication} 中配置的全局 CORS 策略
 * 允许跨域访问，无需额外处理。
 */
@RestController
@RequestMapping("/ai")
@AllArgsConstructor
public class AiController {

    /** 注入 AI 服务，由 AiCodeHelperServiceFactory 装配 */
    private final AiCodeHelperService aiCodeHelperService;

    /**
     * 流式对话接口（SSE）。
     *
     * <p>前端通过 {@code EventSource} 建立长连接，后端以 Server-Sent Events 格式
     * 逐 token 推送大模型的回复内容，实现实时流式输出。
     *
     * <p>完整请求示例：
     * {@code GET /api/ai/chat?memoryId=1&message=你好}
     *
     * <p>流程说明：
     * <ol>
     *   <li>接收用户 {@code memoryId}（会话ID）和 {@code message}（消息内容）</li>
     *   <li>调用 {@link AiCodeHelperService#chatStream} 获取 {@code Flux<String>}</li>
     *   <li>将每个 token 包装为 {@link ServerSentEvent} 推送给客户端</li>
     *   <li>流结束后，EventSource 触发 {@code onerror}（readyState=2），前端判断为完成</li>
     * </ol>
     *
     * @param memoryId 会话 ID，相同 ID 共享历史记忆，不同 ID 独立隔离
     * @param message  用户输入的消息内容
     * @return SSE 事件流，每个事件的 data 字段为一个 token 片段
     */
    @GetMapping("/chat")
    public Flux<ServerSentEvent<String>> chatStream(int memoryId, String message) {
        return aiCodeHelperService.chatStream(memoryId, message)
                .map(content -> ServerSentEvent.<String>builder()
                        .data(content)
                        .build());
    }
}