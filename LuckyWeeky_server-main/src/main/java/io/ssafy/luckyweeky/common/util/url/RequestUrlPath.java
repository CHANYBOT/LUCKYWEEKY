package io.ssafy.luckyweeky.common.util.url;

public class RequestUrlPath {
    private static final String COMMON_URL="/api/v1/";
    private static final int COMMON_URL_LENGTH = COMMON_URL.length();

    public static String[] getURI(String path) throws IllegalArgumentException{
        if(!path.contains(COMMON_URL)){
            throw new IllegalArgumentException("잘못된요청url");
        }
        return path.substring(COMMON_URL_LENGTH).split("/");
    }

    public static String url(String path) throws IllegalArgumentException{
        if(!path.contains(COMMON_URL)){
            throw new IllegalArgumentException("잘못된요청url");
        }
        return path.substring(COMMON_URL_LENGTH);
    }
}
