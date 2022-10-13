package com.slk.xcj.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slk.xcj.pojo.AjaxResult;
import com.slk.xcj.pojo.LoginUser;
import com.slk.xcj.shiro.service.TokenService;
import com.slk.xcj.utils.SpringUtils;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroLogoutFilter extends LogoutFilter {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        TokenService tokenService = SpringUtils.getBean(TokenService.class);
        LoginUser loginUser = tokenService.getLoginUser((HttpServletRequest) request);
        if (loginUser != null) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
        }
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(200);
        res.setContentType("application/json");
        res.setCharacterEncoding("utf-8");
        res.getWriter().print(objectMapper.writeValueAsString(AjaxResult.success("200", "退出成功")));

        // 返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        return false;
    }
}
