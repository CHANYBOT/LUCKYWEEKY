package io.ssafy.luckyweeky.user.application.validator;

import jakarta.servlet.http.Part;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class FileValidator {
    private static FileValidator instance;
    private final long maxFileSizeInBytes;
    private final String[] allowedMimeTypes;

    // 생성자를 private으로 제한해 외부에서 객체 생성 금지
    private FileValidator(long maxFileSizeInBytes, String[] allowedMimeTypes) {
        this.maxFileSizeInBytes = maxFileSizeInBytes;
        this.allowedMimeTypes = allowedMimeTypes;
    }

    // 싱글톤 인스턴스를 반환하는 메서드
    public static FileValidator getInstance() {
        if (instance == null) {
            instance = new FileValidator(
                    1 * 1024 * 1024, // 1MB
                    new String[] {"image/jpeg", "image/png", "image/gif"} // 허용된 MIME 타입
            );
        }
        return instance;
    }

    /**
     * Part 데이터를 기반으로 파일 유효성 검사
     *
     * @param part 업로드된 파일의 Part 객체
     * @return 파일이 유효한지 여부
     */
    public boolean isValid(Part part){
        return part==null||(isValidSize(part) && isValidMimeType(part) && isValidImageContent(part));
    }

    private boolean isValidSize(Part part){
        return part.getSize() <= maxFileSizeInBytes;
    }

    private boolean isValidMimeType(Part part){
        String mimeType = part.getContentType(); // Part에서 MIME 타입 직접 가져오기
        if (mimeType == null) {
            return false;
        }
        for (String allowedMimeType : allowedMimeTypes) {
            if (mimeType.equals(allowedMimeType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidImageContent(Part part){
        try (InputStream inputStream = part.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            return image != null; // 이미지 데이터를 정상적으로 읽을 수 있는지 확인
        }catch (IOException e){
            return false;
        }
    }
}
