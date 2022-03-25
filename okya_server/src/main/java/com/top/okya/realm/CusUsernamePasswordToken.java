package com.top.okya.realm;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author: maojiaqi
 * @Date: 2020/3/30 17:41
 * @describe： 重写UsernamePasswordToken,支持检验年度
 */
public class CusUsernamePasswordToken extends UsernamePasswordToken {

    private String setYear;
    public CusUsernamePasswordToken(String loginName, String password, boolean rememberMe, String setYear){
        super(loginName, password, rememberMe);
        this.setYear = setYear;
    }

    public String getSetYear() {
        return setYear;
    }
}
