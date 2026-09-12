package com.blog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 站点配置：站点标题、副标题、默认主题色。
 * 可通过 application.properties 中的 blog.* 配置项覆盖。
 */
@Component
@ConfigurationProperties(prefix = "blog")
public class SiteConfig {

    /** 站点标题 */
    private String title = "探索，永无止境";

    /** 站点副标题 / 标语 */
    private String subtitle = "分享技术，记录生活";

    /** 默认主题色：green 或 red */
    private String theme = "green";

    /** 页脚版权信息 */
    private String copyright = "© 2026 我的博客 | Powered by SpringBoot3 + JDK21";

    /** 站点访问地址（用于启动横幅展示，可在配置中覆盖） */
    private String baseUrl = "http://localhost:8080";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}


