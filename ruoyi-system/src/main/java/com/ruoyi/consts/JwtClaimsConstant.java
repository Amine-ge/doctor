package com.ruoyi.consts;

// JwtClaimsConstant.java
public interface JwtClaimsConstant {
    String USER = "user";                    // claims 里放整段用户 JSON 的 key
    String REDIS_TOKEN_PREFIX = "login:tk:"; // Redis 前缀
}
