package com.top.okya.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.top.okya.pojo.AsPermission;
import com.top.okya.service.MenuService;
import com.top.okya.util.common.HumpLineTrans;
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
 * @Date: 2020/4/9 16:04
 * @describe：
 */

@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    @Autowired
    MenuService menuService;

    // 获取用户角色菜单、按钮列表
    @RequestMapping(value = "/getMenuButton", method = RequestMethod.POST)
    @ResponseBody
    public String getMenuButton(@RequestParam("roleCode") String roleCode, @RequestParam("setYear") String setYear) {
        try {
            log.info("-------初始化登录用户菜单、按钮--------");
            return new ResponseObject(true, menuService.getMenuButtonData(roleCode, "menu", setYear), menuService.getMenuButtonData(roleCode, "button", setYear)).invoke();
        } catch (Exception e) {
            log.info("-------初始化登录用户菜单、按钮失败--------");
            return new ResponseObject(true, "初始化登录用户菜单、按钮失败", null).invoke();
        }
    }

    // 分页获取部件信息
    @RequestMapping(value = "/getCompos", method = RequestMethod.POST)
    @ResponseBody
    public String getCompos(@RequestParam("compoName") String compoName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("-------分页获取部件信息-------");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), menuService.getCompos(compoName, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc", setYear)), null).invoke();
        } catch (Exception e) {
            log.info("-------分页获取部件信息失败--------");
            return new ResponseObject(true, "分页获取部件信息失败", null).invoke();
        }
    }

    // 获取父级部件选择器数据
    @RequestMapping(value = "/getParentCompos", method = RequestMethod.POST)
    @ResponseBody
    public String getParentCompos(@RequestParam("setYear") String setYear) {
        try {
            log.info("-------获取父级部件选择器数据-------");
            return new ResponseObject(true, JSONArray.toJSONString(menuService.getParentCompos(setYear)), null).invoke();
        } catch (Exception e) {
            log.info("-------获取父级部件选择器数据失败--------");
            return new ResponseObject(true, "获取父级部件选择器数据失败", null).invoke();
        }
    }

    // 获取所有部件选择器数据
    @RequestMapping(value = "/getAllCompos", method = RequestMethod.POST)
    @ResponseBody
    public String getAllCompos(@RequestParam("setYear") String setYear) {
        try {
            log.info("-------获取所有部件选择器数据--------");
            return new ResponseObject(true, JSONArray.toJSONString(menuService.getAllCompos(setYear)), null).invoke();
        } catch (Exception e) {
            log.info("-------获取所有部件选择器数据失败--------");
            return new ResponseObject(true, "获取所有部件选择器数据失败", null).invoke();
        }
    }

    // 新增部件
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(AsPermission asPermission) {
        try {
            menuService.saveCompo(asPermission);
            log.info("-------新增部件成功--------");
            return new ResponseObject(true, "新增部件成功", null).invoke();
        } catch (Exception e) {
            log.error("新增部件发生异常，失败" + e.toString());
            return new ResponseObject(false, "新增部件失败", null).invoke();
        }
    }

    // 修改部件
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(AsPermission asPermission) {
        try {
            menuService.updateCompo(asPermission);
            log.info("-------修改部件成功--------");
            return new ResponseObject(true, "修改部件成功", null).invoke();
        } catch (Exception e) {
            log.error("修改部件发生异常，失败" + e.toString());
            return new ResponseObject(false, "修改部件失败", null).invoke();
        }
    }

    // 删除部件
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {
        try {
            menuService.deleteCompo(JSONObject.parseArray(datas, AsPermission.class), setYear);
            log.info("删除部件信息成功");
            return new ResponseObject(true, "删除部件信息成功", null).invoke();
        } catch (Exception e) {
            log.info("删除部件信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "删除部件信息失败", null).invoke();
        }
    }

    // 获取角色部件权限列表
    @RequestMapping(value = "/getComposByRole", method = RequestMethod.POST)
    @ResponseBody
    public String getComposByRole(@RequestParam("roleCode") String roleCode, @RequestParam("setYear") String setYear) {
        try {
            log.info("-------获取角色部件权限列表--------");
            return new ResponseObject(true, JSONObject.toJSONString(menuService.getComposByRole(roleCode, setYear)), null).invoke();
        } catch (Exception e) {
            log.info("-------获取角色部件权限列表失败--------");
            return new ResponseObject(true, "获取角色部件权限列表失败", null).invoke();
        }
    }
}
