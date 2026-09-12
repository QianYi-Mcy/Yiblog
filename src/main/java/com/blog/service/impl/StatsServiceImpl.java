package com.blog.service.impl;

import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CommentMapper;
import com.blog.mapper.VisitRecordMapper;
import com.blog.service.StatsService;
import com.blog.vo.DashboardStatsVO;
import com.blog.vo.NameValueVO;
import com.blog.vo.TrendPointVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘统计业务实现。
 */
@Service
public class StatsServiceImpl implements StatsService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final VisitRecordMapper visitRecordMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    /** 系统上线日期，用于计算运行天数 */
    @Value("${blog.launch-date:2026-01-01}")
    private String launchDate;

    public StatsServiceImpl(VisitRecordMapper visitRecordMapper,
                            ArticleMapper articleMapper,
                            CommentMapper commentMapper) {
        this.visitRecordMapper = visitRecordMapper;
        this.articleMapper = articleMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public DashboardStatsVO getDashboardStats() {
        DashboardStatsVO vo = new DashboardStatsVO();
        vo.setTotalViews(visitRecordMapper.countTotalViews());
        vo.setTotalVisitors(visitRecordMapper.countTotalVisitors());
        vo.setTodayViews(visitRecordMapper.countTodayViews());
        vo.setTodayNewVisitors(visitRecordMapper.countTodayNewVisitors());
        vo.setTotalArticles(articleMapper.countTotalArticles());
        vo.setTotalComments(commentMapper.countTotalComments());
        vo.setPendingComments(commentMapper.countPendingComments());
        return vo;
    }

    @Override
    public List<TrendPointVO> getVisitTrend(int days) {
        return toTrend(visitRecordMapper.selectVisitTrend(days));
    }

    @Override
    public List<TrendPointVO> getVisitorTrend(int days) {
        return toTrend(visitRecordMapper.selectVisitorTrend(days));
    }

    @Override
    public List<NameValueVO> getTopArticles(int limit) {
        List<NameValueVO> result = new ArrayList<>();
        for (Map<String, Object> row : articleMapper.selectTopArticles(limit)) {
            result.add(new NameValueVO(
                    String.valueOf(row.get("title")),
                    toLong(row.get("views"))));
        }
        return result;
    }

    @Override
    public List<NameValueVO> getProvinceDistribution() {
        List<NameValueVO> result = new ArrayList<>();
        for (Map<String, Object> row : visitRecordMapper.selectProvinceDistribution()) {
            result.add(new NameValueVO(
                    row.get("name") == null ? "未知" : String.valueOf(row.get("name")),
                    toLong(row.get("value"))));
        }
        return result;
    }

    @Override
    public long getRunDays() {
        try {
            LocalDate start = LocalDate.parse(launchDate, DATE_FMT);
            long days = ChronoUnit.DAYS.between(start, LocalDate.now());
            // 上线当天算第 1 天
            return Math.max(days + 1, 1);
        } catch (Exception e) {
            return 1;
        }
    }

    /** 把 mapper 的 Map 结果规范化为 TrendPointVO，日期统一为字符串 */
    private List<TrendPointVO> toTrend(List<Map<String, Object>> rows) {
        List<TrendPointVO> list = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Object dateObj = row.get("date");
            String dateStr;
            if (dateObj instanceof java.sql.Date sqlDate) {
                dateStr = sqlDate.toLocalDate().format(DATE_FMT);
            } else if (dateObj instanceof LocalDate localDate) {
                dateStr = localDate.format(DATE_FMT);
            } else {
                dateStr = String.valueOf(dateObj);
            }
            list.add(new TrendPointVO(dateStr, toLong(row.get("count"))));
        }
        return list;
    }

    private long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }
}
