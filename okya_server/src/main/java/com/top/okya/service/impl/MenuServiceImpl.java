package com.top.okya.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.top.okya.dao.AsPermissionMapper;
import com.top.okya.dao.VtRolePermissionMapper;
import com.top.okya.pojo.AsPermission;
import com.top.okya.service.MenuService;
import com.top.okya.system.redis.RedisUtil;
import com.top.okya.util.elHelp.ElMenuBean;
import com.top.okya.util.elHelp.ElTreeBean;
import com.top.okya.util.elHelp.FormatElData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author: maojiaqi
 * @Date: 2020/4/9 16:11
 * @describe：
 */
@Service("MenuService")
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private VtRolePermissionMapper vtRolePermissionMapper;
    @Autowired
    private AsPermissionMapper asPermissionMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getMenuButtonData(String roleCode, String type, String setYear) {
        if(redisTemplate.hasKey("RoleToComponnet-" + roleCode + '-' + type + '-' + setYear)){
            log.info("-------初始化登录用户菜单/按钮完成(redis)--------");
            return redisTemplate.opsForValue().get("RoleToComponnet-" + roleCode + '-' + type + '-' + setYear).toString();
        } else {
            if (type.equals("menu")) {
                List<ElMenuBean> elMenuBeans = FormatElData.formatMenu(vtRolePermissionMapper.getMenuButton(roleCode, type, setYear), "");
                redisTemplate.opsForValue().set("RoleToComponnet-" + roleCode + '-' + type + '-' + setYear, JSONArray.toJSONString(elMenuBeans));
                log.info("-------初始化登录用户菜单完成(DB)--------");
                return JSONArray.toJSONString(elMenuBeans);
            } else {
                List<ElMenuBean> vtRolePermissions = vtRolePermissionMapper.getMenuButton(roleCode, type, setYear);
                redisTemplate.opsForValue().set("RoleToComponnet-" + roleCode + '-' + type + '-' + setYear, JSONArray.toJSONString(vtRolePermissions));
                log.info("-------初始化登录用户按钮完成(DB)--------");
                return JSONArray.toJSONString(vtRolePermissions);
            }
        }
    }

    @Override
    public List<ElMenuBean> getCompos(String compoName, String sortProp, String sortOrder, String setYear) {

        log.info("-------获取系统部件--------");
        return FormatElData.formatMenu(asPermissionMapper.getCompos(setYear, compoName, sortProp, sortOrder), "");
    }

    @Override
    public List<ElTreeBean> getParentCompos(String setYear) {
        log.info("-------获取父级部件--------");
        return FormatElData.formatTree(asPermissionMapper.getParentCompos(setYear), "");
    }

    @Override
    public void saveCompo(AsPermission asPermission) {
        log.info("-------新增部件--------");
        asPermission.setGuid(UUID.randomUUID().toString());
        if (asPermission.getParentId() == null || asPermission.getParentId() == "") {
            asPermission.setParentId("0");
        }
        asPermissionMapper.saveCompo(asPermission);
    }

    @Override
    public void updateCompo(AsPermission asPermission) {
        log.info("-------修改部件--------");
        if (asPermission.getParentId() == null || asPermission.getParentId() == "") {
            asPermission.setParentId("0");
        }
        asPermissionMapper.updateCompo(asPermission);
        if(asPermission.getCompoType().equals("button")){
            Set<String> keys = redisTemplate.keys("RoleToComponnet-*-button-*");
            if (CollectionUtils.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } else {
            Set<String> keys = redisTemplate.keys("RoleToComponnet-*-menu-*");
            if (CollectionUtils.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        }
    }

    @Override
    public void deleteCompo(List<AsPermission> parseArray, String setYear) {
        log.info("删除部件信息");
        asPermissionMapper.deleteCompo(parseArray, setYear);
        Set<String> keys = redisTemplate.keys("RoleToComponnet-*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public List<ElTreeBean> getAllCompos(String setYear) {
        log.info("-------获取所有部件--------");
        return FormatElData.formatTree(asPermissionMapper.getAllCompos(setYear), "");
    }

    @Override
    public String[] getComposByRole(String roleCode, String setYear) {
        log.info("-------获取指定角色授权的部件--------");
        return vtRolePermissionMapper.getComposByRole(roleCode, setYear);
    }
}
