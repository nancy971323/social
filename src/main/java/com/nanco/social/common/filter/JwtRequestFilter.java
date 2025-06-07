package com.nanco.social.common.filter;

import com.nanco.social.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        Long userId = null;
        String userName = null;

        // 檢查Authorization頭是否包含Bearer令牌
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            try {
                userId = jwtUtil.extractUserId(token);
                userName = jwtUtil.extractUserName(token);
            } catch (Exception e) {
                logger.error("無法解析JWT令牌", e);
            }
        }

        // 如果令牌有效，設置用戶信息到請求屬性中並設置 Spring Security 認證
        if (userId != null && userName != null && jwtUtil.validateToken(token)) {
            request.setAttribute("userId", userId);
            request.setAttribute("userName", userName);
            
            // 設置 Spring Security 認證
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    // 排除不需要JWT驗證的路徑
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/api/user/register") || path.equals("/api/user/login");
    }
}
