package com.top.okya.util.elHelp;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/28 20:23
 * @describe： elementUI下拉树数据封装
 */

public class ElTreeBean {

    String id;

    String label;

    String parentId;

    List<ElTreeBean> children;

    public List<ElTreeBean> getChildren() {
        return children;
    }

    public void setChildren(List<ElTreeBean> children) {
        this.children = children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
