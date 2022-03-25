package com.top.okya.service.impl;

import com.top.okya.dao.AsUserMapper;
import com.top.okya.dao.VtUserInfoMapper;
import com.top.okya.pojo.AsUser;
import com.top.okya.pojo.AsUserCompany;
import com.top.okya.pojo.AsUserRole;
import com.top.okya.pojo.VtUserInfo;
import com.top.okya.service.UserService;
import com.top.okya.util.common.HumpLineTrans;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author: maojiaqi
 * @Date: 2020/3/26 20:23
 * @describe：
 */

@Service("UserService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    VtUserInfoMapper vtUserInfoMapper;
    @Autowired
    AsUserMapper asUserMapper;

    @Override
    public List<VtUserInfo> findPage(String userName, String roleCode, String sortProp, String sortOrder, String setYear) {
        log.info("分页查询用户信息");
        return vtUserInfoMapper.findPage(setYear, userName, roleCode, HumpLineTrans.humpToLine(sortProp), sortOrder.equals("descending") ? "desc" : "asc");
    }

    @Override
    @Transactional
    public void saveUser(VtUserInfo vtUserInfo) {
        log.info("新增用户信息");
        AsUser asUser = new AsUser(UUID.randomUUID().toString(), vtUserInfo.getUserCode(), vtUserInfo.getUserName(),
                new SimpleHash("MD5", new SimpleHash("MD5", "1", "", 1).toHex(), "", 1).toHex(),
                vtUserInfo.getUsed().equals("是") ? "Y" : "N", vtUserInfo.getSetYear());
        AsUserRole asUserRole = new AsUserRole(UUID.randomUUID().toString(), vtUserInfo.getUserCode(), vtUserInfo.getRoleCode(), vtUserInfo.getSetYear());
        AsUserCompany asUserCompany = new AsUserCompany(UUID.randomUUID().toString(), vtUserInfo.getUserCode(), vtUserInfo.getCoCode(), vtUserInfo.getSetYear());
        asUserMapper.saveUser(asUser);
        asUserMapper.saveUserRole(asUserRole);
        asUserMapper.saveUserCompany(asUserCompany);
    }

    @Override
    @Transactional
    public void updateUser(VtUserInfo vtUserInfo) {
        log.info("修改用户信息");
        AsUser asUser = new AsUser(vtUserInfo.getUserCode(), vtUserInfo.getUserName(),
                vtUserInfo.getUsed().equals("是") ? "Y" : "N", vtUserInfo.getSetYear());
        AsUserRole asUserRole = new AsUserRole(vtUserInfo.getUserCode(), vtUserInfo.getRoleCode(), vtUserInfo.getSetYear());
        AsUserCompany asUserCompany = new AsUserCompany(vtUserInfo.getUserCode(), vtUserInfo.getCoCode(), vtUserInfo.getSetYear());
        asUserMapper.updateUser(asUser);
        asUserMapper.updateUserRole(asUserRole);
        asUserMapper.updateUserCompany(asUserCompany);
    }

    @Override
    @Transactional
    public void deleteUser(List<VtUserInfo> datas, String setYear) {
        log.info("删除用户信息");
        asUserMapper.deleteUser(datas, setYear);
        asUserMapper.deleteUserRole(datas, setYear);
        asUserMapper.deleteUserCompany(datas, setYear);
    }

    @Override
    public boolean editPass(String id, String pass, String oldPass) {
        return asUserMapper.editPass(id,new SimpleHash("MD5", pass, "", 1).toHex(),new SimpleHash("MD5", oldPass, "", 1).toHex());
    }
}
