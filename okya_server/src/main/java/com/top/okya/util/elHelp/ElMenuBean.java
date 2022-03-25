package com.top.okya.util.elHelp;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/23 14:08
 * @describe： elementUI菜单导航数据封装
 */
public class ElMenuBean {
    String compoId;
    String parentId;
    String compoName;
    String compoType;
    String icon;
    String url;
    String orderNum;
    String parentName;
    String describe;
    String compo;
    List<ElMenuBean> children;

    public String getCompoId() {
        return compoId;
    }

    public void setCompoId(String compoId) {
        this.compoId = compoId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCompoName() {
        return compoName;
    }

    public void setCompoName(String compoName) {
        this.compoName = compoName;
    }

    public String getCompoType() {
        return compoType;
    }

    public void setCompoType(String compoType) {
        this.compoType = compoType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCompo() {
        return compo;
    }

    public void setCompo(String compo) {
        this.compo = compo;
    }

    public List<ElMenuBean> getChildren() {
        return children;
    }

    public void setChildren(List<ElMenuBean> children) {
        this.children = children;
    }
}
