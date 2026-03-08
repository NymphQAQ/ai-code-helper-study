package art.qijian.aicodehelper.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class WebSearchTool {

    @Tool(name="webQuestionTool", value="Retrieves relevant interview questions from mianshaya.com based on a keyvord.\n" +
            "Use this tool when the user asks for interview questions about specific technologies，\n" +
            "programming concepts, or job-related topics. The input shoutd be a clear search term.")
    public String wenwebQuestionTool(@P(value = "the keyword to search") String keyword) {

        ArrayList<String> questions = new ArrayList<>();
        String encode = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://www.baidu.com/s?wd=" + encode;

        Document doc;
        try {
            doc=Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            return e.getMessage();
        }
        Elements select = doc.select(".");
        for (org.jsoup.nodes.Element element : select) {
            String text = element.text();
            questions.add(text);
        }
        return String.join("\n", questions);


    }

}
