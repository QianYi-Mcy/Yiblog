package com.blog.controller;

import com.blog.common.Result;
import com.blog.service.StatsService;
import com.blog.vo.DashboardStatsVO;
import com.blog.vo.NameValueVO;
import com.blog.vo.TrendPointVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 仪表盘统计接口。
 */
@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final StatsService statsService;

    public AdminStatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /** 仪表盘顶部 7 个统计卡片 */
    @GetMapping("/dashboard")
    public Result<DashboardStatsVO> dashboard() {
        return Result.ok(statsService.getDashboardStats());
    }

    /** 浏览量趋势 */
    @GetMapping("/visitTrend")
    public Result<List<TrendPointVO>> visitTrend(@RequestParam(defaultValue = "7") int days) {
        return Result.ok(statsService.getVisitTrend(days));
    }

    /** 访客趋势 */
    @GetMapping("/visitorTrend")
    public Result<List<TrendPointVO>> visitorTrend(@RequestParam(defaultValue = "7") int days) {
        return Result.ok(statsService.getVisitorTrend(days));
    }

    /** TOP N 文章阅读量 */
    @GetMapping("/topArticles")
    public Result<List<NameValueVO>> topArticles(@RequestParam(defaultValue = "10") int limit) {
        return Result.ok(statsService.getTopArticles(limit));
    }

    /** 访客省份分布 */
    @GetMapping("/provinceDistribution")
    public Result<List<NameValueVO>> provinceDistribution() {
        return Result.ok(statsService.getProvinceDistribution());
    }

    /** 系统运行天数 */
    @GetMapping("/runDays")
    public Result<Long> runDays() {
        return Result.ok(statsService.getRunDays());
    }
}
