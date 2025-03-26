package io.ssafy.luckyweeky.schedule.presentation.converter;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.implement.Converter;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.application.dto.SubScheduleDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class JsonObjectToScheduleDto implements Converter<JsonObject, ScheduleDto> {
    private final static JsonObjectToScheduleDto instance = new JsonObjectToScheduleDto();

    public static JsonObjectToScheduleDto getInstance() {
        return instance;
    }

    @Override
    public ScheduleDto convert(JsonObject source) {
        try {
            List<SubScheduleDto> subSchedules = new ArrayList<>();
            source.get("subSchedules").getAsJsonArray().forEach(subScheduleElement -> {
                JsonObject subSchedule = subScheduleElement.getAsJsonObject();
                subSchedules.add(new SubScheduleDto(
                        getAsStringOrThrow(subSchedule, "title"),
                        getAsStringOrThrow(subSchedule, "description"),
                        getAsStringOrThrow(subSchedule, "startTime"),
                        getAsStringOrThrow(subSchedule, "endTime")
                ));
            });
            return new ScheduleDto(
                    Long.parseLong(getAsStringOrThrow(source, "userId")),
                    getAsStringOrThrow(source, "mainTitle"),
                    getAsStringOrThrow(source, "color"),
                    getAsStringOrThrow(source, "startTime"),
                    getAsStringOrThrow(source, "endTime"),
                    subSchedules
            );
        } catch (Exception e) {
            return null;
        }
    }
    private String getAsStringOrThrow(JsonObject jsonObject, String key) {

        if (!jsonObject.has(key) || jsonObject.get(key).isJsonNull()) {
            throw new IllegalArgumentException("필수요소 존재하지 않음");
        }
        return jsonObject.get(key).getAsString();
    }
}
