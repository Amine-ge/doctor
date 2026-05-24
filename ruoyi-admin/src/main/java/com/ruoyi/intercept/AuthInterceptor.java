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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WecomProps wecomProps;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws IOException {
        String headerName = wecomProps.getAuthHeader();
        String raw = resolveHeader(req, headerName);
        if (raw == null || raw.trim().isEmpty()) {
            return unauthorized(resp, "缺少 " + headerName);
        }

        String token = raw.trim();
        token = token.regionMatches(true, 0, "Bearer ", 0, 7) ? token.substring(7).trim() : token;

        io.jsonwebtoken.Claims claims;
        try {
            claims = MyJwtUtil.parseJWT(wecomProps.getSecret(), token);
        } catch (Exception e) {
            return unauthorized(resp, "token 无效");
        }

        String userJson = (String) claims.get(JwtClaimsConstant.USER);
        AiUser me = JSONUtil.toBean(userJson, AiUser.class);

        String key = JwtClaimsConstant.REDIS_TOKEN_PREFIX + me.getId();
        String cached = stringRedisTemplate.opsForValue().get(key);
        if (cached == null || !cached.equals(token)) {
            return unauthorized(resp, "token 已过期或失效");
        }

        UserHold.set(me);
        return true;
    }

    private static String resolveHeader(HttpServletRequest req, String configuredName) {
        List<String> candidates = new ArrayList<>();
        if (configuredName != null && !configuredName.trim().isEmpty()) {
            candidates.add(configuredName.trim());
        }
        candidates.add("X-Wecom-Token");
        candidates.add("X-wecom-token");
        candidates.add("x-wecom-token");

        for (String candidate : candidates) {
            String value = req.getHeader(candidate);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        }

        Enumeration<String> names = req.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
            String actualName = names.nextElement();
            for (String candidate : candidates) {
                if (actualName.equalsIgnoreCase(candidate)) {
                    String value = req.getHeader(actualName);
                    if (value != null && !value.trim().isEmpty()) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    private static boolean unauthorized(HttpServletResponse resp, String msg) throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\"}");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }
}
