package com.top.okya.service;

import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/26 20:05
 * @describe： 通用业务层
 */
public interface CommonService {

    /**
     * @title
     * @description 通用分页查询接口
     * @author maojq
     * @param tableName 数据库表名称
     * @param searchProperties 查询条件
     * @param sortProp 排序对象
     * @param sortOrder 排序方式
     * @param setYear 年度
     * @param needYear 是否需要年度字段
     * @updateTime 2022/3/7 15:47
     * @return List
     * @throws
     */
    List findPage(String tableName, String searchProperties, String sortProp, String sortOrder, String setYear, String needYear);

    /**
     * @title
     * @description 通用下拉框数据源接口
     * @author maojq
     * @param tableName 数据库表名称
     * @param searchProperties 查询条件
     * @param setYear 年度
     * @param needYear 是否需要年度字段
     * @updateTime 2022/3/7 15:47
     * @return List
     * @throws
     */
    List getSelectorData(String tableName, String searchProperties, String setYear, String needYear);

    /**
     * @title
     * @description 通用保存接口
     * @author maojq
     * @param jsonToMap 待保存数据
     * @param tableName 数据库表名称
     * @param setYear 年度
     * @param needYear 是否需要年度字段
     * @updateTime 2022/3/7 15:47
     * @return int
     * @throws
     */
    int save(Map<String, Object> jsonToMap, String tableName, String setYear, String needYear);

    /**
     * @title
     * @description 通用下拉树数据源接口
     * @author maojq
     * @param tableName 数据库表名称
     * @param searchProperties 查询条件
     * @param setYear 年度
     * @updateTime 2022/3/7 15:47
     * @return String
     * @throws
     */
    String getSelectorTreeData(String tableName, String searchProperties, String setYear);

    /**
     * @title
     * @description 通用更新接口
     * @author maojq
     * @param jsonToMap 待更新数据
     * @param tableName 数据库表名称
     * @param setYear 年度
     * @param needYear 是否需要年度字段
     * @updateTime 2022/3/7 15:47
     * @return int
     * @throws
     */
    int updateCommon(Map<String, Object> jsonToMap, String tableName, String setYear, String needYear);

    /**
     * @title
     * @description 通用删除接口
     * @author maojq
     * @param list 待删除的数据集合
     * @param tableName 数据库表名称
     * @param setYear 年度
     * @updateTime 2022/3/7 15:47
     * @return int
     * @throws
     */
    int deletCommon(List list, String tableName, String setYear);

    /**
     * @title
     * @description 获取数据库列名接口
     * @author maojq
     * @param tableName 数据库表名称
     * @updateTime 2022/3/7 15:47
     * @return int
     * @throws
     */
    List<Map<String, String>> getTableCols(String tableName);

}
