package com.top.okya.service.impl;

import com.top.okya.dao.VtUserLoginMapper;
import com.top.okya.pojo.VtUserLogin;
import com.top.okya.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ComponentDataService")
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private VtUserLoginMapper vtUserLoginMapper;

    @Override
    public VtUserLogin userLogin(String userCode, String setYear) {
        List<VtUserLogin> vtUserLogins = vtUserLoginMapper.userLogin(userCode, setYear);
        if (vtUserLogins != null && vtUserLogins.size() > 0) {
            return vtUserLogins.get(0);
        } else {
            return null;
        }
    }

}
