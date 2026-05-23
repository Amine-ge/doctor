package com.ruoyi.ai.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 脸诊分析记录对象 ai_face_record
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class AiFaceRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID：一次脸诊=一条记录 */
    private Long id;

    /** 用户ID（关联 user.id） */
    @Excel(name = "用户ID", readConverterExp = "关=联,u=ser.id")
    private Long userId;

    /** 脸部图片URL（OSS/CDN/本地路径） */
    @Excel(name = "脸部图片URL", readConverterExp = "O=SS/CDN/本地路径")
    private String imageUrl;

    /** detected：是否识别到正脸（1=是，0=否） */
    @Excel(name = "detected：是否识别到正脸", readConverterExp = "1==是，0=否")
    private Integer fqDetected;

    /** quality_score：照片质量分(0.000~1.000) */
    @Excel(name = "quality_score：照片质量分(0.000~1.000)")
    private BigDecimal fqQualityScore;

    /** lighting_ok：光线是否合格（1=合格） */
    @Excel(name = "lighting_ok：光线是否合格", readConverterExp = "1==合格")
    private Integer fqLightingOk;

    /** occlusion_ok：遮挡是否合格（1=合格） */
    @Excel(name = "occlusion_ok：遮挡是否合格", readConverterExp = "1==合格")
    private Integer fqOcclusionOk;

    /** skin_tone：肤色/类型（自定义字典，如 fair/medium/tan 或 I~VI） */
    @Excel(name = "skin_tone：肤色/类型", readConverterExp = "自=定义字典，如,f=air/medium/tan,或=,I=~VI")
    private String skinTone;

    /** oiliness_level：出油程度（none/mild/moderate/severe…） */
    @Excel(name = "oiliness_level：出油程度", readConverterExp = "n=one/mild/moderate/severe…")
    private String oilinessLevel;

    /** dark_circles_level：黑眼圈程度（none/mild/moderate/severe） */
    @Excel(name = "dark_circles_level：黑眼圈程度", readConverterExp = "n=one/mild/moderate/severe")
    private String darkCirclesLevel;

    /** acne_level：痘痘程度（none/few/moderate/severe） */
    @Excel(name = "acne_level：痘痘程度", readConverterExp = "n=one/few/moderate/severe")
    private String acneLevel;

    /** lip_color：唇色（如 pale/pink/red/brown 等） */
    @Excel(name = "lip_color：唇色", readConverterExp = "如=,p=ale/pink/red/brown,等=")
    private String lipColor;

    /** notes：一行说明（可空） */
    @Excel(name = "notes：一行说明", readConverterExp = "可=空")
    private String notes;

    /** 视觉模型原始JSON */
    @Excel(name = "视觉模型原始JSON")
    private String visionJson;

    /** AI返回的文本诊断结果（可选，自然语言总结） */
    @Excel(name = "AI返回的文本诊断结果", readConverterExp = "可=选，自然语言总结")
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

    public void setFqDetected(Integer fqDetected) 
    {
        this.fqDetected = fqDetected;
    }

    public Integer getFqDetected() 
    {
        return fqDetected;
    }

    public void setFqQualityScore(BigDecimal fqQualityScore) 
    {
        this.fqQualityScore = fqQualityScore;
    }

    public BigDecimal getFqQualityScore() 
    {
        return fqQualityScore;
    }

    public void setFqLightingOk(Integer fqLightingOk) 
    {
        this.fqLightingOk = fqLightingOk;
    }

    public Integer getFqLightingOk() 
    {
        return fqLightingOk;
    }

    public void setFqOcclusionOk(Integer fqOcclusionOk) 
    {
        this.fqOcclusionOk = fqOcclusionOk;
    }

    public Integer getFqOcclusionOk() 
    {
        return fqOcclusionOk;
    }

    public void setSkinTone(String skinTone) 
    {
        this.skinTone = skinTone;
    }

    public String getSkinTone() 
    {
        return skinTone;
    }

    public void setOilinessLevel(String oilinessLevel) 
    {
        this.oilinessLevel = oilinessLevel;
    }

    public String getOilinessLevel() 
    {
        return oilinessLevel;
    }

    public void setDarkCirclesLevel(String darkCirclesLevel) 
    {
        this.darkCirclesLevel = darkCirclesLevel;
    }

    public String getDarkCirclesLevel() 
    {
        return darkCirclesLevel;
    }

    public void setAcneLevel(String acneLevel) 
    {
        this.acneLevel = acneLevel;
    }

    public String getAcneLevel() 
    {
        return acneLevel;
    }

    public void setLipColor(String lipColor) 
    {
        this.lipColor = lipColor;
    }

    public String getLipColor() 
    {
        return lipColor;
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
            .append("fqDetected", getFqDetected())
            .append("fqQualityScore", getFqQualityScore())
            .append("fqLightingOk", getFqLightingOk())
            .append("fqOcclusionOk", getFqOcclusionOk())
            .append("skinTone", getSkinTone())
            .append("oilinessLevel", getOilinessLevel())
            .append("darkCirclesLevel", getDarkCirclesLevel())
            .append("acneLevel", getAcneLevel())
            .append("lipColor", getLipColor())
            .append("notes", getNotes())
            .append("visionJson", getVisionJson())
            .append("aiDiagnosis", getAiDiagnosis())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("healthScore", getHealthScore())
            .toString();
    }
}
