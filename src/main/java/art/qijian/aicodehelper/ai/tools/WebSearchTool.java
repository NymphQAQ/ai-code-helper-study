package art.qijian.aicodehelper.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * WebSearchTool 是一个可被 LangChain4j 调用的网络搜索工具。
 * 支持通过关键词抓取百度搜索结果，返回结构化文本。
 */
@Component
public class WebSearchTool {

    @Value("${web-search.user-agent:Mozilla/5.0}")
    private String userAgent;
    @Value("${web-search.timeout-millis:5000}")
    private int timeoutMillis;
    @Value("${web-search.max-results:5}")
    private int maxResults;

    /**
     * 网络搜索工具，适用于检索技术面试题、技术关键词、外部公开资料。
     * @param keyword 搜索关键词
     * @return 结构化搜索结果
     */
    @Tool(name="webSearchTool", value="根据关键词实时检索互联网公开信息，适用于查找面试题、技术资料、最新动态等。输入应为简洁明确的关键词。")
    public String webSearchTool(@P("搜索关键词") String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return "搜索关键词不能为空。";
        }
        String encode = URLEncoder.encode(keyword.trim(), StandardCharsets.UTF_8);
        String url = "https://www.baidu.com/s?wd=" + encode;
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent(userAgent)
                    .timeout(timeoutMillis)
                    .get();
            Elements results = doc.select("div.result, div.result-op");
            List<String> output = new ArrayList<>();
            int count = 0;
            for (Element result : results) {
                if (count >= maxResults) break;
                Element title = result.selectFirst("h3");
                Element summary = result.selectFirst(".c-abstract, .c-span-last, .c-line-clamp3, .c-line-clamp2");
                if (title != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(count + 1).append(". ").append(title.text());
                    if (summary != null) {
                        sb.append("\n   摘要: ").append(summary.text());
                    }
                    output.add(sb.toString());
                    count++;
                }
            }
            if (output.isEmpty()) {
                return "未检索到与“" + keyword + "”相关的有效网页结果。";
            }
            return String.join("\n", output);
        } catch (IOException e) {
            return "网络搜索失败：" + e.getMessage();
        }
    }
}
