package io.ssafy.luckyweeky.schedule.application.converter;

import io.ssafy.luckyweeky.common.implement.Converter;
import io.ssafy.luckyweeky.common.util.generator.SnowflakeIdGenerator;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.application.dto.SubScheduleDto;
import io.ssafy.luckyweeky.schedule.domain.model.MainScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.model.SubScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleEntityToScheduleDto implements Converter<ScheduleEntity, ScheduleDto> {
    private final static ScheduleEntityToScheduleDto instance = new ScheduleEntityToScheduleDto();

    public static ScheduleEntityToScheduleDto getInstance() {
        return instance;
    }

    @Override
    public ScheduleDto convert(ScheduleEntity source) {
        List<SubScheduleDto> subSchedules = new ArrayList<>();
        source.getSubScheduleEntityList().forEach(subScheduleEntity -> {
            subSchedules.add(new SubScheduleDto(
                    subScheduleEntity.getTitle(),
                    subScheduleEntity.getDescription(),
                    subScheduleEntity.getStartTime(),
                    subScheduleEntity.getEndTime()
            ));
        });
        return new ScheduleDto(
                source.getMainScheduleEntity().getTitle(),
                source.getMainScheduleEntity().getColor(),
                source.getMainScheduleEntity().getStartTime(),
                source.getMainScheduleEntity().getEndTime(),
                subSchedules
        );
    }
}
