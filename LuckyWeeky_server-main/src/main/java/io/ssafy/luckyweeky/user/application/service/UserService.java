package io.ssafy.luckyweeky.user.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.ssafy.luckyweeky.common.DispatcherServlet;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.infrastructure.provider.JwtTokenProvider;
import io.ssafy.luckyweeky.user.application.converter.GeneralSignupUserDtoToUserEntityWithSaltConverter;
import io.ssafy.luckyweeky.user.application.converter.UserEntityToLoginUserDto;
import io.ssafy.luckyweeky.user.application.converter.UserEntityWithSalt;
import io.ssafy.luckyweeky.common.infrastructure.s3.S3Fileloader;
import io.ssafy.luckyweeky.common.util.security.OpenCrypt;
import io.ssafy.luckyweeky.user.application.dto.GeneralSignupUserDto;
import io.ssafy.luckyweeky.user.application.dto.LoginUserDto;
import io.ssafy.luckyweeky.user.domain.model.UserEntity;
import io.ssafy.luckyweeky.user.domain.model.UserSaltEntity;
import io.ssafy.luckyweeky.user.infrastructure.repository.UserRepository;
import jakarta.servlet.http.Part;
import org.apache.ibatis.annotations.One;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class UserService {
    private final UserRepository userRepository;
    // Access Token 유효 시간 (30분)
    private static final long ACCESS_TOKEN_VALIDITY = 30 * 60 * 1000;
    // Refresh Token 유효 시간 (7일)
    private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000;

    public UserService() {
        this.userRepository = (UserRepository) XmlBeanFactory.getBean("userRepository");
    }

    /**
     * 이메일 존재 여부 체크
     *
     * @param email 확인할 이메일
     * @return 이메일 존재 시 true, 존재하지 않으면 false
     */
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * 로그인 처리
     *
     * @param loginUser 사용자 정보(email,password)
     * @return 로그인 성공 시 access_token, refresh_token담긴 map반환
     */
    public Map<String, String> login(LoginUserDto loginUser) {
        String salt = userRepository.getUserSalt(loginUser.getEmail());
        UserEntity user = userRepository.findByEmail(loginUser.getEmail());

        if (salt != null && user != null && user.getPasswordHash().equals(OpenCrypt.getEncryptPassword(loginUser.getPassword(), salt))) {
            // Access Token Claims 생성
            Claims accessClaims = Jwts.claims();
            accessClaims.put("name", user.getUsername());
            Claims refreshClaims = Jwts.claims();

            String userId = user.getUserId() + "";
            // Access Token 생성
            String accessToken = JwtTokenProvider.getInstance().createToken(userId, accessClaims, ACCESS_TOKEN_VALIDITY);
            // Refresh Token 생성
            String refreshToken = JwtTokenProvider.getInstance().createToken(userId, refreshClaims, REFRESH_TOKEN_VALIDITY);

            return userRepository.updateRefreshToken(user.getUserId(), refreshToken)
                    ? Map.of("accessToken", accessToken, "refreshToken", refreshToken)
                    : null;
        }
        return null; // 로그인 실패
    }

    /**
     * 회원가입 처리
     *
     * @param generalSignupUser 사용자 회원가입 정보
     * @param filePart          사용자 프로필 이미지정보
     * @return 회원가입 성공 시 true, 실패 시 false
     */
    public boolean generalRegister(GeneralSignupUserDto generalSignupUser, Part filePart) throws IOException {
        // 이미 이메일이 존재하면 회원가입 실패
        if (isEmailExists(generalSignupUser.getEmail())) {
            return false;
        }

        if (filePart != null) {
            File tempFile = null;
            try {
                // 파일 확장자 추출
                int lastDotIndex = generalSignupUser.getProfileImageKey().lastIndexOf(".");
                String extension = (lastDotIndex != -1) ? generalSignupUser.getProfileImageKey().substring(lastDotIndex) : ".jpg";

                // 프로젝트 디렉토리 기준으로 임시 파일 저장 경로 설정
                String tempDirPath = DispatcherServlet.getWebInfPath() + "/temp";
                File tempDir = new File(tempDirPath);
                if (!tempDir.exists()) {
                    tempDir.mkdirs(); // 디렉토리가 없으면 생성
                }

                // 임시 파일 생성
                tempFile = File.createTempFile("upload-", extension, tempDir);

                // 파일 쓰기
                filePart.write(tempFile.getAbsolutePath());

                // S3에 파일 등록
                S3Fileloader.getInstance().upload(tempFile, generalSignupUser.getProfileImageKey());
            } catch (IOException e) {
                throw new IOException("File Upload Error");
            } finally {
                if (tempFile != null && tempFile.exists()) {
                    // 임시 파일 삭제
                    tempFile.delete();
                }
            }
        }
        UserEntityWithSalt result = GeneralSignupUserDtoToUserEntityWithSaltConverter.getInstance().convert(generalSignupUser);
        String salt = result.getSalt();
        UserEntity userEntity = result.getUserEntity();
        UserSaltEntity userSaltEntity = new UserSaltEntity(userEntity.getUserId(), salt);

        System.out.println(salt);
        System.out.println(userEntity);
        System.out.println(userSaltEntity);

        return userRepository.insertUser(userEntity, userSaltEntity);
    }

    /**
     * 사용자 refresh_token무효화
     *
     * @param refreshToken
     */
    public void invalidateRefreshToken(String refreshToken) {
        // redis 로직 추가
        String userId = JwtTokenProvider.getInstance().getSubject(refreshToken);
        if (userId != null) {
            userRepository.deleteTokenById(Long.parseLong(userId));
        }
    }

    /**
     * 사용자 tokens 검사 and 생성
     *
     * @param Map
     * @return 유효시 시 새로운access_token, refresh_token
     * 유효하지 않을 시 null
     */
    public Map<String, String> createTokens(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        String refreshToken = (String) params.get("refreshToken");
        if (userId == null || refreshToken == null) {
            return null;
        }
        UserEntity user = userRepository.findById(Long.parseLong(userId));

        Claims accessClaims = Jwts.claims();
        accessClaims.put("name", user.getUsername());
        String newAccessToken = JwtTokenProvider.getInstance().createToken(userId, accessClaims, ACCESS_TOKEN_VALIDITY);
        String newRefreshToken = JwtTokenProvider.getInstance().createToken(userId, Jwts.claims(), REFRESH_TOKEN_VALIDITY);

        return userRepository.updateRefreshToken(user.getUserId(), newRefreshToken)
                ? Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken)
                : null;
    }
}

