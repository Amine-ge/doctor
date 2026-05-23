package com.ruoyi.ai.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TongguVo {
    private Long id;
    private String imageUrl;
    private String tongueColor;
    /** 舌苔颜色（如 white、yellow、gray、none 等） */
    private String coatingColor;
    /** 舌苔厚度（如 thin、medium、thick 等） */
    private String coatingThickness;
    /** 湿润度（如 dry、normal、wet 等） */
    private String moisture;
    /** 裂纹情况（如 none、few、many 等） */
    private String fissures;
    /** 齿痕情况（如 none、mild、obvious 等） */
    private String teethMarks;
    /** 舌面质地（如 smooth、rough、greasy 等） */
    private String surfaceTexture;
    private String aiDiagnosis;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    private BigDecimal healthScore;
    // 状态文案：良好 / 正常 / 未检测 等
    private String statusText;

}
