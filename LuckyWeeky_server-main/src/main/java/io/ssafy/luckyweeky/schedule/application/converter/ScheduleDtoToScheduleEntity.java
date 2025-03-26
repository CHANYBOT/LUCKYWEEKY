package io.ssafy.luckyweeky.schedule.application.converter;

import io.ssafy.luckyweeky.common.implement.Converter;
import io.ssafy.luckyweeky.common.util.generator.SnowflakeIdGenerator;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.domain.model.MainScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.model.SubScheduleEntity;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDtoToScheduleEntity implements Converter<ScheduleDto, ScheduleEntity> {
    private final static ScheduleDtoToScheduleEntity instance = new ScheduleDtoToScheduleEntity();

    public static ScheduleDtoToScheduleEntity getInstance() {
        return instance;
    }

    @Override
    public ScheduleEntity convert(ScheduleDto source) {
        MainScheduleEntity mainScheduleEntity = new MainScheduleEntity(
                SnowflakeIdGenerator.getInstance().nextId(),
                source.getUserId(),
                source.getMainTitle(),
                source.getStartTime(),
                source.getEndTime(),
                source.getColor()
        );
        List<SubScheduleEntity> subScheduleEntityList = new ArrayList<>();
        source.getSubSchedules().forEach(subScheduleDto -> {
            subScheduleEntityList.add(new SubScheduleEntity(
                    SnowflakeIdGenerator.getInstance().nextId(),
                    mainScheduleEntity.getMainScheduleId(),
                    subScheduleDto.getTitle(),
                    subScheduleDto.getDescription(),
                    subScheduleDto.getStartTime(),
                    subScheduleDto.getEndTime()
            ));
        });
        return new ScheduleEntity(mainScheduleEntity, subScheduleEntityList);
    }
}
