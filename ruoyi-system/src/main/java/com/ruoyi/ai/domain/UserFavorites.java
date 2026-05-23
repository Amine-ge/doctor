package com.ruoyi.ai.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户收藏对象 user_favorites
 * 
 * @author ruoyi
 * @date 2025-12-10
 */
public class UserFavorites extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID（关联系统账号） */
    @Excel(name = "用户ID", readConverterExp = "关=联系统账号")
    private Long userId;

    /** 药材ID（外键关联 herbs.id） */
    @Excel(name = "药材ID", readConverterExp = "外=键关联,h=erbs.id")
    private Long herbId;

    /** 收藏时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "收藏时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 1=已收藏，0=取消 */
    @Excel(name = "1=已收藏，0=取消")
    private Long status;

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

    public void setHerbId(Long herbId) 
    {
        this.herbId = herbId;
    }

    public Long getHerbId() 
    {
        return herbId;
    }

    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("herbId", getHerbId())
            .append("createdAt", getCreatedAt())
            .append("status", getStatus())
            .toString();
    }
}
