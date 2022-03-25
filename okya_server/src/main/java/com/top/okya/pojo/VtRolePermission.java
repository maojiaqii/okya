package com.top.okya.pojo;

import lombok.Data;

@Data
public class VtRolePermission {
    private String guid;

    private String roleCode;

    private String compoId;

    private String setYear;

    private String parentId;

    private String compo;

    private String compoName;

    private String compoType;

    private String icon;

    private String url;

    private String orderNum;

    public VtRolePermission() {
    }
}