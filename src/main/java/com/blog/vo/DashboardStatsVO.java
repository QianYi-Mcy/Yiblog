package com.blog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 仪表盘顶部 7 个统计卡片的数据。
 */
@Data
public class DashboardStatsVO implements Serializable {

    /** 总浏览量 */
    private long totalViews;

    /** 总访客数 */
    private long totalVisitors;

    /** 今日浏览 */
    private long todayViews;

    /** 今日新访客 */
    private long todayNewVisitors;

    /** 文章总数 */
    private long totalArticles;

    /** 评论总数 */
    private long totalComments;

    /** 待审评论 */
    private long pendingComments;
}
