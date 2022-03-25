package com.top.okya.pojo;

import lombok.Data;

/**
 * (AsFormMainTableColums)实体类
 *
 * @author maojiaqi
 * @since 2020-06-03 16:20:34
 */
@Data
public class AsFormMainTableColums {

    private String guid;
    
    private String formId;
    
    private String tableName;
    
    private String formField;
    
    private String tableField;
    
    private String tableFieldType;

    private String isNull;
    
    private String isUnique;
    
    private String joinTable;
    
    private String joinTableColumn;
    
    private String setYear;

    public AsFormMainTableColums() {
    }
}