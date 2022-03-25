package com.top.okya.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.top.okya.pojo.AsRole;
import com.top.okya.service.RoleService;
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
 * @Date: 2020/3/27 20:19
 * @describe：
 */
@Controller
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Autowired
    RoleService roleService;

    // 角色选择下拉菜单取数
    @RequestMapping(value = "/getRoles", method = RequestMethod.POST)
    @ResponseBody
    public String getRoles(@RequestParam("setYear") String setYear) {
        try {
            log.info("角色选择下拉菜单取数");
            return new ResponseObject(true, JSONArray.toJSONString(roleService.getRoles(setYear)), null).invoke();
        } catch (Exception e) {
            log.error("角色选择下拉菜单取数发生异常，失败" + e.toString());
            return new ResponseObject(false, "角色选择下拉菜单取数失败", null).invoke();
        }
    }

    // 分页获取角色信息
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    @ResponseBody
    public String findPage(@RequestParam("roleCode") String roleCode, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("分页获取角色信息");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), roleService.findPage(roleCode, sortProp, sortOrder, setYear)), null).invoke();
        } catch (Exception e) {
            log.error("分页获取角色信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "分页获取角色信息失败", null).invoke();
        }
    }

    // 新增角色
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(AsRole asRole) {
        try {
            roleService.saveRole(asRole);
            log.info("新增角色信息成功");
            return new ResponseObject(true, "新增角色信息成功", null).invoke();
        } catch (Exception e) {
            log.error("新增角色信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "新增角色信息失败", null).invoke();
        }
    }

    // 修改角色
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(AsRole asRole) {
        try {
            roleService.updateRole(asRole);
            log.info("修改角色信息成功");
            return new ResponseObject(true, "修改角色信息成功", null).invoke();
        } catch (Exception e) {
            log.info("修改角色信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "修改角色信息失败", null).invoke();
        }
    }

    // 删除角色
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {
        try {
            roleService.deleteRole(JSONObject.parseArray(datas, AsRole.class), setYear);
            log.info("删除角色信息成功");
            return new ResponseObject(true, "删除角色信息成功", null).invoke();
        } catch (Exception e) {
            log.info("删除角色信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "删除角色信息失败", null).invoke();
        }
    }

    // 更新角色部件权限
    @RequestMapping(value = "/setRolePermissions", method = RequestMethod.POST)
    @ResponseBody
    public String setRolePermissions(@RequestParam("permission") String permission, @RequestParam("setYear") String setYear, @RequestParam("roleCode") String roleCode) {
        try {
            roleService.setRolePermissions(JSONObject.parseArray(permission, String.class), roleCode, setYear);
            log.info("更新角色部件权限成功");
            return new ResponseObject(true, "更新角色部件权限成功", null).invoke();
        } catch (Exception e) {
            log.info("更新角色部件权限失败" + e.toString());
            return new ResponseObject(false, "更新角色部件权限失败", null).invoke();
        }

    }
}
