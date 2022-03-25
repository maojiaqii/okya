package com.top.okya.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.top.okya.dao.NoticeMapper;
import com.top.okya.pojo.BsNotices;
import com.top.okya.service.NoticeService;
import com.top.okya.system.config.ServerConfig;
import com.top.okya.util.common.DateFormatUtil;
import com.top.okya.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author: maojiaqi
 * @Date: 2020/8/28 15:48
 * @describe：
 */
@Service("NoticeService")
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    private static final String fileAddress = "/file/business/";

    @Autowired
    NoticeMapper noticeMapper;

    @Override
    public boolean receiveAllNotices(String userCode) {
        List<Map<String, Object>> allNotices = noticeMapper.receiveAllNotices(userCode);
        if (allNotices != null && allNotices.size() > 0) {
            log.info("查询到用户编号：" + userCode + "有" + allNotices.size() + "条未送达消息");
            JSONArray jsonArray = new JSONArray();
            allNotices.forEach(v -> {
                String noticeLevel = v.get("NOTICE_LEVEL").toString();
                String fColor = "green";
                String fText = "一般";
                if (noticeLevel.equals("1")) {
                    fColor = "orange";
                    fText = "中等";
                }
                if (noticeLevel.equals("2")) {
                    fColor = "red";
                    fText = "特急";
                }
                if (WebSocketServer.sendInfo("<i>来自：<strong><font color=\"blue\">" + v.get("CREATE_USER_NAME").toString() + "</font></strong>的消息</br>发送时间：<u>" + v.get("CREATE_DATE").toString() + "</u></br>紧急程度：<font color=\"" + fColor + "\">" + fText + "</font></br>请点击右上角头像中的<font color=\"red\">消息中心</font>查看！<i>", userCode)) {
                    log.info("给用户编号：" + userCode + "发送消息标号：" + v.get("NOTICE_ID").toString() + "成功");
                    jsonArray.add(v.get("NOTICE_ID").toString());
                }
            });
            noticeMapper.updateNoticeTarget(jsonArray, userCode, "1", null);
            log.info("批量更新消息接收状态完成");
            return true;
        }
        log.info("未查询到用户编号：" + userCode + "有未送达消息");
        return false;
    }

    @Override
    @Transactional
    public String save(String datas) throws Exception {
        log.info("开始保存消息");
        JSONObject jsonObject = JSONObject.parseObject(datas);
        String noticeId = jsonObject.get("NOTICE_ID").toString();
        String noticeFile = jsonObject.get("NOTICE_FILE").toString();
        log.info(noticeFile);
        String noticeLevel = jsonObject.get("NOTICE_LEVEL").toString();
        BsNotices bsNotices = new BsNotices(UUID.randomUUID().toString(), noticeId, jsonObject.get("NOTICE_TITLE").toString(), jsonObject.get("NOTICE_CONTENT").toString(), "", jsonObject.get("userCode").toString(), jsonObject.get("userName").toString(), DateFormatUtil.getYearTimeWithCharacter(), "0", noticeLevel);
        if (!noticeFile.startsWith("[")) {
            bsNotices.setNoticeFile(noticeFile);
        } else {
            JSONArray fileArrays = JSONObject.parseArray(noticeFile);
            if (fileArrays != null && fileArrays.size() > 0) {
                for(Object aFile : fileArrays){
                    JSONObject jo = JSONObject.parseObject(aFile.toString());
                    String fileName = UUID.randomUUID().toString() + "." + jo.get("name").toString().split("\\.")[1];
                    log.info("客户端文件名：" + jo.get("name") + "在服务端存储的文件名称：" + fileName);
                    bsNotices.setNoticeFile(fileAddress + fileName + "," + bsNotices.getNoticeFile());
                    String[] d = jo.get("data").toString().split("base64,");//将字符串分成数组
                    if (d != null && d.length == 2) {
                        BASE64Decoder decoder = new BASE64Decoder();
                        //Base64解码
                        byte[] b = decoder.decodeBuffer(d[1]);
                        for (int i = 0; i < b.length; ++i) {
                            if (b[i] < 0) {
                                //调整异常数据
                                b[i] += 256;
                            }
                        }
                        OutputStream out = new FileOutputStream(ServerConfig.uploadFolder + "/business/" + fileName);
                        out.write(b);
                        out.flush();
                        out.close();
                        log.info("文件保存成功");
                    } else {
                        throw new Exception("数据不合法，无法生成上传的文件");
                    }
                }
            }
        }
        noticeMapper.saveNotice(bsNotices);
        for (Object uc : JSONArray.parseArray(jsonObject.get("selectUser").toString())) {
            if (WebSocketServer.isLogin(uc.toString())) {
                String fColor = "green";
                String fText = "一般";
                if (noticeLevel.equals("1")) {
                    fColor = "orange";
                    fText = "中等";
                }
                if (noticeLevel.equals("2")) {
                    fColor = "red";
                    fText = "特急";
                }
                if (WebSocketServer.sendInfo("<i>来自：<strong><font color=\"blue\">" + bsNotices.getCreateUserName() + "</font></strong>的消息</br>发送时间：<u>" + bsNotices.getCreateDate() + "</u></br>紧急程度：<font color=\"" + fColor + "\">" + fText + "</font></br>请点击右上角头像中的<font color=\"red\">消息中心</font>查看！<i>", uc.toString())) {
                    noticeMapper.saveNoticeTarget(noticeId, uc.toString(), "1");
                } else {
                    noticeMapper.saveNoticeTarget(noticeId, uc.toString(), "0");
                }
            } else {
                noticeMapper.saveNoticeTarget(noticeId, uc.toString(), "0");
            }
        }
        log.info("消息保存成功");
        return bsNotices.getNoticeFile();
    }

    @Override
    public String getAllUsers(String setYear) {
        return JSONArray.toJSONString(noticeMapper.getAllUsers(setYear));
    }

    @Override
    public String getSelectUsers(String userCode) {
        return JSONArray.toJSONString(noticeMapper.getSelectUsers(userCode));
    }

    @Override
    @Transactional
    public void saveFrequentContacts(String userCode, String userCodes) {
        noticeMapper.deleteFrequentContacts(userCode);
        noticeMapper.saveFrequentContacts(userCode, JSONArray.parseArray(userCodes));
    }

    @Override
    public void updateNoticeTarget(String userCode, String noticeId, String receiveStatus) {
        noticeMapper.updateNoticeTarget(JSONArray.parseArray(noticeId), userCode, receiveStatus, receiveStatus.equals("2") ? DateFormatUtil.getYearTimeWithCharacter() : null);
    }

    @Override
    public String getUnCheckCount(String userCode) {
        return noticeMapper.getUnCheckCount(userCode);
    }

    @Override
    public String getNoticeUsers(String noticeId) {
        return JSONArray.toJSONString(noticeMapper.getNoticeUsers(noticeId));
    }
}
