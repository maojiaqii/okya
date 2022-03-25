package com.top.okya.service.impl;

import com.top.okya.dao.EChartMapper;
import com.top.okya.service.EChartService;
import com.top.okya.util.common.DateFormatUtil;
import com.top.okya.util.eChart.EChartBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/8/25 11:55
 * @describe：
 */

@Service
public class EChartServiceImpl implements EChartService {

    @Autowired
    private EChartMapper eChartMapper;

    @Override
    public List<EChartBean> getCommonData(String tableName) {
        return eChartMapper.getCommonData(tableName, DateFormatUtil.getYearMonthDay());
    }
}
