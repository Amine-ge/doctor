package com.ruoyi.ai.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

// com.ruoyi.ai.domain.dto 或 domain.vo 下都可以，内部用 DO
@Data
public class HealthDailyScoreDO {

    /** 某一天，例如 2025-12-01 */
    private LocalDate day;

    /** 面诊平均分 */
    private BigDecimal faceScore;

    /** 舌诊平均分 */
    private BigDecimal tongueScore;

    /** 指甲平均分 */
    private BigDecimal nailScore;

    /** 总评分（来自 ai_symptom_report.health_score） */
    private BigDecimal totalScore;
}
