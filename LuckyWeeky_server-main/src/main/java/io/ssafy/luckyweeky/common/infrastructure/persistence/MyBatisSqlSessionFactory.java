package io.ssafy.luckyweeky.common.infrastructure.persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyBatisSqlSessionFactory {
    private static volatile SqlSessionFactory sqlSessionFactory;

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            synchronized (MyBatisSqlSessionFactory.class) {
                if (sqlSessionFactory == null) {
                    sqlSessionFactory = initializeFactory();
                }
            }
        }
        return sqlSessionFactory;
    }

    private static SqlSessionFactory initializeFactory() {
        try {
            // 드라이버 명시적 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("MySQL JDBC Driver loaded successfully.");

            // 환경 변수에서 값을 가져오고 유효성 검사
            String DB_URL = getPropertyVariable("DB_URL");
            String DB_USERNAME = getPropertyVariable("DB_USERNAME");
            String DB_PASSWORD = getPropertyVariable("DB_PASSWORD");

            // Properties 객체에 환경 변수 추가
            Properties props = new Properties();
            props.setProperty("DB_URL", DB_URL);
            props.setProperty("DB_USERNAME", DB_USERNAME);
            props.setProperty("DB_PASSWORD", DB_PASSWORD);
//
//            System.out.println("Environment variables loaded:");
//            System.out.println("DB_URL: " + DB_URL);
//            System.out.println("DB_USERNAME: " + DB_USERNAME);
            // 비밀번호는 보안상 로깅하지 않음

            // MyBatis 설정 파일 읽기
            String resource = "mybatis-config.xml"; // 설정 파일 경로
            try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
                SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, props);
//                System.out.println("MyBatis SqlSessionFactory initialized successfully.");
                return factory;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to IO error. Check your MyBatis configuration file.", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Failed to initialize SqlSessionFactory due to missing environment variables.", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException.MYSQL Driver not found.", e);
        }
    }

    private static String getPropertyVariable(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("Environment Variable '" + key + "' is empty.");
        }
        return value;
    }
}
