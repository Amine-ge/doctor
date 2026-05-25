package com.ruoyi.ai.controller;

import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.service.VisionService;
import com.ruoyi.ai.support.AiAsyncTaskManager;
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

    @Resource
    private AiAsyncTaskManager aiAsyncTaskManager;

    @PostMapping("/tongue")
    public R<Map<String, Object>> analyzeTongue(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        try {
            Long userId = aiUser.getId();
            String imageUrl = body.get("url");
            String taskId = aiAsyncTaskManager.submit(() -> service.tongue(imageUrl, userId));
            return R.ok(aiAsyncTaskManager.processingPayload(taskId), "accepted");
        } catch (ServiceException e) {
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
            Long userId = aiUser.getId();
            String imageUrl = body.get("url");
            String taskId = aiAsyncTaskManager.submit(() -> service.face(imageUrl, userId));
            return R.ok(aiAsyncTaskManager.processingPayload(taskId), "accepted");
        } catch (ServiceException e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/nail")
    public R<Map<String, Object>> analyzeNail(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        try {
            Long userId = aiUser.getId();
            String imageUrl = body.get("url");
            String taskId = aiAsyncTaskManager.submit(() -> service.nail(imageUrl, userId));
            return R.ok(aiAsyncTaskManager.processingPayload(taskId), "accepted");
        } catch (ServiceException e) {
            return R.fail(e.getMessage());
        }
    }
}
