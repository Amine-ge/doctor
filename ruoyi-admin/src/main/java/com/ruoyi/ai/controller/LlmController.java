package com.ruoyi.ai.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.ai.domain.AiUser;
import com.ruoyi.ai.domain.dto.AskDTO;
import com.ruoyi.ai.service.LlmService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.utils.UserHold;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/ai/llm")
public class LlmController {

    @Resource
    private LlmService service;

    @PostMapping("/ask")
    public R<String> summary(@RequestBody AskDTO dto) {
        AiUser aiUser = UserHold.get();
        if (aiUser == null) {
            return R.fail("请先登录");
        }
        return R.ok(service.ask(dto.getQuestion(),dto.getFaceId(),dto.getNailId(),dto.getTongueId(), aiUser.getId()));
    }

    @PostMapping("/tongue/{id}/generate")
    public R<?> generateTongue(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        return R.ok(  service.generateTongueDiagnosis(id, finalJson));
    }

    @PostMapping("/face/{id}/generate")
    public R<?> generateFace(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        return R.ok(service.generateFaceDiagnosis(id, finalJson));
    }
    @PostMapping("/nail/{id}/generate")
    public R<?> generateNail(@PathVariable Long id, @RequestBody JSONObject finalJson) {
        return R.ok(service.generateNailDiagnosis(id, finalJson));
    }

}
