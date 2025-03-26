package io.ssafy.luckyweeky.common.env;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SecretManagerContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String secretJson = System.getenv("LUCKYWEEKY_ENV_VARS");

            // 환경 변수 확인
            if (secretJson == null) {
//                System.out.println("환경 변수 'LUCKYWEEKY_ENV_VARS'가 설정되지 않았습니다. 로컬 설정 파일을 사용합니다.");
                secretJson = readLocalSecrets(sce); // 로컬 JSON 파일 읽기
            }

            if (secretJson == null) {
                throw new IllegalStateException("환경 변수와 로컬 설정 파일이 모두 설정되지 않았습니다.");
            }

            // JSON 파싱 및 System.setProperty 설정
            JSONObject secrets = new JSONObject(secretJson);
            secrets.keySet().forEach(key -> {
                String value = secrets.getString(key);
                System.setProperty(key, value);
//                System.out.println("환경 변수 '" + key + "' 값이 설정되었습니다");
            });

        } catch (Exception e) {
            throw new RuntimeException("환경 변수 초기화에 실패했습니다.", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 필요 시 리소스 정리
    }

    // 로컬 JSON 파일 읽기
    private String readLocalSecrets(ServletContextEvent sce) {
        try {
            String realPath = sce.getServletContext().getRealPath("/WEB-INF/local-secrets.json");
//            System.out.println("로컬 설정 파일 경로: " + realPath);

            File file = new File(realPath);
            if (!file.exists()) {
//                System.err.println("로컬 설정 파일이 존재하지 않습니다: " + realPath);
                return null;
            }

            try (Scanner scanner = new Scanner(new FileReader(file))) {
                scanner.useDelimiter("\\Z"); // 파일 끝까지 읽기
                return scanner.next();
            }
        } catch (IOException e) {
//            System.err.println("로컬 설정 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
            return null;
        }
    }
}
