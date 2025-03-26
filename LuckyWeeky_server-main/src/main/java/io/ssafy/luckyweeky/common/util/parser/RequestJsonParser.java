package io.ssafy.luckyweeky.common.util.parser;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.http.Part;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestJsonParser {
    private static final RequestJsonParser instance = new RequestJsonParser();
    private final Gson gson;

    private RequestJsonParser() {
        this.gson = new Gson();
    }

    public static RequestJsonParser getInstance() {
        return instance;
    }


    public JsonObject parse(Part jsonPart) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(jsonPart.getInputStream()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();

            // JSON 데이터 유효성 확인 후 파싱
            return JsonParser.parseString(jsonString).getAsJsonObject();
        }catch (IOException e) {
            throw new IOException("Failed to parse JSON에러코드");
        }
    }

    // 바디에서 JSON 데이터를 직접 파싱
    public JsonObject parseFromBody(BufferedReader bodyReader) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bodyReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();

            // JSON 데이터 유효성 확인 후 파싱
            return JsonParser.parseString(jsonString).getAsJsonObject();
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON from body");
        }
    }
}
