package com.slk.xcj.shiro;

import com.slk.xcj.pojo.LoginUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroRealm extends AuthorizingRealm {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // 设置Realm使用UsernamePasswordToken
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("--->  用户权限处理");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        logger.info("--->  用户登录处理");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 用户输入名称
        String userName = token.getUsername();

        // 根据用户名到数据库中查找用户信息，这里模拟一下
        LoginUser loginUser;
        if ("admin".equals(userName)) {
            loginUser = new LoginUser();
            loginUser.setUserName("admin");
            String password = new SimpleHash("md5", "admin123", "admin").toString();
            loginUser.setPassword(password);
        } else {
            return null;
        }

        ByteSource salt = ByteSource.Util.bytes(loginUser.getUserName());

        // 方法中都是数据库中查询出来的，所以比较的应该是传过去的参数和UsernamePasswordToken中的属性值
        return new SimpleAuthenticationInfo(
                loginUser,
                loginUser.getPassword(),
                salt,
                getName()
        );
    }
}
