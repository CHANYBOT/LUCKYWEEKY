package io.ssafy.luckyweeky.schedule.domain.model;

import java.time.LocalDateTime;

public class SubScheduleEntity {
    private Long subScheduleId; // 소일정 고유 ID
    private Long mainScheduleId; // 대일정 ID
    private String title; // 소일정 제목
    private String description; // 소일정 내용
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private boolean isCompleted; // 완료 여부
    private LocalDateTime createdAt; // 생성 날짜
    private LocalDateTime updatedAt; // 업데이트 날짜

    public SubScheduleEntity(Long subScheduleId, Long mainScheduleId, String title, String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.subScheduleId = subScheduleId;
        this.mainScheduleId = mainScheduleId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getSubScheduleId() {
        return subScheduleId;
    }

    public void setSubScheduleId(Long subScheduleId) {
        this.subScheduleId = subScheduleId;
    }

    public Long getMainScheduleId() {
        return mainScheduleId;
    }

    public void setMainScheduleId(Long mainScheduleId) {
        this.mainScheduleId = mainScheduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
