package com.ruoyi.ai.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.mapper.AiFaceRecordMapper;
import com.ruoyi.ai.mapper.AiNailRecordMapper;
import com.ruoyi.ai.mapper.AiTongueRecordMapper;
import com.ruoyi.ai.service.DoubaoVisionService;
import com.ruoyi.ai.utils.VisionJsonUtils;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.utils.UserHold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class DoubaoVisionController {

    private static final Logger log = LoggerFactory.getLogger(DoubaoVisionController.class);
    private static final String FACE_STATUS_PROCESSING = "PROCESSING";
    private static final String FACE_STATUS_SUCCESS = "SUCCESS";
    private static final String FACE_STATUS_FAILED = "FAILED";
    private static final String FACE_FAILED_PREFIX = "FAILED: ";

    @Resource
    private DoubaoVisionService doubaoVisionService;

    @Resource
    private AiTongueRecordMapper aiTongueRecordMapper;

    @Resource
    private AiFaceRecordMapper aiFaceRecordMapper;

    @Resource
    private AiNailRecordMapper aiNailRecordMapper;

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @PostMapping("/tongue")
    public R<AiTongueRecord> analyzeTongue(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            AiTongueRecord record = buildTongueRecord(body.get("url"), aiUser.getId());
            aiTongueRecordMapper.insertAiTongueRecord(record);
            return R.ok(record);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/face")
    public R<Map<String, Object>> analyzeFace(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            String imageUrl = body.get("url");
            if (imageUrl == null || (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://"))) {
                return R.fail("image url must be public http/https");
            }

            AiFaceRecord record = new AiFaceRecord();
            record.setUserId(aiUser.getId());
            record.setImageUrl(imageUrl);
            record.setCreatedAt(new Date());
            record.setUpdatedAt(new Date());
            aiFaceRecordMapper.insertAiFaceRecord(record);

            Long recordId = record.getId();
            taskExecutor.execute(() -> completeFaceAnalyze(recordId, imageUrl, aiUser.getId()));

            return R.ok(faceStatusPayload(record), "accepted");
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/nail")
    public R<AiNailRecord> analyzeNail(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            AiNailRecord record = buildNailRecord(body.get("url"), aiUser.getId());
            aiNailRecordMapper.insertAiNailRecord(record);
            return R.ok(record);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("/face/{recordId}")
    public R<Map<String, Object>> getFaceAnalyzeResult(@PathVariable Long recordId) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("please login");
        }

        AiFaceRecord record = aiFaceRecordMapper.selectAiFaceRecordById(recordId);
        if (record == null || !aiUser.getId().equals(record.getUserId())) {
            return R.fail("record not found");
        }
        return R.ok(faceStatusPayload(record));
    }

    @PostMapping("/report")
    public R<Object> analyzeMedicalReport(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeMedicalReport(body.get("url")));
            return R.ok(JSONUtil.parseObj(visionJson));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/medicine")
    public R<Object> analyzeMedicine(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeMedicine(body.get("url")));
            return R.ok(JSONUtil.parseObj(visionJson));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    private AiTongueRecord buildTongueRecord(String imageUrl, Long userId) {
        String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeTongue(imageUrl));
        JSONObject obj = JSONUtil.parseObj(visionJson);
        JSONObject tq = obj.getJSONObject("tongue_quality");

        int detected = Convert.toBool(tq.get("detected"), false) ? 1 : 0;
        if (detected == 0) {
            throw new ServiceException("图片中未检测到舌头");
        }

        AiTongueRecord record = new AiTongueRecord();
        record.setUserId(userId);
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
        return record;
    }

    private AiFaceRecord buildFaceRecord(String imageUrl, Long userId) {
        String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeFace(imageUrl));
        JSONObject obj = JSONUtil.parseObj(visionJson);
        JSONObject tq = obj.getJSONObject("face_quality");

        AiFaceRecord record = new AiFaceRecord();
        record.setUserId(userId);
        record.setImageUrl(imageUrl);
        record.setVisionJson(visionJson);
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
        return record;
    }

    private void completeFaceAnalyze(Long recordId, String imageUrl, Long userId) {
        try {
            AiFaceRecord result = buildFaceRecord(imageUrl, userId);
            result.setId(recordId);
            result.setUpdatedAt(new Date());
            aiFaceRecordMapper.updateAiFaceRecord(result);
        } catch (Exception e) {
            log.error("Async face analyze failed, recordId={}", recordId, e);
            AiFaceRecord failed = new AiFaceRecord();
            failed.setId(recordId);
            failed.setAiDiagnosis(FACE_FAILED_PREFIX + safeMessage(e));
            failed.setUpdatedAt(new Date());
            aiFaceRecordMapper.updateAiFaceRecord(failed);
        }
    }

    private Map<String, Object> faceStatusPayload(AiFaceRecord record) {
        Map<String, Object> data = new HashMap<>();
        data.put("recordId", record.getId());
        data.put("status", resolveFaceStatus(record));
        data.put("record", record);
        if (record.getAiDiagnosis() != null && record.getAiDiagnosis().startsWith(FACE_FAILED_PREFIX)) {
            data.put("message", record.getAiDiagnosis().substring(FACE_FAILED_PREFIX.length()));
        }
        return data;
    }

    private String resolveFaceStatus(AiFaceRecord record) {
        if (record.getAiDiagnosis() != null && record.getAiDiagnosis().startsWith(FACE_FAILED_PREFIX)) {
            return FACE_STATUS_FAILED;
        }
        if (record.getVisionJson() == null || record.getVisionJson().isBlank()) {
            return FACE_STATUS_PROCESSING;
        }
        if (record.getFqDetected() == null || record.getFqDetected() != 1) {
            return FACE_STATUS_FAILED;
        }
        return FACE_STATUS_SUCCESS;
    }

    private String safeMessage(Exception e) {
        return e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
    }

    private AiNailRecord buildNailRecord(String imageUrl, Long userId) {
        String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeNail(imageUrl));
        JSONObject obj = JSONUtil.parseObj(visionJson);
        JSONObject tq = obj.getJSONObject("nail_quality");
        int detected = Convert.toBool(tq.get("detected"), false) ? 1 : 0;
        if (detected == 0) {
            throw new ServiceException("No nail detected in image");
        }

        AiNailRecord record = new AiNailRecord();
        record.setUserId(userId);
        record.setImageUrl(imageUrl);
        record.setVisionJson(visionJson);
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
        return record;
    }
}
