package com.top.okya.service;

import com.top.okya.pojo.AsForm;
import com.top.okya.pojo.AsFormSearchBusiness;
import com.top.okya.pojo.AsTable;
import com.top.okya.pojo.AsTableColumns;

import java.net.SocketException;
import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/28 20:05
 * @describe：
 */

public interface FormService {
    String getRadioCheckBoxData(String setYear, String datasource);

    String getSelectorData(String setYear, String datasource, String conditions, String affectField, String relation);

    String getSelectTreeData(String setYear, String datasource, String conditions, String affectField, String relation);

    String getDatasourceField(String datasource);

    int saveFormTempLet(String formJson);

    List<AsForm> findFormTemplet(String formName, String sortProp, String sortOrder);

    int deleteFormTempLet(List<AsForm> asForms);

    List<Map<String, String>> getFormTemplet();

    int saveFormDataSource(String formId, String hasSetYear, String mainTableName, String childTableNames, String childTableColumns, String mainTableColumns, String viewSql);

    List<Map<String, String>> findFormDataSource(String formId, String tableName, String sortProp, String sortOrder);

    void createTable(String formId, String hasSetYear, String mainTableName, String childTableNames, String childTableColumns, String mainTableColumns, String viewSql);

    void dropTable(String mainTableName, String childTableNames);

    String getFormDataSource(String formId);

    List<String> getOldFormTables(String formId);

    void clearTableInfo(List<String> oldTables);

    void dropOldTable(List<String> oldTables);

    String getFormBusinessDataSource(String formId);

    List<Map<String, String>> getFormTempletForBusiness();

    int saveFormBusiness(String formId, String tableId, String dataSource, String maxHeight, String size, String showRowNum, String showPagination, String showSum, String showOperation, String permsNew, String permsEdit, String permsDelete, String permsExport, String columnInfos, String searchInfos);

    List<Map<String, String>> findFormBusiness(String formId, String sortProp, String sortOrder);

    String getSelectFormBusiness(String formId);

    int updateFormBusiness(String formId, String tableId, String dataSource, String maxHeight, String size, String showRowNum, String showPagination, String showSum, String showOperation, String permsNew, String permsEdit, String permsDelete, String permsExport, String columnInfos, String searchInfos);

    int deleteFormBusiness(List<String> formIds);

    List<Map<String, String>> getPageButton(String formId);

    List<AsFormSearchBusiness> getPageSearchField(String formId);

    List<AsTable> getPageTable(String formId);

    List<AsTableColumns> getPageTableColumnsList(String formId);

    String getFormTempletByFormId(String formId);

    void saveDynamicFormData(Map<String, String> parameterNames) throws SocketException, Exception;

    String getDynamicFormData(String formId, String guid) throws Exception;

    void deleteDynamicFormData(String formId, List<String> guids, String fileCompo) throws Exception;

    String needYear(String formId);
}
