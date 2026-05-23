package com.ruoyi.ai.utils;

import cn.hutool.json.JSONUtil;

public final class VisionJsonUtils {

    private VisionJsonUtils() {}

    public static String extractJsonObject(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("visionJson is null");
        }

        String s = raw.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("visionJson is empty");
        }

        // 去掉 ```json / ``` 包裹
        s = stripCodeFence(s);

        // 已是纯 JSON
        if (s.startsWith("{") && s.endsWith("}")) {
            JSONUtil.parseObj(s); // 校验
            return s;
        }

        // 扫描提取 {...}
        String extracted = scanFirstJsonObject(s);
        if (extracted == null) {
            throw new IllegalArgumentException("Cannot extract JSON from vision result");
        }

        JSONUtil.parseObj(extracted); // 最终校验
        return extracted;
    }

    private static String stripCodeFence(String s) {
        String t = s.trim();
        if (t.startsWith("```")) {
            t = t.replaceFirst("^```\\s*[jJ]?[sS]?[oO]?[nN]?\\s*", "");
        }
        if (t.endsWith("```")) {
            int idx = t.lastIndexOf("```");
            if (idx >= 0) {
                t = t.substring(0, idx).trim();
            }
        }
        return t;
    }

    private static String scanFirstJsonObject(String text) {
        int start = text.indexOf('{');
        if (start < 0) return null;

        int depth = 0;
        boolean inString = false;
        boolean escape = false;

        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);

            if (inString) {
                if (escape) {
                    escape = false;
                } else if (c == '\\') {
                    escape = true;
                } else if (c == '"') {
                    inString = false;
                }
            } else {
                if (c == '"') {
                    inString = true;
                } else if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        return text.substring(start, i + 1).trim();
                    }
                }
            }
        }
        return null;
    }
}
