package com.blog.service;

import com.blog.vo.DashboardStatsVO;
import com.blog.vo.NameValueVO;
import com.blog.vo.TrendPointVO;

import java.util.List;

/**
 * 仪表盘统计业务接口。
 */
public interface StatsService {

    /** 顶部 7 个统计卡片 */
    DashboardStatsVO getDashboardStats();

    /** 浏览量趋势（近 days 天） */
    List<TrendPointVO> getVisitTrend(int days);

    /** 访客趋势（近 days 天，按 IP 去重） */
    List<TrendPointVO> getVisitorTrend(int days);

    /** 阅读量 TOP N 文章 */
    List<NameValueVO> getTopArticles(int limit);

    /** 访客省份分布 */
    List<NameValueVO> getProvinceDistribution();

    /** 系统运行天数 */
    long getRunDays();
}
