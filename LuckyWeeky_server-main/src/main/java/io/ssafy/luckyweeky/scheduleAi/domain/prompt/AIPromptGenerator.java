package io.ssafy.luckyweeky.scheduleAi.domain.prompt;

import io.ssafy.luckyweeky.scheduleAi.application.dto.request.CreateAiScheduleRequestDTO;
import io.ssafy.luckyweeky.scheduleAi.application.dto.request.ReRequestAiScheduleDTO;

import java.time.LocalDateTime;

public class AIPromptGenerator {
    //=============================== JSON 응답 템플릿 ===========================================
    private static final String RESULT_TEMPLATE = "{\n" +
            "    \"mainTitle\": {주요 목적},\n" +
            "    \"startTime\": String (\"yyyy-MM-ddTHH:mm:ss\"),\n" +
            "    \"endTime\": String (\"yyyy-MM-ddTHH:mm:ss\"),\n" +
            "    \"subSchedules\": [\n" +
            "        {\n" +
            "            \"title\": {이부분은 특히 아주 **구체적**으로, 다양할수록 좋음.}\n" +
            "            \"startTime\": String (\"yyyy-MM-ddTHH:mm:ss\"),\n" +
            "            \"endTime\": String (\"yyyy-MM-ddTHH:mm:ss\")\n" +
            "        }\n" +
            "    ]\n" +
            "}\n";
//===============================================================================================


    // 1. AI 일정 생성
    public static final String INITIAL_PROMPT_TEMPLATE = "너는 일정계획 전문가이고," +
            "요청에 정확히 맞는 세부 일정을 계획해야해. 2번 json 응답 형식에 맞게, 요청을 제외한 응답를 반환해야해.\n"
            + "요청====== \n"
            + "시작 날짜:%s \n"
            + "종료 날짜:%s \n"
            + "목표(할 일):%s \n"
            + "투자가능한 시간:%s \n"
            + "추가 요청사항:%s \n\n"
            + "(응답는 앞뒤 아무말도 없이 반드시 아래처럼 ***json***으로만 출력해야함.) \n"
            + RESULT_TEMPLATE;

    // 2. AI 일정 재요청
    public static final String FOLLOW_UP_PROMPT_TEMPLATE = "아래 1번 데이터를 기반으로 2번 추가 정보를 반영해서, 3번 json 응답 형식에 맞게 응답를 반환해야해.**\n\n"
            + "1번: %s\n\n"
            + "2번: %s\n\n"
            + "3번응답:(응답는 앞뒤 아무말도 없이 ***json***으로만 응답해야함.예시({\n" +
            "    \"mainTitle\": \"독서하기\",\n" +
            "    \"startTime\": \"2022-03-01T00:00:00\",\n" +
            "    \"endTime\": \"2022-03-31T23:59:59\",\n" +
            "    \"subSchedules\": [\n" +
            "        {\n" +
            "            \"title\": \"독서 - '모비딕' 3장부터 4장까지\",\n" +
            "            \"startTime\": \"2022-03-02T10:00:00\",\n" +
            "            \"endTime\": \"2022-03-02T11:00:00\"\n" +
            "        },...이후는너무길어서생략): \n\n"+RESULT_TEMPLATE;


    // 3. AI 음성 일정 생성
    public static final String CLOVA_INITIAL_PROMPT_TEMPLATE = "너는 일정계획 전문가이고," +
            "아래 1번 요청에 정확히 맞는 세부 일정을 계획해야해. 만약 정보가 부족하거나, 맥락이 이상하다면 맥락을 유추해서 정확한정보를 반환해야해.특히 2번 json 응답 형식에 맞게 응답를 반환해야해."+
            "유의할점은, 일정생성시 1번의 투자가능시간과 추가 요청사항을 충분히 반영해야해. 아무리 추상적이어도 맥락을 이해하고 구체적인 정보를 줘야해.\n\n"
            +"그리고 구체적인 날짜정보가 없다면, "+ LocalDateTime.now() +"부터 1주일이야. \n\n"
            + "1. 요청: %s\n"
            + "2. 응답:\n(응답는 앞뒤 아무말도 없이 반드시 아래처럼 ***json***으로만 출력해야함.)====== \n"
            + RESULT_TEMPLATE;

    /**
     * AnalyticalData 데이터를 기반으로 첫 요청 프롬프트를 생성합니다.
     *
     * @param data 프롬프트 생성에 필요한 데이터
     * @return 생성된 프롬프트 문자열
     * @throws IllegalArgumentException 데이터가 유효하지 않을 경우
     */
    public static String generateInitialPrompt(CreateAiScheduleRequestDTO data) {
        validateData(data);
        return String.format(
                INITIAL_PROMPT_TEMPLATE,
                data.getStartDate(),
                data.getEndDate(),
                data.getTask(),
                data.getAvailableTime(),
                data.getAdditionalRequest() != null ? data.getAdditionalRequest() : "없음"
        );
    }

    public static String generateReRequestPrompt(ReRequestAiScheduleDTO data) {
        validateData(data);
        return String.format(
                FOLLOW_UP_PROMPT_TEMPLATE,
                data.getOriginalPrompt(),
                data.getNewAdditionalRequest()
        );
    }

    public static String generateVoicePrompt(String sttResult) {
        validateData(sttResult);
        return String.format(
                CLOVA_INITIAL_PROMPT_TEMPLATE,
                sttResult
        );
    }

    private static void validateData(String sttResult) {
        if (sttResult == null) {
            throw new NullPointerException("A03");
        }
    }


    // === 유효성 검사 ===
    public static void validateData(CreateAiScheduleRequestDTO data) {
        if (data == null) {
            throw new NullPointerException("A01");
        }
    }
    public static void validateData(ReRequestAiScheduleDTO data) {
        if (data == null) {
            throw new NullPointerException("A02");
        }
    }
}
