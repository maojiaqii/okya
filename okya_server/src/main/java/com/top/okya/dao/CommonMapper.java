package com.top.okya.dao;

import com.top.okya.util.elHelp.ElTreeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
    List<Map<String, String>> findPage(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("jsonToMap") Map<String, Object> jsonToMap, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder, @Param("needYear") String needYear);

    List<Map<String, String>> getSelectorData(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("jsonToMap") Map<String, Object> jsonToMap, @Param("needYear") String needYear);

    int saveData(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("jsonToMap") Map<String, Object> jsonToMap, @Param("needYear") String needYear);

    List<ElTreeBean> getSelectorTreeData(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("jsonToMap") Map<String, Object> jsonToMap);

    int updateCommon(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("jsonToMap") Map<String, Object> jsonToMap, @Param("needYear") String needYear);

    int deletCommon(@Param("tableName") String tableName, @Param("setYear") String setYear, @Param("list") List list);

    /**
     * 根据表名的获取列信息
     *
     * @return 业务数据表名
     */
    List<Map<String, String>> getTableCols(@Param("tableName") String tableName);
}
