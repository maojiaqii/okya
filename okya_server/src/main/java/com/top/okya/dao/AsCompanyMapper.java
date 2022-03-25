package com.top.okya.dao;

import com.top.okya.util.elHelp.ElTreeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AsCompanyMapper {
    List<ElTreeBean> getCompanyTree(@Param("setYear") String setYear);
}