package com.ruoyi.ai.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.dto.AskDTO;
import com.ruoyi.ai.service.LlmService;
import com.ruoyi.ai.support.AiAsyncTaskManager;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.utils.FastGptUtils;
import com.ruoyi.utils.UserHold;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/llm")
public class LlmController {

    @Resource
    private LlmService service;

    @Resource
    private AiAsyncTaskManager aiAsyncTaskManager;

    @PostMapping("/ask")
    public R<String> summary(@RequestBody AskDTO dto) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        try {
            if (dto == null) {
                return R.fail("request body is required");
            }
            return R.ok(service.ask(dto.getQuestion(),dto.getFaceId(),dto.getNailId(),dto.getTongueId(), aiUser.getId()));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/fastgpt/chat")
    public R<Map<String, Object>> fastGptChat(@RequestBody Map<String, String> body) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        try {
            String message = body == null ? null : body.get("message");
            if (message == null || message.trim().isEmpty()) {
                return R.fail("message is required");
            }

            String chatId = body.get("chatId");
            if (chatId == null || chatId.isBlank()) {
                chatId = "ai-doctor-user-" + aiUser.getId();
            }

            final String finalChatId = chatId;
            String taskId = aiAsyncTaskManager.submit(() -> {
                String answer = FastGptUtils.chat(finalChatId, message);
                Map<String, Object> result = new HashMap<>();
                result.put("answer", answer);
                result.put("chatId", finalChatId);
                return result;
            });

            Map<String, Object> data = aiAsyncTaskManager.processingPayload(taskId);
            data.put("chatId", chatId);
            return R.ok(data);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/tongue/{id}/generate")
    public R<?> generateTongue(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        try {
            return R.ok(service.generateTongueDiagnosis(id, finalJson));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("/face/{id}/generate")
    public R<?> generateFace(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        try {
            return R.ok(service.generateFaceDiagnosis(id, finalJson));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
    @PostMapping("/nail/{id}/generate")
    public R<?> generateNail(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        try {
            return R.ok(service.generateNailDiagnosis(id, finalJson));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

}
