package com.top.okya.dao;

import com.top.okya.pojo.VtUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VtUserInfoMapper {
    List<VtUserInfo> findPage(@Param("setYear") String setYear, @Param("userName") String userName, @Param("roleCode") String roleCode, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);
}