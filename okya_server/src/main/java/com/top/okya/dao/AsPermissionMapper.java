package com.top.okya.dao;

import com.top.okya.pojo.AsPermission;
import com.top.okya.util.elHelp.ElMenuBean;
import com.top.okya.util.elHelp.ElTreeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AsPermissionMapper {
    List<ElMenuBean> getCompos(@Param("setYear") String setYear, @Param("compoName") String compoName, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);
    List<ElTreeBean> getParentCompos(@Param("setYear") String setYear);

    int saveCompo(@Param("asPermission") AsPermission asPermission);

    int updateCompo(@Param("asPermission") AsPermission asPermission);

    int deleteCompo(@Param("asPermissions") List<AsPermission> parseArray, @Param("setYear") String setYear);

    List<ElTreeBean> getAllCompos(@Param("setYear") String setYear);
}