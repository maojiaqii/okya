package com.top.okya.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.top.okya.dao.CommonMapper;
import com.top.okya.service.CommonService;
import com.top.okya.util.elHelp.ElTreeBean;
import com.top.okya.util.elHelp.FormatElData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/6/1 15:35
 * @describe： 通用业务层实现
 */

@Service("CommonService")
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    CommonMapper commonMapper;

    @Override
    public List<Map<String, String>> findPage(String tableName, String searchProperties, String sortProp, String sortOrder, String setYear,String needYear) {
        Map<String,Object> jsonToMap = JSONObject.parseObject(searchProperties);
        return commonMapper.findPage(tableName, setYear, jsonToMap, sortProp, sortOrder.equals("descending") ? "desc" : "asc",needYear);
    }

    @Override
    public List<Map<String, String>> getSelectorData(String tableName, String searchProperties, String setYear,String needYear) {
        Map<String,Object> jsonToMap = JSONObject.parseObject(searchProperties);
        return commonMapper.getSelectorData(tableName, setYear, jsonToMap,needYear);
    }

    @Override
    @Transactional
    public int save(Map<String, Object> jsonToMap, String tableName, String setYear,String needYear) {
        log.info("插入["+tableName+"]数据开始");
        return commonMapper.saveData(tableName, setYear, jsonToMap,needYear);
    }

    @Override
    public String getSelectorTreeData(String tableName, String searchProperties, String setYear) {
        Map<String,Object> jsonToMap = JSONObject.parseObject(searchProperties);
        List<ElTreeBean> datas = FormatElData.formatTree(commonMapper.getSelectorTreeData(tableName, setYear, jsonToMap), "");
        log.info("下拉树数据源取数完成");
        return JSON.toJSONString(datas);
    }

    @Override
    @Transactional
    public int updateCommon(Map<String, Object> jsonToMap, String tableName, String setYear,String needYear) {
        log.info("修改["+tableName+"]数据开始");
        return commonMapper.updateCommon(tableName, setYear, jsonToMap,needYear);
    }

    @Override
    @Transactional
    public int deletCommon(List list, String tableName, String setYear) {
        log.info("删除["+tableName+"]数据开始");
        return commonMapper.deletCommon(tableName, setYear, list);
    }

    @Override
    public List<Map<String, String>> getTableCols(String tableName) {
        log.info("获取["+tableName+"]所有列信息开始");
        return commonMapper.getTableCols(tableName.toUpperCase());
    }
}
