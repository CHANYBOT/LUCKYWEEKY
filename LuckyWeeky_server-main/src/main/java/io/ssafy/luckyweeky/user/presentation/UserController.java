package io.ssafy.luckyweeky.user.presentation;

import com.google.gson.JsonObject;
import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.common.implement.Controller;
import io.ssafy.luckyweeky.common.util.parser.RequestJsonParser;
import io.ssafy.luckyweeky.common.util.security.CookieUtil;
import io.ssafy.luckyweeky.common.util.stream.FileHandler;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import io.ssafy.luckyweeky.user.application.dto.GeneralSignupUserDto;
import io.ssafy.luckyweeky.user.application.dto.LoginUserDto;
import io.ssafy.luckyweeky.user.application.service.UserService;
import io.ssafy.luckyweeky.user.application.validator.FileValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class UserController implements Controller {
    private static final String USER_PART = "user";
    private static final String FILE_PART = "file";

    private final UserService userService;

    public UserController() {
        this.userService = (UserService) XmlBeanFactory.getBean("userService");
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String action = RequestUrlPath.getURI(request.getRequestURI())[1];

        switch (action){
            case "RClmJ"://signup
                signUp(request, response,respJson);
                break;
            case "LWyAtd"://login
                login(request, response,respJson);
                break;
            case "TGCOwi":
                refreshToken(request, response, respJson);
                break;
            case "odsQk":
                logout(request, response, respJson);
                break;
        }
    }

    public void signUp(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parse(FileHandler.getFilePart(request,USER_PART));
        Part filePart = FileHandler.getFilePart(request,FILE_PART);
        String profileImageKey = FileHandler.processFilePart(filePart);

        GeneralSignupUserDto user = new GeneralSignupUserDto(
                jsonObject.get("email").getAsString(),
                jsonObject.get("password").getAsString(),
                jsonObject.get("username").getAsString(),
                jsonObject.get("birthDate").getAsString(),
                profileImageKey
        );

        // 4. 회원 가입 서비스 호출
        if (userService.generalRegister(user, filePart)) {
            respJson.addProperty("email", user.getEmail());
        } else {
            throw new IllegalArgumentException("회원가입 실패 코드 작성");
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        JsonObject jsonObject = RequestJsonParser.getInstance().parseFromBody(request.getReader());
        LoginUserDto loginUserDto = new LoginUserDto(
                jsonObject.get("email").getAsString(),
                jsonObject.get("password").getAsString()
        );
        Map<String,String> tokens = userService.login(loginUserDto);
        if(tokens==null){
            throw new IOException("login fail");
        }
        System.out.println("accessToken:"+tokens.get("accessToken"));
        respJson.addProperty("accessToken", tokens.get("accessToken"));
        CookieUtil.addRefreshTokenCookie(response,tokens.get("refreshToken"));
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response, JsonObject respJson) throws ServletException, IOException {
        String refreshToken = CookieUtil.getRefreshToken(request);
        Map<String,String> newTokens = userService.createTokens(Map.of(
                "refreshToken",refreshToken,
                "userId",request.getAttribute("userId")
        ));
        respJson.addProperty("accessToken", newTokens.get("accessToken"));
        CookieUtil.addRefreshTokenCookie(response,newTokens.get("refreshToken"));
    }


    public void logout(HttpServletRequest request, HttpServletResponse response,JsonObject respJson) {
        // 1. Refresh Token 무효화
        String refreshToken = CookieUtil.getRefreshToken(request);

        if (refreshToken != null) {
            userService.invalidateRefreshToken(refreshToken); // Refresh Token 무효화 로직 (예: DB에서 삭제)
        }
        CookieUtil.deleteRefreshTokenCookie(response);
    }
}

