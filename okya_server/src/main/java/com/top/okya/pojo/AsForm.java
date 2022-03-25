package com.top.okya.pojo;

import lombok.Data;

/**
 * (AsForm)实体类
 *
 * @author maojiaqi
 * @since 2020-05-30 10:15:39
 */

@Data
public class AsForm{

    private String guid;

    private String formId;

    private String formName;

    private String form;

    private String setYear;

    public AsForm() {
    }

    public AsForm(String guid, String formId, String formName, String form, String setYear) {
        this.guid = guid;
        this.formId = formId;
        this.formName = formName;
        this.form = form;
        this.setYear = setYear;
    }

    public AsForm(String guid, String formId, String formName, String form) {
        this.guid = guid;
        this.formId = formId;
        this.formName = formName;
        this.form = form;
    }
}