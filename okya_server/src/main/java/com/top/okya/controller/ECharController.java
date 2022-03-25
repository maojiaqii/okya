package com.top.okya.controller;

import com.alibaba.fastjson.JSONArray;
import com.top.okya.service.EChartService;
import com.top.okya.util.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:19
 * @describe：
 */
@Controller
@RequestMapping("/eChart")
@Slf4j
public class ECharController {

    @Autowired
    EChartService eChartService;

    // 获取单位当日开票量
    @RequestMapping(value = "/getCommonData", method = RequestMethod.POST)
    @ResponseBody
    public String getCommonData(@RequestParam("tableName") String tableName) {
        try {
            log.info("获取EChart数据");
            return new ResponseObject(true, JSONArray.toJSONString(eChartService.getCommonData(tableName)), null).invoke();
        } catch (Exception e) {
            log.error("获取EChart数据发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取EChart数据失败", null).invoke();
        }
    }

}
