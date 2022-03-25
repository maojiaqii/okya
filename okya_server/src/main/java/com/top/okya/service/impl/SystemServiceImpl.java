package com.top.okya.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.top.okya.dao.SystemMapper;
import com.top.okya.service.SystemService;
import com.top.okya.system.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author: maojiaqi
 * @Date: 2020/8/28 15:48
 * @describe：
 */
@Service("SystemService")
public class SystemServiceImpl implements SystemService {

    @Autowired
    SystemMapper systemMapper;

    @Override
    public String save(String sysTitle, String sysIcon) throws Exception {

        if(sysIcon == null || sysIcon == ""){
            systemMapper.save(sysTitle, null);
            return "";
        } else {
            String dataPrix = ""; //base64格式前头
            String data = "";//实体部分数据
            String[] d = sysIcon.split("base64,");//将字符串分成数组
            if (d != null && d.length == 2) {
                dataPrix = d[0];
                data = d[1];
            } else {
                return "数据不合法";
            }
            String suffix = "";//图片后缀，用以识别哪种格式数据
            //data:image/jpeg;base64,base64编码的jpeg图片数据
            if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {
                suffix = ".jpg";
            } else if ("data:image/x-icon;".equalsIgnoreCase(dataPrix)) {
                //data:image/x-icon;base64,base64编码的icon图片数据
                suffix = ".ico";
            } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {
                //data:image/gif;base64,base64编码的gif图片数据
                suffix = ".gif";
            } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {
                //data:image/png;base64,base64编码的png图片数据
                suffix = ".png";
            } else {
                return "文件不合法";
            }
            String imgFilePath = ServerConfig.uploadFolder + "logo" + suffix;//新生成的图片
            BASE64Decoder decoder = new BASE64Decoder();
            //Base64解码
            byte[] b = decoder.decodeBuffer(data);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    //调整异常数据
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            String imgurl = "/file/" + "logo" + suffix;
            systemMapper.save(sysTitle, imgurl);
            return imgurl;
        }
    }

    @Override
    public String getSetup() {
        return JSONObject.toJSONString(systemMapper.getSetup());
    }
}
