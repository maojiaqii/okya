package com.top.okya.controller;

import com.top.okya.service.SystemService;
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
 * @Date: 2020/3/26 19:55
 * @describe： 通用控制层
 */

@Controller
@RequestMapping("/sysSetup")
@Slf4j
public class SystemController {

    @Autowired
    SystemService systemService;

    // 保存系统配置
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam("sysTitle") String sysTitle, @RequestParam("sysIcon") String sysIcon) {
        try {
            return new ResponseObject(true, systemService.save(sysTitle, sysIcon), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存系统配置发生异常，失败" + e.toString());
            return new ResponseObject(false, "保存系统配置失败", null).invoke();
        }
    }

    // 保存系统配置
    @RequestMapping(value = "/getSetup", method = RequestMethod.POST)
    @ResponseBody
    public String getSetup() {
        try {
            log.info("开始获取系统配置！");
            return new ResponseObject(true, systemService.getSetup(), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取系统配置发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取系统配置失败", null).invoke();
        }
    }

}
