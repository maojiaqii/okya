package com.top.okya.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.top.okya.pojo.AsForm;
import com.top.okya.pojo.AsFormSearchBusiness;
import com.top.okya.pojo.AsRole;
import com.top.okya.pojo.AsTable;
import com.top.okya.pojo.AsTableColumns;
import com.top.okya.service.FormService;
import com.top.okya.util.elHelp.FormatElData;
import com.top.okya.util.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/28 20:08
 * @describe： 表单设计器控制类
 */

@Controller
@Slf4j
@RequestMapping("/form")
public class FormController {

    @Autowired
    FormService formService;

    // 单选框、多选框数据源
    @RequestMapping(value = "/getRadioCheckBoxData", method = RequestMethod.POST)
    @ResponseBody
    public String getRadioCheckBoxData(@RequestParam("setYear") String setYear, @RequestParam("datasource") String datasource) {
        try {
            log.info("开始获取单选、多选数据源");
            return new ResponseObject(true, formService.getRadioCheckBoxData(setYear, datasource), null).invoke();
        } catch (Exception e) {
            log.info("单选、多选数据源取数失败" + e.toString());
            return new ResponseObject(false, "单选、多选数据源取数失败", null).invoke();
        }
    }

    // 下拉选择器数据源
    @RequestMapping(value = "/getSelectorData", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectorData(@RequestParam("setYear") String setYear, @RequestParam("datasource") String datasource, HttpServletRequest req) {
        try {
            log.info("开始获取下拉选择器数据源");
            return new ResponseObject(true, formService.getSelectorData(setYear, datasource, req.getParameter("conditions"), req.getParameter("affectField"), req.getParameter("relation")), null).invoke();
        } catch (Exception e) {
            log.info("下拉选择器数据源取数失败" + e.toString());
            return new ResponseObject(false, "下拉选择器数据源取数失败", null).invoke();
        }
    }

    // 下拉树数据源
    @RequestMapping(value = "/getSelectTreeData", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectTreeData(@RequestParam("setYear") String setYear, @RequestParam("datasource") String datasource, HttpServletRequest req) {
        try {
            log.info("开始获取下拉树数据源");
            return new ResponseObject(true, formService.getSelectTreeData(setYear, datasource, req.getParameter("conditions"), req.getParameter("affectField"), req.getParameter("relation")), null).invoke();
        } catch (Exception e) {
            log.info("下拉树数据源取数失败" + e.toString());
            return new ResponseObject(false, "下拉树数据源取数失败", null).invoke();
        }
    }

    // 获取数据源字段
    @RequestMapping(value = "/getDatasourceField", method = RequestMethod.POST)
    @ResponseBody
    public String getDatasourceField(@RequestParam("datasource") String datasource) {
        try {
            log.info("开始获取数据源字段");
            return new ResponseObject(true, formService.getDatasourceField(datasource), null).invoke();
        } catch (Exception e) {
            log.info("数据源字段取数失败" + e.toString());
            return new ResponseObject(false, "数据源字段取数失败", null).invoke();
        }
    }

    // 保存表单模板
    @RequestMapping(value = "/saveFormTemplet", method = RequestMethod.POST)
    @ResponseBody
    public String saveFormTemplet(@RequestParam("formJson") String formJson, @RequestParam("setYear") String setYear) {
        try {
            log.info("开始保存表单模板");
            if (formService.saveFormTempLet(formJson) == 1) {
                return new ResponseObject(true, "保存表单模板成功", null).invoke();
            } else {
                log.info("保存表单模板失败，未能成功插入数据");
                return new ResponseObject(false, "保存表单模板失败", null).invoke();
            }
        } catch (Exception e) {
            log.info("保存表单模板失败" + e.toString());
            return new ResponseObject(false, "保存表单模板失败", null).invoke();
        }
    }

    // 分页展示表单模板
    @RequestMapping(value = "/findFormTemplet", method = RequestMethod.POST)
    @ResponseBody
    public String findFormTemplet(@RequestParam("formName") String formName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("开始查询表单模板");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), formService.findFormTemplet(formName, sortProp, sortOrder)), null).invoke();
        } catch (Exception e) {
            log.info("查询表单模板失败" + e.toString());
            return new ResponseObject(false, "查询表单模板失败", null).invoke();
        }
    }

    // 删除表单模板
    @RequestMapping(value = "/deleteFormTempLet", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFormTempLet(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {
        try {
            formService.deleteFormTempLet(JSONObject.parseArray(datas, AsForm.class));
            log.info("删除表单模板成功");
            return new ResponseObject(true, "删除表单模板成功", null).invoke();
        } catch (Exception e) {
            log.error("删除表单模板发生异常，失败");
            return new ResponseObject(false, "删除表单模板失败", null).invoke();
        }
    }

    // 表单模板下拉框数据
    @RequestMapping(value = "/getFormTemplet", method = RequestMethod.POST)
    @ResponseBody
    public String getFormTemplet(@RequestParam("setYear") String setYear) {
        try {
            log.info("表单模板下拉框取数");
            return new ResponseObject(true, JSONArray.toJSONString(formService.getFormTemplet()), null).invoke();
        } catch (Exception e) {
            log.error("表单模板下拉框取数发生异常，失败" + e.toString());
            return new ResponseObject(false, "表单模板下拉框取数失败", null).invoke();
        }
    }

    // 表单模板下拉框数据
    @RequestMapping(value = "/getFormTempletForBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String getFormTempletForBusiness(@RequestParam("setYear") String setYear) {
        try {
            log.info("表单模板下拉框取数");
            return new ResponseObject(true, JSONArray.toJSONString(formService.getFormTempletForBusiness()), null).invoke();
        } catch (Exception e) {
            log.error("表单模板下拉框取数发生异常，失败" + e.toString());
            return new ResponseObject(false, "表单模板下拉框取数失败", null).invoke();
        }
    }

    // 保存表单数据源
    @RequestMapping(value = "/saveFormDataSource", method = RequestMethod.POST)
    @ResponseBody
    public String saveFormDataSource(@RequestParam("formId") String formId, @RequestParam("hasSetYear") String hasSetYear, @RequestParam("mainTableName") String mainTableName, @RequestParam("childTableNames") String childTableNames, @RequestParam("childTableColumns") String childTableColumns, @RequestParam("mainTableColumns") String mainTableColumns, @RequestParam("viewSql") String viewSql, @RequestParam("setYear") String setYear) {

        log.info("开始保存表单数据源");
        try {
            log.info("查询当前表单已存在的数据源信息，表单id：" + formId);
            List<String> oldTables = formService.getOldFormTables(formId);
            if (oldTables != null && oldTables.size() > 0) {
                log.info("查询到当前表单已存在的数据源信息，开始删除");
                formService.clearTableInfo(oldTables);
                formService.dropOldTable(oldTables);
                log.info("删除当前表单已存在的数据源信息完成");
            }
        } catch (Exception e) {
            log.info("查询当前表单已存在的数据源信息并删除时发生异常" + e.toString());
            return new ResponseObject(false, "查询当前表单已存在的数据源信息并删除时发生异常", null).invoke();
        }

        try {
            log.info("开始创建物理表");
            formService.createTable(formId, hasSetYear, mainTableName, childTableNames, childTableColumns, mainTableColumns, viewSql);
            log.info("物理表创建完成");
        } catch (Exception e) {
            log.info("物理表创建失败" + e.toString());
            return new ResponseObject(false, "物理表创建失败，保存表单数据源操作终止" + e.toString(), null).invoke();
        }
        try {
            log.info("开始记录表单数据源信息");
            if (formService.saveFormDataSource(formId, hasSetYear, mainTableName, childTableNames, childTableColumns, mainTableColumns, viewSql) > 1) {
                log.info("表单数据源信息记录完成");
                return new ResponseObject(true, "保存表单数据源成功", null).invoke();
            } else {
                log.info("未能成功记录表单数据源信息");
                log.info("操作回滚开始");
                formService.dropTable(mainTableName, childTableNames);
                log.info("操作回滚结束");
                return new ResponseObject(false, "未能成功记录表单数据源信息，保存表单数据源操作终止，操作回滚完成", null).invoke();
            }
        } catch (Exception e) {
            log.info("记录表单数据源信息失败" + e.toString());
            log.info("操作回滚开始");
            formService.dropTable(mainTableName, childTableNames);
            log.info("操作回滚结束");
            return new ResponseObject(false, "记录表单数据源信息失败，保存表单数据源操作终止，操作回滚完成", null).invoke();
        }
    }

    // 表单数据源分页数据
    @RequestMapping(value = "/findFormDataSource", method = RequestMethod.POST)
    @ResponseBody
    public String findFormDataSource(@RequestParam("formId") String formId, @RequestParam("tableName") String tableName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("开始查询表单数据源");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), formService.findFormDataSource(formId, tableName, sortProp, sortOrder)), null).invoke();
        } catch (Exception e) {
            log.info("查询表单数据源失败" + e.toString());
            return new ResponseObject(false, "查询表单数据源失败", null).invoke();
        }
    }

    // 获取表单数据源
    @RequestMapping(value = "/getFormDataSource", method = RequestMethod.POST)
    @ResponseBody
    public String getFormDataSource(@RequestParam("formId") String formId, @RequestParam("setYear") String setYear) {
        try {
            log.info("开始获取选中表单数据源");
            return new ResponseObject(true, formService.getFormDataSource(formId), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取选中表单数据源失败" + e.toString());
            return new ResponseObject(false, "获取选中表单数据源失败", null).invoke();
        }
    }

    // 保存表单数据源
    @RequestMapping(value = "/deleteFormDb", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFormDb(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {

        log.info("开始删除表单数据源");
        try {
            List<String> oldTables = new ArrayList<>();
            List<Map> list = JSONObject.parseArray(datas, Map.class);
            list.forEach(map -> {
                String formId = map.get("FORM_ID").toString();
                log.info("查询当前表单已存在的数据源信息，表单id：" + formId);
                oldTables.addAll(formService.getOldFormTables(formId));
            });
            if (oldTables != null && oldTables.size() > 0) {
                log.info("查询到当前表单已存在的数据源信息，开始删除");
                formService.clearTableInfo(oldTables);
                formService.dropOldTable(oldTables);
                log.info("删除当前表单已存在的数据源信息完成");
            }
            return new ResponseObject(true, "删除所选表单的数据源信息完成", null).invoke();
        } catch (Exception e) {
            log.info("删除所选表单的数据源信息发生异常" + e.toString());
            return new ResponseObject(false, "删除所选表单的数据源信息发生异常", null).invoke();
        }
    }

    // 获取所选表单对应的业务数据源
    @RequestMapping(value = "/getFormBusinessDataSource", method = RequestMethod.POST)
    @ResponseBody
    public String getFormBusinessDataSource(@RequestParam("setYear") String setYear, @RequestParam("formId") String formId) {
        try {
            log.info("获取所选表单对应的业务数据源开始");
            return new ResponseObject(true, formService.getFormBusinessDataSource(formId), null).invoke();
        } catch (Exception e) {
            log.error("获取所选表单对应的业务数据源发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取所选表单对应的业务数据源失败", null).invoke();
        }
    }

    // 保存表单业务信息
    @RequestMapping(value = "/saveFormBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String saveFormBusiness(@RequestParam("formId") String formId, @RequestParam("tableId") String tableId, @RequestParam("dataSource") String dataSource, @RequestParam("maxHeight") String maxHeight,
                                   @RequestParam("size") String size, @RequestParam("showRowNum") String showRowNum, @RequestParam("showPagination") String showPagination, @RequestParam("showSum") String showSum,
                                   @RequestParam("showOperation") String showOperation, @RequestParam("permsNew") String permsNew, @RequestParam("permsEdit") String permsEdit, @RequestParam("permsDelete") String permsDelete, @RequestParam("permsExport") String permsExport,
                                   @RequestParam("columnInfos") String columnInfos, @RequestParam("searchInfos") String searchInfos) {

        log.info("开始保存表单业务信息");
        try {
            if (formService.saveFormBusiness(formId, tableId, dataSource, maxHeight, size, showRowNum, showPagination, showSum, showOperation, permsNew, permsEdit, permsDelete, permsExport, columnInfos, searchInfos) > 1) {
                log.info("表单业务信息记录完成");
                return new ResponseObject(true, "保存表单业务信息成功", null).invoke();
            } else {
                log.info("未能成功保存表单业务信息");
                return new ResponseObject(false, "未能成功保存表单业务信息", null).invoke();
            }
        } catch (Exception e) {
            log.info("保存表单业务信息失败" + e.toString());
            return new ResponseObject(false, "保存表单业务信息失败", null).invoke();
        }
    }

    // 修改表单业务信息
    @RequestMapping(value = "/updateFormBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String updateFormBusiness(@RequestParam("formId") String formId, @RequestParam("tableId") String tableId, @RequestParam("dataSource") String dataSource, @RequestParam("maxHeight") String maxHeight,
                                     @RequestParam("size") String size, @RequestParam("showRowNum") String showRowNum, @RequestParam("showPagination") String showPagination, @RequestParam("showSum") String showSum,
                                     @RequestParam("showOperation") String showOperation, @RequestParam("permsNew") String permsNew, @RequestParam("permsEdit") String permsEdit, @RequestParam("permsDelete") String permsDelete, @RequestParam("permsExport") String permsExport,
                                     @RequestParam("columnInfos") String columnInfos, @RequestParam("searchInfos") String searchInfos) {

        log.info("开始保存修改的表单业务信息");
        try {
            if (formService.updateFormBusiness(formId, tableId, dataSource, maxHeight, size, showRowNum, showPagination, showSum, showOperation, permsNew, permsEdit, permsDelete, permsExport, columnInfos, searchInfos) > 1) {
                log.info("表单业务信息记录完成");
                return new ResponseObject(true, "修改表单业务信息成功", null).invoke();
            } else {
                log.info("未能成功修改表单业务信息");
                return new ResponseObject(false, "未能成功修改表单业务信息", null).invoke();
            }
        } catch (Exception e) {
            log.info("修改表单业务信息失败" + e.toString());
            return new ResponseObject(false, "修改表单业务信息失败", null).invoke();
        }
    }

    // 表单业务分页数据
    @RequestMapping(value = "/findFormBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String findFormBusiness(@RequestParam("formId") String formId, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear) {
        try {
            log.info("开始查询表单业务");
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), formService.findFormBusiness(formId, sortProp, sortOrder)), null).invoke();
        } catch (Exception e) {
            log.info("查询表单业务失败" + e.toString());
            return new ResponseObject(false, "查询表单业务失败", null).invoke();
        }
    }

    // 获取选中表单对应业务信息
    @RequestMapping(value = "/getSelectFormBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectFormBusiness(@RequestParam("formId") String formId) {
        try {
            log.info("开始获取选中表单对应业务信息");
            return new ResponseObject(true, formService.getSelectFormBusiness(formId), null).invoke();
        } catch (Exception e) {
            log.info("获取选中表单对应业务信息失败" + e.toString());
            return new ResponseObject(false, "获取选中表单对应业务信息失败", null).invoke();
        }
    }

    // 删除表单业务信息
    @RequestMapping(value = "/deleteFormBusiness", method = RequestMethod.POST)
    @ResponseBody
    public String deleteFormBusiness(@RequestParam("datas") String datas, @RequestParam("setYear") String setYear) {

        log.info("开始删除表单业务信息");
        try {
            List<String> formIds = new ArrayList<>();
            List<Map> list = JSONObject.parseArray(datas, Map.class);
            list.forEach(map -> {
                formIds.add(map.get("FORM_ID").toString());
            });
            formService.deleteFormBusiness(formIds);
            return new ResponseObject(true, "删除所选表单业务信息完成", null).invoke();
        } catch (Exception e) {
            log.info("删除所选表单业务信息发生异常" + e.toString());
            return new ResponseObject(false, "删除所选表单业务信息发生异常", null).invoke();
        }
    }

    // 初始化自定义业务页面
    @RequestMapping(value = "/initPage", method = RequestMethod.POST)
    @ResponseBody
    public String initPage(@RequestParam("formId") String formId) {
        try {
            log.info("开始初始化自定义业务页面，表单ID: " + formId);
            List<Map<String, String>> pageButtons = formService.getPageButton(formId);
            List<AsFormSearchBusiness> searchField = formService.getPageSearchField(formId);
            List<AsTable> asTables = formService.getPageTable(formId);
            List<AsTableColumns> asTableColumnsList = formService.getPageTableColumnsList(formId);
            String formTemplet = formService.getFormTempletByFormId(formId);
            String needYear = formService.needYear(formId);
            JSONObject jso = new JSONObject();
            if (pageButtons.size() > 0) {
                jso.put("pageButtons", JSON.toJSONString(pageButtons.get(0)));
            }
            if (asTableColumnsList.size() > 0) {
                jso.put("asTableColumnsList", JSON.toJSONString(asTableColumnsList));
            }
            if (searchField.size() > 0) {
                jso.put("searchField", JSON.toJSONString(searchField));
            }
            if (asTables.size() > 0) {
                jso.put("asTables", JSON.toJSONString(asTables.get(0)));
            }
            jso.put("formTemplet", formTemplet);
            jso.put("needYear", needYear);
            return new ResponseObject(true, jso.toJSONString(), null).invoke();
        } catch (Exception e) {
            log.info("获取选中表单对应业务信息失败" + e.toString());
            return new ResponseObject(false, "获取选中表单对应业务信息失败", null).invoke();
        }
    }

    // 保存自定义业务页面数据
    @RequestMapping(value = "/saveDynamicFormData", method = RequestMethod.POST)
    @ResponseBody
    public String saveDynamicFormData(HttpServletRequest req) {
        try {
            log.info("开始保存自定义业务页面数据");
            Map<String, String> map = new HashMap();
            Enumeration paramNames = req.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                map.put(paramName, req.getParameter(paramName));
            }
            formService.saveDynamicFormData(map);
            return new ResponseObject(true, "保存成功！", null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存自定义业务页面数据失败" + e.toString());
            return new ResponseObject(false, "保存失败", null).invoke();
        }
    }

    // 获取选中数据明细
    @RequestMapping(value = "/getDynamicFormData", method = RequestMethod.POST)
    @ResponseBody
    public String getDynamicFormData(@RequestParam("formId") String formId, @RequestParam("guid") String guid) {
        try {
            log.info("开始获取选中的明细数据");
            return new ResponseObject(true, formService.getDynamicFormData(formId, guid), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取选中的明细数据失败" + e.toString());
            return new ResponseObject(false, "获取选中的明细数据失败", null).invoke();
        }
    }

    // 获取选中数据明细
    @RequestMapping(value = "/deleteDynamicFormData", method = RequestMethod.POST)
    @ResponseBody
    public String deleteDynamicFormData(@RequestParam("datas") String datas, @RequestParam("sysFormId") String sysFormId, @RequestParam("fileCompo") String fileCompo) {
        try {
            List<Map> list = JSONObject.parseArray(datas, Map.class);
            List<String> guids = new ArrayList<>();
            list.forEach(map -> {
                guids.add(map.get("GUID").toString());
            });
            formService.deleteDynamicFormData(sysFormId, guids, fileCompo);
            log.info("删除选中的数据成功");
            return new ResponseObject(true, "删除选中的数据成功", null).invoke();
        } catch (Exception e) {
            log.info("删除选中的数据发生异常，失败" + e.toString());
            return new ResponseObject(false, "删除选中的数据失败", null).invoke();
        }
    }
}
