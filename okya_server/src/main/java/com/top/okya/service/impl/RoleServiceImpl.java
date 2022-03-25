package com.top.okya.service.impl;

import com.top.okya.dao.AsRoleMapper;
import com.top.okya.pojo.AsRole;
import com.top.okya.pojo.AsRolePermission;
import com.top.okya.service.RoleService;
import com.top.okya.util.common.HumpLineTrans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:29
 * @describe：
 */
@Slf4j
@Service("RoleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    AsRoleMapper asRoleMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<AsRole> getRoles(String setYear) {
        log.info("获取角色下拉菜单数据");
        return asRoleMapper.getRoles(setYear);
    }

    @Override
    public List findPage(String roleCode, String sortProp, String sortOrder, String setYear) {
        log.info("分页查询角色信息");
        return asRoleMapper.findPage(setYear, roleCode, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc");
    }

    @Override
    public int saveRole(AsRole asRole) {
        log.info("新增角色信息");
        asRole.setGuid(UUID.randomUUID().toString());
        return asRoleMapper.saveRole(asRole);
    }

    @Override
    public int updateRole(AsRole asRole) {
        log.info("修改角色信息");
        return asRoleMapper.updateRole(asRole);
    }

    @Override
    public int deleteRole(List<AsRole> parseArray, String setYear) {
        log.info("删除角色信息");
        return asRoleMapper.deleteRole(parseArray, setYear);
    }

    @Override
    @Transactional
    public void setRolePermissions(List<String> permission, String roleCode, String setYear) {
        log.info("更新角色部件权限");
        int a = asRoleMapper.deleteRolePermissions(roleCode, setYear);
        if (permission != null && permission.size() > 0) {
            List<AsRolePermission> asRolePermissions = new ArrayList<>();
            for (String compoId : permission) {
                asRolePermissions.add(new AsRolePermission(UUID.randomUUID().toString(), roleCode, compoId, setYear));
            }
            asRoleMapper.saveRolePermissions(asRolePermissions);
        }
        redisTemplate.delete("RoleToComponnet-" + roleCode + "-*-" + setYear);
    }
}
