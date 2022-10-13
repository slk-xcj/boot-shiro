package com.slk.xcj.shiro;

import com.slk.xcj.exception.ServiceException;
import com.slk.xcj.pojo.LoginUser;
import com.slk.xcj.shiro.service.TokenService;
import com.slk.xcj.utils.SpringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 登录注册之类的请求根本不会经过这里，因为在ShiroConfig中已经配置路径对应过滤器
// 这也导致了每次请求都会进行一次登录操作，每次到数据库中查用户数据，不合适。不知道这么理解对不对？
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return executeLogin(request, response);
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        TokenService tokenService = SpringUtils.getBean(TokenService.class);
        LoginUser loginUser = tokenService.getLoginUser((HttpServletRequest) request);
        if (loginUser != null) {
            // 刷新Redis中用户信息过去时间
            tokenService.verifyToken(loginUser);
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser.getUserName(), loginUser.getPassword());
            // 提交给realm进行登入，如果错误他会抛出异常并被捕获
            getSubject(request, response).login(usernamePasswordToken);
            return true;
        }
        throw new ServiceException(20001, "Token失效，请重新登录");
    }

    // 跨域主要为了退出用，退出的时候直接发送请求不会经过前端Axios跨域处理
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
