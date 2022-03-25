package com.top.okya.dao;

import com.top.okya.pojo.AsUser;
import com.top.okya.pojo.AsUserCompany;
import com.top.okya.pojo.AsUserRole;
import com.top.okya.pojo.VtUserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AsUserMapper {

    int deleteUser(@Param("datas") List<VtUserInfo> datas, @Param("setYear") String setYear);
    int deleteUserRole(@Param("datas") List<VtUserInfo> datas, @Param("setYear") String setYear);
    int deleteUserCompany(@Param("datas") List<VtUserInfo> datas, @Param("setYear") String setYear);

    int saveUser(@Param("asUser") AsUser asUser);
    int saveUserRole(@Param("asUserRole") AsUserRole asUserRole);
    int saveUserCompany(@Param("asUserCompany") AsUserCompany asUserCompany);

    int updateUser(@Param("asUser") AsUser asUser);
    int updateUserRole(@Param("asUserRole") AsUserRole asUserRole);
    int updateUserCompany(@Param("asUserCompany") AsUserCompany asUserCompany);

    boolean editPass(@Param("id") String id, @Param("pass") String pass, @Param("oldPass") String oldPass);
}