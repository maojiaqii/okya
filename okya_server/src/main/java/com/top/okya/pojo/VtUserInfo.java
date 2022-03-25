package com.top.okya.pojo;

import lombok.Data;

@Data
public class VtUserInfo {
    private String userCode;

    private String userName;

    private String used;

    private String setYear;

    private String roleCode;

    private String roleName;

    private String coCode;

    private String coName;

    public VtUserInfo() {
    }
}