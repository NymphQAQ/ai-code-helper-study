package art.qijian.aicodehelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * AI Code Helper 应用程序入口类。
 *
 * <p>本项目是一个基于 Spring Boot + LangChain4j 的 AI 代码助手后端服务，
 * 集成了阿里云 DashScope（通义千问大模型），支持以下核心能力：
 * <ul>
 *   <li>流式 SSE 对话接口（多轮对话，含会话记忆）</li>
 *   <li>RAG 检索增强生成（基于本地文档向量化）</li>
 *   <li>网络实时搜索工具（WebSearchTool）</li>
 *   <li>输入安全防护（SafeInputGuardrail）</li>
 * </ul>
 *
 * <p>默认端口：8080，context-path：/api
 * <p>前端地址：http://localhost:5173（Vite 开发服务器）
 */
@SpringBootApplication
public class AiCodeHelperApplication {

    /**
     * Spring Boot 应用启动入口。
     *
     * @param args 命令行参数（可传入 --spring.profiles.active=xxx 切换环境）
     */
    public static void main(String[] args) {
        SpringApplication.run(AiCodeHelperApplication.class, args);
    }

    /**
     * 全局跨域配置 Bean。
     *
     * <p>为所有路径（/**）配置 CORS，允许任意来源、方法、请求头，
     * 并允许携带凭证（Cookie/Session），确保前端本地开发时不受同源策略限制。
     *
     * <p>生产环境建议将 allowedOriginPatterns 收窄为前端真实域名，
     * 避免安全风险。
     *
     * @return 自定义 WebMvcConfigurer 实例
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

}
