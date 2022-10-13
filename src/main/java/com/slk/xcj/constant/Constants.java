package com.slk.xcj.constant;

public class Constants {
    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 登录用户 redis key，往Redis中存储的时候带冒号的，前面的部分就是命名空间后边的值作为命名空间中的键
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    public static final String HEADER = "Authorization";
}
