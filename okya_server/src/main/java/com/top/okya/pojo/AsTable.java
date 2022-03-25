package com.top.okya.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * (AsTable)实体类
 *
 * @author makejava
 * @since 2020-07-28 15:56:28
 */
@Data
public class AsTable implements Serializable {
    private static final long serialVersionUID = -99323714588485090L;
    
    private String guid;
    
    private String tableId;
    
    private String tableName;
    /**
    * 表格数据源
    */
    private String dataSource;
    /**
    * 表格控件尺寸（mini,small,medium）
    */
    private String size;
    /**
    * 表格最大高度
    */
    private String maxHeight;
    /**
    * 是否显示行号
    */
    private String showRowNum;
    /**
    * 是否显示页脚（批量删除、导出、分页）
    */
    private String showPagination;
    /**
    * 是否显示合计行
    */
    private String showSum;
    /**
    * 是否显示操作列
    */
    private String showOperation;

    /**
     * 表格内部操作按钮权限标志
     */
    private String permsNew;

    /**
    * 表格内部操作按钮权限标志
    */
    private String permsEdit;
    /**
    * 表格内部删除、批量删除按钮权限标志
    */
    private String permsDelete;
    /**
    * 表格内部导出Excel按钮权限标志
    */
    private String permsExport;
    
    private String setYear;

    public AsTable() {
    }

    public AsTable(String guid, String tableId, String tableName, String dataSource, String size, String maxHeight, String showRowNum, String showPagination, String showSum, String showOperation, String permsNew, String permsEdit, String permsDelete, String permsExport) {
        this.guid = guid;
        this.tableId = tableId;
        this.tableName = tableName;
        this.dataSource = dataSource;
        this.size = size;
        this.maxHeight = maxHeight;
        this.showRowNum = showRowNum;
        this.showPagination = showPagination;
        this.showSum = showSum;
        this.showOperation = showOperation;
        this.permsNew = permsNew;
        this.permsEdit = permsEdit;
        this.permsDelete = permsDelete;
        this.permsExport = permsExport;
    }
}