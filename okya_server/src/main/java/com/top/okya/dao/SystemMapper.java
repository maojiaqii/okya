package com.top.okya.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/8/28 15:49
 * @describe：
 */
@Mapper
public interface SystemMapper {
    int save(@Param("sysTitle") String sysTitle, @Param("sysIcon") String sysIcon);

    Map<String, String> getSetup();
}
