package com.top.okya.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.top.okya.dao.CommonMapper;
import com.top.okya.dao.FormMapper;
import com.top.okya.pojo.AsForm;
import com.top.okya.pojo.AsFormMainTable;
import com.top.okya.pojo.AsFormMainTableColums;
import com.top.okya.pojo.AsFormSearchBusiness;
import com.top.okya.pojo.AsFormTableBusiness;
import com.top.okya.pojo.AsTable;
import com.top.okya.pojo.AsTableColumns;
import com.top.okya.service.FormService;
import com.top.okya.system.config.ServerConfig;
import com.top.okya.util.common.HumpLineTrans;
import com.top.okya.util.elHelp.ElTreeBean;
import com.top.okya.util.elHelp.FormatElData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author: maojiaqi
 * @Date: 2020/5/6 11:22
 * @describe：
 */

@Service("FormService")
@Slf4j
public class FormServiceImpl implements FormService {

    private static final String fileAddress = "/file/business/";

    @Autowired
    FormMapper formMapper;
    @Autowired
    CommonMapper commonMapper;

    @Override
    public String getRadioCheckBoxData(String setYear, String datasource) {
        List<Map<String, String>> datas = formMapper.getRadioCheckBoxData(datasource, setYear);
        log.info("单选、多选数据源取数完成");
        return JSON.toJSONString(datas);
    }

    @Override
    public String getSelectorData(String setYear, String datasource, String conditions, String affectField, String relation) {
        List<Map<String, String>> datas = formMapper.getSelectorData(datasource, setYear, JSONObject.parseArray(conditions, String.class), affectField, relation);
        log.info("下拉选择器数据源取数完成");
        return JSON.toJSONString(datas);
    }

    @Override
    public String getSelectTreeData(String setYear, String datasource, String conditions, String affectField, String relation) {
        List<ElTreeBean> datas = FormatElData.formatTree(formMapper.getSelectTreeData(datasource, setYear, JSONObject.parseArray(conditions, String.class), affectField, relation), "");
        log.info("下拉树数据源取数完成");
        return JSON.toJSONString(datas);
    }

    @Override
    public String getDatasourceField(String datasource) {
        List<Map<String, String>> datas = formMapper.getDatasourceField(datasource.toUpperCase());
        log.info("数据源字段取数完成");
        return JSON.toJSONString(datas);
    }

    @Override
    @Transactional
    public int saveFormTempLet(String formJson) {
        JSONObject jsonObject = JSONObject.parseObject(formJson);
        AsForm asForm = new AsForm(UUID.randomUUID().toString(), jsonObject.getString("formId"), jsonObject.getString("formName"), formJson);
        formMapper.deleteFormTempLet(Arrays.asList(asForm));
        return formMapper.saveFormTempLet(asForm);
    }

