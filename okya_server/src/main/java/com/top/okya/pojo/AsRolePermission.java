package com.top.okya.pojo;

import lombok.Data;

@Data
public class AsRolePermission {
    private String guid;

    private String roleCode;

    private String compoId;

    private String setYear;

    public AsRolePermission() {
    }

    public AsRolePermission(String guid, String roleCode, String compoId, String setYear) {
        this.guid = guid;
        this.roleCode = roleCode;
        this.compoId = compoId;
        this.setYear = setYear;
    }

}