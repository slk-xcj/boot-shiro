package com.slk.xcj.pojo;

import java.io.Serializable;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;

    private String password;

    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间，具体值会在创建token是赋予
     */
    private Long expireTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", loginTime=" + loginTime +
                ", expireTime=" + expireTime +
                '}';
    }
}
