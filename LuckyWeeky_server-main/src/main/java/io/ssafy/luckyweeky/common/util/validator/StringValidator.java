package io.ssafy.luckyweeky.common.util.validator;

public class StringValidator {
    // 비속어 목록
    private static final String[] PROHIBITED_WORDS = {"씨발", "병신", "개새끼"};

    private static final String SQL_INJECTION_PATTERN = "('.+--)|(\\|)|(%7C)|(;)|(--|\\/\\*|\\*\\/)";
    private static final String PROHIBITED_CHARACTERS = "[<>\"']";

    // true : 유효성 성공
    public static boolean isValid(String input) {
        return !containsProhibitedWords(input) &&
                !containsSQLInjectionPattern(input) &&
                !containsProhibitedCharacters(input);
    }

    private static boolean containsProhibitedWords(String input) {
        for (String word : PROHIBITED_WORDS) {
            if (input.toLowerCase().contains(word)) {
                return true; // 비속어 포함
            }
        }
        return false;
    }

    private static boolean containsSQLInjectionPattern(String input) {
        return input.matches(SQL_INJECTION_PATTERN);
    }

    private static boolean containsProhibitedCharacters(String input) {
        return input.matches(".*" + PROHIBITED_CHARACTERS + ".*");
    }
}
