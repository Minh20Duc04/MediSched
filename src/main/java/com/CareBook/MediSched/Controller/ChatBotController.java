package com.CareBook.MediSched.Controller;

import com.CareBook.MediSched.Dto.ChatBotRequest;
import com.CareBook.MediSched.Dto.ChatBotResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class ChatBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    @Qualifier("openAiRestTemplate")
    private RestTemplate template;


    @PostMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt){ //mỗi lần chạy phải thay đổi api key trong yml và đổi api key trong Authorization trong header
        ChatBotRequest request = new ChatBotRequest(model, prompt);
        ChatBotResponse chatBotResponse = template.postForObject(apiURL, request, ChatBotResponse.class);
        return chatBotResponse.getChoices().get(0).getMessage().getContent();
    }

}
