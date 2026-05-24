package com.ruoyi.ai.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.utils.VisionJsonUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.consts.AiModels;
import com.ruoyi.consts.AiPrompts;
import com.ruoyi.ai.service.VisionService;
import com.ruoyi.utils.DashScopeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class VisionServiceImpl implements VisionService {

    private final AiTongueRecordMapper aiTongueRecordMapper;
    private final AiFaceRecordMapper aiFaceRecordMapper;
    private final AiNailRecordMapper aiNailRecordMapper;
    /**
     * 舌诊：只做 Vision，不做 LLM 说明
     */
    public AiTongueRecord tongue(String imageUrl, Long id) {
        checkUrl(imageUrl);

        // Step1: Vision
        String visionUser = "分析这张舌头图片URL，仅按系统提示输出严格JSON：\n" + imageUrl;
        String visionJson = DashScopeUtils.chat(AiModels.VISION_DEFAULT, AiPrompts.SYS_TONGUE_VISION, visionUser);
        // 清理 Markdown 代码块标记
        visionJson = visionJson.replace("```json", "").replace("```", "").trim();
        log.info("Tongue Vision JSON: {}", visionJson);


        boolean typeJSON = JSONUtil.isTypeJSON(visionJson);
        log.info("Tongue JSON: {}", typeJSON);

        AiTongueRecord record = null;
        // Step2: JSON 解析（结构化字段）
        if (JSONUtil.isTypeJSON(visionJson)) {
            var obj = JSONUtil.parseObj(visionJson);
            JSONObject tq = obj.getJSONObject("tongue_quality");
            int detected = Convert.toBool(tq.get("detected"), false) ? 1 : 0;
            log.info("Tongue Detected: {}", detected);
            if (detected == 0){
                throw new ServiceException("图片中未检测到舌头！");
            }
            record = new AiTongueRecord();
            record.setUserId(id);
            record.setImageUrl(imageUrl);
            record.setVisionJson(visionJson);
            record.setTqDetected(detected);
            record.setTqLightingOk(Convert.toBool(tq.get("lighting_ok"), false) ? 1 : 0);
            record.setTqOcclusionOk(Convert.toBool(tq.get("occlusion_ok"), false) ? 1 : 0);
            record.setTqAngleOk(Convert.toBool(tq.get("angle_ok"), false) ? 1 : 0);
            record.setTqQualityScore(Convert.toBigDecimal(tq.get("quality_score"), BigDecimal.ZERO));

            record.setTongueColor(obj.getStr("tongue_color"));
            record.setCoatingColor(obj.getStr("coating_color"));
            record.setCoatingThickness(obj.getStr("coating_thickness"));
            record.setMoisture(obj.getStr("moisture"));
            record.setFissures(obj.getStr("fissures"));
            record.setTeethMarks(obj.getStr("teeth_marks"));
            record.setSurfaceTexture(obj.getStr("surface_texture"));
            record.setHealthScore(tq.getBigDecimal("health_score"));
        }

        // ❌ LLM 部分注释
        /*
        String payload = JSONUtil.createObj()
                .set("tongue", JSONUtil.parseObj(visionJson))
                .toString();
        String explain = DashScopeUtils.chat(AiModels.LLM_DEFAULT, AiPrompts.SYS_TONGUE_EXPLAIN, payload);
        record.setAiDiagnosis(explain);
        */
        if(record != null) aiTongueRecordMapper.insertAiTongueRecord(record);
        return record;
    }

    /**
     * 面诊：只做 Vision，不做 LLM
     */
    public AiFaceRecord face(String imageUrl, Long id) {
        checkUrl(imageUrl);

        String visionUser = "分析这张面部图片URL，仅按系统提示输出严格JSON：\n" + imageUrl;
        String visionJson = DashScopeUtils.chat(AiModels.VISION_DEFAULT, AiPrompts.SYS_FACE_VISION, visionUser);
        log.info("Face Vision JSON: {}", visionJson);

        AiFaceRecord record = new AiFaceRecord();
        record.setUserId(id);
        record.setImageUrl(imageUrl);
        record.setVisionJson(visionJson);
        String clean = VisionJsonUtils.extractJsonObject(visionJson);

        var obj = JSONUtil.parseObj(clean);
        JSONObject tq = obj.getJSONObject("face_quality");

        int detected = Convert.toBool(tq.get("detected"), false) ? 1 : 0;
        if (detected == 0) {
            throw new ServiceException("No face detected in image");
        }
        record.setFqDetected(detected);
        record.setFqLightingOk(Convert.toBool(tq.get("lighting_ok"), false) ? 1 : 0);
        record.setFqOcclusionOk(Convert.toBool(tq.get("occlusion_ok"), false) ? 1 : 0);
        record.setFqQualityScore(Convert.toBigDecimal(tq.get("quality_score"), BigDecimal.ZERO));

        record.setSkinTone(obj.getStr("skin_tone"));
        record.setOilinessLevel(obj.getStr("oiliness_level"));
        record.setDarkCirclesLevel(obj.getStr("dark_circles_level"));
        record.setAcneLevel(obj.getStr("acne_level"));
        record.setLipColor(obj.getStr("lip_color"));
        record.setNotes(obj.getStr("notes"));
        record.setHealthScore(tq.getBigDecimal("health_score"));


        // ❌ 注释掉 LLM
        /*
        String payload = JSONUtil.createObj()
                .set("face", JSONUtil.parseObj(visionJson))
                .toString();
        String explain = DashScopeUtils.chat(AiModels.LLM_DEFAULT, AiPrompts.SYS_FACE_EXPLAIN, payload);
        record.setAiDiagnosis(explain);
        */

        aiFaceRecordMapper.insertAiFaceRecord(record);
        return record;
    }

    /**
     * 指诊：只做 Vision，不做 LLM
     */
    public AiNailRecord nail(String imageUrl, Long id) {
        checkUrl(imageUrl);

        String visionUser = "分析这张指甲图片URL，仅按系统提示输出严格JSON：\n" + imageUrl;
        String visionJson = DashScopeUtils.chat(AiModels.VISION_DEFAULT, AiPrompts.SYS_NAIL_VISION, visionUser);
        log.info("Nail Vision JSON: {}", visionJson);

        AiNailRecord record = new AiNailRecord();
        record.setUserId(id);
        record.setImageUrl(imageUrl);
        record.setVisionJson(visionJson);
        String clean = VisionJsonUtils.extractJsonObject(visionJson);

        var obj = JSONUtil.parseObj(clean);
        JSONObject tq = obj.getJSONObject("nail_quality");

        int detected = Convert.toBool(tq.get("detected"), false) ? 1 : 0;
        if (detected == 0) {
            throw new ServiceException("No nail detected in image");
        }
        record.setNqDetected(detected);
        record.setNqLightingOk(Convert.toBool(tq.get("lighting_ok"), false) ? 1 : 0);
        record.setNqOcclusionOk(Convert.toBool(tq.get("occlusion_ok"), false) ? 1 : 0);
        record.setNqQualityScore(Convert.toBigDecimal(tq.get("quality_score"), BigDecimal.ZERO));

        record.setNailColor(obj.getStr("nail_color"));
        record.setNailTexture(obj.getStr("nail_texture"));
        record.setNailShape(obj.getStr("nail_shape"));
        record.setLunulaVisibility(obj.getStr("lunula_visibility"));
        record.setBreakability(obj.getStr("breakability"));
        record.setNotes(obj.getStr("notes"));
        record.setHealthScore(tq.getBigDecimal("health_score"));


        // ❌ 注释掉 LLM
        /*
        String payload = JSONUtil.createObj()
                .set("nail", JSONUtil.parseObj(visionJson))
                .toString();
        String explain = DashScopeUtils.chat(AiModels.LLM_DEFAULT, AiPrompts.SYS_NAIL_EXPLAIN, payload);
        record.setAiDiagnosis(explain);
        */

        aiNailRecordMapper.insertAiNailRecord(record);
        return record;
    }

    /** URL校验 */
    private void checkUrl(String url) {
        if (StrUtil.isBlank(url) || !url.startsWith("http")) {
            throw new ServiceException("图片URL必须为公网可访问的 http/https 链接");
        }
    }
}

