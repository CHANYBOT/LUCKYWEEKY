package io.ssafy.luckyweeky.schedule.domain.repository;

import io.ssafy.luckyweeky.schedule.domain.model.SubScheduleEntity;

import java.util.List;
import java.util.Map;

public interface SubScheduleMapper {
    void insertSubSchedule(SubScheduleEntity subScheduleEntity);
    List<SubScheduleEntity> selectSubSchedulesByMainScheduleIdAndDateRange(Map<String, Object> params);

    void deleteSubScheduleByMainScheduleId(Long mainScheduleId);

    void deleteSubSchedule(String subScheduleTitle);
}
