package com.ruoyi.ai.service.impl;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.consts.AiModels;
import com.ruoyi.consts.AiPrompts;
import com.ruoyi.ai.service.LlmService;
import com.ruoyi.utils.DashScopeUtils;
import org.springframework.stereotype.Service;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.ruoyi.ai.domain.AiSymptomReport;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;

import com.ruoyi.ai.mapper.AiSymptomReportMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class LlmServiceImpl implements LlmService {

    private final AiSymptomReportMapper aiSymptomReportMapper;
    private final AiTongueRecordMapper aiTongueRecordMapper;
    private final AiFaceRecordMapper aiFaceRecordMapper;
    private final AiNailRecordMapper aiNailRecordMapper;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String ask(String question, Long faceId, Long nailId, Long tongueId, Long aiUserId) {

        if (aiUserId == null) {
            throw new IllegalArgumentException("aiUserId 不能为空");
        }
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("question 不能为空");
        }

        // 时间窗
        Date sinceTongue = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
        Date sinceFace   = Date.from(Instant.now().minus(30, ChronoUnit.DAYS));
        Date sinceNail   = Date.from(Instant.now().minus(30, ChronoUnit.DAYS));

        // 1) 舌诊：优先用用户指定 tongueId，否则回退到“时间窗内最新”
        AiTongueRecord tongue = null;
        if (tongueId != null) {
            tongue = aiTongueRecordMapper.selectAiTongueRecordById(tongueId);
            // 归属校验（字段名按你的表改）
            if (tongue == null || !aiUserId.equals(tongue.getUserId())) {
                throw new IllegalArgumentException("舌诊记录不存在或不属于当前用户");
            }
        } else {
            tongue = aiTongueRecordMapper.selectLatestByUserAfter(aiUserId, sinceTongue);
        }

        // 2) 面诊
        AiFaceRecord face = null;
        if (faceId != null) {
            face = aiFaceRecordMapper.selectAiFaceRecordById(faceId);
            if (face == null || !aiUserId.equals(face.getUserId())) {
                throw new IllegalArgumentException("面诊记录不存在或不属于当前用户");
            }
        } else {
            face = aiFaceRecordMapper.selectLatestByUserAfter(aiUserId, sinceFace);
        }

        // 3) 指甲
        AiNailRecord nail = null;
        if (nailId != null) {
            nail = aiNailRecordMapper.selectAiNailRecordById(nailId);
            if (nail == null || !aiUserId.equals(nail.getUserId())) {
                throw new IllegalArgumentException("指甲记录不存在或不属于当前用户");
            }
        } else {
            nail = aiNailRecordMapper.selectLatestByUserAfter(aiUserId, sinceNail);
        }

        // 4) 组织上下文
        JSONObject ctx = JSONUtil.createObj()
                .set("user_question", question);

        if (tongue != null && tongue.getVisionJson() != null) {
            ctx.set("tongue", JSONUtil.parseObj(tongue.getVisionJson()));
        }
        if (face != null && face.getVisionJson() != null) {
            ctx.set("face", JSONUtil.parseObj(face.getVisionJson()));
        }
        if (nail != null && nail.getVisionJson() != null) {
            ctx.set("nail", JSONUtil.parseObj(nail.getVisionJson()));
        }

        String chat = DashScopeUtils.chat(
                AiModels.LLM_DEFAULT,
                AiPrompts.SYS_TEXT_SUMMARY,
                ctx.toString()
        );

        // ====== 解析 AI 返回 ======
        int overallScore = 0;
        String summaryText = chat; // 兜底：模型异常时直接存全文

        try {
            JsonNode root = MAPPER.readTree(chat);

            if (root.has("overall_health_score")) {
                overallScore = root.get("overall_health_score").asInt(0);
            }

            if (root.has("summary_text")) {
                String text = root.get("summary_text").asText();
                if (text != null && !text.isBlank()) {
                    summaryText = text;
                }
            }

        } catch (Exception e) {
            log.warn("AI summary JSON 解析失败，已使用兜底文本", e);
        }

        AiSymptomReport report = new AiSymptomReport();
        report.setUserId(aiUserId);
        report.setMainSymptom(question);

        report.setAiDiagnosis(summaryText);
        report.setHealthScore(BigDecimal.valueOf(overallScore));

        if (tongue != null) report.setTongueRecordId(tongue.getId());
        if (face != null)   report.setFaceRecordId(face.getId());
        if (nail != null)   report.setNailRecordId(nail.getId());

        aiSymptomReportMapper.insertAiSymptomReport(report);
        return summaryText;
    }


    @Override
    public String generateTongueDiagnosis(Long id, com.alibaba.fastjson2.JSONObject finalJson) {
        AiTongueRecord record = aiTongueRecordMapper.selectAiTongueRecordById(id);
        if (record == null) {
            throw new IllegalArgumentException("Tongue record not found");
        }
        if (record.getTqDetected() == null || record.getTqDetected() != 1) {
            throw new IllegalArgumentException("No tongue detected, cannot generate tongue report");
        }
        String text = generate(AiPrompts.SYS_TONGUE_EXPLAIN, finalJson);
        aiTongueRecordMapper.updateDiagnosis(id, text);
        return text ;
    }

    @Override
    public String generateFaceDiagnosis(Long id, com.alibaba.fastjson2.JSONObject finalJson) {
        AiFaceRecord record = aiFaceRecordMapper.selectAiFaceRecordById(id);
        if (record == null) {
            throw new IllegalArgumentException("Face record not found");
        }
        if (record.getFqDetected() == null || record.getFqDetected() != 1) {
            throw new IllegalArgumentException("No face detected, cannot generate face report");
        }
        String text = generate(AiPrompts.SYS_FACE_EXPLAIN, finalJson);
        aiFaceRecordMapper.updateDiagnosis(id, text);
        return text ;
    }

    @Override
    public String generateNailDiagnosis(Long id, com.alibaba.fastjson2.JSONObject finalJson) {
        AiNailRecord record = aiNailRecordMapper.selectAiNailRecordById(id);
        if (record == null) {
            throw new IllegalArgumentException("Nail record not found");
        }
        if (record.getNqDetected() == null || record.getNqDetected() != 1) {
            throw new IllegalArgumentException("No nail detected, cannot generate nail report");
        }
        String text = generate(AiPrompts.SYS_NAIL_EXPLAIN, finalJson);
        aiNailRecordMapper.updateDiagnosis(id, text);
        return text ;
    }

    @Override
    public String polishTrendAdvice(JSONObject ruleAdviceJson, Long aiUserId) {
        if (aiUserId == null) {
            throw new IllegalArgumentException("aiUserId 不能为空");
        }
        if (ruleAdviceJson == null) {
            throw new IllegalArgumentException("ruleAdviceJson 不能为空");
        }


        String raw = DashScopeUtils.chat(
                AiModels.LLM_DEFAULT,
                AiPrompts.SYS_TREND_ADVICE_POLISH,
                ruleAdviceJson.toString()
        );

        return raw;
    }

    /**
     * 通用生成诊断文本
     */
    private String generate(String systemPrompt, com.alibaba.fastjson2.JSONObject finalJson) {
        String payload = finalJson.toString();
        return DashScopeUtils.chat(
                AiModels.LLM_DEFAULT,
                systemPrompt,
                payload
        );
    }
}
