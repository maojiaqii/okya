package com.top.okya.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.top.okya.dao.ReportMapper;
import com.top.okya.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: maojiaqi
 * @Date: 2021/2/24 16:39
 * @describe：
 */
@Slf4j
@Service("ReportService")
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;

    @Override
    @Transactional
    public int saveReport(String datas) {
        log.info("开始创建报表");
        Map<String, Object> resultStr = JSONObject.parseObject(datas);
        String reportId = resultStr.get("REPORT_ID").toString();
        String asReportInsertField = "GUID";
        String asReportInsertValue = "'" + UUID.randomUUID().toString();
        List<Map<String, Object>> lm = null;
        log.info("开始拼接sql语句");
        for (Map.Entry<String, Object> entry : resultStr.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.contains("_")) {
                asReportInsertField = asReportInsertField + "," + key;
                asReportInsertValue = asReportInsertValue + "','" + (key.equals("COLUMN_INFOS") ? JSON.toJSONString(value) : value.toString());
            } else if (key.equals("size")) {
                asReportInsertField = asReportInsertField + ",\"" + key + "\"";
                asReportInsertValue = asReportInsertValue + "','" + value.toString();
            } else if (key.equals("searchInfos")) {
                JSONArray ja = JSONArray.parseArray(value.toString());
                if (ja != null && ja.size() > 0) {
                    log.info("遍历到查询条件，暂停拼接sql语句，先入库查询条件信息");
                    lm = new ArrayList<>();
                    for (Object p : ja) {
                        Map<String, Object> mp = JSONObject.parseObject(p.toString());
                        mp.put("guid", UUID.randomUUID().toString());
                        mp.put("reportId", reportId);
                        lm.add(mp);
                    }
                    reportMapper.saveReportSearch(lm);
                    log.info("入库查询条件信息完成，继续拼接sql语句");
                }
            }
        }
        log.info("拼接sql语句完成，入库");
        return reportMapper.saveReport(asReportInsertField, asReportInsertValue + "'");
    }

    @Override
    @Transactional
    public int deletReport(String datas) {
        log.info("开始删除报表");
        JSONArray delDatas = JSONObject.parseArray(datas);
        if (delDatas != null && delDatas.size() > 0) {
            log.info("遍历删除的报表信息");
            List<String> lm = new ArrayList<>();
            for (Object p : delDatas) {
                Map<String, Object> mp = JSONObject.parseObject(p.toString());
                String rid = mp.get("REPORT_ID").toString();
                lm.add(rid);
                log.info("报表信息: ID = " + mp.get("REPORT_ID").toString());
            }
            log.info("遍历删除的报表信息完成，开始删除");
            return reportMapper.deleteReport(lm) + reportMapper.deleteReportSearch(lm);
        }
        return 0;
    }

    @Override
    @Transactional
    public int updateReport(String datas) {
        log.info("开始修改报表");
        Map<String, Object> resultStr = JSONObject.parseObject(datas);
        String reportId = resultStr.get("REPORT_ID").toString();
        List<String> reportIds = new ArrayList<>();
        reportIds.add(reportId);
        log.info("修改报表的ID:" + reportId);
        reportMapper.deleteReport(reportIds);
        reportMapper.deleteReportSearch(reportIds);
        log.info("删除原有报表数据完成");
        String asReportInsertField = "GUID";
        String asReportInsertValue = "'" + UUID.randomUUID().toString();
        List<Map<String, Object>> lm = null;
        log.info("开始拼接sql语句");
        for (Map.Entry<String, Object> entry : resultStr.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.contains("_") && !key.equals("ROW_ID")) {
                asReportInsertField = asReportInsertField + "," + key;
                asReportInsertValue = asReportInsertValue + "','" + (key.equals("COLUMN_INFOS") ? JSON.toJSONString(value) : value.toString());
            } else if (key.equals("size")) {
                asReportInsertField = asReportInsertField + ",\"" + key + "\"";
                asReportInsertValue = asReportInsertValue + "','" + value.toString();
            } else if (key.equals("searchInfos")) {
                JSONArray ja = JSONArray.parseArray(value.toString());
                if (ja != null && ja.size() > 0) {
                    log.info("遍历到查询条件，暂停拼接sql语句，先入库查询条件信息");
                    lm = new ArrayList<>();
                    for (Object p : ja) {
                        Map<String, Object> mp = JSONObject.parseObject(p.toString());
                        mp.put("guid", UUID.randomUUID().toString());
                        mp.put("reportId", reportId);
                        lm.add(mp);
                    }
                    reportMapper.saveReportSearch(lm);
                    log.info("入库查询条件信息完成，继续拼接sql语句");
                }
            }
        }
        log.info("拼接sql语句完成，入库");
        return reportMapper.saveReport(asReportInsertField, asReportInsertValue + "'");
    }

    @Override
    public Map<String, Object> getReport(String reportId) {
        return reportMapper.getReport(reportId);
    }

    @Override
    public String getSearchProp(String reportId) {
        return JSON.toJSONString(reportMapper.getReportSearch(reportId));
    }
}
