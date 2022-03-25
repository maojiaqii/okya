package com.top.okya.service;

import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:28
 * @describe：
 */
public interface ReportService {

    int saveReport(String datas);

    String getSearchProp(String reportId);

    int updateReport(String datas);

    int deletReport(String datas);

    Map<String, Object> getReport(String reportId);
}
