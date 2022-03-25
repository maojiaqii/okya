package com.top.okya.pojo;

import lombok.Data;

@Data
public class AsUserRole {
    private String guid;

    private String userCode;

    private String roleCode;

    private String setYear;

    public AsUserRole() {
    }

    public AsUserRole(String guid, String userCode, String roleCode, String setYear){
        this.guid = guid;
        this.userCode = userCode;
        this.roleCode = roleCode;
        this.setYear = setYear;
    }

    public AsUserRole(String userCode, String roleCode, String setYear){
        this.userCode = userCode;
        this.roleCode = roleCode;
        this.setYear = setYear;
    }

}