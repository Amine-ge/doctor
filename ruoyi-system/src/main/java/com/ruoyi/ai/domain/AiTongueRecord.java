package com.ruoyi.ai.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 舌诊分析记录对象 ai_tongue_record
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class AiTongueRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID，一次舌诊对应一条记录 */
    private Long id;

    /** 用户ID（关联用户表，可为空） */
    @Excel(name = "用户ID", readConverterExp = "关=联用户表，可为空")
    private Long userId;

    /** 舌头图片URL（存储原始或压缩后的图片路径） */
    @Excel(name = "舌头图片URL", readConverterExp = "存=储原始或压缩后的图片路径")
    private String imageUrl;

    /** 是否检测到舌头（1=是，0=否） */
    @Excel(name = "是否检测到舌头", readConverterExp = "1==是，0=否")
    private Integer tqDetected;

    /** 图像质量得分（0.000~1.000） */
    @Excel(name = "图像质量得分", readConverterExp = "0=.000~1.000")
    private BigDecimal tqQualityScore;

    /** 光照是否合格（1=合格，0=不合格） */
    @Excel(name = "光照是否合格", readConverterExp = "1==合格，0=不合格")
    private Integer tqLightingOk;

    /** 遮挡是否合格（1=合格，0=不合格） */
    @Excel(name = "遮挡是否合格", readConverterExp = "1==合格，0=不合格")
    private Integer tqOcclusionOk;

    /** 拍摄角度是否合格（1=合格，0=不合格） */
    @Excel(name = "拍摄角度是否合格", readConverterExp = "1==合格，0=不合格")
    private Integer tqAngleOk;

    /** 舌体颜色（如 pale、red、dark-red、purple 等） */
    @Excel(name = "舌体颜色", readConverterExp = "如=,p=ale、red、dark-red、purple,等=")
    private String tongueColor;

    /** 舌苔颜色（如 white、yellow、gray、none 等） */
    @Excel(name = "舌苔颜色", readConverterExp = "如=,w=hite、yellow、gray、none,等=")
    private String coatingColor;

    /** 舌苔厚度（如 thin、medium、thick 等） */
    @Excel(name = "舌苔厚度", readConverterExp = "如=,t=hin、medium、thick,等=")
    private String coatingThickness;

    /** 湿润度（如 dry、normal、wet 等） */
    @Excel(name = "湿润度", readConverterExp = "如=,d=ry、normal、wet,等=")
    private String moisture;

    /** 裂纹情况（如 none、few、many 等） */
    @Excel(name = "裂纹情况", readConverterExp = "如=,n=one、few、many,等=")
    private String fissures;

    /** 齿痕情况（如 none、mild、obvious 等） */
    @Excel(name = "齿痕情况", readConverterExp = "如=,n=one、mild、obvious,等=")
    private String teethMarks;

    /** 舌面质地（如 smooth、rough、greasy 等） */
    @Excel(name = "舌面质地", readConverterExp = "如=,s=mooth、rough、greasy,等=")
    private String surfaceTexture;

    /** 视觉模型原始JSON */
    @Excel(name = "视觉模型原始JSON")
    private String visionJson;

    /** AI 返回的文本诊断结果（模型生成的自然语言分析结论） */
    @Excel(name = "AI 返回的文本诊断结果", readConverterExp = "模=型生成的自然语言分析结论")
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

    public void setTqDetected(Integer tqDetected) 
    {
        this.tqDetected = tqDetected;
    }

    public Integer getTqDetected() 
    {
        return tqDetected;
    }

    public void setTqQualityScore(BigDecimal tqQualityScore) 
    {
        this.tqQualityScore = tqQualityScore;
    }

    public BigDecimal getTqQualityScore() 
    {
        return tqQualityScore;
    }

    public void setTqLightingOk(Integer tqLightingOk) 
    {
        this.tqLightingOk = tqLightingOk;
    }

    public Integer getTqLightingOk() 
    {
        return tqLightingOk;
    }

    public void setTqOcclusionOk(Integer tqOcclusionOk) 
    {
        this.tqOcclusionOk = tqOcclusionOk;
    }

    public Integer getTqOcclusionOk() 
    {
        return tqOcclusionOk;
    }

    public void setTqAngleOk(Integer tqAngleOk) 
    {
        this.tqAngleOk = tqAngleOk;
    }

    public Integer getTqAngleOk() 
    {
        return tqAngleOk;
    }

    public void setTongueColor(String tongueColor) 
    {
        this.tongueColor = tongueColor;
    }

    public String getTongueColor() 
    {
        return tongueColor;
    }

    public void setCoatingColor(String coatingColor) 
    {
        this.coatingColor = coatingColor;
    }

    public String getCoatingColor() 
    {
        return coatingColor;
    }

    public void setCoatingThickness(String coatingThickness) 
    {
        this.coatingThickness = coatingThickness;
    }

    public String getCoatingThickness() 
    {
        return coatingThickness;
    }

    public void setMoisture(String moisture) 
    {
        this.moisture = moisture;
    }

    public String getMoisture() 
    {
        return moisture;
    }

    public void setFissures(String fissures) 
    {
        this.fissures = fissures;
    }

    public String getFissures() 
    {
        return fissures;
    }

    public void setTeethMarks(String teethMarks) 
    {
        this.teethMarks = teethMarks;
    }

    public String getTeethMarks() 
    {
        return teethMarks;
    }

    public void setSurfaceTexture(String surfaceTexture) 
    {
        this.surfaceTexture = surfaceTexture;
    }

    public String getSurfaceTexture() 
    {
        return surfaceTexture;
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
            .append("tqDetected", getTqDetected())
            .append("tqQualityScore", getTqQualityScore())
            .append("tqLightingOk", getTqLightingOk())
            .append("tqOcclusionOk", getTqOcclusionOk())
            .append("tqAngleOk", getTqAngleOk())
            .append("tongueColor", getTongueColor())
            .append("coatingColor", getCoatingColor())
            .append("coatingThickness", getCoatingThickness())
            .append("moisture", getMoisture())
            .append("fissures", getFissures())
            .append("teethMarks", getTeethMarks())
            .append("surfaceTexture", getSurfaceTexture())
            .append("visionJson", getVisionJson())
            .append("aiDiagnosis", getAiDiagnosis())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("healthScore", getHealthScore())
            .toString();
    }
}
