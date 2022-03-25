package com.top.okya.dao;

import com.top.okya.pojo.VtUserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VtUserLoginMapper {
    List<VtUserLogin> userLogin(@Param("userCode") String userCode, @Param("setYear") String setYear);
}