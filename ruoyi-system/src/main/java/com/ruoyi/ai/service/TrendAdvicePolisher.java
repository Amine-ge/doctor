package com.ruoyi.ai.service;

public class TrendAdvicePolisher {
    public static String extractFirstJsonObject(String text) {
        if (text == null) throw new IllegalArgumentException("LLM output is null");
        int start = text.indexOf('{');
        if (start < 0) throw new IllegalArgumentException("No '{' found in LLM output: " + text);

        int depth = 0;
        for (int i = start; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return text.substring(start, i + 1).trim();
            }
        }
        throw new IllegalArgumentException("Unclosed JSON object in LLM output: " + text);
    }
}
