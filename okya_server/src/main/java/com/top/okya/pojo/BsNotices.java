package com.top.okya.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * (BsNotices)实体类
 *
 * @author mjq
 * @since 2021-04-25 09:50:12
 */
@Data
public class BsNotices implements Serializable {
    private static final long serialVersionUID = -82079972694439733L;
    
    private String guid;
    
    private String noticeId;
    
    private String noticeTitle;
    
    private String noticeContent;
    
    private String noticeFile;
    
    private String createUserCode;
    
    private String createUserName;
    
    private String createDate;
    
    private String sendStatus;

    private String noticeLevel;

    public BsNotices() {
    }

    public BsNotices(String guid, String noticeId, String noticeTitle, String noticeContent, String noticeFile, String createUserCode, String createUserName, String createDate, String sendStatus, String noticeLevel) {
        this.guid = guid;
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.noticeFile = noticeFile;
        this.createUserCode = createUserCode;
        this.createUserName = createUserName;
        this.createDate = createDate;
        this.sendStatus = sendStatus;
        this.noticeLevel = noticeLevel;
    }
}