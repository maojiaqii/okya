package com.top.okya.pojo;

import lombok.Data;

@Data
public class VtUserLogin {
    private String userCode;

    private String userName;

    private String passWord;

    private String used;

    private String setYear;

    private String roleCode;

    private String roleName;

    public VtUserLogin() {
    }
}