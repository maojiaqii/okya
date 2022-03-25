package com.top.okya.util.elHelp;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/4/12 20:26
 * @describe： el-Cascader数据封装
 */
public class ElCascaderBean {

    String label;
    String value;
    boolean leaf;
    String parentId;
    List<ElCascaderBean> children;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public List<ElCascaderBean> getChildren() {
        return children;
    }

    public void setChildren(List<ElCascaderBean> children) {
        this.children = children;
    }
}
