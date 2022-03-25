package com.top.okya.dao;

import com.top.okya.pojo.AsRole;
import com.top.okya.pojo.AsRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AsRoleMapper {

    List<AsRole> getRoles(@Param("setYear") String setYear);

    List<AsRole> findPage(@Param("setYear") String setYear, @Param("roleCode") String roleCode, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);

    int saveRole(@Param("asRole") AsRole asRole);

    int updateRole(@Param("asRole") AsRole asRole);

    int deleteRole(@Param("asRoles") List<AsRole> parseArray, @Param("setYear") String setYear);

    int deleteRolePermissions(@Param("roleCode") String roleCode, @Param("setYear") String setYear);

    int saveRolePermissions(@Param("asRolePermissions") List<AsRolePermission> asRolePermissions);
}