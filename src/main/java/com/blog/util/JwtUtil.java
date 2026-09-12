package com.blog.util;

import com.blog.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类：生成与解析 Token。
 */
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        // HS256 要求密钥至少 256 bit（32 字节），不足时 jjwt 会抛异常
        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    /** 生成 Token */
    public String generateToken(Integer userId, String username) {
        long now = System.currentTimeMillis();
        long expire = now + jwtProperties.getExpireHours() * 3600_000L;

        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", userId);
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .issuer(jwtProperties.getIssuer())
                .subject(username)
                .issuedAt(new Date(now))
                .expiration(new Date(expire))
                .signWith(secretKey)
                .compact();
    }

    /** 解析 Token，非法或过期返回 null */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /** 从 Token 中取用户名 */
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims == null ? null : claims.getSubject();
    }

    /** 校验 Token 是否有效 */
    public boolean isValid(String token) {
        return parseToken(token) != null;
    }
}
