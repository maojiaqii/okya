package com.top.okya.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * (AsFinance)实体类
 *
 * @author maojiaqi
 * @since 2020-06-08 18:18:09
 */
@Data
public class AsFinance implements Serializable {
    private static final long serialVersionUID = -73869302146964767L;
    
    private String guid;
    
    private String financeCode;
    
    private String financeName;
    
    private String admCode;
    
    private String ip;
    
    private String port;
    
    private String setYear;
    
    private String hold1;
    
    private String hold2;

    public AsFinance() {
    }
}