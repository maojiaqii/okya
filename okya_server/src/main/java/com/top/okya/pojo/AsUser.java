package com.top.okya.pojo;

import lombok.Data;

@Data
public class AsUser {
    private String guid;

    private String userCode;

    private String userName;

    private String passWord;

    private String used;

    private String setYear;

    public AsUser() {
    }

    public AsUser(String guid, String userCode, String userName, String passWord, String used, String setYear){
        this.guid = guid;
        this.userCode = userCode;
        this.userName = userName;
        this.passWord = passWord;
        this.used = used;
        this.setYear = setYear;
    }

    public AsUser(String userCode, String userName, String used, String setYear){
        this.userCode = userCode;
        this.userName = userName;
        this.used = used;
        this.setYear = setYear;
    }

}