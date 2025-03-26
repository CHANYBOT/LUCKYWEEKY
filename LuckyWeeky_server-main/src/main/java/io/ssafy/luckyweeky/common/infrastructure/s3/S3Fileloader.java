package io.ssafy.luckyweeky.common.infrastructure.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class S3Fileloader {
    private static S3Fileloader instance;
    private static String BUCKET_NAME;
    private static final Region REGION = Region.AP_NORTHEAST_2;

    private final S3Client s3Client;

    private S3Fileloader(String accessKey, String secretKey) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public static synchronized S3Fileloader getInstance() {
        if (instance == null) {
            // 환경 변수에서 값 가져오기
            String accessKey = System.getProperty("AWS_ACCESS_KEY");
            String secretKey = System.getProperty("AWS_SECRET_KEY");
            BUCKET_NAME = System.getProperty("BUCKET_NAME");

            // 유효성 검사
            if (accessKey == null || secretKey == null || BUCKET_NAME == null) {
                throw new IllegalStateException(
                        "환경 변수 누락: " +
                                (accessKey == null ? "AWS_ACCESS_KEY " : "") +
                                (secretKey == null ? "AWS_SECRET_KEY " : "") +
                                (BUCKET_NAME == null ? "BUCKET_NAME" : "")
                );
            }

            instance = new S3Fileloader(accessKey, secretKey);
        }
        return instance;
    }

    public String upload(File file, String keyName) throws IOException {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    .build();
            s3Client.putObject(putObjectRequest, file.toPath());
            return "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/" + keyName;
        } catch (S3Exception e) {
            throw new IOException("S3 이미지 업로드 에러", e);
        }
    }

    public InputStream download(String keyName) throws Exception {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    .build();

            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            e.printStackTrace();
            throw new Exception("S3 이미지 파일 not found 에러", e);
        }
    }

    public synchronized void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}
