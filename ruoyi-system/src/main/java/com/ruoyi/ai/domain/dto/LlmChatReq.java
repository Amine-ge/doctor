package com.ruoyi.ai.domain.dto;

import javax.validation.constraints.NotBlank;

public class LlmChatReq {
    private String model = "qwen-plus";
    private String systemPrompt = "You are a helpful assistant.";
    @NotBlank(message = "question不能为空")
    private String question;
    // getter/setter
}
