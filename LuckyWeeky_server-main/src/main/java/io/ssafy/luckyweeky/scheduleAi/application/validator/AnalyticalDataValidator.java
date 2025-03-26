package io.ssafy.luckyweeky.scheduleAi.application.validator;

import io.ssafy.luckyweeky.common.util.validator.StringValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AnalyticalDataValidator {

    public static boolean isValid(String startDate, String endDate, String task, String availableTime, String additionalRequest) {
        return isValidDate(startDate) &&
                isValidDate(endDate) &&
                isValidTask(task) &&
                isValidAvailableTime(availableTime)&&
                isValidAdditionalRequest(additionalRequest);
    }

    public static void validate(String startDate, String endDate, String task, String availableTime, String additionalRequest) throws IllegalArgumentException {
        if(!isValid(startDate, endDate, task, availableTime, additionalRequest)) {
            throw new IllegalArgumentException("분석 요청 데이터 유효성 에러코드작성");
        }
    }

    private static boolean isValidDate(String date) {
        if (date == null || date.isBlank()) {
            return false;
        }

        try {
            LocalDate.parse(date); // 날짜 형식 유효성 검사
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidTask(String task) {
        return task != null && !task.isBlank() && StringValidator.isValid(task); // 작업(task)이 null 또는 비어 있지 않은지 확인
    }

    private static boolean isValidAvailableTime(String availableTime) {
        if (availableTime == null || availableTime.isBlank()) {
            return false;
        }

        return StringValidator.isValid(availableTime);
    }

    private static boolean isValidAdditionalRequest(String additionalRequest) {
        if (additionalRequest == null || additionalRequest.isBlank()) {
            return true;
        }

        return StringValidator.isValid(additionalRequest);
    }
}
