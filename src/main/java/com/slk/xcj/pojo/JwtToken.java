package com.slk.xcj.pojo;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {
    private static final long serialVersionUID = 1L;

    private String token;

    public JwtToken() {
    }

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
