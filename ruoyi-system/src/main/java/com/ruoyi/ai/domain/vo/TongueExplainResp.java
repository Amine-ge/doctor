package com.ruoyi.ai.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TongueExplainResp {
    private String visionJson;       // Vision返回的“客观特征”JSON（字符串原样返回）
    private Double qualityScore;     // 从 JSON 的 tongue_quality.quality_score 提取
    private String explanation;      // LLM 生成的可读说明（非医疗）
    private String visionModel;      // 如 qwen-vl-max
    private String textModel;        // 如 qwen-plus
    private LocalDateTime analyzedAt;
    // getter/setter
}
