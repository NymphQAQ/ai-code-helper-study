package art.qijian.aicodehelper.ai.controller;

import art.qijian.aicodehelper.ai.AiCodeHelperService;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
@AllArgsConstructor
public class AiController {

    private final AiCodeHelperService aiCodeHelperService;

    @GetMapping("/chat")
    public Flux<ServerSentEvent<String>> chatStream(int memoryId, String message) {
        return aiCodeHelperService.chatStream(memoryId, message)
                .map(content -> ServerSentEvent.<String>builder()
                        .data(content)
                        .build());
    }
}