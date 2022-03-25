package com.top.okya.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * (AsFormSearchBusiness)实体类
 *
 * @author makejava
 * @since 2020-07-29 16:42:35
 */
@Data
public class AsFormSearchBusiness implements Serializable {
    private static final long serialVersionUID = 805708929196390736L;
    
    private String guid;
    
    private String formId;
    
    private String tableId;
    
    private String searchField;
    
    private String searchName;
    
    private String searchCompo;
    
    private String searchCondition;
    
    private String dataSource;

    public AsFormSearchBusiness() {
    }
}