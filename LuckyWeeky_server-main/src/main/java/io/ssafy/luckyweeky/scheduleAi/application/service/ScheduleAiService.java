package io.ssafy.luckyweeky.scheduleAi.application.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.scheduleAi.application.dto.request.CreateAiScheduleRequestDTO;
import io.ssafy.luckyweeky.scheduleAi.application.dto.request.ReRequestAiScheduleDTO;
import io.ssafy.luckyweeky.scheduleAi.domain.prompt.AIPromptGenerator;

import java.io.IOException;

public class ScheduleAiService {
    private final ChatgptService chatgptService;

    public ScheduleAiService() {
        this.chatgptService = (ChatgptService) XmlBeanFactory.getBean("chatgptService");

    }
    public String generateSchedule(CreateAiScheduleRequestDTO createAiScheduleRequestDTO) throws IOException {
        // 프롬프트 생성
        String prompt = AIPromptGenerator.generateInitialPrompt(createAiScheduleRequestDTO);

        // ChatGPT 호출
        return chatgptService.createChat(prompt);
    }

    public String reGenerateSchedule(ReRequestAiScheduleDTO reRequestAiScheduleDTO) throws IOException {
        String prompt = AIPromptGenerator.generateReRequestPrompt(reRequestAiScheduleDTO);
        // ChatGPT 호출
        return chatgptService.createChat(prompt);
    }

    public String generateClovaSchedule(String sttResult) throws IOException {
        // Clova 결과를 기반으로 프롬프트 생성
        String generatedPrompt = AIPromptGenerator.generateVoicePrompt(sttResult);
        return chatgptService.createChat(generatedPrompt);
    }
}
