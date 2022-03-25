package com.top.okya.service;

import com.top.okya.pojo.AsRole;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/27 20:28
 * @describe：
 */
public interface RoleService {

    List<AsRole> getRoles(String setYear);

    List findPage(String roleCode, String sortProp, String sortOrder, String setYear);

    int saveRole(AsRole asRole);

    int updateRole(AsRole asRole);

    int deleteRole(List<AsRole> parseArray, String setYear);

    void setRolePermissions(List<String> permission, String roleCode, String setYear);
}
