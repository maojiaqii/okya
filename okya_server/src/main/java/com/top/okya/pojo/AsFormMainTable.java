package com.top.okya.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * (AsFormMainTable)实体类
 *
 * @author makejava
 * @since 2020-07-15 14:10:23
 */
@Data
public class AsFormMainTable implements Serializable {
    private static final long serialVersionUID = 288820955307378112L;

    private String guid;

    private String formId;

    private String tableName;

    private String setYear;

    private String isMainTable;

    private String hasSetYear;

    private String mainFormId;

    private String viewSql;

    public AsFormMainTable() {
    }
}