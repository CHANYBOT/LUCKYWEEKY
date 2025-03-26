package io.ssafy.luckyweeky.common.filter;

import io.jsonwebtoken.Claims;
import io.ssafy.luckyweeky.common.infrastructure.provider.JwtTokenProvider;

import io.ssafy.luckyweeky.common.util.security.CookieUtil;
import io.ssafy.luckyweeky.common.util.url.RequestUrlPath;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class JwtAuthenticationFilter implements Filter {
    // 제외할 URL 목록
    private static final Set<String> EXCLUDE_URLS = Set.of
            (
                    "aB12Xz/LWyAtd",
                    "aB12Xz/RClmJ",
                    "qweSJqwo"
            );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = RequestUrlPath.url(httpRequest.getRequestURI());

        // 토큰 검증이 필요없는 url
        if (EXCLUDE_URLS.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 1-1. 헤더에서 Authorization 값 추출
            // 1-2. 토큰 재발급 요청일때 Cookie에서 refreshToken 추출
            String token = EXCLUDE_URLS.contains("aB12Xz/TGCOwi")? CookieUtil.getRefreshToken(httpRequest) :JwtTokenProvider.getInstance().resolveToken(httpRequest);
            // 2. 토큰 검증
            if (token != null && JwtTokenProvider.getInstance().validateToken(token)) {
                // 토큰이 유효한 경우: 사용자 정보를 SecurityContext에 저장하거나 클레임 추출
                Long userId = Long.parseLong(JwtTokenProvider.getInstance().getSubject(token));
                httpRequest.setAttribute("userId", userId); // 요청 속성에 사용자 정보 저장
                chain.doFilter(request, response);
                return;
            }
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.getWriter().write("{\"error\": \"token validation failed\"}");
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.getWriter().write("{\"error\": \"token validation failed\"}");
        }
    }


}
