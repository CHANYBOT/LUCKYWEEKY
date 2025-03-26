package io.ssafy.luckyweeky.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

public class CORSFilter implements Filter {
    // 허용할 Origin 집합
    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:3000",
            "http://localhost:5173",
            "https://luckyweeky.store",
            "http://luckyweeky.store"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 요청의 Origin 가져오기
        String origin = httpRequest.getHeader("Origin");
        // Origin이 허용된 목록에 있는지 확인
        if (origin == null || !ALLOWED_ORIGINS.contains(origin)) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.getWriter().write("{\"error\": \"This request is not allowed\"}");
            return;
        }
        // CORS 헤더 설정
        httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        // 브라우저가 CORS 정책을 확인하기 위해 서버에 OPTIONS 요청을 보냄
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키 전송을 허용하려면 추가

        // OPTIONS 요청에 대한 처리 (Preflight 요청)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 다음 필터나 서블릿으로 요청 전달
        chain.doFilter(request, response);
    }
}
