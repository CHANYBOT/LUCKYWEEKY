package io.ssafy.luckyweeky.schedule.presentation.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.application.service.ScheduleService;
import io.ssafy.luckyweeky.schedule.presentation.converter.JsonObjectToScheduleDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ScheduleController implements Controller {//UAKRPCjN
    private final ScheduleService scheduleService;
    public ScheduleController() {
        this.scheduleService =(ScheduleService) XmlBeanFactory.getBean("scheduleService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1];

        switch (action){
            case "OqwSjA":{
                addSchedule(request, response,respJson);
                break;
            }
            case "WJsdDo":{
                getThisWeekSchedules(request, response,respJson);
                break;
            }
            case "lCSZB":{
                getSchedulesByDate(request,response,respJson);
                break;
            }case "SHVLC": {
                deleteSubSchedule(request, response, respJson);
                break;
            }case "BDSdE": {
                saveAiSchedule(request, response, respJson);
                break;
            }
        }
    }

    private void saveAiSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        // userId를 JSON 데이터에 추가
        jsonObject.addProperty("userId", (Long) request.getAttribute("userId"));

        System.out.println("Received JSON Object: " + jsonObject);
        ScheduleDto scheduleDto = JsonObjectToScheduleDto.getInstance().convert(jsonObject);
        if (scheduleDto == null) {
            System.err.println("Converted ScheduleDto is null");
        }
        scheduleDto.setUserId((Long) request.getAttribute("userId"));

        if(scheduleDto == null){
            throw new IllegalArgumentException("request body is invalid");
        }
        if(!scheduleService.addSchedule(scheduleDto)){
            throw new IllegalArgumentException("schedule register failed");
        }
        // 등록된 일정 데이터 가져오기
        Map<String, Object> params = new HashMap<>();
        params.put("userId", scheduleDto.getUserId());
        params.put("startDate", scheduleDto.getStartTime());
        params.put("endDate", scheduleDto.getEndTime());

        // 일정 데이터를 JSON 형식으로 변환 후 응답에 추가
        String scheduleData = scheduleService.getSchedulesByDateRange(params).toString();
        respJson.add("schedule", JsonParser.parseString(scheduleData).getAsJsonArray());

    }

    private void addSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        // 요청 본문에서 JSON 데이터를 파싱
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        jsonObject.addProperty("userId", (Long) request.getAttribute("userId"));
        ScheduleDto scheduleDto = JsonObjectToScheduleDto.getInstance().convert(jsonObject);
        if(scheduleDto==null){
            throw new IllegalArgumentException("request body is invalid");
        }
        if(!scheduleService.addSchedule(scheduleDto)){
            throw new IllegalArgumentException("schedule register failed");
        }
    }

    private void getThisWeekSchedules(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        LocalDateTime now = LocalDateTime.now();
        Map<String ,Object> params = new HashMap<>(
                Map.of(
                        "userId", request.getAttribute("userId"),
                        "startDate", now.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0),
                        "endDate", now.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59)
                )
        );
        respJson.add("schedules", JsonParser.parseString(scheduleService.getSchedulesByDateRange(params).toString()).getAsJsonArray());
    }

    private void getSchedulesByDate(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        LocalDateTime date = LocalDateTime.parse(jsonObject.get("date").getAsString(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String ,Object> params = new HashMap<>(
                Map.of(
                        "userId", request.getAttribute("userId"),
                        "startDate", date.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0),
                        "endDate", date.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59)
                )
        );
        respJson.add("schedules", JsonParser.parseString(scheduleService.getSchedulesByDateRange(params).toString()).getAsJsonArray());
        System.out.println(respJson.get("schedules").toString());
    }

//    임시메서드
    private void deleteSubSchedule(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        String userId = request.getAttribute("userId").toString();
        String subScheduleTitle = jsonObject.get("subScheduleTitle").getAsString();

        if (!scheduleService.deleteSubSchedule(Map.of("subScheduleTitle",subScheduleTitle,"userId",userId))) {
            throw new IllegalArgumentException("Failed to delete sub-schedule");
        }
    }
}
