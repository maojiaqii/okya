package com.top.okya.controller;

import com.alibaba.fastjson.JSONArray;
import com.top.okya.pojo.VtUserLogin;
import com.top.okya.realm.CusUsernamePasswordToken;
import com.top.okya.service.NoticeService;
import com.top.okya.service.UserService;
import com.top.okya.util.response.ResponseObject;
import com.top.okya.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author: maojiaqi
 * @Date: 2020/3/18 17:56
 * @describe： 用户登录验证接口
 */

@Controller
@CrossOrigin
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private NoticeService noticeService;

    // 用户登录
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(HttpSession session, @RequestParam("userCode") String userCode,
                            @RequestParam("password") String password, @RequestParam("setYear") String setYear) {
        log.info("-------开始执行用户登录认证：用户代码【" + userCode + "】--------");
        // 从SecurityUtils里边创建一个 subject
        Subject subject = SecurityUtils.getSubject();
        // 在认证提交前准备 token（令牌）
        CusUsernamePasswordToken token = new CusUsernamePasswordToken(userCode, password, false, setYear);
        // 执行认证登陆
        try {
            subject.login(token);
            log.info("-------用户【" + userCode + "】认证成功--------");
            VtUserLogin user = (VtUserLogin) subject.getPrincipals().getPrimaryPrincipal();
            return new ResponseObject(true, JSONArray.toJSONString(user), (String) subject.getSession().getId()).invoke();
        } catch (UnknownAccountException uae) {
            log.info("-------用户【" + userCode + "】认证失败--------");
            return new ResponseObject(false, "未知账户！", null).invoke();
        } catch (IncorrectCredentialsException ice) {
            log.info("-------用户【" + userCode + "】认证失败--------");
            return new ResponseObject(false, "密码不正确！", null).invoke();
        } catch (LockedAccountException lae) {
            log.info("-------用户【" + userCode + "】认证失败--------");
            return new ResponseObject(false, "账户已锁定!", null).invoke();
        } catch (ExcessiveAttemptsException eae) {
            log.info("-------用户【" + userCode + "】认证失败--------");
            return new ResponseObject(false, "用户名或密码错误次数过多!", null).invoke();
        } catch (AuthenticationException ae) {
            log.info("-------用户【" + userCode + "】认证失败--------");
            return new ResponseObject(false, "用户名或密码不正确！", null).invoke();
        }
    }

    // 用户登出
    @RequestMapping(value = "/userLogout", method = RequestMethod.POST)
    @ResponseBody
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        String userCode = ((VtUserLogin) subject.getPrincipal()).getUserCode();
        log.info("-------开始执行用户登出：用户代码【" + userCode + "】--------");
        subject.logout();
        log.info("-------用户登出成功：用户代码【" + userCode + "】--------");
        return new ResponseObject(true, "登出成功", null).invoke();
    }

    /*// 用户登录状态
    @RequestMapping(value = "/getAuth", method = RequestMethod.GET)
    @ResponseBody
    public Integer isAuth(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated())
        {
            log.info("-------检查用户代码【"+subject.getPrincipal()+"】登录状态：已登录--------");
            return 1;
        }
        log.info("-------检查用户代码【"+subject.getPrincipal()+"】登录状态：未登录--------");
        return 0;
    }*/
    @RequestMapping(value = "/editPass", method = RequestMethod.POST)
    @ResponseBody
    public String editPass(@RequestParam("id") String id, @RequestParam("pass") String pass, @RequestParam("oldPass") String oldPass) {
        try {
            boolean flag = userService.editPass(id, pass, oldPass);
            return new ResponseObject(flag, "", null).invoke();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject(false, "修改失败", null).invoke();

        }

    }

    @RequestMapping(value = "/receiveAllNotices", method = RequestMethod.POST)
    @ResponseBody
    public String receiveAllNotices(@RequestParam("userCode") String userCode) {
        try {
            if (WebSocketServer.isLogin(userCode)) {
                return new ResponseObject(noticeService.receiveAllNotices(userCode), noticeService.getUnCheckCount(userCode), null).invoke();
            } else {
                return new ResponseObject(false, "未登录消息中心服务器，获取消息提醒失败！", null).invoke();
            }
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseObject(false, "发生未知异常，获取消息提醒失败！", null).invoke();
        }
    }
}
