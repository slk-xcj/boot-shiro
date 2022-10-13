package com.slk.xcj.shiro.service;

import com.slk.xcj.constant.Constants;
import com.slk.xcj.pojo.LoginUser;
import com.slk.xcj.redis.RedisCache;
import com.slk.xcj.utils.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class TokenService {
    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    private static final Long MILLIS_MINUTE_TEN = 10 * 60 * 1000L;

    @Resource
    private RedisCache redisCache;

    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带token
        String token = getToken(request);
        if (StringUtils.hasLength(token)) {
            Claims claims = parseToken(token);
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            return redisCache.getCacheObject(userKey);
        }
        return null;
    }

    public void delLoginUser(String token) {
        if (StringUtils.hasLength(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    private Claims parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);

        return createToken(claims);
    }

    private void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() +  MILLIS_MINUTE_TEN);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, 30, TimeUnit.MINUTES);
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_USER_KEY + uuid;
    }

    private String createToken(Map<String, Object> claims) {
        byte[] bytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(bytes);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.hasLength(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }
}
