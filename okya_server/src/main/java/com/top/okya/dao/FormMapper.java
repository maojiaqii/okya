package com.top.okya.dao;

import com.top.okya.pojo.AsForm;
import com.top.okya.pojo.AsFormMainTable;
import com.top.okya.pojo.AsFormMainTableColums;
import com.top.okya.pojo.AsFormSearchBusiness;
import com.top.okya.pojo.AsFormTableBusiness;
import com.top.okya.pojo.AsTable;
import com.top.okya.pojo.AsTableColumns;
import com.top.okya.util.elHelp.ElTreeBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 表单设计器数据库访问层
 *
 * @author maojiaqi
 * @since 2020-05-06 13:14:21
 */
@Mapper
public interface FormMapper {

    /**
     * 单选框数据源
     *
     * @return 对象列表
     */
    List<Map<String, String>> getRadioCheckBoxData(@Param("tableName") String tableName, @Param("setYear") String setYear);

    /**
     * 下拉选择器数据源
     *
     * @return 对象列表
     */
    List<Map<String, String>> getSelectorData(@Param("tableName") String datasource, @Param("setYear") String setYear, @Param("conditions") List<String> conditions, @Param("affectField") String affectField, @Param("relation") String relation);

    /**
     * 下拉树数据源
     *
     * @return 对象列表
     */
    List<ElTreeBean> getSelectTreeData(@Param("tableName") String datasource, @Param("setYear") String setYear, @Param("conditions") List<String> conditions, @Param("affectField") String affectField, @Param("relation") String relation);

    /**
     * 数据源字段
     *
     * @return 对象列表
     */
    List<Map<String, String>> getDatasourceField(@Param("datasource") String datasource);

    /**
     * 保存表单模板
     *
     * @return 插入记录数
     */
    int saveFormTempLet(@Param("asForm") AsForm asForm);

    /**
     * 查询表单模板
     */
    List<AsForm> findFormTemplet(@Param("formName") String formName, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);

    /**
     * 查询表单模板(下拉)
     */
    List<Map<String, String>> findFormTempletSelector();

    /**
     * 删除表单模板
     *
     * @return 插入记录数
     */
    int deleteFormTempLet(@Param("asForms") List<AsForm> asForms);

    /**
     * 保存表单数据源
     *
     * @param asFormMainTableList
     * @return 插入记录数
     */
    int saveFormTable(@Param("asFormMainTableList") List<AsFormMainTable> asFormMainTableList);

    /**
     * 保存表单数据源
     *
     * @return 插入记录数
     */
    int saveFormColumns(@Param("asFormMainTableColumsList") List<AsFormMainTableColums> asFormMainTableColumsList);

    /**
     * 动态建表单主表
     */
    void createTable(@Param("mainTableName") String mainTableName, @Param("mainTableColumns") String mainTableColumns, @Param("constrains") String constrains);

    /**
     * 动态建表单主表
     */
    void createView(@Param("viewSql") String viewSql);

    /**
     * 查询表单数据源
     */
    List<Map<String, String>> findFormDataSource(@Param("formId") String formId, @Param("tableName") String tableName, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);

    /**
     * 删除物理表
     */
    void dropTable(@Param("tableName") String tableName);

    /**
     * 获取表单、物理表关系
     */
    List<AsFormMainTable> getTableInfo(@Param("formId") String formId);

    /**
     * 获取表单、物理表关系
     */
    List<AsFormMainTableColums> getTableColumns(@Param("formId") String formId);

    /**
     * 获取表单关联的物理表
     */
    List<String> getOldFormTables(@Param("formId") String formId);

    /**
     * 删除表单表名对应关系
     *
     * @return 插入记录数
     */
    int deleteFormTable(@Param("oldTables") List<String> oldTables);

    /**
     * 删除表单表列对应关系
     *
     * @return 插入记录数
     */
    int deleteFormColumns(@Param("oldTables") List<String> oldTables);

    /**
     * 根据formId获取对应业务数据的表名
     *
     * @return 业务数据表名
     */
    String getFormBusinessTableName(@Param("formId") String formId);

    /**
     * 查询表单模板(下拉)
     */
    List<Map<String, String>> getFormTempletForBusiness();

    /**
     * 保存表单业务信息
     */
    int saveFormTableBusiness(@Param("asFormTableBusiness") AsFormTableBusiness asFormTableBusiness);

    int saveTable(@Param("asTable") AsTable asTable);

    int saveTableColumns(@Param("asTableColumns") List<Map<String, Object>> asTableColumns, @Param("tableId") String tableId);

    /**
     * 查询表单业务
     */
    List<Map<String, String>> findFormBusiness(@Param("formId") String formId, @Param("sortProp") String sortProp, @Param("sortOrder") String sortOrder);

    /**
     * 根据表单id获取对应业务信息
     */
    List<AsTable> getSelectFormTableByFormId(@Param("formIds") List<String> formId);

    List<AsTableColumns> getSelectFormTableColumnsByFormId(@Param("formId") String formId);

    /**
     * 修改表单业务信息
     */
    int updateTable(@Param("asTable") AsTable asTable);

    int deleteTableColumns(@Param("tableIds") List<String> tableId);

    int deleteTables(@Param("tableIds") List<String> tableIds);

    int deleteFormBusiness(@Param("tableIds") List<String> tableIds);

    int saveFormSearchBusiness(@Param("asFormSearchBusiness") List<Map<String, Object>> asFormSearchBusiness, @Param("formId") String formId, @Param("tableId") String tableId);

    /*
     * 根据表单id获取查询字段
     * */
    List<AsFormSearchBusiness> getSelectFormSearchByFormId(@Param("formId") String formId);

    int deleteFormSearch(@Param("tableIds") List<String> tableIds);

    List<Map<String, String>> getPageButton(@Param("formId") String formId);

    String getFormTempletByFormId(@Param("formId") String formId);

    void insertIntoTable(@Param("tableName") String tableName, @Param("col") String col, @Param("val") String val);

    List<Map<String, String>> getSelectRecord(@Param("tableName") String tableName, @Param("field") String field, @Param("guid") List<String> guid);

    void deleteSelectRecord(@Param("tableName") String tableName, @Param("field") String field, @Param("guid") List<String> guid);

    String needYear(String formId);
}