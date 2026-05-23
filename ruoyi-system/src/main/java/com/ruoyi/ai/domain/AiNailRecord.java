package com.ruoyi.ai.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 指甲检测记录对象 ai_nail_record
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class AiNailRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID：一次指甲检测=一条记录 */
    private Long id;

    /** 用户ID（关联 user.id） */
    @Excel(name = "用户ID", readConverterExp = "关=联,u=ser.id")
    private Long userId;

    /** 指甲图片URL（OSS/CDN/本地路径） */
    @Excel(name = "指甲图片URL", readConverterExp = "O=SS/CDN/本地路径")
    private String imageUrl;

    /** detected：是否检测到指甲（1=是，0=否） */
    @Excel(name = "detected：是否检测到指甲", readConverterExp = "1==是，0=否")
    private Integer nqDetected;

    /** quality_score：图片质量分(0.000~1.000) */
    @Excel(name = "quality_score：图片质量分(0.000~1.000)")
    private BigDecimal nqQualityScore;

    /** lighting_ok：光线是否合格（1=合格） */
    @Excel(name = "lighting_ok：光线是否合格", readConverterExp = "1==合格")
    private Integer nqLightingOk;

    /** occlusion_ok：遮挡是否合格（1=合格） */
    @Excel(name = "occlusion_ok：遮挡是否合格", readConverterExp = "1==合格")
    private Integer nqOcclusionOk;

    /** nail_color：指甲颜色（健康粉色/偏白/偏黄/偏暗/发青/无法判断） */
    @Excel(name = "nail_color：指甲颜色", readConverterExp = "健=康粉色/偏白/偏黄/偏暗/发青/无法判断")
    private String nailColor;

    /** nail_texture：表面质地（光滑/轻微纹理/明显纹理/凹陷/无法判断） */
    @Excel(name = "nail_texture：表面质地", readConverterExp = "光=滑/轻微纹理/明显纹理/凹陷/无法判断")
    private String nailTexture;

    /** nail_shape：形状（正常/匙状/凹陷/凸起/无法判断） */
    @Excel(name = "nail_shape：形状", readConverterExp = "正=常/匙状/凹陷/凸起/无法判断")
    private String nailShape;

    /** lunula_visibility：月牙可见性（无/少量/正常/无法判断） */
    @Excel(name = "lunula_visibility：月牙可见性", readConverterExp = "无=/少量/正常/无法判断")
    private String lunulaVisibility;

    /** breakability：易碎程度（正常/偏脆/非常脆/无法判断） */
    @Excel(name = "breakability：易碎程度", readConverterExp = "正=常/偏脆/非常脆/无法判断")
    private String breakability;

    /** notes：一行说明（可选） */
    @Excel(name = "notes：一行说明", readConverterExp = "可=选")
    private String notes;

    /** 视觉模型原始JSON */
    @Excel(name = "视觉模型原始JSON")
    private String visionJson;

    /** AI生成的文本诊断结果（自然语言描述） */
    @Excel(name = "AI生成的文本诊断结果", readConverterExp = "自=然语言描述")
    private String aiDiagnosis;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    /** 健康评分（0~100），用于趋势分析 */
    @Excel(name = "健康评分", readConverterExp = "0=~100")
    private BigDecimal healthScore;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setNqDetected(Integer nqDetected) 
    {
        this.nqDetected = nqDetected;
    }

    public Integer getNqDetected() 
    {
        return nqDetected;
    }

    public void setNqQualityScore(BigDecimal nqQualityScore) 
    {
        this.nqQualityScore = nqQualityScore;
    }

    public BigDecimal getNqQualityScore() 
    {
        return nqQualityScore;
    }

    public void setNqLightingOk(Integer nqLightingOk) 
    {
        this.nqLightingOk = nqLightingOk;
    }

    public Integer getNqLightingOk() 
    {
        return nqLightingOk;
    }

    public void setNqOcclusionOk(Integer nqOcclusionOk) 
    {
        this.nqOcclusionOk = nqOcclusionOk;
    }

    public Integer getNqOcclusionOk() 
    {
        return nqOcclusionOk;
    }

    public void setNailColor(String nailColor) 
    {
        this.nailColor = nailColor;
    }

    public String getNailColor() 
    {
        return nailColor;
    }

    public void setNailTexture(String nailTexture) 
    {
        this.nailTexture = nailTexture;
    }

    public String getNailTexture() 
    {
        return nailTexture;
    }

    public void setNailShape(String nailShape) 
    {
        this.nailShape = nailShape;
    }

    public String getNailShape() 
    {
        return nailShape;
    }

    public void setLunulaVisibility(String lunulaVisibility) 
    {
        this.lunulaVisibility = lunulaVisibility;
    }

    public String getLunulaVisibility() 
    {
        return lunulaVisibility;
    }

    public void setBreakability(String breakability) 
    {
        this.breakability = breakability;
    }

    public String getBreakability() 
    {
        return breakability;
    }

    public void setNotes(String notes) 
    {
        this.notes = notes;
    }

    public String getNotes() 
    {
        return notes;
    }

    public void setVisionJson(String visionJson) 
    {
        this.visionJson = visionJson;
    }

    public String getVisionJson() 
    {
        return visionJson;
    }

    public void setAiDiagnosis(String aiDiagnosis) 
    {
        this.aiDiagnosis = aiDiagnosis;
    }

    public String getAiDiagnosis() 
    {
        return aiDiagnosis;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }

    public void setHealthScore(BigDecimal healthScore) 
    {
        this.healthScore = healthScore;
    }

    public BigDecimal getHealthScore() 
    {
        return healthScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("imageUrl", getImageUrl())
            .append("nqDetected", getNqDetected())
            .append("nqQualityScore", getNqQualityScore())
            .append("nqLightingOk", getNqLightingOk())
            .append("nqOcclusionOk", getNqOcclusionOk())
            .append("nailColor", getNailColor())
            .append("nailTexture", getNailTexture())
            .append("nailShape", getNailShape())
            .append("lunulaVisibility", getLunulaVisibility())
            .append("breakability", getBreakability())
            .append("notes", getNotes())
            .append("visionJson", getVisionJson())
            .append("aiDiagnosis", getAiDiagnosis())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("healthScore", getHealthScore())
            .toString();
    }
}
