package com.slk.xcj.controller;

import com.slk.xcj.pojo.AjaxResult;
import com.slk.xcj.pojo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SysUserController {
    private final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @GetMapping(value = "/user/info")
    public AjaxResult getUserInfo() {

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", "Super Admin");
        userInfo.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        return AjaxResult.success(userInfo);
    }

    @GetMapping(value = "/table/list")
    public AjaxResult getTableList() {
        Subject subject = SecurityUtils.getSubject();
        // 获取到当前登录用户信息
        LoginUser loginUser = (LoginUser) subject.getPrincipal();
        logger.info(loginUser.toString());

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> bookMap = new HashMap<>();
        bookMap.put("id", "book_0001");
        bookMap.put("title", "Shiro在前后端分离中不好用");
        bookMap.put("author", "Super Cui");
        bookMap.put("display_time", "2022-10-13");
        bookMap.put("pageviews", "999+");
        bookMap.put("status", "火爆");

        list.add(bookMap);

        return AjaxResult.success(list);
    }
}
