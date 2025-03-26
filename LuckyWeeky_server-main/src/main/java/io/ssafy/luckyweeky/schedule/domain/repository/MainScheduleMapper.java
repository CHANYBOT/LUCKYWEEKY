package io.ssafy.luckyweeky.schedule.domain.repository;

import io.ssafy.luckyweeky.schedule.domain.model.MainScheduleEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MainScheduleMapper {
    void insertMainSchedule(MainScheduleEntity mainScheduleEntity);
    MainScheduleEntity selectMainScheduleById(Long id);
    List<MainScheduleEntity> selectMainSchedulesByDateRange(Map<String, Object> params);
}
