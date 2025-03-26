package io.ssafy.luckyweeky.scheduleAi.application.dto.request;

public class ReRequestAiScheduleDTO {
    private final String originSchedule;
    private final String newAdditionalRequest;

    public ReRequestAiScheduleDTO(String originSchedule, String newAdditionalRequest) {
        this.originSchedule = originSchedule;
        this.newAdditionalRequest = newAdditionalRequest;
    }

    public String getOriginalPrompt() {
        return originSchedule;
    }

    public String getNewAdditionalRequest() {
        return newAdditionalRequest;
    }
}
