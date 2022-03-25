package com.top.okya.controller;

import com.top.okya.service.NoticeService;
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
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    // 保存消息
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam("datas") String datas) {
        try {
            noticeService.save(datas);
            return new ResponseObject(true, "发送消息完成", null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存消息发生异常，失败" + e.toString());
            return new ResponseObject(false, "保存消息失败", null).invoke();
        }
    }

    // 保存常用联系人
    @RequestMapping(value = "/saveFrequentContacts", method = RequestMethod.POST)
    @ResponseBody
    public String saveFrequentContacts(@RequestParam("userCodes") String userCodes, @RequestParam("userCode") String userCode) {
        try {
            noticeService.saveFrequentContacts(userCode, userCodes);
            return new ResponseObject(true, "保存常用联系人成功", null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存常用联系人发生异常，失败" + e.toString());
            return new ResponseObject(false, "保存常用联系人失败", null).invoke();
        }
    }

    // 获取所有用户
    @RequestMapping(value = "/getAllUsers", method = RequestMethod.POST)
    @ResponseBody
    public String getAllUsers(@RequestParam("setYear") String setYear) {
        try {
            log.info("开始获取所有用户");
            return new ResponseObject(true, noticeService.getAllUsers(setYear), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取所有用户发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取所有用户失败", null).invoke();
        }
    }

    // 获取常用联系人
    @RequestMapping(value = "/getSelectUsers", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectUsers(@RequestParam("userCode") String userCode) {
        try {
            log.info("开始获取常用联系人");
            return new ResponseObject(true, noticeService.getSelectUsers(userCode), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取常用联系人发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取常用联系人失败", null).invoke();
        }
    }

    // 更新消息接收状态
    @RequestMapping(value = "/updateNoticeTarget", method = RequestMethod.POST)
    @ResponseBody
    public String updateNoticeTarget(@RequestParam("userCode") String userCode, @RequestParam("noticeId") String noticeId, @RequestParam("receiveStatus") String receiveStatus) {
        try {
            log.info("开始更新消息接收状态");
            noticeService.updateNoticeTarget(userCode, noticeId, receiveStatus);
            return new ResponseObject(true, "更新消息接收状态成功", null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("更新消息接收状态发生异常，失败" + e.toString());
            return new ResponseObject(false, "更新消息接收状态失败", null).invoke();
        }
    }

    // 获取消息接收人
    @RequestMapping(value = "/getNoticeUsers", method = RequestMethod.POST)
    @ResponseBody
    public String getNoticeUsers(@RequestParam("noticeId") String noticeId) {
        try {
            log.info("开始获取消息接收人");
            return new ResponseObject(true, noticeService.getNoticeUsers(noticeId), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取消息接收人发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取消息接收人失败", null).invoke();
        }
    }

}