    @Override
    public List<AsForm> findFormTemplet(String formName, String sortProp, String sortOrder) {
        log.info("分页查询表单模板");
        return formMapper.findFormTemplet(formName, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc");
    }

    @Override
    public int deleteFormTempLet(List<AsForm> asForms) {
        return formMapper.deleteFormTempLet(asForms);
    }

    @Override
    public List<Map<String, String>> getFormTemplet() {
        log.info("表单模板下拉框取数开始");
        return formMapper.findFormTempletSelector();
    }

    @Override
    public List<Map<String, String>> getFormTempletForBusiness() {
        log.info("表单模板下拉框取数开始");
        return formMapper.getFormTempletForBusiness();
    }

    @Override
    @Transactional
    public int saveFormDataSource(String formId, String hasSetYear, String mainTableName, String childTableNames, String childTableColumns, String mainTableColumns, String viewSql) {

        log.info("开始初始化表单数据源信息");
        List<AsFormMainTable> asFormMainTableList = new ArrayList<>();
        List<AsFormMainTableColums> asFormMainTableColumsList = new ArrayList<>();
        if (!childTableNames.equals("{}")) {
            Map<String, Object> childTableNamesMap = JSONObject.parseObject(childTableNames);
            Map<String, Object> childTableColumnsMap = JSONObject.parseObject(childTableColumns);
            childTableNamesMap.forEach((k, v) -> {
                AsFormMainTable asFormMainTable = new AsFormMainTable();
                asFormMainTable.setGuid(UUID.randomUUID().toString());
                asFormMainTable.setFormId(k);
                asFormMainTable.setTableName(v.toString());
                asFormMainTable.setIsMainTable("1");
                asFormMainTable.setMainFormId(formId);
                asFormMainTableList.add(asFormMainTable);
            });
            childTableColumnsMap.forEach((k, v) -> {
                List<AsFormMainTableColums> childTableColumsList = JSONObject.parseArray(v.toString(), AsFormMainTableColums.class);
                childTableColumsList.forEach(p -> {
                    p.setGuid(UUID.randomUUID().toString());
                    p.setFormId(k);
                    p.setTableName(childTableNamesMap.get(k).toString());
                });
                asFormMainTableColumsList.addAll(childTableColumsList);
            });
        }

        List<AsFormMainTableColums> mainTableColumsList = JSONObject.parseArray(mainTableColumns, AsFormMainTableColums.class);
        mainTableColumsList.forEach(p -> {
            p.setGuid(UUID.randomUUID().toString());
            p.setFormId(formId);
            p.setTableName(mainTableName);
        });
        asFormMainTableColumsList.addAll(mainTableColumsList);

        AsFormMainTable asFormMainTable = new AsFormMainTable();
        asFormMainTable.setGuid(UUID.randomUUID().toString());
        asFormMainTable.setFormId(formId);
        asFormMainTable.setTableName(mainTableName);
        asFormMainTable.setIsMainTable("0");
        asFormMainTable.setHasSetYear(hasSetYear);
        asFormMainTable.setMainFormId(formId);
        asFormMainTable.setViewSql(viewSql);
        asFormMainTableList.add(asFormMainTable);
        log.info("初始化表单数据源信息完成");
        return formMapper.saveFormTable(asFormMainTableList) + formMapper.saveFormColumns(asFormMainTableColumsList);
    }

    @Override
    public List<Map<String, String>> findFormDataSource(String formId, String tableName, String sortProp, String sortOrder) {
        log.info("分页查询表单数据源");
        return formMapper.findFormDataSource(formId, tableName, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc");
    }

    @Override
    public void createTable(String formId, String hasSetYear, String mainTableName, String childTableNames, String childTableColumns, String mainTableColumns, String viewSql) {
        if (!childTableNames.equals("{}")) {
            log.info("开始创建副表");
            Map<String, Object> childTableNamesMap = JSONObject.parseObject(childTableNames);
            Map<String, Object> childTableColumnsMap = JSONObject.parseObject(childTableColumns);
            childTableColumnsMap.forEach((k, v) -> {
                log.info("副表：" + childTableNamesMap.get(k).toString() + "开始创建");
                AtomicReference<String> createMainTable = new AtomicReference<>("");
                AtomicReference<String> constrains = new AtomicReference<>("");
                List<AsFormMainTableColums> childTableColumsList = JSONObject.parseArray(v.toString(), AsFormMainTableColums.class);
                childTableColumsList.forEach(p -> {
                    // oracle_bak: createMainTable.set(createMainTable.get() + p.getTableField() + " " + (p.getTableFieldType().equals("clob") ? "clob" : "varchar2(200)") + " " + (p.getIsNull().equals("Y") ? "" : "not null") + ",");
                    createMainTable.set(createMainTable.get() + p.getTableField() + " " + (p.getTableFieldType().equals("clob") ? "longtext" : "varchar(1000)") + " " + (p.getIsNull().equals("Y") ? "" : "not null") + ",");
                    if (p.getIsUnique().equals("Y")) {
                        constrains.set(constrains.get() + p.getTableField() + ",");
                    }
                });
                if (hasSetYear.equals("Y")) {
                    // oracle_bak: createMainTable.set(createMainTable.get() + "set_year varchar2(4) NOT NULL,");
                    createMainTable.set(createMainTable.get() + "set_year varchar(4) NOT NULL,");
                }
                formMapper.createTable(childTableNamesMap.get(k).toString(), createMainTable.get(), constrains.get().length() > 0 ? constrains.get().substring(0, constrains.get().length() - 1) : "");
                log.info("副表：" + childTableNamesMap.get(k).toString() + "创建完成");
            });
            log.info("副表创建完成");
        }

        log.info("主表开始创建，表名：" + mainTableName);
        List<AsFormMainTableColums> mainTableColumsList = JSONObject.parseArray(mainTableColumns, AsFormMainTableColums.class);
        AtomicReference<String> createMainTable = new AtomicReference<>("");
        AtomicReference<String> constrains = new AtomicReference<>("");
        mainTableColumsList.forEach(p -> {
            // oracle_bak: createMainTable.set(createMainTable.get() + p.getTableField() + " " + (p.getTableFieldType().equals("clob") ? "clob" : "varchar2(200)") + " " + (p.getIsNull().equals("Y") ? "" : "not null") + ",");
            createMainTable.set(createMainTable.get() + p.getTableField() + " " + (p.getTableFieldType().equals("clob") ? "longtext" : "varchar(1000)") + " " + (p.getIsNull().equals("Y") ? "" : "not null") + ",");
            if (p.getIsUnique().equals("Y")) {
                constrains.set(constrains.get() + p.getTableField() + ",");
            }
        });
        if (hasSetYear.equals("Y")) {
            // oracle_bak: createMainTable.set(createMainTable.get() + "set_year varchar2(4) NOT NULL,");
            createMainTable.set(createMainTable.get() + "set_year varchar(4) NOT NULL,");
        }
        formMapper.createTable(mainTableName, createMainTable.get(), constrains.get().length() > 0 ? constrains.get().substring(0, constrains.get().length() - 1) : "");
        log.info("主表创建完成，表名：" + mainTableName);

        log.info("开始创建视图：" + viewSql);
        // 检查视图是否加入主表GUID字段，如未找到，强制加入T.GUID，可能引起无法找到T的错误，推荐前台设计视图语句时，手动加入主表GUID字段
        StringBuilder sb = new StringBuilder(viewSql.toUpperCase());
        //if (sb.indexOf(" GUID") > 0 || sb.indexOf(".GUID") > 0) {
            formMapper.createView(sb.toString());
        //} else {
        //    formMapper.createView(sb.insert(sb.indexOf("SELECT ") + 7, "T.GUID, ").toString());
        //}
        log.info("创建视图完成");
    }

    @Override
    public void dropTable(String mainTableName, String childTableNames) {
        if (!childTableNames.equals("{}")) {
            log.info("开始删除副表");
            Map<String, Object> childTableNamesMap = JSONObject.parseObject(childTableNames);
            childTableNamesMap.forEach((k, v) -> {
                formMapper.dropTable(v.toString());
                log.info("副表：" + v.toString() + "删除完成");
            });
            log.info("所有副表删除完成");
        }

        log.info("开始删除主表，表名：" + mainTableName);
        formMapper.dropTable(mainTableName);
        log.info("主表删除完成，表名：" + mainTableName);
    }

    @Override
    public String getFormDataSource(String formId) {

        AtomicReference<JSONObject> jsonObject = new AtomicReference<>();

        List<AsFormMainTable> asFormMainTableList = formMapper.getTableInfo(formId);
        if (asFormMainTableList != null && asFormMainTableList.size() > 0) {
            asFormMainTableList.forEach(p -> {
                if (p.getIsMainTable().equals("0")) {
                    jsonObject.set((JSONObject) JSON.toJSON(p));
                    jsonObject.get().put("mainTableName", p.getTableName());
                    jsonObject.get().put("viewSql", p.getViewSql());
                }
            });
            JSONObject jsb1 = new JSONObject();
            JSONObject jsb2 = new JSONObject();
            asFormMainTableList.forEach(p -> {
                if (p.getIsMainTable().equals("1")) {
                    jsb1.put(p.getFormId(), p.getTableName());
                    List<AsFormMainTableColums> asFormMainTableColumsList = formMapper.getTableColumns(p.getFormId());
                    if (asFormMainTableColumsList != null && asFormMainTableColumsList.size() > 0) {
                        jsb2.put(p.getFormId(), JSON.toJSON(asFormMainTableColumsList));
                    }
                }
            });
            jsonObject.get().put("childTableNames", jsb1);
            jsonObject.get().put("childTableColumns", jsb2);
            List<AsFormMainTableColums> asFormMainTableColumsList = formMapper.getTableColumns(formId);
            if (asFormMainTableColumsList != null && asFormMainTableColumsList.size() > 0) {
                jsonObject.get().put("mainTableColumns", JSON.toJSON(asFormMainTableColumsList));
            }
        }
        return jsonObject.get().toJSONString();
    }

    @Override
    public List<String> getOldFormTables(String formId) {
        return formMapper.getOldFormTables(formId);
    }

    @Override
    @Transactional
    public void clearTableInfo(List<String> oldTables) {
        formMapper.deleteFormTable(oldTables);
        formMapper.deleteFormColumns(oldTables);
    }

    @Override
    public void dropOldTable(List<String> oldTables) {
        oldTables.forEach(p -> formMapper.dropTable(p));
    }

    @Override
    public String getFormBusinessDataSource(String formId) {
        String tableName = formMapper.getFormBusinessTableName(formId);
        List<Map<String, String>> cols = commonMapper.getTableCols(tableName);
        JSONObject jso = new JSONObject();
        jso.put("dataSource", tableName);
        jso.put("columnIds", JSON.toJSONString(cols));
        return jso.toJSONString();
    }

    @Override
    @Transactional
    public int saveFormBusiness(String formId, String tableId, String dataSource, String maxHeight, String size, String showRowNum, String showPagination, String showSum, String showOperation, String permsNew, String permsEdit, String permsDelete, String permsExport, String columnInfos, String searchInfos) {

        AsFormTableBusiness asFormTableBusiness = new AsFormTableBusiness(UUID.randomUUID().toString(), formId, tableId);
        AsTable asTable = new AsTable(UUID.randomUUID().toString(), tableId, "", dataSource, size, maxHeight, showRowNum, showPagination, showSum, showOperation, permsNew, permsEdit, permsDelete, permsExport);
        List<Map<String, Object>> asTableColumns = new ArrayList<>();
        JSONArray columnInfoArrays = JSONObject.parseArray(columnInfos);
        if (columnInfoArrays != null && columnInfoArrays.size() > 0) {
            columnInfoArrays.forEach(p -> {
                Map<String, Object> resultStr = JSONObject.parseObject(p.toString());
                asTableColumns.add(resultStr);
            });
        }
        List<Map<String, Object>> asFormSearchBusiness = new ArrayList<>();
        JSONArray searchInfosArrays = JSONObject.parseArray(searchInfos);
        if (searchInfosArrays != null && searchInfosArrays.size() > 0) {
            searchInfosArrays.forEach(p -> {
                Map<String, Object> resultStr = JSONObject.parseObject(p.toString());
                asFormSearchBusiness.add(resultStr);
            });
        }
        return formMapper.saveFormTableBusiness(asFormTableBusiness) + formMapper.saveTable(asTable) + formMapper.saveTableColumns(asTableColumns, tableId) + formMapper.saveFormSearchBusiness(asFormSearchBusiness, formId, tableId);
    }

    @Override
    public List<Map<String, String>> findFormBusiness(String formId, String sortProp, String sortOrder) {
        log.info("分页查询表单业务");
        return formMapper.findFormBusiness(formId, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc");
    }

    @Override
    public String getSelectFormBusiness(String formId) {

        List<String> formIds = new ArrayList<>();
        formIds.add(formId);
        List<AsTable> asTables = formMapper.getSelectFormTableByFormId(formIds);
        if (asTables != null && asTables.size() == 1) {
            JSONObject jo = (JSONObject) JSON.toJSON(asTables.get(0));
            List<AsTableColumns> asTableColumns = formMapper.getSelectFormTableColumnsByFormId(formId);
            jo.put("columnInfos", JSON.toJSONString(asTableColumns));
            List<AsFormSearchBusiness> asFormSearchBusinesses = formMapper.getSelectFormSearchByFormId(formId);
            jo.put("searchInfos", JSON.toJSONString(asFormSearchBusinesses));
            log.info("获取选中表单业务信息完成" + jo.toJSONString());
            return jo.toJSONString();
        } else {
            log.info("为获取到选中表单业务信息，返回空");
            return "";
        }
    }

    @Override
    public int updateFormBusiness(String formId, String tableId, String dataSource, String maxHeight, String size, String showRowNum, String showPagination, String showSum, String showOperation, String permsNew, String permsEdit, String permsDelete, String permsExport, String columnInfos, String searchInfos) {
        AsTable asTable = new AsTable(UUID.randomUUID().toString(), tableId, "", dataSource, size, maxHeight, showRowNum, showPagination, showSum, showOperation, permsNew, permsEdit, permsDelete, permsExport);
        List<Map<String, Object>> asTableColumns = new ArrayList<>();
        JSONArray columnInfoArrays = JSONObject.parseArray(columnInfos);
        if (columnInfoArrays != null && columnInfoArrays.size() > 0) {
            columnInfoArrays.forEach(p -> {
                Map<String, Object> resultStr = JSONObject.parseObject(p.toString());
                asTableColumns.add(resultStr);
            });
        }
        List<Map<String, Object>> asFormSearchBusiness = new ArrayList<>();
        JSONArray searchInfosArrays = JSONObject.parseArray(searchInfos);
        if (searchInfosArrays != null && searchInfosArrays.size() > 0) {
            searchInfosArrays.forEach(p -> {
                Map<String, Object> resultStr = JSONObject.parseObject(p.toString());
                asFormSearchBusiness.add(resultStr);
            });
        }
        List<String> tableIds = new ArrayList<>();
        tableIds.add(tableId);
        formMapper.deleteTableColumns(tableIds);
        formMapper.deleteFormSearch(tableIds);
        return formMapper.updateTable(asTable) + formMapper.saveTableColumns(asTableColumns, tableId) + formMapper.saveFormSearchBusiness(asFormSearchBusiness, formId, tableId);
    }

    @Override
    public int deleteFormBusiness(List<String> formIds) {
        List<AsTable> asTables = formMapper.getSelectFormTableByFormId(formIds);
        List<String> tableIds = new ArrayList<>();
        asTables.forEach(p -> tableIds.add(p.getTableId()));
        return formMapper.deleteFormSearch(tableIds) + formMapper.deleteTableColumns(tableIds) + formMapper.deleteTables(tableIds) + formMapper.deleteFormBusiness(tableIds);
    }

    @Override
    public List<Map<String, String>> getPageButton(String formId) {
        log.info("获取按钮组");
        return formMapper.getPageButton(formId);
    }

    @Override
    public List<AsFormSearchBusiness> getPageSearchField(String formId) {
        log.info("获取查询字段组");
        return formMapper.getSelectFormSearchByFormId(formId);
    }

    @Override
    public String needYear(String formId) {
        log.info("获取needYear字段");
        return formMapper.needYear(formId);
    }

    @Override
    public List<AsTable> getPageTable(String formId) {
        log.info("获取表格");
        List<String> formIds = new ArrayList<>();
        formIds.add(formId);
        return formMapper.getSelectFormTableByFormId(formIds);
    }

    @Override
    public List<AsTableColumns> getPageTableColumnsList(String formId) {
        log.info("获取表格列");
        return formMapper.getSelectFormTableColumnsByFormId(formId);
    }

    @Override
    public String getFormTempletByFormId(String formId) {
        log.info("获取表单模板");
        return formMapper.getFormTempletByFormId(formId);
    }

    @Override
    @Transactional
    public void saveDynamicFormData(Map<String, String> parameterNames) throws Exception {

        String formId = parameterNames.get("sysFormId");
        String setYear = parameterNames.get("setYear");
        String createUser = parameterNames.get("CREAT_USER");
        JSONArray fileCompos = JSONArray.parseArray(parameterNames.get("fileCompo"));

        List<AsFormMainTable> asFormMainTableList = formMapper.getTableInfo(formId);
        List<AsFormMainTableColums> tableColumns = formMapper.getTableColumns(formId);
        if (asFormMainTableList != null && asFormMainTableList.size() > 0) {
            if (asFormMainTableList.size() == 1) {
                log.info("此表单只有一个主表" + asFormMainTableList.get(0).getTableName() + "，无其他子表");
                // 判断是否是修改（先删除后插入）
                if (parameterNames.get("GUID") != null) {
                    log.info("判断是否是修改（先删除后插入）：【是】");
                    List<String> guids = new ArrayList<>();
                    guids.add(parameterNames.get("GUID"));
                    List<Map<String, String>> mapList = formMapper.getSelectRecord(asFormMainTableList.get(0).getTableName(), "GUID", guids);
                    if (mapList != null && mapList.size() == 1) {
                        Map<String, String> mp = mapList.get(0);
                        if (fileCompos != null && fileCompos.size() > 0) {
                            for (AsFormMainTableColums asFormMainTableColums : tableColumns) {
                                if (fileCompos.contains(asFormMainTableColums.getFormField())) {
                                    if (mp.get(asFormMainTableColums.getTableField().toUpperCase()) != null) {
                                        String fl = mp.get(asFormMainTableColums.getTableField().toUpperCase());
                                        if (parameterNames.get(asFormMainTableColums.getFormField()) != "[]") {
                                            JSONObject jo = (JSONObject) JSONArray.parseArray(parameterNames.get(asFormMainTableColums.getFormField())).get(0);
                                            if (jo.get("data") != null) {
                                                File file = new File(ServerConfig.uploadFolder + "/business/" + fl.substring(fl.lastIndexOf("/")));
                                                if (file.isFile() && file.exists()) {
                                                    log.info("删除文件：" + file.getName());
                                                    file.delete();
                                                }
                                            }
                                        } else {
                                            File file = new File(ServerConfig.uploadFolder + "/business/" + fl.substring(fl.lastIndexOf("/")));
                                            if (file.isFile() && file.exists()) {
                                                log.info("删除文件：" + file.getName());
                                                file.delete();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    formMapper.deleteSelectRecord(asFormMainTableList.get(0).getTableName(), "GUID", guids);
                    log.info("删除修改前的主表数据完成");
                }

                // 开始插入数据
                log.info("开始组装主表的插入语句");
                AtomicReference<String> insertField = new AtomicReference<>("GUID");
                AtomicReference<String> insertValue = new AtomicReference<>("'" + UUID.randomUUID().toString());
                for (AsFormMainTableColums v : tableColumns) {
                    insertField.set(insertField.get() + "," + v.getTableField());
                    if (fileCompos != null && fileCompos.size() > 0 && fileCompos.contains(v.getFormField())) {
                        log.info(parameterNames.get(v.getFormField()));
                        if (parameterNames.get(v.getFormField()) != "[]") {
                            JSONObject jo = (JSONObject) JSONArray.parseArray(parameterNames.get(v.getFormField())).get(0);
                            if (jo.get("data") != null) {
                                String fileName = UUID.randomUUID().toString() + "." + jo.get("name").toString().split("\\.")[1];
                                log.info("客户端文件名：" + jo.get("name") + "在服务端存储的文件名称：" + fileName);
                                insertValue.set(insertValue.get() + "','" + fileAddress + fileName);
                                String[] d = jo.get("data").toString().split("base64,");//将字符串分成数组
                                if (d != null && d.length == 2) {
                                    BASE64Decoder decoder = new BASE64Decoder();
                                    //Base64解码
                                    byte[] b = decoder.decodeBuffer(d[1]);
                                    for (int i = 0; i < b.length; ++i) {
                                        if (b[i] < 0) {
                                            //调整异常数据
                                            b[i] += 256;
                                        }
                                    }
                                    OutputStream out = new FileOutputStream(ServerConfig.uploadFolder + "/business/" + fileName);
                                    out.write(b);
                                    out.flush();
                                    out.close();
                                    log.info("文件保存成功");
                                } else {
                                    throw new Exception("数据不合法，无法生成上传的文件");
                                }
                            } else if (jo.get("url") != null) {
                                insertValue.set(insertValue.get() + "','" + jo.get("url").toString().substring(jo.get("url").toString().indexOf(fileAddress)));
                            }
                        } else {
                            insertValue.set(insertValue.get() + "','");
                        }
                    } else {
                        insertValue.set(insertValue.get() + "','" + (parameterNames.get(v.getFormField()) == null ? "" : parameterNames.get(v.getFormField())));
                    }
                }
                if (asFormMainTableList.get(0).getHasSetYear().equals("Y")) {
                    insertField.set(insertField.get() + ", SET_YEAR");
                    insertValue.set(insertValue.get() + "','" + setYear);
                }
                insertField.set(insertField.get() + ", CREAT_USER");
                insertValue.set(insertValue.get() + "','" + createUser);

                log.info("组装完成，开始入库");
                formMapper.insertIntoTable(asFormMainTableList.get(0).getTableName(), insertField.get(), insertValue.get() + "'");
            } else {
                log.info("此表单有一个主表，且有其他子表");
                // 开始插入数据
                Map<String, Map<String, String>> map1 = new HashMap<>();
                Map<String, List<Map<String, String>>> map2 = new HashMap<>();
                AtomicBoolean hasSetYear = new AtomicBoolean(false);
                AtomicReference<AsFormMainTable> asFormMainTable = new AtomicReference<>();
                for (AsFormMainTable formMainTable : asFormMainTableList) {
                    if (formMainTable.getIsMainTable().equals("0")) {
                        log.info("主表：" + formMainTable.getTableName());
                        // 判断是否是修改（先删除后插入）
                        List<Map<String, String>> mapList = null;
                        if (parameterNames.get("GUID") != null) {
                            log.info("判断是否是修改（先删除后插入）：【是】");
                            List<String> guids = new ArrayList<>();
                            guids.add(parameterNames.get("GUID"));
                            mapList = formMapper.getSelectRecord(formMainTable.getTableName(), "GUID", guids);
                            if (mapList != null && mapList.size() == 1) {
                                Map<String, String> mp = mapList.get(0);
                                if (fileCompos != null && fileCompos.size() > 0) {
                                    for (AsFormMainTableColums asFormMainTableColums : tableColumns) {
                                        if (fileCompos.contains(asFormMainTableColums.getFormField())) {
                                            if (mp.get(asFormMainTableColums.getTableField().toUpperCase()) != null) {
                                                String fl = mp.get(asFormMainTableColums.getTableField().toUpperCase());
                                                if (parameterNames.get(asFormMainTableColums.getFormField()) != "[]") {
                                                    JSONObject jo = (JSONObject) JSONArray.parseArray(parameterNames.get(asFormMainTableColums.getFormField())).get(0);
                                                    if (jo.get("data") != null) {
                                                        File file = new File(ServerConfig.uploadFolder + "/business/" + fl.substring(fl.lastIndexOf("/")));
                                                        if (file.isFile() && file.exists()) {
                                                            log.info("删除文件：" + file.getName());
                                                            file.delete();
                                                        }
                                                    }
                                                } else {
                                                    File file = new File(ServerConfig.uploadFolder + "/business/" + fl.substring(fl.lastIndexOf("/")));
                                                    if (file.isFile() && file.exists()) {
                                                        log.info("删除文件：" + file.getName());
                                                        file.delete();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                formMapper.deleteSelectRecord(formMainTable.getTableName(), "GUID", guids);
                                log.info("删除修改前的主表数据完成");
                            }
                        }

                        // 开始插入数据
                        log.info("开始组装主表的插入语句");
                        hasSetYear.set(formMainTable.getHasSetYear().equals("Y"));
                        AtomicReference<String> insertField = new AtomicReference<>("GUID");
                        AtomicReference<String> insertValue = new AtomicReference<>("'" + UUID.randomUUID().toString());
                        List<Map<String, String>> finalMapList = mapList;
                        for (AsFormMainTableColums t : tableColumns) {// 开始插入数据
                            insertField.set(insertField.get() + "," + t.getTableField());
                            if (t.getJoinTable() != null && !t.getJoinTable().equals("") && asFormMainTableList.stream().anyMatch(task -> task.getTableName().equals(t.getJoinTable()))) {
                                log.info("当前字段：" + t.getTableField().toUpperCase() + "为关联子表字段");
                                // 判断是否是修改（先删除后插入）
                                if (finalMapList != null && finalMapList.size() == 1) {
                                    log.info("判断是否是修改（先删除后插入）：【是】");
                                    log.info("开始删除子表：" + t.getJoinTable() + "目标字段：" + t.getJoinTableColumn() + "修改前的数据");
                                    List<String> guids = new ArrayList<>();
                                    guids.add(finalMapList.get(0).get(t.getTableField().toUpperCase()));
                                    formMapper.deleteSelectRecord(t.getJoinTable(), t.getJoinTableColumn(), guids);
                                    log.info("删除子表：" + t.getJoinTable() + "修改前的数据完成，继续组装主表的插入语句");
                                }

                                String mainId = UUID.randomUUID().toString();
                                insertValue.set(insertValue.get() + "','" + mainId);
                                HashMap<String, String> map3 = new HashMap<>();
                                map3.put(t.getJoinTableColumn(), mainId);
                                map1.put(t.getJoinTable(), map3);
                            } else {
                                if (fileCompos != null && fileCompos.size() > 0 && fileCompos.contains(t.getFormField())) {
                                    log.info(parameterNames.get(t.getFormField()));
                                    if (parameterNames.get(t.getFormField()) != "[]") {
                                        JSONObject jo = (JSONObject) JSONArray.parseArray(parameterNames.get(t.getFormField())).get(0);
                                        if (jo.get("data") != null) {
                                            String fileName = UUID.randomUUID().toString() + "\\." + jo.get("name").toString().split("\\.")[1];
                                            log.info("客户端文件名：" + jo.get("name") + "在服务端存储的文件名称：" + fileName);
                                            insertValue.set(insertValue.get() + "','" + fileAddress + fileName);
                                            String[] d = jo.get("data").toString().split("base64,");//将字符串分成数组
                                            if (d != null && d.length == 2) {
                                                BASE64Decoder decoder = new BASE64Decoder();
                                                //Base64解码
                                                byte[] b = decoder.decodeBuffer(d[1]);
                                                for (int i = 0; i < b.length; ++i) {
                                                    if (b[i] < 0) {
                                                        //调整异常数据
                                                        b[i] += 256;
                                                    }
                                                }
                                                OutputStream out = new FileOutputStream(ServerConfig.uploadFolder + "/business/" + fileName);
                                                out.write(b);
                                                out.flush();
                                                out.close();
                                                log.info("文件保存成功");
                                            } else {
                                                throw new Exception("数据不合法，无法生成上传的文件");
                                            }
                                        } else if (jo.get("url") != null) {
                                            insertValue.set(insertValue.get() + "','" + jo.get("url").toString().substring(jo.get("url").toString().indexOf(fileAddress)));
                                        }
                                    } else {
                                        insertValue.set(insertValue.get() + "','");
                                    }
                                } else {
                                    insertValue.set(insertValue.get() + "','" + (parameterNames.get(t.getFormField()) == null ? "" : parameterNames.get(t.getFormField())));
                                }
                            }
                        }
                        if (hasSetYear.get()) {
                            insertField.set(insertField.get() + ", SET_YEAR");
                            insertValue.set(insertValue.get() + "','" + setYear);
                        }
                        insertField.set(insertField.get() + ", CREAT_USER");
                        insertValue.set(insertValue.get() + "','" + createUser);
                        log.info("组装主表的插入语句完成，开始入库");
                        formMapper.insertIntoTable(formMainTable.getTableName(), insertField.get(), insertValue.get() + "'");
                        asFormMainTable.set(formMainTable);
                    }
                }
                asFormMainTableList.remove(asFormMainTable.get());
                log.info("开始获取子表数据库物理表字段对应前台表单字段关系");
                asFormMainTableList.forEach(v -> {
                    String tv = parameterNames.get(v.getFormId()) != null ? parameterNames.get(v.getFormId()) : "{}";
                    if (tv != "{}") {
                        List<AsFormMainTableColums> tableColumns1 = formMapper.getTableColumns(v.getFormId());
                        JSONArray objects = JSONArray.parseArray(tv);
                        List<Map<String, String>> lm = new ArrayList<>();
                        objects.forEach(p -> {
                            Map<String, Object> resultStr = JSONObject.parseObject(p.toString());
                            HashMap<String, String> map = new HashMap<>();
                            map.putAll(map1.get(v.getTableName()));
                            tableColumns1.forEach(t -> {
                                if (resultStr.get(t.getFormField()) != null) {
                                    map.put(t.getTableField(), resultStr.get(t.getFormField()).toString());
                                }
                            });
                            lm.add(map);
                        });
                        map2.put(v.getTableName(), lm);
                    }
                });
                log.info("获取子表的数据库物理表字段对应前台表单字段关系完成，开始组装子表的插入语句");
                for (Map.Entry<String, List<Map<String, String>>> entry : map2.entrySet()) {
                    String mapKey = entry.getKey();
                    log.info("当前子表：" + mapKey);
                    List<Map<String, String>> mapValue = entry.getValue();
                    for (Map<String, String> mp : mapValue) {
                        String insertField = "GUID";
                        String insertValue = "'" + UUID.randomUUID().toString();
                        for (Map.Entry<String, String> etry : mp.entrySet()) {
                            String mapKey1 = etry.getKey();
                            String mapValue1 = etry.getValue();
                            insertField += "," + mapKey1;
                            insertValue += "','" + mapValue1;
                        }
                        if (hasSetYear.get()) {
                            insertField += ", SET_YEAR";
                            insertValue += "','" + setYear;
                        }
                        log.info("组装子表的插入语句完成，开始入库");
                        formMapper.insertIntoTable(mapKey, insertField, insertValue + "'");
                    }
                }
            }
        }
    }

    @Override
    public String getDynamicFormData(String formId, String guid) throws Exception {
        List<AsFormMainTable> asFormMainTableList = formMapper.getTableInfo(formId);
        if (asFormMainTableList != null && asFormMainTableList.size() > 0) {
            if (asFormMainTableList.size() == 1) {
                log.info("此表单只有一个主表" + asFormMainTableList.get(0).getTableName() + "，无其他子表");
                List<String> guids = new ArrayList<>();
                guids.add(guid);
                List<Map<String, String>> mapList = formMapper.getSelectRecord(asFormMainTableList.get(0).getTableName(), "GUID", guids);
                if (mapList != null && mapList.size() == 1) {
                    log.info("主表数据获取完成，开始获取数据库物理表字段对应前台表单字段关系");
                    Map<String, String> map = new HashMap<>();
                    List<AsFormMainTableColums> tableColumns = formMapper.getTableColumns(formId);
                    Map<String, String> mp = mapList.get(0);
                    log.info("获取数据库物理表字段对应前台表单字段关系完成，开始数据转换");
                    tableColumns.forEach(v -> {
                        if (v.getFormField() != null) {
                            log.info(v.getTableField().toUpperCase() + " -> " + v.getFormField());
                            map.put(v.getFormField(), mp.get(v.getTableField().toUpperCase()));
                        }
                    });
                    log.info("数据转换完成");
                    if (asFormMainTableList.get(0).getHasSetYear().equals("Y")) {
                        map.put("SET_YEAR", mp.get("SET_YEAR"));
                    }
                    map.put("GUID", mp.get("GUID"));
                    return JSON.toJSONString(map);
                } else {
                    throw new Exception("未查询到选中的数据");
                }
            } else {
                log.info("此表单有一个主表，且有其他子表");
                Map<String, Object> map = new HashMap<>();
                for (AsFormMainTable v : asFormMainTableList) {
                    if (v.getIsMainTable().equals("0")) {
                        log.info("主表：" + v.getTableName());
                        List<String> guids = new ArrayList<>();
                        guids.add(guid);
                        List<Map<String, String>> mapList = formMapper.getSelectRecord(v.getTableName(), "GUID", guids);
                        if (mapList != null && mapList.size() == 1) {
                            log.info("主表数据获取完成，开始获取主表的数据库物理表字段对应前台表单字段关系");
                            List<AsFormMainTableColums> tableColumns = formMapper.getTableColumns(formId);
                            Map<String, String> mp = mapList.get(0);
                            log.info("获取主表的数据库物理表字段对应前台表单字段关系完成，开始数据转换");
                            tableColumns.forEach(c -> {
                                if (c.getFormField() != null) {
                                    if (c.getJoinTable() != null && !c.getJoinTable().equals("") && asFormMainTableList.stream().anyMatch(task -> task.getTableName().equals(c.getJoinTable()))) {
                                        log.info("当前字段：" + c.getTableField().toUpperCase() + "为关联子表字段，开始获取子表：" + c.getJoinTable() + "目标字段：" + c.getJoinTableColumn() + "的数据");
                                        List<String> vals = new ArrayList<>();
                                        vals.add(mp.get(c.getTableField().toUpperCase()));
                                        List<Map<String, String>> mapList1 = formMapper.getSelectRecord(c.getJoinTable(), c.getJoinTableColumn(), vals);
                                        if (mapList1 != null && mapList1.size() > 0) {
                                            log.info("子表数据获取完成，开始获取子表的数据库物理表字段对应前台表单字段关系");
                                            List<AsFormMainTableColums> tableColumns1 = formMapper.getTableColumns(c.getFormField());
                                            List<Map<String, String>> mapList2 = new ArrayList<>();
                                            log.info("获取子表的数据库物理表字段对应前台表单字段关系完成，开始数据转换");
                                            mapList1.forEach(m -> {
                                                Map<String, String> map1 = new HashMap<>();
                                                tableColumns1.forEach(c1 -> {
                                                    if (c1.getFormField() != null) {
                                                        log.info(c1.getTableField().toUpperCase() + " -> " + c1.getFormField());
                                                        map1.put(c1.getFormField(), m.get(c1.getTableField().toUpperCase()));
                                                    }
                                                });
                                                map1.put("GUID", m.get("GUID"));
                                                if (m.get("SET_YEAR") != null && !m.get("SET_YEAR").equals("")) {
                                                    map1.put("SET_YEAR", m.get("SET_YEAR"));
                                                }
                                                mapList2.add(map1);
                                            });
                                            map.put(c.getFormField(), JSON.toJSONString(mapList2));
                                        } else {
                                            log.info("子表数据获取完成，无数据，直接返回空数组");
                                            map.put(c.getFormField(), JSON.toJSONString(new String[]{}));
                                        }
                                        /*// 存储主表对应子表的main_id
                                        map.put(c.getFormField() + "-" + c.getTableField(), mp.get(c.getTableField().toUpperCase()));*/
                                    } else {
                                        log.info(c.getTableField().toUpperCase() + " -> " + c.getFormField());
                                        map.put(c.getFormField(), mp.get(c.getTableField().toUpperCase()));
                                    }
                                }
                            });
                            if (v.getHasSetYear().equals("Y")) {
                                map.put("SET_YEAR", mp.get("SET_YEAR"));
                            }
                            map.put("GUID", mp.get("GUID"));
                            return JSON.toJSONString(map);
                        } else {
                            throw new Exception("未查询到选中的数据");
                        }
                    }
                }
            }
        }
        throw new Exception("根据表单ID查询数据库物理表发生异常");
    }

    @Override
    public void deleteDynamicFormData(String formId, List<String> guids, String fileCompo) throws Exception {
        List<AsFormMainTable> asFormMainTableList = formMapper.getTableInfo(formId);
        JSONArray fileCompos = JSONArray.parseArray(fileCompo);
        if (asFormMainTableList != null && asFormMainTableList.size() > 0) {
            if (asFormMainTableList.size() >= 1) {
                for (AsFormMainTable v : asFormMainTableList) {
                    if (v.getIsMainTable().equals("0")) {
                        List<Map<String, String>> mapList = formMapper.getSelectRecord(v.getTableName(), "GUID", guids);
                        List<AsFormMainTableColums> tableColumns = formMapper.getTableColumns(formId);

                        if (fileCompos != null && fileCompos.size() > 0) {
                            for (AsFormMainTableColums asFormMainTableColums : tableColumns) {
                                if (fileCompos.contains(asFormMainTableColums.getFormField())) {
                                    for (Map<String, String> mp : mapList) {
                                        if (mp.get(asFormMainTableColums.getTableField().toUpperCase()) != null) {
                                            String fl = mp.get(asFormMainTableColums.getTableField().toUpperCase());
                                            File file = new File(ServerConfig.uploadFolder + "/business/" + fl.substring(fl.lastIndexOf("/")));
                                            if (file.isFile() && file.exists()) {
                                                log.info("删除文件：" + file.getName());
                                                file.delete();
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        tableColumns.forEach(c -> {
                            if (c.getJoinTable() != null && !c.getJoinTable().equals("") && asFormMainTableList.stream().anyMatch(task -> task.getTableName().equals(c.getJoinTable()))) {
                                log.info("删除子表：" + c.getJoinTable() + "的数据，目标字段：" + c.getJoinTableColumn());
                                formMapper.deleteSelectRecord(c.getJoinTable(), c.getJoinTableColumn(), mapList.stream().map(mp -> mp.get(c.getTableField().toUpperCase())).collect(Collectors.toList()));
                            }
                        });
                        log.info("删除主表：" + v.getTableName() + "的数据，目标字段：GUID");
                        formMapper.deleteSelectRecord(v.getTableName(), "GUID", guids);
                    }
                }
            }
        } else {
            throw new Exception("根据表单ID查询数据库物理表发生异常");
        }
    }
}
