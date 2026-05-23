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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class DoubaoVisionController {

    @Resource
    private DoubaoVisionService doubaoVisionService;

    @Resource
    private AiTongueRecordMapper aiTongueRecordMapper;

    @Resource
    private AiFaceRecordMapper aiFaceRecordMapper;

    @Resource
    private AiNailRecordMapper aiNailRecordMapper;

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
    public R<AiFaceRecord> analyzeFace(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }

        try {
            AiFaceRecord record = buildFaceRecord(body.get("url"), aiUser.getId());
            aiFaceRecordMapper.insertAiFaceRecord(record);
            return R.ok(record);
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
        record.setFqDetected(Convert.toBool(tq.get("detected"), false) ? 1 : 0);
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

    private AiNailRecord buildNailRecord(String imageUrl, Long userId) {
        String visionJson = VisionJsonUtils.extractJsonObject(doubaoVisionService.analyzeNail(imageUrl));
        JSONObject obj = JSONUtil.parseObj(visionJson);
        JSONObject tq = obj.getJSONObject("nail_quality");

        AiNailRecord record = new AiNailRecord();
        record.setUserId(userId);
        record.setImageUrl(imageUrl);
        record.setVisionJson(visionJson);
        record.setNqDetected(Convert.toBool(tq.get("detected"), false) ? 1 : 0);
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
