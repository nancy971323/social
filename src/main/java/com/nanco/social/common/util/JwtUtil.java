package com.nanco.social.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 使用安全的密鑰生成方式
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration; // 默認24小時

    /**
     * 從JWT令牌中提取用戶ID
     */
    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 從JWT令牌中提取用戶名
     */
    public String extractUserName(String token) {
        return extractClaim(token, claims -> claims.get("userName", String.class));
    }

    /**
     * 從JWT令牌中提取到期時間
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 從JWT令牌中提取指定的聲明
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 提取JWT令牌中的所有聲明
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 檢查JWT令牌是否過期
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 為用戶生成JWT令牌
     */
    public String generateToken(Long userId, String userName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);
        return createToken(claims);
    }

    /**
     * 創建JWT令牌
     */
    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * 驗證JWT令牌是否有效
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
