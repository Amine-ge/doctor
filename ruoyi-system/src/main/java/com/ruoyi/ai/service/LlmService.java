package com.ruoyi.ai.service;

import com.alibaba.fastjson2.JSONObject;

public interface LlmService {
    String ask(String question, Long faceId, Long nailId, Long tongueId, Long aiUserId);

    String generateTongueDiagnosis(Long id, JSONObject finalJson);

    String generateFaceDiagnosis(Long id, JSONObject finalJson);

    String generateNailDiagnosis(Long id, JSONObject finalJson);

    public String polishTrendAdvice(cn.hutool.json.JSONObject ruleAdviceJson, Long aiUserId);

}
