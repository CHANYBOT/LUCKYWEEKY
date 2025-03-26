package io.ssafy.luckyweeky.common.util.stream;

import io.ssafy.luckyweeky.common.infrastructure.s3.S3Fileloader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageStreamUtil {
    private static final String PREFIX = "/api/v1/qweSJqwo/";
    private static final int BUFFER_SIZE = 8192;

    // InputStream에서 OutputStream으로 스트리밍 처리
    public static void streamImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyName = request.getRequestURI().substring(PREFIX.length());

        // 확장자 추출 및 MIME 타입 설정
        String extension = keyName.substring(keyName.lastIndexOf(".") + 1).toLowerCase();
        String contentType = getMimeType(extension);
        response.setContentType(contentType);

        // S3에서 파일 스트림 가져오기
        try (InputStream inputStream = S3Fileloader.getInstance().download(keyName)) {
            if (inputStream == null) {
                throw new IOException("이미지를 찾을 수 없습니다.");
            }
            // 클라이언트로 스트리밍
            try (OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }
        }catch (Exception e) {
            throw new IOException("이미지를 찾을 수 없습니다.");
        }
    }

    // 확장자에 따른 MIME 타입 반환
    private static String getMimeType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream"; // 기본 MIME 타입
        }
    }
}
