package com.top.okya.pojo;

import lombok.Data;

@Data
public class AsUserCompany {
    private String guid;

    private String userCode;

    private String coCode;

    private String setYear;

    public AsUserCompany() {
    }

    public AsUserCompany(String guid, String userCode, String coCode, String setYear) {
        this.guid = guid;
        this.userCode = userCode;
        this.coCode = coCode;
        this.setYear = setYear;
    }

    public AsUserCompany(String userCode, String coCode, String setYear) {
        this.userCode = userCode;
        this.coCode = coCode;
        this.setYear = setYear;
    }

}