package com.ruoyi.intercept;

import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.consts.JwtClaimsConstant;
import com.ruoyi.properties.WecomProps;
import com.ruoyi.utils.MyJwtUtil;
import com.ruoyi.utils.UserHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WecomProps wecomProps;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws IOException {
        String headerName = wecomProps.getAuthHeader();
        String raw = req.getHeader(headerName);
        if (raw == null || raw.isEmpty()) return unauthorized(resp, "缺少 X-Wecom-Token");

        // 兼容可能误加了 "Bearer "
        String token = raw.startsWith("Bearer ") ? raw.substring(7) : raw;

        // 1) 解析 JWT
        io.jsonwebtoken.Claims claims;
        try {
            claims = MyJwtUtil.parseJWT(wecomProps.getSecret(), token);
        } catch (Exception e) {
            return unauthorized(resp, "token 无效");
        }

        // 2) 取出用户 JSON -> 对象
        String userJson = (String) claims.get(JwtClaimsConstant.USER);
        AiUser me = JSONUtil.toBean(userJson, AiUser.class);

        // 3) Redis 二次校验（顶号/过期）
        String key = JwtClaimsConstant.REDIS_TOKEN_PREFIX + me.getId(); // login:tk:<id>
        String cached = stringRedisTemplate.opsForValue().get(key);
        if (cached == null || !cached.equals(token)) {
            return unauthorized(resp, "token 已过期或失效");
        }

        // 4) 放到上下文（两种都给）
//        req.setAttribute("currentTeacher", me);
//        UserContext.set(me);
        UserHold.set(me);
        return true;
    }

    private static boolean unauthorized(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\"}");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        UserContext.clear();
    }
}
