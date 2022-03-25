package com.top.okya.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.top.okya.pojo.AsFormSearchBusiness;
import com.top.okya.pojo.AsTable;
import com.top.okya.pojo.AsTableColumns;
import com.top.okya.service.ReportService;
import com.top.okya.util.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:19
 * @describe：
 */
@Controller
@RequestMapping("/report")
@Slf4j
public class ReportController {

    @Autowired
    ReportService reportService;

    // 新增报表
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam("datas") String datas) {
        try {
            reportService.saveReport(datas);
            log.info("创建报表成功");
            return new ResponseObject(true, "创建报表成功", null).invoke();
        } catch (Exception e) {
            log.error("创建报表发生异常，失败" + e.toString());
            return new ResponseObject(false, "创建报表失败", null).invoke();
        }
    }

    // 修改报表
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@RequestParam("datas") String datas) {
        try {
            reportService.updateReport(datas);
            log.info("修改报表成功");
            return new ResponseObject(true, "修改报表成功", null).invoke();
        } catch (Exception e) {
            log.error("修改报表发生异常，失败" + e.toString());
            return new ResponseObject(false, "修改报表失败", null).invoke();
        }
    }

    // 删除报表
    @RequestMapping(value = "/delet", method = RequestMethod.POST)
    @ResponseBody
    public String delet(@RequestParam("datas") String datas) {
        try {
            reportService.deletReport(datas);
            log.info("删除报表成功");
            return new ResponseObject(true, "删除报表成功", null).invoke();
        } catch (Exception e) {
            log.error("删除报表发生异常，失败" + e.toString());
            return new ResponseObject(false, "删除报表失败", null).invoke();
        }
    }

    // 获取报表查询条件
    @RequestMapping(value = "/getSearchProp", method = RequestMethod.POST)
    @ResponseBody
    public String getSearchProp(@RequestParam("reportId") String reportId) {
        try {
            String searchProps = reportService.getSearchProp(reportId);
            log.info("获取报表查询条件成功");
            return new ResponseObject(true, searchProps, null).invoke();
        } catch (Exception e) {
            log.error("获取报表查询条件发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取报表查询条件失败", null).invoke();
        }
    }

    // 初始化自定义报表页面
    @RequestMapping(value = "/initPage", method = RequestMethod.POST)
    @ResponseBody
    public String initPage(@RequestParam("reportId") String reportId) {
        try {
            log.info("开始初始化自定义报表页面，报表ID: " + reportId);
            String searchField = reportService.getSearchProp(reportId);
            Map<String, Object> reportTemplet = reportService.getReport(reportId);
            if (searchField.length() > 0 && !searchField.equals("[]")) {
                reportTemplet.put("searchField", searchField);
            }
            return new ResponseObject(true, JSON.toJSONString(reportTemplet), null).invoke();
        } catch (Exception e) {
            log.info("初始化报表失败" + e.toString());
            return new ResponseObject(false, "初始化报表失败", null).invoke();
        }
    }

}
