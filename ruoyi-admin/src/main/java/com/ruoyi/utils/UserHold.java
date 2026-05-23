package com.ruoyi.utils;


import com.ruoyi.ai.domain.AiUser;

public class UserHold {
    private static final ThreadLocal<AiUser> TL = new ThreadLocal<>();

    public static void set(AiUser user) { TL.set(user); }
    public static AiUser get() { return TL.get(); }
    public static void clear() { TL.remove(); }
}
