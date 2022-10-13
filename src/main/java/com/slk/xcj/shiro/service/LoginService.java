package com.slk.xcj.shiro.service;

import com.slk.xcj.exception.ServiceException;
import com.slk.xcj.pojo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginService {

    @Resource
    private TokenService tokenService;

    public String login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            LoginUser loginUser = new LoginUser();
            loginUser.setUserName(username);
            loginUser.setPassword(password);
            return tokenService.createToken(loginUser);
        } catch (UnknownAccountException e) {
            throw new ServiceException(20001, "用户名不存在");
        } catch (IncorrectCredentialsException e) {
            throw new ServiceException(20001, "用户名或密码错误");
        }
    }
}
