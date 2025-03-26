package io.ssafy.luckyweeky.schedule.application.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubScheduleDto {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String title;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public SubScheduleDto(String title, String description, String startTime, String endTime) {
        this.title = title;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime,FORMATTER);
        this.endTime = LocalDateTime.parse(endTime,FORMATTER);
    }

    public SubScheduleDto(String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    };

    @Override
    public String toString() {
        return "{" +
                "title:'" + title + '\'' +
                ", description:'" + description + '\'' +
                ", startTime:'" + startTime.format(FORMATTER) +'\'' +
                ", endTime:'" + endTime.format(FORMATTER) +'\'' +
                '}';
    }
}
