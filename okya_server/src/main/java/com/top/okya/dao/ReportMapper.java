package com.top.okya.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报表设计器数据库访问层
 *
 * @author maojiaqi
 * @since 2020-05-06 13:14:21
 */
@Mapper
public interface ReportMapper {

    int saveReport(@Param("col") String col, @Param("val") String val);

    int saveReportSearch(@Param("vals") List<Map<String, Object>> vals);

    List<Map<String, String>> getReportSearch(@Param("reportId") String reportId);

    int deleteReport(@Param("reportIds") List<String> reportIds);

    int deleteReportSearch(@Param("reportIds") List<String> reportIds);

    Map<String, Object> getReport(@Param("reportId") String reportId);
}