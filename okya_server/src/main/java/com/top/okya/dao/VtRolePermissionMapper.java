package com.top.okya.dao;

import com.top.okya.util.elHelp.ElMenuBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VtRolePermissionMapper {
    List<ElMenuBean> getMenuButton(@Param("roleCode") String roleCode, @Param("type") String type, @Param("setYear") String setYear);

    String[] getComposByRole(@Param("roleCode") String roleCode, @Param("setYear") String setYear);
}