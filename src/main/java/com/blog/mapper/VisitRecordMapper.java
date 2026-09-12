package com.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.entity.VisitRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 访问记录 Mapper，含仪表盘统计所需的聚合查询。
 */
@Mapper
public interface VisitRecordMapper extends BaseMapper<VisitRecord> {

    /** 总浏览量（全部访问记录数） */
    @Select("SELECT COUNT(*) FROM visit_record")
    long countTotalViews();

    /** 总访客数（按 IP 去重） */
    @Select("SELECT COUNT(DISTINCT ip) FROM visit_record")
    long countTotalVisitors();

    /** 今日浏览量 */
    @Select("SELECT COUNT(*) FROM visit_record WHERE DATE(visit_time) = CURDATE()")
    long countTodayViews();

    /** 今日新访客（今日出现、且此前从未出现过的 IP 数） */
    @Select("SELECT COUNT(DISTINCT ip) FROM visit_record " +
            "WHERE DATE(visit_time) = CURDATE() " +
            "AND ip NOT IN (SELECT ip FROM (SELECT DISTINCT ip FROM visit_record WHERE DATE(visit_time) < CURDATE()) t)")
    long countTodayNewVisitors();

    /** 近 N 天浏览量趋势，返回 list[{date, count}] */
    @Select("SELECT DATE(visit_time) AS date, COUNT(*) AS count " +
            "FROM visit_record " +
            "WHERE visit_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "GROUP BY DATE(visit_time) ORDER BY DATE(visit_time)")
    List<Map<String, Object>> selectVisitTrend(@Param("days") int days);

    /** 近 N 天访客趋势（按 IP 去重），返回 list[{date, count}] */
    @Select("SELECT DATE(visit_time) AS date, COUNT(DISTINCT ip) AS count " +
            "FROM visit_record " +
            "WHERE visit_time >= DATE_SUB(CURDATE(), INTERVAL #{days} DAY) " +
            "GROUP BY DATE(visit_time) ORDER BY DATE(visit_time)")
    List<Map<String, Object>> selectVisitorTrend(@Param("days") int days);

    /** 访客省份分布（按 IP 去重），返回 list[{name, value}] */
    @Select("SELECT IFNULL(province, '未知') AS name, COUNT(DISTINCT ip) AS value " +
            "FROM visit_record GROUP BY province ORDER BY value DESC")
    List<Map<String, Object>> selectProvinceDistribution();
}
