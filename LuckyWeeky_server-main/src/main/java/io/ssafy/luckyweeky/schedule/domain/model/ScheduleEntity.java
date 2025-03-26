package io.ssafy.luckyweeky.schedule.domain.model;

import io.ssafy.luckyweeky.schedule.domain.model.SubScheduleEntity;

import java.util.List;

public class ScheduleEntity {
    private final MainScheduleEntity mainScheduleEntity;
    private final List<SubScheduleEntity> subScheduleEntityList;

    public ScheduleEntity(MainScheduleEntity mainScheduleEntity, List<SubScheduleEntity> subScheduleEntityList) {
        this.mainScheduleEntity = mainScheduleEntity;
        this.subScheduleEntityList = subScheduleEntityList;
    }

    public MainScheduleEntity getMainScheduleEntity() {
        return mainScheduleEntity;
    }

    public List<SubScheduleEntity> getSubScheduleEntityList() {
        return subScheduleEntityList;
    }


}
