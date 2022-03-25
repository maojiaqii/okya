package com.top.okya.controller;

import com.top.okya.service.CompanyService;
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
 * @Date: 2020/3/28 20:08
 * @describe：
 */

@Controller
@Slf4j
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    // 单位选择下拉树取数
    @RequestMapping(value = "/getCoTree", method = RequestMethod.POST)
    @ResponseBody
    public String getCoTree(@RequestParam("setYear") String setYear) {
        try {
            log.info("单位选择下拉树取数");
            return new ResponseObject(true, companyService.getCompanyTree(setYear), null).invoke();
        } catch (Exception e) {
            log.info("单位选择下拉树取数失败" + e.toString());
            return new ResponseObject(false, "单位选择下拉树取数失败", null).invoke();
        }
    }
}
