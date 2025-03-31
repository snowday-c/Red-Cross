package com.example.redcross.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.redcross.entity.User;
import com.example.redcross.exception.UserException;
import com.example.redcross.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt拦截器
 */


@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从http请求的header中获取token
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }

        // 2. 开始执行认证
        if (StrUtil.isBlank(token)) {
            throw new UserException("无token，请重新登录");
        }

        // 3. 获取token中的userId
        String userId;
        User user;
        try {
            userId = JWT.decode(token).getAudience().get(0);
            user = userService.getUserById(Integer.parseInt(userId));
        } catch (Exception e) {
            throw new UserException("token验证失败，请重新登录");
        }

        // 4. 检查Redis中的token是否匹配（实现单点登录控制）
        String currentToken = JwtTokenUtils.getCurrentToken(user.getUserId());
        if (!token.equals(currentToken)) {
            throw new UserException("您的账号已在其他地方登录，当前会话已失效");
        }

        // 5. 验证token签名
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new UserException("token验证失败，请重新登录");
        }

        return true;
    }
}