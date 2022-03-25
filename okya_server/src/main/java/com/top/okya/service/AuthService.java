package com.top.okya.service;

import com.top.okya.pojo.VtUserLogin;

public interface AuthService {

    /**
     * @title userLogin
     * @description
     * @author maojq
     * @param userCode 用户代码
     * @param setYear 年度
     * @updateTime 2022/3/7 15:36
     * @throws
     */
    VtUserLogin userLogin(String userCode, String setYear);
}
