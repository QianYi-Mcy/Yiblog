package com.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 相关配置，前缀 blog.jwt。
 */
@Component
@ConfigurationProperties(prefix = "blog.jwt")
public class JwtProperties {

    /** 签名密钥（至少 32 字节，用于 HS256） */
    private String secret = "Yiblog-Graduation-Project-Secret-Key-2026-Please-Change-Me-32bytes";

    /** 过期时间（小时） */
    private long expireHours = 24;

    /** 签发者 */
    private String issuer = "yiblog";

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpireHours() {
        return expireHours;
    }

    public void setExpireHours(long expireHours) {
        this.expireHours = expireHours;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
