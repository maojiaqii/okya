package com.top.okya.dao;

import com.alibaba.fastjson.JSONArray;
import com.top.okya.pojo.BsNotices;
import com.top.okya.util.elHelp.ElTransferBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * (BsNotices)表数据库访问层
 *
 * @author mjq
 * @since 2021-04-25 09:50:12
 */
@Mapper
public interface NoticeMapper {

    List<ElTransferBean> getSelectUsers(@Param("userCode") String userCode);

    List<ElTransferBean> getAllUsers(@Param("setYear") String setYear);

    void saveFrequentContacts(@Param("userCode") String userCode, @Param("userCodes") JSONArray userCodes);

    void deleteFrequentContacts(@Param("userCode") String userCode);

    void saveNotice(@Param("bsNotices") BsNotices bsNotices);

    void saveNoticeTarget(@Param("noticeId") String noticeId, @Param("selectUser") String selectUser, @Param("receiveStatus") String receiveStatus);

    List<Map<String, Object>> receiveAllNotices(@Param("userCode") String userCode);

    void updateNoticeTarget(@Param("noticeId") JSONArray noticeId, @Param("selectUser") String selectUser, @Param("receiveStatus") String receiveStatus, @Param("checkDate") String checkDate);

    String getUnCheckCount(@Param("userCode") String userCode);

    List<String> getNoticeUsers(@Param("noticeId") String noticeId);
}