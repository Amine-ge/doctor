package com.ruoyi.ai.controller;

import com.ruoyi.ai.domain.AiFaceRecord;
import com.ruoyi.ai.domain.AiNailRecord;
import com.ruoyi.ai.domain.AiTongueRecord;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.service.VisionService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.utils.UserHold;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/qianwen/ai")
public class VisionController {

    @Resource
    private VisionService service;

    @PostMapping("/tongue")
    public R<AiTongueRecord> analyzeTongue(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        AiTongueRecord record = null;
        try {
            record = service.tongue(body.get("url"), aiUser.getId());
        } catch (ServiceException e) {
            return R.fail(e.getMessage());
        }
        return R.ok(record);
    }

    @PostMapping("/face")
    public R<AiFaceRecord> analyzeFace(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        try {
            return R.ok(service.face(body.get("url"),aiUser.getId()));
        } catch (ServiceException e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/nail")
    public R<AiNailRecord> analyzeNail(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        return R.ok(service.nail(body.get("url"),aiUser.getId()));
    }
}
