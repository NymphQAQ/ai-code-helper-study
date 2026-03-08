package art.qijian.aicodehelper.ai.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

/**
 * SafeInputGuardrail 是输入安全防护实现类，用于在消息发送给大模型之前进行前置校验。
 *
 * <p>实现了 LangChain4j 的 {@link InputGuardrail} 接口，
 * 在 {@link art.qijian.aicodehelper.ai.AiCodeHelperService} 上通过
 * {@code @InputGuardrails({SafeInputGuardrail.class})} 注解挂载。
 *
 * <p>当前防护规则：拦截包含「删除」等危险操作关键词的请求，<br>
 * 命中后返回 {@code fatal} 结果，终止请求链路并将错误信息返回给调用方。
 *
 * <p>可扩展方向：
 * <ul>
 *   <li>增加正则表达式匹配（如 SQL 注入、命令注入特征）</li>
 *   <li>引入黑名单词库文件，动态加载</li>
 *   <li>接入内容安全 API（如阿里云内容安全）</li>
 *   <li>添加上下文感知校验（结合历史消息判断意图）</li>
 * </ul>
 */
public class SafeInputGuardrail implements InputGuardrail {

    /**
     * 对用户输入进行安全校验。
     *
     * <p>校验逻辑：
     * <ol>
     *   <li>提取用户消息的纯文本内容</li>
     *   <li>检查是否包含敏感操作关键词（当前为「删除」相关词语）</li>
     *   <li>命中则返回 {@code fatal} 拒绝结果，阻止请求继续传递给模型</li>
     *   <li>未命中则返回 {@code success}，允许请求正常通过</li>
     * </ol>
     *
     * @param userMessage 用户发送的消息对象
     * @return {@link InputGuardrailResult} 校验结果：
     *         {@code success()} 表示通过，{@code fatal(reason)} 表示拦截
     */
    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        String s = userMessage.singleText();
        // 拦截包含危险操作关键词的请求，防止用户通过 AI 触发破坏性操作
        if (s.contains("删除") || s.contains("删除文件") || s.contains("删除代码") || s.contains("删除项目")) {
            return fatal("请求包含敏感词，已被拒绝");
        }
        // 校验通过，允许消息继续流向大模型
        return success();
    }
}
