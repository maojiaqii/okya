package com.top.okya.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * (AsTableColumns)实体类
 *
 * @author makejava
 * @since 2020-07-28 15:56:44
 */
@Data
public class AsTableColumns implements Serializable {
    private static final long serialVersionUID = -25837122921892182L;

    private String guid;

    private String prop;

    private String label;
    /**
     * 列宽度
     */
    private String width;
    /**
     * 是否可排序
     */
    private String sortable;
    /**
     * 是否可见
     */
    private String visible;
    /**
     * 列头提示内容
     */
    private String content;
    /**
     * 是否可过滤
     */
    private String filter;
    /**
     * 数据对齐方式，默认居中
     */
    private String align;
    /**
     * 列最小宽度
     */
    private String minWidth;

    private String setYear;

    private String tableId;

    public AsTableColumns() {
    }

}