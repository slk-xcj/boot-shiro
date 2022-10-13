package com.slk.xcj.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager")SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("logout", new ShiroLogoutFilter());

        factoryBean.setFilters(filterMap);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/captchaImage", "anon");

        // 退出 logout地址
        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return factoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("shiroRealm") ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);

        // 关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultWebSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);

        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    // 系统身份验证和权限验证
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("md5");
        credentialsMatcher.setHashIterations(1);

        // 是否使用缓存
        shiroRealm.setCachingEnabled(true);
        shiroRealm.setCredentialsMatcher(credentialsMatcher);

        return shiroRealm;
    }
}
