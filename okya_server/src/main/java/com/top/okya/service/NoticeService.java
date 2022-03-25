package com.top.okya.service;

/**
 * @author: maojiaqi
 * @Date: 2021/4/24 10:30
 * @describe：
 */
public interface NoticeService {
    String save(String datas) throws Exception;

    String getAllUsers(String setYear);

    String getSelectUsers(String userCode);

    void saveFrequentContacts(String userCode, String userCodes);

    boolean receiveAllNotices(String userCode);

    void updateNoticeTarget(String userCode, String noticeId, String receiveStatus);

    String getUnCheckCount(String userCode);

    String getNoticeUsers(String noticeId);
}
