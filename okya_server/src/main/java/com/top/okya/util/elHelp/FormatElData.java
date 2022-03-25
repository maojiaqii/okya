package com.top.okya.util.elHelp;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: maojiaqi
 * @Date: 2020/3/23 14:06
 * @describe： 动态菜单数据格式化, 下拉树数据格式化
 */
public class FormatElData {

    private FormatElData() {};

    // 返回el-table需要的数据
    public static String formatTableData(Page page, List tableData){
        HashMap<String, Object> map = new HashMap<>();
        if(page != null && tableData != null){
            map.put("content",tableData);
            map.put("count", page.getTotal());
        }
        return JSONArray.toJSONString(map);
    }

    // 动态菜单
    public static List<ElMenuBean> formatMenu(List<ElMenuBean> dataList, String parentId) {
        if (parentId.equals("")) {
            parentId = "0";
        }
        List<ElMenuBean> newTreeList = new ArrayList<ElMenuBean>();
        for (ElMenuBean data : dataList) {
            if (data.getParentId().equals(parentId)) {
                data.setChildren(formatMenu(dataList, data.getCompoId()));
                newTreeList.add(data);
            }
        }
        return newTreeList;
    }

    // 下拉树
    public static List<ElTreeBean> formatTree(List<ElTreeBean> dataList, String parentId) {
        if (parentId.equals("")) {
            parentId = "0";
        }
        List<ElTreeBean> newTreeList = new ArrayList<ElTreeBean>();
        for (ElTreeBean data : dataList) {
            if (data.getParentId().equals(parentId)) {
                data.setChildren(formatTree(dataList, data.getId()));
                newTreeList.add(data);
            }
        }
        return newTreeList;
    }

    // 级联选择器
    public static List<ElCascaderBean> formatCascader(List<ElCascaderBean> dataList, String parentId) {
        if (parentId.equals("")) {
            parentId = "0";
        }
        List<ElCascaderBean> elCascaderList = new ArrayList<ElCascaderBean>();
        for (ElCascaderBean data : dataList) {
            if (data.getParentId().equals(parentId)) {
                data.setChildren(formatCascader(dataList, data.getValue()));
                data.setLeaf(data.getChildren() == null || data.getChildren().size() == 0);
                elCascaderList.add(data);
            }
        }
        return elCascaderList.size() == 0 ? null : elCascaderList;
    }
}
