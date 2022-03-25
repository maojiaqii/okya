package com.top.okya.pojo;

import lombok.Data;

@Data
public class AsCompany {
    private String guid;

    private String coCode;

    private String coName;

    private String parentCode;

    private String setYear;

    private String isLeaf;

    public AsCompany() {
    }
}