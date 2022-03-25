package com.top.okya.service;

import com.top.okya.util.eChart.EChartBean;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:28
 * @describe：
 */
public interface EChartService {

    List<EChartBean> getCommonData(String tableName);
}
