package io.ssafy.luckyweeky.scheduleAi.application.dto.request;

import java.time.LocalDateTime;

public class CreateAiScheduleRequestDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String task;
    private String availableTime;
    private String additionalRequest;

    public CreateAiScheduleRequestDTO(LocalDateTime startDate, LocalDateTime endDate, String task, String availableTime, String additionalRequest) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.task = task;
        this.availableTime = availableTime;
        this.additionalRequest = additionalRequest;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getTask() {
        return task;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public String getAdditionalRequest() {
        return additionalRequest;
    }

    @Override
    public String toString() {
        return "CreateAiScheduleRequestDTO{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", task='" + task + '\'' +
                ", availableTime='" + availableTime + '\'' +
                ", additionalRequest='" + additionalRequest + '\'' +
                '}';
    }
}
