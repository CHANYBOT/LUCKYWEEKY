package io.ssafy.luckyweeky.schedule.application.service;

import com.fasterxml.jackson.core.type.TypeReference;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.infrastructure.cache.RedisManager;
import io.ssafy.luckyweeky.schedule.application.converter.ScheduleDtoToScheduleEntity;
import io.ssafy.luckyweeky.schedule.application.converter.ScheduleEntityToScheduleDto;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.infrastructure.repository.ScheduleRepository;
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService() {
        this.scheduleRepository = (ScheduleRepository) XmlBeanFactory.getBean("scheduleRepository");
    }

    public boolean addSchedule(ScheduleDto scheduleDto) {
        ScheduleEntity scheduleEntity = ScheduleDtoToScheduleEntity.getInstance().convert(scheduleDto);
        if (scheduleRepository.insertSchedule(scheduleEntity)) {
            RedisManager.invalidateCacheBasedOnTitle(Long.toString(scheduleDto.getUserId()));
            return true;
        }
        return false;
    }

    public List<ScheduleDto> getSchedulesByDateRange(Map<String, Object> params) {
        List<ScheduleEntity> scheduleEntities = null;
        try (Jedis jedis = RedisManager.getPool().getResource()) {
            ObjectMapper objectMapper = new ObjectMapper();

            String key = String.join(":",
                    Objects.requireNonNullElse(params.get("userId"), "defaultUser").toString(),
                    Objects.requireNonNullElse(params.get("startDate"), "defaultDate").toString(),
                    "schedule");

            // 캐시에서 데이터 조회
            String scheduleData = jedis.get(key);
            System.out.println(scheduleData);
            scheduleEntities = scheduleData == null ?
                    scheduleRepository.getSchedulesByDateRange(params) :
                    objectMapper.readValue(scheduleData, new TypeReference<>() {});

            if (scheduleData == null) {
                scheduleData = objectMapper.writeValueAsString(scheduleEntities);
                // 캐시에 데이터 저장 (TTL: 1시간 = 3600초)c
                jedis.setex(key, 3600, scheduleData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("redis connection error");
            scheduleEntities = scheduleRepository.getSchedulesByDateRange(params);
        }
        // 검증 로직


        //
        List<ScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleEntities.forEach(scheduleEntity -> {
            scheduleDtos.add(ScheduleEntityToScheduleDto.getInstance().convert(scheduleEntity));
        });
        return scheduleDtos;
    }


    public boolean deleteSubSchedule(Map<String,String> params) {
        String subScheduleTitle = params.get("subScheduleTitle");
        if (scheduleRepository.deleteSubSchedule(subScheduleTitle)) {
            String userId = params.get("userId");
            RedisManager.invalidateCacheBasedOnTitle(userId);
            return true;
        }
        return false;
    }

}
