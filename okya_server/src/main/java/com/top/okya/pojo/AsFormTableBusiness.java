package com.top.okya.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * (AsFormTableBusiness)实体类
 *
 * @author makejava
 * @since 2020-07-28 15:53:59
 */
@Data
public class AsFormTableBusiness implements Serializable {
    private static final long serialVersionUID = -25348072942166417L;
    
    private String guid;
    
    private String formId;
    
    private String tableId;
    
    private String setYear;

    public AsFormTableBusiness() {
    }

    public AsFormTableBusiness(String guid, String formId, String tableId) {
        this.formId = formId;
        this.guid = guid;
        this.tableId = tableId;
    }

}