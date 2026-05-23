package com.ruoyi.ai.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 症状自述对象 ai_symptom_report
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class AiSymptomReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID，一次症状自述=一条记录 */
    private Long id;

    /** 用户ID（关联 user.id） */
    @Excel(name = "用户ID", readConverterExp = "关=联,u=ser.id")
    private Long userId;

    /** 关联的舌诊记录ID（tongue_record.id） */
    @Excel(name = "关联的舌诊记录ID", readConverterExp = "t=ongue_record.id")
    private Long tongueRecordId;

    /** 关联的脸诊记录ID（face_record.id） */
    @Excel(name = "关联的脸诊记录ID", readConverterExp = "f=ace_record.id")
    private Long faceRecordId;

    /** 关联的指甲诊记录ID（nail_record.id） */
    @Excel(name = "关联的指甲诊记录ID", readConverterExp = "n=ail_record.id")
    private Long nailRecordId;

    /** 主要症状描述（用户填写） */
    @Excel(name = "主要症状描述", readConverterExp = "用=户填写")
    private String mainSymptom;

    /** AI综合诊断建议（自然语言说明） */
    @Excel(name = "AI综合诊断建议", readConverterExp = "自=然语言说明")
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

    public void setTongueRecordId(Long tongueRecordId) 
    {
        this.tongueRecordId = tongueRecordId;
    }

    public Long getTongueRecordId() 
    {
        return tongueRecordId;
    }

    public void setFaceRecordId(Long faceRecordId) 
    {
        this.faceRecordId = faceRecordId;
    }

    public Long getFaceRecordId() 
    {
        return faceRecordId;
    }

    public void setNailRecordId(Long nailRecordId) 
    {
        this.nailRecordId = nailRecordId;
    }

    public Long getNailRecordId() 
    {
        return nailRecordId;
    }

    public void setMainSymptom(String mainSymptom) 
    {
        this.mainSymptom = mainSymptom;
    }

    public String getMainSymptom() 
    {
        return mainSymptom;
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
            .append("tongueRecordId", getTongueRecordId())
            .append("faceRecordId", getFaceRecordId())
            .append("nailRecordId", getNailRecordId())
            .append("mainSymptom", getMainSymptom())
            .append("aiDiagnosis", getAiDiagnosis())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("healthScore", getHealthScore())
            .toString();
    }
}
