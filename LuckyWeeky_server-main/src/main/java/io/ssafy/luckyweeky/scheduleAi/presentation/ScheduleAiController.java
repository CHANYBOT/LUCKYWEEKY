package io.ssafy.luckyweeky.scheduleAi.presentation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.scheduleAi.application.dto.request.CreateAiScheduleRequestDTO;
import io.ssafy.luckyweeky.scheduleAi.application.dto.request.ReRequestAiScheduleDTO;
import io.ssafy.luckyweeky.scheduleAi.application.service.ClovaService;
import io.ssafy.luckyweeky.scheduleAi.application.service.ScheduleAiService;
import io.ssafy.luckyweeky.scheduleAi.domain.prompt.AIPromptGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

public class ScheduleAiController implements Controller {

    private final ScheduleAiService scheduleAiService;
    private final ClovaService clovaService;

    public ScheduleAiController() {
        this.scheduleAiService = (ScheduleAiService) XmlBeanFactory.getBean("scheduleAiService");
        this.clovaService = (ClovaService) XmlBeanFactory.getBean("clovaService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1]; // URI에서 액션 추출

        try {
            switch (action) {
                case "SmdBid": { // 일정 생성 요청
                    generateSchedule(request, response, respJson);
                    break;
                }
                case "LdslbEd": { // 다른 액션 처리
                    reRequestGenerateSchedule(request, response, respJson);
                    break;
                }
                case "DnbDiw": { // 다른 액션 처리
                    processClova(request, response, respJson);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported action: " + action);
                }
            }
        } catch (Exception e) {
            respJson.addProperty("result", "false");
            respJson.addProperty("error", e.getMessage());
        }
    }




//    AI 일정 재요청==============================================
    private void reRequestGenerateSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException {
        // JSON 데이터 파싱
        JsonObject requestData = null;
        try (BufferedReader reader = request.getReader()) {
            requestData = RequestJsonParser.getInstance().parseFromBody(reader);
        }

        // validation
        String newAdditionalRequest = requestData.get("newAdditionalRequest").getAsString();
        String originSchedule = requestData.get("originSchedule").getAsString();
        if (originSchedule == null || newAdditionalRequest == null) throw new NullPointerException("R01");

        // DTO 생성
        ReRequestAiScheduleDTO reRequestAiScheduleDTO = new ReRequestAiScheduleDTO(originSchedule, newAdditionalRequest);

        // 서비스 호출
        String aiReGeneratedResult = scheduleAiService.reGenerateSchedule(reRequestAiScheduleDTO);
        System.out.println("aiReGeneratedResult: " + aiReGeneratedResult);
        // 응답 데이터 구성
        respJson.addProperty("result", "true");
        respJson.add("schedule", JsonParser.parseString(aiReGeneratedResult).getAsJsonObject());
    }

    //    AI 일정 생성==============================================
    private void generateSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException, ServletException {
        // JSON 데이터 파싱
        JsonObject requestData = null;
        try (BufferedReader reader = request.getReader()) {
            requestData = RequestJsonParser.getInstance().parseFromBody(reader);
        }


        // DTO 생성
        CreateAiScheduleRequestDTO createAiScheduleRequestDTO = new CreateAiScheduleRequestDTO(
                LocalDateTime.parse(requestData.get("startDate").getAsString()), // LocalDateTime으로 변환
                LocalDateTime.parse(requestData.get("endDate").getAsString()),   // LocalDateTime으로 변환
                requestData.get("task").getAsString(),
                requestData.get("availableTime").getAsString(),
                requestData.has("additionalRequest") ? requestData.get("additionalRequest").getAsString() : null
        );

        System.out.println("createAiScheduleRequestDTO: " + createAiScheduleRequestDTO);
        // 서비스 호출
        String aiGeneratedResult = scheduleAiService.generateSchedule(createAiScheduleRequestDTO);
        System.out.println("====aiGeneratedResult====\n"+aiGeneratedResult);

        // 응답 데이터 구성
        respJson.addProperty("result", "true");
        respJson.add("schedule", JsonParser.parseString(aiGeneratedResult).getAsJsonObject());
    }

    //    AI 일정 생성==============================================

    private void processClova(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) {
        try {
            String sttResult = speachToTextClova(request, response, respJson);
//            String sttResult = "헬스장가려고하는데, 일주일 내내 하루 한 시간 삼분할로 운동할 예정이야";
            System.out.println("STT Result: " + sttResult);
            if (sttResult == null) throw new NullPointerException("A03");

            // AI서비스 호출
            String aiGeneratedResult = scheduleAiService.generateClovaSchedule(sttResult);
            System.out.println("aiGeneratedResult: " + aiGeneratedResult);


            // 응답 데이터 구성
            respJson.addProperty("result", "true");
            respJson.add("schedule", JsonParser.parseString(aiGeneratedResult).getAsJsonObject());

        } catch (Exception e) {
            e.printStackTrace();
            respJson.addProperty("result", "false");
            respJson.addProperty("error", e.getMessage());
        }
    }
    private String speachToTextClova(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException, ServletException {

        Part audioFile = request.getPart("audioFile");
        InputStream audioStream = audioFile.getInputStream();

        return clovaService.callClovaSTT(audioStream); // Naver Clova 호출
    }
}
