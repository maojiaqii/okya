package com.top.okya.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * (BsNoticeTarget)实体类
 *
 * @author mjq
 * @since 2021-04-25 09:52:29
 */
@Data
public class BsNoticeTarget implements Serializable {
    private static final long serialVersionUID = 870087706075446318L;
    
    private String guid;
    
    private String noticeId;
    
    private String userCode;
    
    private String receiveStatus;
    
    private String checkDate;

    public BsNoticeTarget() {
    }

    public BsNoticeTarget(String guid, String noticeId, String userCode, String receiveStatus, String checkDate) {
        this.guid = guid;
        this.noticeId = noticeId;
        this.userCode = userCode;
        this.receiveStatus = receiveStatus;
        this.checkDate = checkDate;
    }
}