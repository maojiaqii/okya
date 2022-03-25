package com.top.okya.realm;

import com.top.okya.pojo.VtUserLogin;
import com.top.okya.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: maojiaqi
 * @Date: 2020/3/18 17:56
 * @describe： shiro用户验证授权
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private AuthService authService;

    // 对登录用户的授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    // 对登录用户的认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("-------开始身份认证--------");
        // token由控制层传入，获取token中存储的用户名
        CusUsernamePasswordToken token = (CusUsernamePasswordToken) authenticationToken;
        String userName = (String) token.getPrincipal();
        String setYear = token.getSetYear();
        // 根据用户名去数据库查询是否存在该用户
        VtUserLogin user = authService.userLogin(userName, setYear);
        if (user != null) {
            // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
            return new SimpleAuthenticationInfo(user, user.getPassWord(), getName());
        } else {
            log.info("-------身份认证失败：用户名【"+userName+"】不存在--------");
            throw new AccountException("用户名不正确");
        }
    }
}
