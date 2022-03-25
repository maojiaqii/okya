package com.top.okya.service;

import com.top.okya.pojo.VtUserInfo;

import java.util.List;

/**
 * @author: maojiaqi
 * @Date: 2020/3/26 20:05
 * @describe：
 */
public interface UserService {

    List<VtUserInfo> findPage(String userName, String roleCode, String sortProp, String sortOrder, String setYear);

    void saveUser(VtUserInfo vtUserInfo);

    void updateUser(VtUserInfo vtUserInfo);

    void deleteUser(List<VtUserInfo> datas, String setYear);

    boolean editPass(String id, String pass, String oldPass);
}
