package io.ssafy.luckyweeky.schedule.domain.model;

import java.time.LocalDateTime;

public class MainScheduleEntity {
    private Long mainScheduleId; // 대일정 고유 ID
    private Long userId; // 유저 ID
    private String title; // 대일정 제목
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간
    private String color; // 색상 코드 (기본값: #cccccc)
    private LocalDateTime createdAt; // 생성 날짜
    private LocalDateTime updatedAt; // 업데이트 날짜

    public MainScheduleEntity(Long mainScheduleId, Long userId, String title, LocalDateTime startTime, LocalDateTime endTime, String color) {
        this.mainScheduleId = mainScheduleId;
        this.userId = userId;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = (color != null) ? color : "#cccccc";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMainScheduleId() {
        return mainScheduleId;
    }

    public void setMainScheduleId(Long mainScheduleId) {
        this.mainScheduleId = mainScheduleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    @Override
    public String toString() {
        return "MainScheduleEntity{" +
                "mainScheduleId=" + mainScheduleId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", color='" + color + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
