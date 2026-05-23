// 返回 VO（LLM）
package com.ruoyi.ai.domain.dto;
import java.time.LocalDateTime;
public class LlmChatResp {
    private Long logId;
    private String model;
    private String question;
    private String answer;
    private LocalDateTime createTime;
    // getter/setter
}
