package com.ruoyi.ai.Enum;

public enum VisionType {
    TONGUE("tongue"),
    FACE("face"),
    NAIL("nail");
    private final String code;

    VisionType(String code) { this.code = code; }

    public static VisionType fromCode(String code) {
        for (VisionType t : values()) {
            if (t.code.equalsIgnoreCase(code)) return t;
        }
        throw new IllegalArgumentException("未知的vision type: " + code);
    }
}
