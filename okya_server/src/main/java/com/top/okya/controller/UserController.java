package com.top.okya.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.top.okya.pojo.VtUserInfo;
import com.top.okya.service.UserService;
import com.top.okya.util.elHelp.FormatElData;
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
 * @describe：
 */

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    // 分页获取用户信息
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    @ResponseBody
    public String findPage(@RequestParam("userName") String userName, @RequestParam("roleCode") String roleCode, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("分页获取用户信息");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), userService.findPage(userName, roleCode, sortProp, sortOrder, setYear)), null).invoke();
        } catch (Exception e) {
            log.error("分页获取用户信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "分页获取用户信息失败", null).invoke();
        }
    }

    // 新增用户
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(VtUserInfo vtUserInfo) {
        try {
            userService.saveUser(vtUserInfo);
            log.info("新增用户信息成功");
            return new ResponseObject(true, "新增用户信息成功", null).invoke();
        } catch (Exception e) {
            log.error("新增用户信息发生异常，失败");
            return new ResponseObject(false, "新增用户信息失败", null).invoke();
        }
    }

    // 修改用户
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(VtUserInfo vtUserInfo) {
        try {
            userService.updateUser(vtUserInfo);
            log.info("修改用户信息成功");
            return new ResponseObject(true, "修改用户信息成功", null).invoke();
        } catch (Exception e) {
            log.error("修改用户信息发生异常，失败");
            return new ResponseObject(false, "修改用户信息失败", null).invoke();
        }
    }

    // 删除用户
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {
        try {
            userService.deleteUser(JSONObject.parseArray(datas, VtUserInfo.class), setYear);
            log.info("删除用户信息成功");
            return new ResponseObject(true, "删除用户信息成功", null).invoke();
        } catch (Exception e) {
            log.error("删除用户信息发生异常，失败");
            return new ResponseObject(false, "删除用户信息失败", null).invoke();
        }
    }
}
