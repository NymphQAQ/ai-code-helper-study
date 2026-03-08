package art.qijian.aicodehelper.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

import java.util.List;

//@AiService
public interface AiCodeHelperService {

    @SystemMessage(fromResource = "system-prompt.txt")
    String chat(String prompt);

    @SystemMessage(fromResource = "system-prompt.txt")
    Report chatReport(String prompt);



    record Report(String name, List<String> messages) {
    }
}
