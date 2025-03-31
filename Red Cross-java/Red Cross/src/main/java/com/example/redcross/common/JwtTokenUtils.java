package com.example.redcross.common;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.redcross.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtTokenUtils {

    private static UserService staticUserService;
    private static StringRedisTemplate staticRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private UserService userService;


    @Autowired
    private  StringRedisTemplate redisTemplate;


    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }
    @PostConstruct
    public void init() {
        staticUserService = this.userService;
        staticRedisTemplate = this.redisTemplate;
    }

    /**
     * 生成token并存入Redis（实现单点登录）
     */
    public static String genToken(String userId, String password) {
        // 生成新token前，先清除旧的token
        invalidateToken(Integer.valueOf(userId));

        String token = JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 12))
                .sign(Algorithm.HMAC256(password));

        // 将token存入Redis，key格式为 USER_TOKEN:userId
        staticRedisTemplate.opsForValue().set(
                "USER_TOKEN:" + userId,
                token,
                12, // 过期时间12小时
                TimeUnit.HOURS
        );
        return token;
    }

    /**
     * 获取当前用户的token（从Redis）
     */
    public static String getCurrentToken(Integer userId) {
        return staticRedisTemplate.opsForValue().get("USER_TOKEN:" + userId);
    }

    /**
     * 使token失效（登出或单点登录时使用）
     */
    public static void invalidateToken(Integer userId) {
        staticRedisTemplate.delete("USER_TOKEN:" + userId);
    }


//    public static User getCurrentUser() {
//        String token = null;
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            token = request.getHeader("token");
//            if (StrUtil.isBlank(token)) {
//                token = request.getParameter("token");
//            }
//            if (StrUtil.isBlank(token)) {
//                log.error("获取当前登录的token失败， token: {}", token);
//                return null;
//            }
//            // 解析token，获取用户的id
//            String userId = JWT.decode(token).getAudience().get(0);
//            return staticUserService.getUserById(Integer.valueOf(userId));
//        } catch (Exception e) {
//            log.error("获取当前登录的用户信息失败, token={}", token,  e);
//            return null;
//        }
//    }
}