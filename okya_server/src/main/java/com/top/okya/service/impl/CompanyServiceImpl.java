package com.top.okya.service.impl;

import com.alibaba.fastjson.JSON;
import com.top.okya.dao.AsCompanyMapper;
import com.top.okya.service.CompanyService;
import com.top.okya.util.elHelp.ElTreeBean;
import com.top.okya.util.elHelp.FormatElData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/28 20:06
 * @describe：
 */

@Slf4j
@Service("CompanyService")
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    AsCompanyMapper asCompanyMapper;

    @Override
    public String getCompanyTree(String setYear) {
        List<ElTreeBean> elTreeBeans = FormatElData.formatTree(asCompanyMapper.getCompanyTree(setYear),"");
        log.info("获取单位下拉树数据完成");
        return JSON.toJSONString(elTreeBeans);
    }
}
