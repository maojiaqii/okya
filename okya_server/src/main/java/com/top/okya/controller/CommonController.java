package com.top.okya.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.top.okya.service.CommonService;
import com.top.okya.util.common.DateFormatUtil;
import com.top.okya.util.elHelp.FormatElData;
import com.top.okya.util.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: maojiaqi
 * @Date: 2020/3/26 19:55
 * @describe： 通用控制层
 */

@Controller
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Autowired
    CommonService commonService;

    // 分页获取数据
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    @ResponseBody
    public String findPage(@RequestParam("searchProperties") String searchProperties, @RequestParam("tableName") String tableName, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam("sortProp") String sortProp, @RequestParam("sortOrder") String sortOrder, @RequestParam("setYear") String setYear,@RequestParam("needYear")String needYear) {
        try {
            if(null == tableName || tableName == ""){
                log.info("分页查询数据表名为空，查询失败！");
                return new ResponseObject(false, "分页查询数据表名为空，查询失败！", null).invoke();
            }
            log.info("分页获取数据开始：数据源" + tableName);
            return new ResponseObject(true, FormatElData.formatTableData(PageHelper.startPage(pageNum, pageSize), commonService.findPage(tableName, searchProperties, sortProp, sortOrder, setYear,needYear)), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("分页获取数据发生异常，失败" + e.getMessage());
            return new ResponseObject(false, "分页获取数据失败", null).invoke();
        }
    }

    // 新增
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam("data") String data, @RequestParam("tableName") String tableName,@RequestParam("needYear")String needYear) {
        try {
            Map<String,Object> jsonToMap = JSONObject.parseObject(data);
            if(null == jsonToMap){
                log.info("未获取到保存的表单数据，保存失败！");
                return new ResponseObject(false, "未获取到保存的表单数据，保存失败！", null).invoke();
            }
            if(null == tableName || tableName == ""){
                log.info("保存插入的表名为空，保存失败！");
                return new ResponseObject(false, "保存插入的表名为空，保存失败！", null).invoke();
            }
            log.info("待插入["+tableName+"]的数据：" + data);
            String setYear = DateFormatUtil.getYear();
            commonService.save(jsonToMap, tableName, setYear, needYear);
            log.info("插入["+tableName+"]数据成功");
            return new ResponseObject(true, "保存成功", null).invoke();
        } catch (Exception e) {
            log.error("插入["+tableName+"]数据发生异常，失败");
            log.info(e.getMessage());
            return new ResponseObject(false, "保存失败", null).invoke();
        }
    }

    // 修改
    @RequestMapping(value = "/updateCommon", method = RequestMethod.POST)
    @ResponseBody
    public String updateCommon(@RequestParam("data") String data, @RequestParam("tableName") String tableName,@RequestParam("needYear")String needYear) {
        try {
            String setYear = DateFormatUtil.getYear();
            Map<String,Object> jsonToMap = JSONObject.parseObject(data);
            if(null == jsonToMap){
                log.info("未获取到修改的表单数据，保存失败！");
                return new ResponseObject(false, "未获取到修改的表单数据，保存失败！", null).invoke();
            }
            if(null == tableName || tableName == ""){
                log.info("保存修改的表名为空，保存失败！");
                return new ResponseObject(false, "修改插入的表名为空，保存失败！", null).invoke();
            }
            log.info("待修改["+tableName+"]的数据：" + data);
            commonService.updateCommon(jsonToMap, tableName, setYear,needYear);
            log.info("修改["+tableName+"]数据成功");
            return new ResponseObject(true, "修改成功", null).invoke();
        } catch (Exception e) {
            log.error("修改["+tableName+"]数据发生异常，失败");
            log.info(e.getMessage());
            return new ResponseObject(false, "修改失败", null).invoke();
        }
    }

    // 删除
    @RequestMapping(value = "/deletCommon", method = RequestMethod.POST)
    @ResponseBody
    public String deletCommon(@RequestParam("data") String data, @RequestParam("tableName") String tableName) {
        try {
            String setYear = DateFormatUtil.getYear();
            Map<String,Object> jsonToMap = JSONObject.parseObject(data);
            if(null == jsonToMap){
                log.info("未获取到要删除的表单数据！");
                return new ResponseObject(false, "未获取到要删除的表单数据", null).invoke();
            }
            if(null == tableName || tableName == ""){
                log.info("要删除数据的表名为空，保存失败！");
                return new ResponseObject(false, "要删除数据的表名为空，保存失败！", null).invoke();
            }
            log.info("待删除["+tableName+"]的数据：" + data);
            Set<Map.Entry<String, Object>> entries = jsonToMap.entrySet();
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, Object> entry : entries) {
                Object value = entry.getValue();
                JSONArray objects = JSONArray.parseArray((String) value);
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject jsonObject = objects.getJSONObject(i);
                    String guid = jsonObject.getString("GUID");
                    list.add(guid);
                }
            }
            commonService.deletCommon(list, tableName, setYear);
            log.info("删除["+tableName+"]数据成功");
            return new ResponseObject(true, "删除成功", null).invoke();
        } catch (Exception e) {
            log.error("删除["+tableName+"]数据发生异常，失败");
            log.info(e.getMessage());
            return new ResponseObject(false, "删除失败", null).invoke();
        }
    }
    // 获取下拉框数据
    @RequestMapping(value = "/getSelectorData", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectorData(@RequestParam("searchProperties") String searchProperties, @RequestParam("tableName") String tableName,@RequestParam("needYear")String needYear) {
        try {
            String setYear = DateFormatUtil.getYear();
            log.info("获取下拉框数据开始：数据源" + tableName);
            return new ResponseObject(true, JSONArray.toJSONString(commonService.getSelectorData(tableName, searchProperties, setYear,needYear)), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取下拉框数据发生异常，失败" + e.getMessage());
            return new ResponseObject(false, "获取下拉框数据失败", null).invoke();
        }
    }

    // 获取下拉树数据
    @RequestMapping(value = "/getSelectorTreeData", method = RequestMethod.POST)
    @ResponseBody
    public String getSelectorTreeData(@RequestParam("searchProperties") String searchProperties, @RequestParam("tableName") String tableName, @RequestParam("setYear") String setYear) {
        try {
            log.info("获取下拉树数据开始：数据源" + tableName);
            return new ResponseObject(true, JSONArray.toJSONString(commonService.getSelectorTreeData(tableName, searchProperties, setYear)), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取下拉树数据发生异常，失败" + e.getMessage());
            return new ResponseObject(false, "获取下拉树数据失败", null).invoke();
        }
    }

    // 获取数据库表的列信息
    @RequestMapping(value = "/getTableCols", method = RequestMethod.POST)
    @ResponseBody
    public String getTableCols(@RequestParam("tableName") String tableName) {
        try {
            log.info("获取列信息开始：表" + tableName);
            return new ResponseObject(true, JSONArray.toJSONString(commonService.getTableCols(tableName)), null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取列信息发生异常，失败" + e.toString());
            return new ResponseObject(false, "获取列信息失败", null).invoke();
        }
    }

}
