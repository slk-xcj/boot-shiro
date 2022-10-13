package com.slk.xcj.controller;

import com.slk.xcj.constant.Constants;
import com.slk.xcj.pojo.AjaxResult;
import com.slk.xcj.pojo.LoginFormVo;
import com.slk.xcj.shiro.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping(value = "/login")
    public AjaxResult login(@RequestBody LoginFormVo loginFormVo) {
        AjaxResult ajaxResult = AjaxResult.success();
        String token = loginService.login(loginFormVo.getUsername(), loginFormVo.getPassword());
        ajaxResult.put(Constants.TOKEN, token);
        return ajaxResult;
    }
}
