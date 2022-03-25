package com.top.okya.dao;

import com.top.okya.util.eChart.EChartBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/8/25 11:58
 * @describe：
 */

@Mapper
public interface EChartMapper {

    List<EChartBean> getCommonData(@Param("tableName") String tableName, @Param("yearMonthDay") String yearMonthDay);
}
